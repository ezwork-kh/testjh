package com.naver.home4.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.home4.domain.Board;
import com.naver.home4.service.BoardService;
import com.naver.home4.service.CommentService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	//�߰�
		//savefolder.properties
		//�Ӽ�=��
		//�� �������� �ۼ��ϸ� �ȴ�.
		//savefoldername=C:\\Users\\sujin\\Documents\\workspace-sts\\Spring_MVC_BOARD\\src\\main\\webapp\\resources\\upload\\
		//���� �������� ���ؼ��� �Ӽ�(property)�� �̿��Ѵ�.
		@Value("${savefoldername}")
		private String saveFolder;
	
	//�۾���
	@GetMapping(value="/BoardWrite.bo")
	//RequestMapping(value="/BoardWrite.bo",method=RequestMethod.GET) �� �����ϰ� �� ��
	public String board_write() {
		return "board/qna_board_write";
	}
	
	//�Խ��� ����
	/*
	 * @RequestMapping(value="/board_write_ok.nhn", method=RequestMethod.POST)
	 * = @PostMapping(value="/Board_write_ok.bo") : Spring �� ������ �߰���.
	 */
	@PostMapping(value="/Board_write_ok.bo")
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);
		System.out.println("���ϸ�:"+board.getBOARD_FILE());
		return "redirect:/BoardList.bo";
	}
	
	/* 
	 * ������ �����̳ʴ� �Ű����� BbsBean��ü�� �����ϰ�
	 * BbsBean��ü�� setter �޼������ ȣ���Ͽ� ����� �Է°��� �����Ѵ�.
	 * �Ű������� �̸��� setter�� property�� ��ġ�ϸ� �ȴ�.
	 */
	@PostMapping("/BoardAddaction.bo")
	public String bbs_write_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();
		if(!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); //���� ���ϸ�
			board.setBOARD_ORIGINAL(fileName); //���� ���ϸ� ����
			
			//���ο� ���� �̸� : ���� ��-��-��
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH)+1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources")+"/upload/";
			String homedir = saveFolder + year +"-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if(!(path1.exists())) {
				path1.mkdir(); //���ο� ���� ����
			}
			
			//���� �̹� �����ϴ� ���
			Random r = new Random();
			int random = r.nextInt(100000000);
			
			/**** Ȯ���� ���ϱ� ���� ****/
			int index = fileName.lastIndexOf(".");
			//���ڿ����� Ư�� ���ڿ��� ��ġ ��(index)�� ��ȯ�Ѵ�.
			//indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
			//lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�Ѵ�.
			//(���ϸ� ���� ������ �ִ� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� ����)
			System.out.println("index = "+index);
			
			String fileExtension = fileName.substring(index+1);
			System.out.println("fileExtension = "+fileExtension);
			/**** Ȯ���� ���ϱ� �� ****/
			
			//���ο� ���ϸ�
			String refileName = "bbs"+year+month+date+random+"."+fileExtension;
			System.out.println("refileName = " + refileName);
			
			//����Ŭ DB�� ����� ���� ��
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);
			
			//transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����Ѵ�.
			uploadfile.transferTo(new File(saveFolder + fileDBName));
			
			//�ٲ� ���ϸ����� ����
			board.setBOARD_FILE(fileDBName);
		}
		
		boardService.insertBoard(board);	//���� �޼��� ȣ��
		return "redirect:BoardList.bo";
	}
	
	@ResponseBody
	@RequestMapping(value="/BoardListAjax.bo")
	public Map<String, Object> obardListAjax(
	@RequestParam(value="page", defaultValue="1", required=false) int page,
	@RequestParam(value="limit", defaultValue="10", required=false) int limit)
			{
		int listcount=boardService.getListCount();
		
		//�� ������ ��
		int maxpage = (listcount+limit-1) / limit;
		
		//���� �������� ������ ���� ������ �� (1, 11, 21,..)
		int startpage = ((page-1)/10)*10+1;
		
		int endpage = startpage+10-1;
		
		if(endpage>maxpage)
			endpage=maxpage;
		
		List<Board> boardlist = boardService.getBoardList(page, limit);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",page);
		map.put("maxpage",maxpage);
		map.put("startpage",startpage);
		map.put("endpage",endpage);
		map.put("listcount",listcount);
		map.put("boardlist",boardlist);
		map.put("limit",limit);
		return map;	
	}
	
	@GetMapping(value="/BoardDetailAction.bo")
	//RequestMapping(value="/BoardWrite.bo",method=RequestMethod.GET) �� �����ϰ� �� ��
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board==null) {
			System.out.println("�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURL());
			mv.addObject("message", "�󼼺��� �����Դϴ�.");
		}else {
			System.out.println("�󼼺��� ����");
			int count = commentService.getListCount(num);
			mv.setViewName("board/qna_board_view");
			mv.addObject("count",count);
			mv.addObject("boarddata", board);
		}
		return mv;
	}
	
	@RequestMapping(value="/BoardList.bo")
	public ModelAndView boardList (
		@RequestParam(value="page", defaultValue="1", required=false) int page,
		ModelAndView mv){
			int limit=10;
			int listcount=boardService.getListCount();
			
			//�� ������ ��
			int maxpage = (listcount+limit-1) / limit;
			
			//���� �������� ������ ���� ������ �� (1, 11, 21,..)
			int startpage = ((page-1)/10)*10+1;
			
			int endpage = startpage+10-1;
			
			if(endpage>maxpage)
				endpage=maxpage;
			
			List<Board> boardlist = boardService.getBoardList(page, limit);
			mv.setViewName("board/qna_board_list");
			mv.addObject("page",page);
			mv.addObject("maxpage",maxpage);
			mv.addObject("startpage",startpage);
			mv.addObject("listcount",listcount);
			mv.addObject("boardlist",boardlist);
			mv.addObject("limit",limit);
			return mv;	
	}
	
	@GetMapping("/BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board boarddata = boardService.getDetail(num);
		//�� ���� �ҷ����� ������ ���
		if(boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(����)�󼼺��� �����Դϴ�.");
			return mv;
		}
		System.out.println("(����)�󼼺��� ����");
		
		//���� �� �������� �̵��� �� ���� �� ������ �����ֱ� ������ boarddata ��ü�� ModelAndView ��ü�� �����Ѵ�.
		mv.addObject("boarddata", boarddata);
		
		//�� ���� �� �������� �̵��ϱ� ���� ��θ� �����Ѵ�.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}
	
	@PostMapping("BoardModifyAction.bo")
	public ModelAndView BoardModifyAction(Board board, String check, String before_file, ModelAndView mv, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
		//��й�ȣ�� �ٸ� ���
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('��й�ȣ�� �ٸ��ϴ�.'); history.back(); </script>");
			out.close();
			return null;
		}
		
		MultipartFile uploadfile = board.getUploadfile();
		//String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
		System.out.println("check=" + check);
		if (check != null && !check.equals("")) {	//���� ���� �״�� ����ϴ� ���
			System.out.println("�������� �״�� ����մϴ�.");
			board.setBOARD_ORIGINAL(check);
			//<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
			//�� ���� ������ board.setBOARD_FILE()���� �ڵ� ����ȴ�.
		} else {
			if (uploadfile != null && !uploadfile.isEmpty()) { //���� ������ ���
				System.out.println("���� ����Ǿ����ϴ�.");
				String fileName = uploadfile.getOriginalFilename(); //���� ���ϸ�
				board.setBOARD_ORIGINAL(fileName);
				
				String fileDBName = fileDBName(fileName, saveFolder);
				
				//transferTo(File Path) : ���ε��� ������ �Ű������� ��ο� ����.
				uploadfile.transferTo(new File(saveFolder + fileDBName));
				
				//�ٲ� ���ϸ����� ����
				board.setBOARD_FILE(fileDBName);
			} else { // uploadfile.isEmpty() �� ��� - ���� �������� ���� ���
				System.out.println("���� ���� �����ϴ�.");
				//<input type="hidden" name="BOARD_ORIGINAL" value="$[boarddata.BOARD_ORIGINAL}">
				//�� �±׿� ���� �ִٸ� ""�� ���� �����Ѵ�.
				board.setBOARD_FILE("");
				board.setBOARD_ORIGINAL("");
			}
		}
		//DAO���� ���� �޼��� ȣ���Ͽ� �����Ѵ�.
		int result = boardService.boardModify(board);
		
		//���� ������ ���
		if (result == 0) {
			System.out.println("�Խ��� ���� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�Խ��� ���� ����");
		} else { //���� ������ ���
			System.out.println("�Խ��� ���� �Ϸ�");
			
			//���� ���� ������ �ְ� ���ο� ������ ������ ���� ������ ����� ���̺� �߰��Ѵ�.
			if(!before_file.equals("")&&!before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			
			//������ �� ������ �����ֱ� ���ؼ� �� ���� ���� �������� �̵��ϱ� ���� ��θ� �����Ѵ�.
			mv.setViewName(url);
		}
		return mv;		
	}

	private String fileDBName(String fileName, String saveFolder) {
		//���ο� ���� �̸� : ���� ��+��+��
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int date = c.get(Calendar.DATE);
		
		String homedir = saveFolder + year +"-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if(!(path1.exists())) {
			path1.mkdir(); //���ο� ���� ����
		}
		
		//���� �̹� �����ϴ� ���
		Random r = new Random();
		int random = r.nextInt(100000000);
		
		/**** Ȯ���� ���ϱ� ���� ****/
		int index = fileName.lastIndexOf(".");
		//���ڿ����� Ư�� ���ڿ��� ��ġ ��(index)�� ��ȯ�Ѵ�.
		//indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
		//lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�Ѵ�.
		//(���ϸ� ���� ������ �ִ� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� ����)
		System.out.println("index = "+index);
		
		String fileExtension = fileName.substring(index+1);
		System.out.println("fileExtension = "+fileExtension);
		/**** Ȯ���� ���ϱ� �� ****/
		
		//���ο� ���ϸ�
		String refileName = "bbs"+year+month+date+random+"."+fileExtension;
		System.out.println("refileName = " + refileName);
		
		//����Ŭ DB�� ����� ���� ��
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDBName = " + fileDBName);

		return fileDBName;
	}
	
	 @GetMapping("BoardReplyView.bo")
	 public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
		 Board board = boardService.getDetail(num);
		 if(board == null) {
			 mv.setViewName("error/error");
			 mv.addObject("url", request.getRequestURL());
			 mv.addObject("message", "�Խ��� �亯�� �������� ����");
		 }else {
			 mv.addObject("boarddata",board);
			 mv.setViewName("board/qna_board_reply");
		 }
		 return mv;
	 }
	 
	 @PostMapping("BoardReplyAction.bo")
	 public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) {
		 int result = boardService.boardReply(board);
		 if (result==0) {
			 mv.setViewName("error/error");
			 mv.addObject("url", request.getRequestURL());
			 mv.addObject("message", "�Խ��� �亯 ó�� ����");
		 } else {
			 mv.setViewName("redirect:BoardList.bo");
		 }
		 return mv;
	 }
	 
	 @GetMapping("BoardFileDown.bo")
	 public void BoardFileDown(String filename, HttpServletRequest request, String original,
			 HttpServletResponse response) throws Exception {
		 String savePath = "resources/upload";
		 
		 //������ ���� ȯ�� ������ ��� �ִ� ��ü�� �����Ѵ�.
		 ServletContext context = request.getSession().getServletContext();
		 String sDownloadPath = context.getRealPath(savePath);
		 
		 //String sFilePath = sDownloadPath + "\\" + fileName;
		 //"\" �߰��ϱ� ���� "\\" ����Ѵ�.
		 String sFilePath = sDownloadPath + "/" + filename;
		 System.out.println(sFilePath);
		 
		 byte b[] = new byte[4096];
		 
		 //sFilePath�� �ִ� ������ MimeType�� ���ؿ´�.
		 String sMimeType = context.getMimeType(sFilePath);
		 System.out.println("sMimeType>>>" + sMimeType);
		 
		 if(sMimeType == null)
			 sMimeType = "application/octet-stream";
		 
		 response.setContentType(sMimeType);
		 
		 // �ѱ� ���ϸ� ������ �� ����
		 String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
		 System.out.println(sEncoding);
		 
		 //Content-Disposition : attachment : �������� �ش� Content�� ó������ �ʰ� �ٿ�ε��ϰ� �ȴ�.
		 response.setHeader("Content-Disposition", "attachment; filename= "+ sEncoding);
		 try (
				 //�� ���������� ��� ��Ʈ�� �����Ѵ�.
				 BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
				 //sFilePath�� ������ ���Ͽ� ���� �Է� ��Ʈ���� �����Ѵ�.
				 BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));
				 ) {
			 int numRead;
			 //read (b, 0, b.length) : ����Ʈ �迭 b�� 0�� ���� b.length ũ�⸸ŭ �о�´�.
			 while ((numRead=in.read(b,0,b.length)) != -1) { //���� �����Ͱ� �����ϴ� ���
				 //����Ʈ �迭 b�� 0������ numReadũ�� ��ŭ �������� ���
				 out2.write(b,0,numRead);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
	 @PostMapping("BoardDeleteAction.bo")
	 public ModelAndView BoardDeleteAction(Board board, String before_file, String BOARD_PASS, int num, ModelAndView mv, 
			 HttpServletResponse response, HttpServletRequest request) throws Exception {
		 //�� ���� ����� ��û�� ����ڰ� ���� �ۼ��� ��������� �Ǵ��ϱ� ���� ��й�ȣ ��ġ�ϴ��� Ȯ��.
		 boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
		 
		 //��й�ȣ ��ġ���� �ʴ� ���
		 if (usercheck == false) {
			 response.setContentType("text/html;charset=utf-8");
			 PrintWriter out = response.getWriter();
			 out.println("<script> alert('��й�ȣ�� �ٸ��ϴ�.'); history.back(); </script>");
			 out.close();
			 return null;
		 }
		 //��й�ȣ ��ġ�ϴ� ���
		 int result = boardService.boearDelete(num);
		 
		 //���� ó�� ������ ���
		 if(result==0) {
			 System.out.println("�Խñ� ���� ����");
			 mv.setViewName("error/error");
			 mv.addObject("url", request.getRequestURL());
			 mv.addObject("message", "���� ����");
			 return mv;
		 }
		 
		 //���� ������ ���
		 System.out.println("�Խñ� ���� ����");
		 
		 
		 response.setContentType("text/html;charset=utf-8");
		 PrintWriter out = response.getWriter();
		 out.println("<script> alert('���� �Ǿ����ϴ�.'); location.href='BoardList.bo'; </script>");
		 out.close();
		 return null;
	 }
	
}
