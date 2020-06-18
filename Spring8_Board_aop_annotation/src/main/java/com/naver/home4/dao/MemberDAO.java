package com.naver.home4.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.home4.domain.Member;

@Repository
public class MemberDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public int insert(Member m) {
		return sqlSession.insert("Members.insert",m);
	}
	
	public Member isId(String id) {
		return sqlSession.selectOne("Members.idcheck",id);
	}
	
	public Member member_info(String id) {
		return sqlSession.selectOne("Members.idcheck",id);
	}
	
	public int update(Member m) {
		return sqlSession.update("Members.update",m);
	}

	public int delete(String id) {
		return sqlSession.delete("Members.delete", id);
	}
	
	public List<Member> getSearchList(Map<String, Object> map) {
		return sqlSession.selectList("Members.getSearchList", map);
	}
	
	public int getListCount() {
		return sqlSession.selectOne("Members.count");
	}

	public int getSearchListCount(Map<String, String> map) {
		return sqlSession.selectOne("Members.searchcount",map);
	}
	
	public List<Member> getList(Map<String, Integer> map) {
		return sqlSession.selectList("Members.list", map);
	}
}
