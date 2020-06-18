package com.naver.home4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naver.home4.domain.Comment;

public interface CommentService {
	
	//±ÛÀÇ °¹¼ö
	public int getListCount(int board_num);
	
	//´ñ±Û ¸ñ·Ï °¡Á®°¡±â
	public List<Comment> getCommentList(int board_num, int page);
	
	//´ñ±Û µî·Ï
	public int commentsInsert(Comment c);
	
	//´ñ±Û »èÁ¦
	public int commentsDelete(int num);
	
	//´ñ±Û ¼öÁ¤
	public int commentsUpdate(Comment co);
}
