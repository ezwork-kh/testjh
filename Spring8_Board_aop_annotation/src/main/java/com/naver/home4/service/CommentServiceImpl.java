/*
  기존의 CommentServiceImpl 클래스에서 메서드 실행 직전에 로그를 출력하도록 할 경우의 소스이다.
 
 이 경우에는 CommentServiceImpl클래스와 LogAdvice클래스가 강하게 결합되어 있다.
 만약 LogAdvice 클래스를 다른 클래스로 변경애햐 하거나,
 공통 기능의 메서드 beforeLog()의 시그니처(리턴타입, 이름, 매개변수)가 변경되는 경우 수정이 불가피하다.
 Advice 클래스 객체를 생성하고 공통 메서드를 호출하는 메서드를 호출하는 코드가 비즈니스 코드에 있다면
 핵심 관심(CommentServiceImpl)과 횡단 관심(LogAdvice)을 분리할 수 있다.
 
 이것을 스프링의 AOP를 이용해서 분리해 보겠습니다 ^ ^ 
 */


package com.naver.home4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.home4.aop.LogAdvice;
import com.naver.home4.dao.CommentDAO;
import com.naver.home4.domain.Comment;

@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	private CommentDAO dao;
	
	@Autowired
	private LogAdvice log;

	@Override
	public int getListCount(int board_num) {
		log.beforeLog();
		return dao.getListCount(board_num);
	}

	@Override
	public List<Comment> getCommentList(int board_num, int page) {
		log.beforeLog();
		int startrow=(page-1)*3+1;
		int endrow=startrow+3-1;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("board_num", board_num);
		map.put("page", page);
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getCommentList(map);
	}

	@Override
	public int commentsInsert(Comment c) {
		log.beforeLog();
		return dao.commentsInsert(c);
		
	}

	@Override
	public int commentsDelete(int num) {
		log.beforeLog();
		return dao.commentDelete(num);
	}

	@Override
	public int commentsUpdate(Comment co) {
		log.beforeLog();
		return dao.commentsUpdate(co);
	}
	
}
