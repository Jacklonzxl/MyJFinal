<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%
   if("sz123456".equals(request.getParameter("password")))
   {
	   session.setAttribute("showuser", "sz123456");
	   response.sendRedirect("/list?type=1&date=2016-06-01&fr=sz123456");
	   return ;
	   
   }else if("weimiisok".equals(request.getParameter("password")))
   {
	   session.setAttribute("showuser", "weimiisok");
	   response.sendRedirect("/list?type=1&date=2016-06-01&fr=weimiisok");
	   return ;
   }
	   
    
%>
<html lang="zh-CN" class="no-js">
	<!--<![endif]-->
	<!-- start: HEAD -->
	<head>
		<title>报表</title>

	</head>
	<!-- end: HEAD -->
	<!-- start: BODY -->
	<body class="login">
    <!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
<br/>
<br/>
<form action="" method="post">
     请输入查询密码: <input type="password" name="password" id="password">&nbsp;<input type="submit">
     
</form>
    
	</body>
	<!-- end: BODY -->
</html>