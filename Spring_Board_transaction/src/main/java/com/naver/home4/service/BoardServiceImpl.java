package com.naver.home4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.home4.dao.BoardDAO;
import com.naver.home4.domain.Board;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDAO dao;

	@Override
	public int getListCount() {
		return dao.getListCount();
	}

	@Override
	public List<Board> getBoardList(int page, int limit) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int startrow = (page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		double i = 1/0;
		System.out.println("BoardServiceImpl > getBoardList()");
		return dao.getBoardList(map);
	}

	@Override
	public Board getDetail(int num) {
		if(setReadCountUpdate(num)!=1)
			return null;
		return dao.detail(num);
	}

	@Override
	public int boardReply(Board board) {
		boardReplyUpdate(board);
		board.setBOARD_RE_LEV(board.getBOARD_RE_LEV()+1);
		board.setBOARD_RE_SEQ(board.getBOARD_RE_SEQ()+1);
		return dao.boardReply(board);
	}

	@Override
	public int boardModify(Board modifyboard) {
		return dao.boardModify(modifyboard);
	}

	@Override
	public int boearDelete(int num) {
		int result = 0;
		Board board = dao.detail(num);
		if(board != null) {
			//�߰� - ������ ���� ��ϵ��� �����ϱ� ���� �޼��� ȣ��
			dao.insert_deleteFiles(board);
			
			result = dao.boardDelete(board);
		}
		return result;
	}

	@Override
	public int setReadCountUpdate(int num) {
		return dao.setReadCountUpdate(num);
	}

	@Override
	public boolean isBoardWriter(int num, String pass) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", num);
		map.put("pass", pass);
		Board result = dao.isBoardWriter(map);
		if(result==null)
			return false;
		else 
			return true;
	}

	@Override
	public void insertBoard(Board board) {
		dao.insertBoard(board);		
	}

	@Override
	public int boardReplyUpdate(Board board) {
		return dao.boardReplyUpdate(board);
	}

	@Override
	public void insert_deleteFile(String before_file) {
		dao.insert_deleteFile(before_file);
	}

	@Override
	public List<String> getDeleteFileList() {		
		return dao.getDeleteFileList();
	}
}
