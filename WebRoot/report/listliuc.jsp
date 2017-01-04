<!DOCTYPE html>
<%@page import="com.my.util.TimeUtil"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
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
		<title>留存列表</title>

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
 
<!-- Table goes in the document BODY  
<a href="list?type=1&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">天下通</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?type=2&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">平安好房</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?type=3&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">咪咕阅读(注册)</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?type=5&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">咪咕阅读(沈阳)</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="list?type=4&date=<%=request.getParameter("date")%>&fr=<%=request.getParameter("fr")%>">亿百润</a>
<br/>
<br/>-->
<strong>
 留存任务数据
 </strong>
<br/><br/>
<table class="gridtable">
<tr>
	<th style="width:100px">日期</th>
	<th style="width:200px">包名</th>
	<th style="width:200px">名称</th>
	<th style="width:100px">cpid</th>
	<th style="width:100px">当天存量</th>
	<th style="width:100px">已做留存</th>
	<th style="width:100px">完成率</th> 
</tr>
<% 
 String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
 for(int i=0;i<list.size();i++){
	 com.jfinal.plugin.activerecord.Record record=list.get(i);
%>
<tr <%if(nowdate.equals(record.get("adddate").toString())){%>style="color:red"<%} %>>
	<td><%=record.get("adddate") %></td>
	<td><%=record.get("packname") %></td>
	<td><%=record.get("name") %></td>
	<td><%=record.get("cpid") %></td>
	<td><%=record.get("cnt") %></td>
	<td><%=record.get("cntb") %></td>
	<td <%if(record.getLong("cntb")!=null&&record.getLong("cntb")/record.getLong("cnt")==1){%>style="color:#7CFC00"<%} %>><%=record.getLong("cntb")!=null?(int)(record.getLong("cntb")*1.001/(record.getLong("cnt")*1.001)*100):0%>%</td>
</tr>
<%} %>
</table>
    
	</body>
	<!-- end: BODY -->
</html>