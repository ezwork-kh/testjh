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
	
	//추가
		//savefolder.properties
		//속성=값
		//의 형식으로 작성하면 된다.
		//savefoldername=C:\\Users\\sujin\\Documents\\workspace-sts\\Spring_MVC_BOARD\\src\\main\\webapp\\resources\\upload\\
		//값을 가져오기 위해서는 속성(property)을 이용한다.
		@Value("${savefoldername}")
		private String saveFolder;
	
	//글쓰기
	@GetMapping(value="/BoardWrite.bo")
	//RequestMapping(value="/BoardWrite.bo",method=RequestMethod.GET) 을 간단하게 쓴 것
	public String board_write() {
		return "board/qna_board_write";
	}
	
	//게시판 저장
	/*
	 * @RequestMapping(value="/board_write_ok.nhn", method=RequestMethod.POST)
	 * = @PostMapping(value="/Board_write_ok.bo") : Spring 새 버전에 추가됨.
	 */
	@PostMapping(value="/Board_write_ok.bo")
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);
		System.out.println("파일명:"+board.getBOARD_FILE());
		return "redirect:/BoardList.bo";
	}
	
	/* 
	 * 스프링 컨테이너는 매개변수 BbsBean객체를 생성하고
	 * BbsBean객체의 setter 메서드들을 호출하여 사용자 입력값을 설정한다.
	 * 매개변수의 이름과 setter의 property가 일치하면 된다.
	 */
	@PostMapping("/BoardAddaction.bo")
	public String bbs_write_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();
		if(!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); //원래 파일명
			board.setBOARD_ORIGINAL(fileName); //원래 파일명 저장
			
			//새로운 폴더 이름 : 오늘 년-월-일
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH)+1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources")+"/upload/";
			String homedir = saveFolder + year +"-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if(!(path1.exists())) {
				path1.mkdir(); //새로운 폴더 생성
			}
			
			//폴더 이미 존재하는 경우
			Random r = new Random();
			int random = r.nextInt(100000000);
			
			/**** 확장자 구하기 시작 ****/
			int index = fileName.lastIndexOf(".");
			//문자열에서 특정 문자열의 위치 값(index)를 반환한다.
			//indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
			//lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환한다.
			//(파일명에 점이 여러개 있는 경우 맨 마지막에 발견되는 문자열의 위치를 리턴)
			System.out.println("index = "+index);
			
			String fileExtension = fileName.substring(index+1);
			System.out.println("fileExtension = "+fileExtension);
			/**** 확장자 구하기 끝 ****/
			
			//새로운 파일명
			String refileName = "bbs"+year+month+date+random+"."+fileExtension;
			System.out.println("refileName = " + refileName);
			
			//오라클 DB에 저장될 파일 명
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);
			
			//transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장한다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));
			
			//바뀐 파일명으로 저장
			board.setBOARD_FILE(fileDBName);
		}
		
		boardService.insertBoard(board);	//저장 메서드 호출
		return "redirect:BoardList.bo";
	}
	
	@ResponseBody
	@RequestMapping(value="/BoardListAjax.bo")
	public Map<String, Object> obardListAjax(
	@RequestParam(value="page", defaultValue="1", required=false) int page,
	@RequestParam(value="limit", defaultValue="10", required=false) int limit)
			{
		int listcount=boardService.getListCount();
		
		//총 페이지 수
		int maxpage = (listcount+limit-1) / limit;
		
		//현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,..)
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
	//RequestMapping(value="/BoardWrite.bo",method=RequestMethod.GET) 을 간단하게 쓴 것
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board==null) {
			System.out.println("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		}else {
			System.out.println("상세보기 성공");
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
			
			//총 페이지 수
			int maxpage = (listcount+limit-1) / limit;
			
			//현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,..)
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
		//글 내용 불러오기 실패한 경우
		if(boarddata == null) {
			System.out.println("(수정)상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(수정)상세보기 실패입니다.");
			return mv;
		}
		System.out.println("(수정)상세보기 성공");
		
		//수정 폼 페이지로 이동할 때 원문 글 내용을 보여주기 때문에 boarddata 객체를 ModelAndView 객체에 저장한다.
		mv.addObject("boarddata", boarddata);
		
		//글 수정 폼 페이지로 이동하기 위해 경로를 설정한다.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}
	
	@PostMapping("BoardModifyAction.bo")
	public ModelAndView BoardModifyAction(Board board, String check, String before_file, ModelAndView mv, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
		//비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('비밀번호가 다릅니다.'); history.back(); </script>");
			out.close();
			return null;
		}
		
		MultipartFile uploadfile = board.getUploadfile();
		//String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
		System.out.println("check=" + check);
		if (check != null && !check.equals("")) {	//기존 파일 그대로 사용하는 경우
			System.out.println("기존파일 그대로 사용합니다.");
			board.setBOARD_ORIGINAL(check);
			//<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
			//위 문장 때문에 board.setBOARD_FILE()값은 자동 저장된다.
		} else {
			if (uploadfile != null && !uploadfile.isEmpty()) { //파일 변경한 경우
				System.out.println("파일 변경되었습니다.");
				String fileName = uploadfile.getOriginalFilename(); //원래 파일명
				board.setBOARD_ORIGINAL(fileName);
				
				String fileDBName = fileDBName(fileName, saveFolder);
				
				//transferTo(File Path) : 업로드한 파일을 매개변수의 경로에 저장.
				uploadfile.transferTo(new File(saveFolder + fileDBName));
				
				//바뀐 파일명으로 저장
				board.setBOARD_FILE(fileDBName);
			} else { // uploadfile.isEmpty() 인 경우 - 파일 선택하지 않은 경우
				System.out.println("선택 파일 없습니다.");
				//<input type="hidden" name="BOARD_ORIGINAL" value="$[boarddata.BOARD_ORIGINAL}">
				//위 태그에 값이 있다면 ""로 값을 변경한다.
				board.setBOARD_FILE("");
				board.setBOARD_ORIGINAL("");
			}
		}
		//DAO에서 수정 메서드 호출하여 수정한다.
		int result = boardService.boardModify(board);
		
		//수정 실패한 경우
		if (result == 0) {
			System.out.println("게시판 수정 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 수정 실패");
		} else { //수정 성공한 경우
			System.out.println("게시판 수정 완료");
			
			//수정 전에 파일이 있고 새로운 파일을 선택한 경우는 삭제할 목록을 테이블에 추가한다.
			if(!before_file.equals("")&&!before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
			
			//수정한 글 내용을 보여주기 위해서 글 내용 보기 페이지로 이동하기 위해 경로를 설정한다.
			mv.setViewName(url);
		}
		return mv;		
	}

	private String fileDBName(String fileName, String saveFolder) {
		//새로운 폴더 이름 : 오늘 년+월+일
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int date = c.get(Calendar.DATE);
		
		String homedir = saveFolder + year +"-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if(!(path1.exists())) {
			path1.mkdir(); //새로운 폴더 생성
		}
		
		//폴더 이미 존재하는 경우
		Random r = new Random();
		int random = r.nextInt(100000000);
		
		/**** 확장자 구하기 시작 ****/
		int index = fileName.lastIndexOf(".");
		//문자열에서 특정 문자열의 위치 값(index)를 반환한다.
		//indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
		//lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환한다.
		//(파일명에 점이 여러개 있는 경우 맨 마지막에 발견되는 문자열의 위치를 리턴)
		System.out.println("index = "+index);
		
		String fileExtension = fileName.substring(index+1);
		System.out.println("fileExtension = "+fileExtension);
		/**** 확장자 구하기 끝 ****/
		
		//새로운 파일명
		String refileName = "bbs"+year+month+date+random+"."+fileExtension;
		System.out.println("refileName = " + refileName);
		
		//오라클 DB에 저장될 파일 명
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
			 mv.addObject("message", "게시판 답변글 가져오기 실패");
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
			 mv.addObject("message", "게시판 답변 처리 실패");
		 } else {
			 mv.setViewName("redirect:BoardList.bo");
		 }
		 return mv;
	 }
	 
	 @GetMapping("BoardFileDown.bo")
	 public void BoardFileDown(String filename, HttpServletRequest request, String original,
			 HttpServletResponse response) throws Exception {
		 String savePath = "resources/upload";
		 
		 //서블릿의 실행 환경 정보를 담고 있는 객체를 리턴한다.
		 ServletContext context = request.getSession().getServletContext();
		 String sDownloadPath = context.getRealPath(savePath);
		 
		 //String sFilePath = sDownloadPath + "\\" + fileName;
		 //"\" 추가하기 위해 "\\" 사용한다.
		 String sFilePath = sDownloadPath + "/" + filename;
		 System.out.println(sFilePath);
		 
		 byte b[] = new byte[4096];
		 
		 //sFilePath에 있는 파일의 MimeType을 구해온다.
		 String sMimeType = context.getMimeType(sFilePath);
		 System.out.println("sMimeType>>>" + sMimeType);
		 
		 if(sMimeType == null)
			 sMimeType = "application/octet-stream";
		 
		 response.setContentType(sMimeType);
		 
		 // 한글 파일명 깨지는 것 방지
		 String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
		 System.out.println(sEncoding);
		 
		 //Content-Disposition : attachment : 브라우저는 해당 Content를 처리하지 않고 다운로드하게 된다.
		 response.setHeader("Content-Disposition", "attachment; filename= "+ sEncoding);
		 try (
				 //웹 브라우저로의 출력 스트림 생성한다.
				 BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
				 //sFilePath로 지정한 파일에 대한 입력 스트림을 생성한다.
				 BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));
				 ) {
			 int numRead;
			 //read (b, 0, b.length) : 바이트 배열 b의 0번 부터 b.length 크기만큼 읽어온다.
			 while ((numRead=in.read(b,0,b.length)) != -1) { //읽을 데이터가 존재하는 경우
				 //바이트 배열 b의 0번부터 numRead크기 만큼 브라우저로 출력
				 out2.write(b,0,numRead);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
	 @PostMapping("BoardDeleteAction.bo")
	 public ModelAndView BoardDeleteAction(Board board, String before_file, String BOARD_PASS, int num, ModelAndView mv, 
			 HttpServletResponse response, HttpServletRequest request) throws Exception {
		 //글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해 비밀번호 일치하는지 확인.
		 boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
		 
		 //비밀번호 일치하지 않는 경우
		 if (usercheck == false) {
			 response.setContentType("text/html;charset=utf-8");
			 PrintWriter out = response.getWriter();
			 out.println("<script> alert('비밀번호가 다릅니다.'); history.back(); </script>");
			 out.close();
			 return null;
		 }
		 //비밀번호 일치하는 경우
		 int result = boardService.boearDelete(num);
		 
		 //삭제 처리 실패한 경우
		 if(result==0) {
			 System.out.println("게시글 삭제 실패");
			 mv.setViewName("error/error");
			 mv.addObject("url", request.getRequestURL());
			 mv.addObject("message", "삭제 실패");
			 return mv;
		 }
		 
		 //삭제 성공한 경우
		 System.out.println("게시글 삭제 성공");
		 
		 
		 response.setContentType("text/html;charset=utf-8");
		 PrintWriter out = response.getWriter();
		 out.println("<script> alert('삭제 되었습니다.'); location.href='BoardList.bo'; </script>");
		 out.close();
		 return null;
	 }
	
}
