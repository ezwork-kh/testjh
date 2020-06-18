/*
 ������ CommentServiceImplŬ��������
 �޼����� ���� ������ �α׸� ����ϵ��� �� ����� �ҽ�
 
  �� ��쿡�� CommentServiceImplŬ������ LogAdviceŬ������ ���ϰ� ���յǾ� �ִ�.
  ���� LogAdvice Ŭ������ �ٸ� Ŭ������ �����ؾ� �ϰų�
  ���� ����� �޼��� befroeLog()�� �ñ״�ó(����Ÿ��, �̸�, �Ű�����)�� ����Ǵ� ���
  ������ �Ұ����ϴ�.
  Advice Ŭ���� ��ü�� �����ϰ� ���� �޼��带 ȣ���ϴ� �޼��带 ȣ���ϴ� �ڵ尡
  ����Ͻ� �ڵ忡 �ִٸ� �ٽ� ����(CommentServiceImpl)�� Ⱦ�� ����(LogAdvice)��
  �и��� �� ����.
  
  �̰��� �������� AOP�� �̿��ؼ� �и�
 * 
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
	
	
	@Override
	public int getListCount(int board_num) {
		return dao.getListCount(board_num);
	}
	
	
	@Override
	public List<Comment> getCommentList(int board_num, int page) {
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
		return dao.commentsInsert(c);
	}

	@Override
	public int commentsDelete(int num) {
		return dao.commentDelete(num);
	}

	@Override
	public int commentsUpdate(Comment co) {
		return dao.commentsUpdate(co);
	}
	
}
