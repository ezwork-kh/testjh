package com.naver.home4.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.naver.home4.domain.MailVO;
import com.naver.home4.domain.Member;
import com.naver.home4.service.MemberService;
import com.naver.home4.task.SendMail;

/*
 @Component�� �̿��ؼ� ������ �����̳ʰ� �ش� Ŭ���� ��ü�� �����ϵ��� ������ �� ������
 ��� Ŭ������ @Component�� �Ҵ��ϸ� � Ŭ������ � ������ �����ϴ��� �ľ��ϱ� ��ƴ�.
 ������ �����ӿ�ũ������ �̷� Ŭ�������� �з��ϱ� ���ؼ� @Component�� ����Ͽ� ������ ����
 �� ���� annotation�� �����Ѵ�.
 
 1. @Controller - ������� ��û�� �����ϴ� Controller Ŭ����
 2. @Repository - ������ ���̽� ������ ó���ϴ� DAO Ŭ����
 3. @Service - ����Ͻ� ������ ó���ϴ� Service Ŭ����
 */
@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberservice;
	
	@Autowired
	private SendMail sendMail;
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
	/* @CookieValue(value="saveid", required=false) Cookie readCookie
	 * �̸��� saveid�� ��Ű�� Cookie Ÿ������ ���޹޴´�.
	 * ������ �̸��� ��Ű�� �������� ���� ���� �ֱ� ������ required=false�� �����ؾ� �Ѵ�.
	 * ��, id ����ϱ⸦ �������� ���� ���� �ֱ� ������ required=false�� �����ؾ� �Ѵ�.
	 * required=true ���¿��� ������ �̸��� ���� ��Ű�� �������� ������ ������ MVC�� �ͼ����� �߻���Ų��.
	 */
	//�α����� �̵�
	@RequestMapping(value = "/login.net", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mv, @CookieValue(value="saveid", required=false) Cookie readCookie) 
	throws Exception {
		if(readCookie != null) {
			mv.addObject("saveid", readCookie.getValue());
			System.out.println("cookie time="+readCookie.getMaxAge());
		}
		mv.setViewName("member/loginForm");
		return mv;
	}
	
	//�α��� ó��
	@RequestMapping(value="/loginProcess.net", method=RequestMethod.POST)
	public String loginProcess(@RequestParam("id") String id,
			@RequestParam("password") String password, @RequestParam(value="remember", defaultValue="") String remember,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		int result = memberservice.isId(id, password);
		System.out.println("����� "+result);
		
		if(result==1) {
			//�α��� ����
			session.setAttribute("id", id);
			Cookie savecookie = new Cookie("saveid",id);
			if(!remember.equals("")) {
				savecookie.setMaxAge(60*60);
				System.out.println("��Ű���� : 60*60");
			}else {
				System.out.println("��Ű���� : 0");
				savecookie.setMaxAge(0);
			}
			response.addCookie(savecookie);
			return "redirect:BoardList.bo";
		}else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if(result==-1) {
				message = "���̵� �������� �ʽ��ϴ�.";
			}
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('"+message+"');");
			out.println("location.href='login.net'; </script>");
			out.close();
			return null;
		}
	}
	
	//ȸ������ �� �̵�
	@RequestMapping(value="/join.net",method=RequestMethod.GET)
	public String join() {
		return "member/joinForm"; //WEB-INF/views/member/joinForm.jsp
	}
	
	//ȸ������ ó��
	@RequestMapping(value="/joinProcess.net", method=RequestMethod.POST)
	public void joinProcess(Member member, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		//��й�ȣ ��ȣȭ �߰�
		//String encPassword = passwordEncoder.encode(member.getPassword());
		//System.out.println(encPassword);
		//member.setPassword(encPassword);
		
		int result = memberservice.insert(member);
		out.println("<script>");
		//������ �� ���
		if(result==1) {
			MailVO vo = new MailVO();
			vo.setTo(member.getEmail());
			vo.setContent(member.getId() + "�� ȸ�������� ���ϵ帳�ϴ�.");
			sendMail.sendMail(vo);
			
			out.println("alert('ȸ�� ������ �����մϴ�.');");
			out.println("location.href='login.net';");
		}else if(result==-1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���.);");
			//out.println("location.href='join.net;"); //���ΰ�ħ�Ǿ� ������ �Է��� �����Ͱ� ��Ÿ���� �ʴ´�.
			out.println("history.back()"); //��й�ȣ�� ������ �ٸ� �����ʹ� �����Ǿ� �ִ�.
		}
		out.println("</script>");
		out.close();
	}
	
	//���̵� �ߺ� üũ
	@RequestMapping(value="/idcheck.net",method=RequestMethod.GET)
	public void idcheck(@RequestParam("id") String id, HttpServletResponse response)throws Exception {
		int result = memberservice.isId(id);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();		
		out.print(result);
	}
	
	//�α׾ƿ�
	@RequestMapping(value="/logout.net", method=RequestMethod.GET)
	public String loginout(HttpSession session) throws Exception{
		session.invalidate();
		return "redirect:login.net";
	}
	
	//ȸ���� ���� ����
	@RequestMapping(value="/member_info.net", method=RequestMethod.GET)
	public ModelAndView member_info(@RequestParam("id") String id, 
			ModelAndView mv) throws Exception {
		Member m = memberservice.member_info(id);
		mv.setViewName("member/member_info");
		mv.addObject("memberinfo", m);
		return mv;
	}	
	
	//ȸ�� ����Ʈ
	@GetMapping(value="member_list.net")
	public ModelAndView memberlist(
			@RequestParam(value="page", defaultValue="1", required = false) int page,
			@RequestParam(value="limit", defaultValue="3", required=false) int limit,
			ModelAndView mv, 
			@RequestParam(value="search_field", defaultValue="-1") int index,
			@RequestParam(value="search_word", defaultValue="") String search_word
			) throws Exception{
		List<Member> list = null;
		int listcount = 0;
		
		list = memberservice.getSearchList(index, search_word, page, limit);
		listcount = memberservice.getSearchListCount(index, search_word);
		
		//�� ������ ��
		int maxpage = (listcount + limit -1) / limit;
		
		//���� �������� ������ ���� ������ ��(1,11,21,...)
		int startpage = ((page-1)/10) * 10 + 1;
		
		//���� �������� ������ ������ ������ ��(10,20,30,...)
		int endpage = startpage + 10 -1;
		
		if (endpage > maxpage)
			endpage = maxpage;
		
		mv.setViewName("member/member_list"); 
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("memberlist",list);
		mv.addObject("search_field", index);
		mv.addObject("search_word", search_word);
		return mv;
	}
	
	//ȸ�� ����
	@RequestMapping(value="/member_delete.net", method=RequestMethod.GET)
	public void member_delete(HttpServletResponse response, @RequestParam(value="id") String id,
			ModelAndView mv) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		memberservice.delete(id);
		PrintWriter out = response.getWriter();
		if(id=="admin") {
			out.println("<script> alert('�����ڴ� �������� �ʽ��ϴ�.'); history.back(); </script> ");
		} else {
			out.println("<script> alert('�����Ǿ����ϴ�.'); location.href='member_list.net' </script>");
		}
	}
		
	//ȸ������ ����������
	@RequestMapping(value="/member_update.net", method=RequestMethod.GET)
	public ModelAndView member_update(HttpSession session, ModelAndView mv) throws Exception {
		String id = (String)session.getAttribute("id");
		Member m = memberservice.member_info(id);
		mv.setViewName("member/updateForm");
		mv.addObject("memberinfo", m);
		return mv;
	}
	
	//ȸ������ ���� ó��
	@RequestMapping(value="/updateProcess.net", method=RequestMethod.POST)
	public void updateProcess(Member member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = memberservice.update(member);
		out.println("<script>");
		//���� �� ���
		if(result==1) {
			out.println("alert('�����Ǿ����ϴ�'); location.href='BoardList.bo'; </script> ");
		} else {
			out.println("alert('���� ����'); history.back(); </script> ");
		}
		out.close();
	}
	
}
