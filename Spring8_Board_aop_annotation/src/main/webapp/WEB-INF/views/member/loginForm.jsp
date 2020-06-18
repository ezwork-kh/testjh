<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="EUC-KR">
<title>로그인</title>
<link href="resources/css/login.css" type="text/css" rel="stylesheet"> <!-- index.jsp 기준 주소 -->
	<script src="resources/js/jquery-3.5.1.js"></script>
	<script>
		$(function(){
			$(".join").click(function(){
				location.href="join.net";
			});
		})
	</script>
</head>
<body>
<form name="loginform" action="loginProcess.net" method="post"><h1>로그인</h1>
	<hr>
		<b>아이디</b>
		<input type="text" name="id" placeholder="Ender id" id="id" required
		<c:if test="${!empty saveid}"> value="${saveid }"</c:if>>
		<b>Password</b>
		<input type="password" name="password" placeholder="Enter password" required>
		<label>
		<input type="checkbox" id="remember" name="remember" style="margin-bottom:15px"
		<c:if test="${!empty saveid}"> checked</c:if>>Remember me</label>
		
		<div class="clearfix">
		<button type="submit" class="submitbtn">로그인</button>
		<button type="button" class="join">회원가입</button>
		</div>
</form>
</body>
</html>