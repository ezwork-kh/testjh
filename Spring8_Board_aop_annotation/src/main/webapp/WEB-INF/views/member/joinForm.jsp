<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<link href="resources/css/join.css" type="text/css" rel="stylesheet"> <!-- index.jsp 기준 주소 -->
<script src="resources/js/jquery-3.5.1.js"></script>
<script>
	$(function(){
		var checkid = false;
		var checkemail = false;
		$('form').submit(function(){
			if(!$.isNumeric($("input[name='age']").val())){
				alert("나이는 숫자를 입력하세요");
				$("input[name='age']").val('');
				$("input[name='age']").focus();
				return false;
			}
			if(!checkid){
				alert("사용가능한 id로 입력하세요.");
				$("input:eq(0)").val('').focus();
				return false
			}
			if(!checkemail){
				alert("email 형식을 확인하세요");
				$("input:eq(6)").focus();
				return false;
			}
			
			
		}); //submit
		
		$("input:eq(6)").on('keyup',
				function(){
					$("#email_message").empty();
					//[A-Za-z0-9_]와 동일한 것이 \w
					//+는 1회 이상 반복을 의미. {1,}와 동일하다.
					//\w+ 는 [A-Za-z0-9_]를 1개이상 사용하라는 의미이다.
					var pattern = /\w+@\w+[.]\w{3}/;
					var email = $("input:eq(6)").val();
					if(!pattern.test(email)){
						$("#email_message").css('color','red').html("이메일 형식이 맞지 않습니다.");
						checkemail=false;
					}else{
						$("#email_message").css('color','green').html("이메일 형식에 맞습니다.");
						checkemail=true;
					}
		}); //email keyup 이벤트 처리 끝
			
		$("input:eq(0)").on('keyup',
				function(){
					$("message").empty(); 
					var id = $("input:eq(0)").val();
					
					$.ajax({
						url : "idcheck.net",
						data : {"id":id},
						success : function(resp){
							if (resp == -1){ //db에 해당 id가 없는 경우 : -1
								$("#message").css('color', 'green').html("사용 가능한 아이디입니다.");
								checkid = true;
							} else{ //id가 있는 경우 : 0
								$("#message").css('color','blue').html("사용중인 아이디입니다.");
								checkid = false;
							}
						}
					});
		});
	})
</script>
</head>

<body>
	<form name="joinform" action="joinProcess.net" method="post">
		<h1>회원가입</h1>
		<hr>
		<b>아이디</b>
		<input type="text" name="id" placeholder="Enter id" required maxLength="12">
		<span id="message"></span>
		<b>비밀번호</b>
		<input type="password" name="password" placeholder="Enter password" required>
		<b>이름</b>
		<input type="text" name="name" placeholder="Enter name" maxlength=15 required>
		<b>나이</b>
		<input type="text" name="age" placeholder="Enter age" maxlength=2 required>
		<b>성별</b>
		<div>
			<input type="radio" name="gender" value="남" checked><span>남자</span>
			<input type="radio" name="gender" value="여"><span>여자</span>
		</div>
		<b>이메일 주소</b>
		<input type="text" name="email" placeholder="Enter email" required><span id="email_message"></span>
		<div class="clearfix">
			<button type="submit" class="submitbtn">회원가입</button>
			<button type="reset" class="cancelbtn">다시작성</button>
		</div>
	</form> 
</body>
</html>