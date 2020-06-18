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
		int result=-1; //아이디 존재하지 않는 경우 -1로 초기화
		if(rmember!=null) { //아이디 존재하는 경우
			//passwordEncoder.matches(rawPassword, encodedPassword)
			//사용자에게 입력받은 패스워드를 비교하고자 할 때 사용하는 메서드이다.
			//rawPassword : 사용자가 입력한 패스워드
			//encodedPassword : DB에 저장된 패스워드
			/*if (passwordEncoder.matches(password, rmember.getPassword())) {
				result=1; //아이디와 비밀번호 일치하는 경우
			}else 
				result=0; //비밀번호만 일치하지 않는 경우
				
				*/
			
			if (password.equals(rmember.getPassword())) {
				result=1; //아이디와 비밀번호 일치하는 경우
			}else 
				result=0; //비밀번호만 일치하지 않는 경우
			
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
		return (rmember==null)? -1: 1; //-1 아이디 존재 X , 1 아이디 존재
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
