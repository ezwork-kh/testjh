package com.naver.home4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naver.home4.domain.Comment;

public interface CommentService {
	
	//���� ����
	public int getListCount(int board_num);
	
	//��� ��� ��������
	public List<Comment> getCommentList(int board_num, int page);
	
	//��� ���
	public int commentsInsert(Comment c);
	
	//��� ����
	public int commentsDelete(int num);
	
	//��� ����
	public int commentsUpdate(Comment co);
}
