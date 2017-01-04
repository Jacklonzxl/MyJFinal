<!DOCTYPE html>
<%@page import="com.my.util.TimeUtil"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
  if("sz123456".equals(request.getParameter("password")))
  {
		   session.setAttribute("showuser", "sz123456");	   
  }
  if(session.getAttribute("showuser")==null||request.getParameter("fr")==null||!((String)session.getAttribute("showuser")).equals(request.getParameter("fr")))
   {
	   response.sendRedirect("/report/v.jsp");
	   return ;
	   
   }
   java.util.List<com.jfinal.plugin.activerecord.Record> list=(java.util.List<com.jfinal.plugin.activerecord.Record>)request.getAttribute("list");
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
<!-- Table goes in the document BODY -->
<a href="list?password=<%=request.getParameter("password") %>&type=1&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">天下通</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?password=<%=request.getParameter("password") %>&type=2&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">平安好房</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?password=<%=request.getParameter("password") %>&type=3&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">咪咕阅读(注册)</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?password=<%=request.getParameter("password") %>&type=5&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">咪咕阅读(沈阳)</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?password=<%=request.getParameter("password") %>&type=4&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">亿百润</a>
<br/>
<br/>
<strong>
<%if(request.getParameter("type").equals("1")){ %>
  天下通数据
<%} %>
<%if(request.getParameter("type").equals("2")){ %>
 平安好房数据
<%} %>
<%if(request.getParameter("type").equals("3")){ %>
咪咕阅读数据(注册)
<%} %>
<%if(request.getParameter("type").equals("5")){ %>
咪咕阅读数据(沈阳)
<%} %>
<%if(request.getParameter("type").equals("4")){ %>
亿百润数据
<%} %> </strong>
<br/><br/>
<table class="gridtable">
<tr>
	<th style="width:200px">日期</th><th style="width:300px">数量</th><th style="width:300px">按小时查看</th>
</tr>
<% 
 for(int i=0;i<list.size();i++){
	 com.jfinal.plugin.activerecord.Record record=list.get(i);
%>
<tr>
	<td><%=record.get("ad") %></td>
	<td><%=record.get("cnt") %></td>
	<td><a target="_blank" href="listhours?password=<%=request.getParameter("password") %>&type=<%=request.getParameter("type")%>&date=<%=record.get("ad")%>&fr=<%=request.getParameter("fr")%>">查看</a></td>
	 
</tr>
<%} %>
</table>
  <br/><br/><br/><br/>
   <%
   String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
   if(request.getParameter("fr").equals("weimiisok")) {%>
   <a  target="_blank"  href="listliuc?date=<%=nowdate%>&fr=<%=request.getParameter("fr")%>">查看留存任务情况</a>
   <%} %>
	</body>
	<!-- end: BODY -->
</html>