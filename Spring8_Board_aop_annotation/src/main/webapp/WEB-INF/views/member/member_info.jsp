<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
 <style>
   table{border-collapse:collapse; border-spacing:0; width:70%; 
   		 border: 1px solid #ddd; margin:2% auto;}
   th, td{text-align:center; padding:8px;}
   tr:nth-child(odd){background:#f2f2f2}
 </style>
 
 <jsp:include page="../board/header.jsp"/>
 <title>회원관리 시스템 관리자모드(회원 정보 보기)</title>
 
 <script>

 </script>
 
</head>
<body>
<c:set var="m" value="${memberinfo}"></c:set>
<table>
	<tr>
		<td>아이디</td>
		<td>${m.id}</td>
	</tr>
	<tr>
		<td>비밀번호</td>
		<td>${m.password}</td>
	</tr>
	<tr>
		<td>이름</td>
		<td>${m.name}</td>
	</tr>
	<tr>
		<td>나이</td>
		<td>${m.age}</td>
	</tr>
	<tr>
		<td>성별</td>
		<td>${m.gender}</td>
	</tr>
	<tr>
		<td>이메일 주소</td>
		<td>${m.email}</td>
	</tr>
	<tr>
		<td colspan=2>
		<a href="member_list.net">리스트로 돌아가기</a></td>
	</tr>
</table>
</body>
</html>