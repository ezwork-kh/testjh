package com.naver.home4.service;

import java.util.List;

import com.naver.home4.domain.Board;

public interface BoardService {
	public int getListCount();
	public List<Board> getBoardList(int page, int limit);
	public Board getDetail(int num);
	public int boardReply(Board board);
	public int boardModify(Board modifyboard);
	public int boearDelete(int num);
	public int setReadCountUpdate(int num);
	public boolean isBoardWriter(int num, String pass);	
	public void insertBoard(Board board);
	
	//½ÃÄö½º ¼öÁ¤
	public int boardReplyUpdate(Board board);
	
	public void insert_deleteFile(String before_file);
	public List<String> getDeleteFileList();
}
