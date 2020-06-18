$(function(){
	$('#comment table').hide();  //1
	var page=1;	//더 보기에서 보여줄 페이지를 기억할 변수
	var count=0; //전체 댓글 갯수
	var maxPage=getMaxPage(); //댓글의 총 페이지수를 구한다.
	if(count!=0)	//댓글 갯수가 0이 아니라면
		getList(1);	//첫 페이지의 댓글을 구해 온다. (한 페이지에 3개씩 가져온다.)
	else{
		$("#message").text("등록된 댓글이 없습니다.")
	}
	
	//최대 몇 페이지를 가질 수 있는지 계산한다.
	function getMaxPage(){
		//글의 총 갯수 - 등록 또는 삭제시 댓글의 총 갯수 변화가 있기 때문에 갯수를 구한다.
		count=parseInt($("#count").text());
		
		//예) 총 리스트가 5개이면 최대 페이지는 2이다.
		//int maxpage = (listcount + limit - 1) / limit;
		return parseInt((count+3-1)/3);
	}
	
	function getList(currentPage){
		$.ajax({
			type:"post",
			url:"CommentList.bo",
			data: {
				"board_num" : $("#board_num").val(),
				"page" : currentPage
			},
			dataType : "json",
			success : function(rdata){
				if (rdata.length>0){
					$("#comment table").show();	//문서가 로딩될 때 hide()했던 부분을 보이게 한다. (1)
					$("#comment thead").show(); //글이 없을 때 hide() 부분을 보이게 한다. (2)
					output = '';
					$(rdata).each(function(){
						img='';
						if($("#loginid").val()==this.id){
							img = "<img src='resources/image/pencil2.png' width='15px' class='update'>"
								+ "<img src='resources/image/remove.png' width='15px' class='remove'>"
								+ "<input type='hidden' value='"+this.num+"'>";
						}
						output += "<tr><td>" + this.id + "</td>";
						output += "<td>" + this.content + "</td>";
						output += "<td>" + this.reg_date + img + "</td></tr>";	
					}); //each end
					
					$("#comment tbody").append(output);
					
					console.log("현재:"+currentPage)
					console.log("max:"+maxPage)
					//현재 페이지가 마지막 페이지이면 "더보기"는 나타나지 않는다.
					if(currentPage==maxPage){
						$("#message").text("");
					} else {
						$("#message").text("더보기");
					}
					//더보기를 클릭할 경우 현재 페이지에서 1증가된 페이지를 보여주기 위해 값을 설정한다.
					page = currentPage+1;
				}else{
					$("#message").text("등록된 댓글이 없습니다.")
					$("#comment thead").hide();//2
					$("#comment tbody").empty();// 데이터가 한 개인 경우 삭제하면서 tbody를 비운다.
				}
			}
		})
	}
	
	//더 보기를 클릭하면 page 내용이 추가로 보여진다.
	$("#message").click(function(){
		getList(page);
	});
	
	//글자수 50개 제한
	$('#content').on('input',function(){
		length = $(this).val().length;
		if (length > 50) {
			length = 50;
			content = content.substring(0, length);
		}
		$(".float-left").text(length + "/50")
	})
	
	//등록 또는 수정완료 버튼을 클릭한 경우
	//버튼의 라벨이 '등록'인 경우는 댓글을 추가하는 경우
	//버튼의 라벨이 '수정완료'인 경우는 댓글을 수정하는 경우
	$('#write').click(function(){
		buttonText = $('#write').text();	//버튼의 라벨 구해오기
		content = $('#content').val();
		$(".float-left").text('총 50자까지 가능합니다.');
		
		if(buttonText == "등록"){
			url = "CommentAdd.bo";
			data = {
					"content" : content,
					"id"	: $("#loginid").val(),
					"board_num" : $("#board_num").val()
			};
		} else {
			url = "CommentUpdate.bo"
			data = {
				"num" : num,
				"content" : content
			};
			$("#write").text("등록");		//다시 등록으로 변경
		}
		
		$.ajax({
			type : "post",
			url : url,
			data : data,
			success : function(result){
				$("#content").val('');
				if (result==1){
					$("#comment tbody").empty();
					if(url=="CommentAdd.bo"){
						$("#count").text(++count);	//등록을 클릭하면 전체 댓글 갯수 증가시키기.
						maxpage = getMaxPage();
					}
					getList(1);	//완료 후 첫 페이지 보여주기
				}
			}
		})
	})
	
	$('#comment').on('click', '.remove', function(){	
		//ajax넣는 경우 부모영역을 잡은 뒤에 on('event','변경할객체',function을 적는다).
		//'.remove' 값이 ajax로 인해 계속 변경되기 때문.
		var num = $(this).next().val();	//댓글 번호
		$.ajax({
			type : "post",
			url	 : "CommentDelete.bo",
			data : {
				"num" : num
			},
			success : function(result){
				if(result == 1) {
					$("#comment tbody").empty();
					$("#count").text(--count);
					maxPage=getMaxPage();
					getList(1);
				}
			}
		})
	})
	
	$('#comment').on('click', '.update', function(){
		before = $(this).parent().prev().text();  //선택한 내용을 가져온다.
		$("#content").focus().val(before);	//textarea에 수정전 내용을 넣는다.
		num = $(this).next().next().val();	// 수정할 댓글 번호를 num에 저장.
		$("#write").text("수정완료");
		$(this).parent().parent().css('background','lightgray');
	})
})
