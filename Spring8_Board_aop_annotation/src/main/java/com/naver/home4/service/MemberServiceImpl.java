package com.naver.home4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naver.home4.dao.MemberDAO;
import com.naver.home4.domain.Member;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDAO dao;
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;

	@Override
	public int isId(String id, String password) {
		Member rmember = dao.isId(id);
		int result=-1; //���̵� �������� �ʴ� ��� -1�� �ʱ�ȭ
		if(rmember!=null) { //���̵� �����ϴ� ���
			//passwordEncoder.matches(rawPassword, encodedPassword)
			//����ڿ��� �Է¹��� �н����带 ���ϰ��� �� �� ����ϴ� �޼����̴�.
			//rawPassword : ����ڰ� �Է��� �н�����
			//encodedPassword : DB�� ����� �н�����
			/*if (passwordEncoder.matches(password, rmember.getPassword())) {
				result=1; //���̵�� ��й�ȣ ��ġ�ϴ� ���
			}else 
				result=0; //��й�ȣ�� ��ġ���� �ʴ� ���
				
				*/
			
			if (password.equals(rmember.getPassword())) {
				result=1; //���̵�� ��й�ȣ ��ġ�ϴ� ���
			}else 
				result=0; //��й�ȣ�� ��ġ���� �ʴ� ���
			
		}
		return result;
	}

	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return (rmember==null)? -1: 1; //-1 ���̵� ���� X , 1 ���̵� ����
	}

	@Override
	public Member member_info(String id) {
		return dao.member_info(id);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public int update(Member m) {
		return dao.update(m);
	}

	@Override
	public List<Member> getSearchList(int index, String search_word, int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(index!=-1) {
			String[] search_field = new String[] {"id", "name", "age", "gender" };
			map.put("search_field", search_field[index]);
			map.put("search_word", "%" + search_word + "%");
		}
		int startrow = (page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start",startrow);
		map.put("end",endrow);
		return dao.getSearchList(map);
	}

	@Override
	public int getSearchListCount(int index, String search_word) {
		Map<String, String> map = new HashMap<String, String>();
		if (index != -1) {
			String[] search_field = new String[] {"id", "name", "age", "gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%" + search_word + "%");
		}
		return dao.getSearchListCount(map);
	}
	
}
