<%@page import="com.cal.dto.CalDto"%>
<%@page import="java.util.List"%>
<%@page import="com.cal.dao.CalDao"%>
<%@page import="com.cal.dao.Util"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html; charset=UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style type="text/css">

	#calendar{
		border-collapse: collapse;
		border:1px solid gray;
	}
	#calendar th{
		width:80px;
		border:1px solid gray;
	}
	#calendar td{
		width:80px;
		height:80px;
		border:1px solid gray;
		text-align:left;
		vertical-align:top;
		position:relative;
	}
	a{
		text-decoration: none;
	}
	
	.clist>p{
		font-size: 5px;
		margin: 1px;
		background-color: pink;
	}
	
	.cpreview{
		position: absolute;
		top:-30px;
		left:10px;
		background-color: pink;
		width: 40px;
		height: 40px;
		text-align: center;
		line-height: 40px;
		border-radius: 40px 40px 40px 1px;
	
	}


</style>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

$(function(){
	$(".countview").hover(function(){
		var year = $(".y").text().trim();
		var month= $(".m").text().trim();
		var cdate= $(this);
		var date = cdate.text().trim();
 //		alert(year + month + date);
		
		function isTwo(n){
			return (n.length<2)?"0"+n:n;
		}
		//alert(year+isTwo(month)+isTwo(date));
		
		var yyyyMMdd = year + isTwo(month) + isTwo(date);
		
		$.ajax({
			type:"post",
			url:"countajax.do",
			data:"id=kh&yyyyMMdd="+yyyyMMdd,
			dataType:"json",
			async:false,
			success:function(msg){
				var count=msg.calcount;
				cdate.after("<div class='cpreview'>"+count+"</div>")
			},
			error:function(){
				alert("통신 실패!");
			}
		});
		
		
	},function(){
		$(".cpreview").remove();
	});
});


</script>


</head>
<body>
<%
	Calendar cal = Calendar.getInstance();

	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH) + 1;	//0부터 시작해서 +1

	String paramYear = request.getParameter("year");
	String paramMonth = request.getParameter("month");
	
	if(paramYear!=null){
		year = Integer.parseInt(paramYear);
	}
	if(paramMonth!=null){
		month = Integer.parseInt(paramMonth);
	}
	
	if(month>12){
		month = 1;
		year++;
	}
	if(month<1){
		month = 12;
		year--;
	}
	
	// 1.현재년도, 현재월, 1일로 달력 설정
	cal.set(year,month-1,1);
	// 2. 1일의 요일
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	
	// 3. 현재 월의 마지막 일
	int endday=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	
	// 달력에 일정표현
	CalDao dao = new CalDao();
	String yyyyMM = year + Util.isTwo(String.valueOf(month));
	System.out.println("yyyyMM출력 : "+ yyyyMM);
	List<CalDto> clist = dao.getCalViewList("kh", yyyyMM);
	
	

%>

	<table id="calendar">
		<caption>
			<a href="calendar.jsp?year=<%=year-1%>&month=<%=month%> ">◁◁</a>
			<a href="calendar.jsp?year=<%=year%>&month=<%=month-1%> ">◀</a>
			<span class="y"><%=year %></span>년
			<span class="m"><%=month %></span>월
			<a href="calendar.jsp?year=<%=year%>&month=<%=month+1%> ">▶</a>
			<a href="calendar.jsp?year=<%=year+1%>&month=<%=month%> ">▷▷</a>
		</caption>
		
		<tr>
			<th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th>
		</tr>

		<tr>
<%
	for(int i=0; i<dayOfWeek-1; i++){
		out.print("<td>&nbsp;</td>");
	}

	int cnt=dayOfWeek;
	
	for(int i=1; i<=endday; i++){
		
%>
			<td>
				<a class="countview" href="calcontroller.do?command=callist&year=<%=year%>&month=<%=month%>&date=<%=i%>" style="color: <%=Util.fontColor(i, dayOfWeek) %>"	><%=i %></a>
				<a href = "insertcalboard.jsp?year=<%=year%>&month=<%=month%>&date=<%=i%>&endday=<%=endday%>">
					<img alt="일정추가" src="img/pen.png" style="width: 10px; height: 10px;"/>
				</a>
				
				<div class="clist">
					<%=Util.getCalView(i,clist)%>
				</div>
				
			</td>
<%
		
		if(cnt%7==0){
%>
			</tr>
			<tr>
<%
		} cnt++;
	}
	
	for(int i = 0; i<(7-(dayOfWeek-1 + endday)%7)%7;i++){
		out.print("<td>&nbsp;</td>");
	}
%>

	</tr>

	</table>





</body>
</html>