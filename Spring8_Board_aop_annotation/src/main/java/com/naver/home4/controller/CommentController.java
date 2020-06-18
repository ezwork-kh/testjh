package com.naver.home4.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naver.home4.domain.Comment;
import com.naver.home4.service.CommentService;

@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	/*
	 * @ResponseBody��?
	 * �޼��忡 @ResponseBody Annotation�� �Ǿ� ������ return�Ǵ� ���� View�� ���ؼ� 
	 * ��µǴ� ���� �ƴ϶� HTTP Response Body�� ���� �������� �ȴ�.
	 * ��) HTTP ���� ���������� ���� HTTP/1.1
	 * Message Header
	   200OK => Start Line Content-Type:text/html => Message Header Connection :
	   close Server : Apache Tomcat... Last-Modified : Mon,...
	   
	 * Message Body
	   <HTML> <head><title>Hello JSP</title></head> <body>Hello JSP!</body></html> =>
	 
	 * ���� ����� HTML�� �ƴ� JSON���� ��ȯ�Ͽ� Message Body�� �����Ϸ��� ���������� �����ϴ� ��ȯ��(Converter)�� ����ؾ� �Ѵ�.
	 * �� ��ȯ�⸦ �̿��ؼ� �ڹ� ��ü�� �پ��� Ÿ������ ��ȯ�Ͽ� HTTP Response Body�� ������ �� �ִ�.
	 	������ ���� ���Ͽ� <mvc:annotation-driven>�� �����ϸ� ��ȯ�Ⱑ �����ȴ�.
	 * �� �߿��� �ڹ� ��ü�� JSON ���� �ٵ�� ��ȯ�� ���� MappingJackson2HttpMessageConverter�� ����Ѵ�.
	 
	 * @ResponseBody�� �̿��ؼ� �� �޼����� ���� ����� JSON���� ��ȯ�Ǿ� HTTP Response BODY�� �����ȴ�.	 	
	 */
	@ResponseBody
	@PostMapping(value="CommentList.bo")
	public List<Comment> CommentList(@RequestParam("board_num") int board_num, 
			@RequestParam(value="page", defaultValue="1", required=false) int page){
		List<Comment> list = commentService.getCommentList(board_num,page);
		return list;
	}
	
	@PostMapping(value = "CommentAdd.bo")
	public void CommentAdd(Comment co, HttpServletResponse response) throws Exception{
		int ok = commentService.commentsInsert(co);
		response.getWriter().print(ok);
	}
	
	@PostMapping(value="CommentDelete.bo")
	public void CommentDelete(int num, HttpServletResponse response) throws Exception{
							//int num => Integer.parseInt(request.getParameter("num"));
		int result = commentService.commentsDelete(num);
		response.getWriter().print(result);
	}
	
	@PostMapping(value = "CommentUpdate.bo")
	public void CommentUpdate(Comment co, HttpServletResponse response) throws Exception{
		int ok = commentService.commentsUpdate(co);
		response.getWriter().print(ok);
	}
}
