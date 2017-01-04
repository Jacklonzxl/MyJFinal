<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <%@page import="com.my.app.wx.bean.Channel"%>
<%@page import="com.my.app.bean.sec.User"%>
<%@page import="com.jfinal.plugin.redis.Redis"%>
<%@page import="com.jfinal.plugin.redis.Cache"%>
<%@page import="redis.clients.jedis.Jedis"%>
<%@page import="com.my.util.TimeUtil"%>
<%@page import="com.my.app.WxApiController"%>
<%@page import="com.my.app.bean.sec.User"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
Cache userCache= Redis.use("userc");
Jedis jedis = userCache.getJedis();
String notice=jedis.get("notice")!=null?jedis.get("notice"):"";
jedis.close();
User user=(User)session.getAttribute("dbuser");
Channel channel=Channel.dao.findFirst("select * from biz_channel where userid='"+user.get("id")+"'");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>后台模板</title>
<script type="text/javascript" src="js/jquery.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />

</head>

<body>
 
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<div  style="width:100%">
    <form method="post" id="form1">
 

      <a href="${pageContext.request.contextPath}/wxphone/link/input"><input style="height:20;width:70px;color: black" type="button" value="添加" /></a>
      <a href="${pageContext.request.contextPath}/wxphone/link/batchinput"><input type="button" value="批量添加" style="height:20;width:90px;color: black"/></a>
    
        <!-- <button id="showPopTxt" ><a href="${pageContext.request.contextPath}/wxphone/link/input" target="menuFrame">添加</a></button>   -->
        <!-- <button id="showPopTxt" >批量添加</button>  -->
     
      
      <div style="float:right;padding-bottom:5px;padding-top: 10;px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:30px;width:100px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:30px;width:100px" placeholder=""/> 
                  状态:
      <select class="select" style="height:30px;width:79px" id="status" name="status">
       <!-- <option value="-2">-选择-</option>  -->
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >进行中</option>     
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
       <option value="3" <c:if test="${param.status=='3'}">selected</c:if> >异常单(未结算)</option>
       <option value="4" <c:if test="${param.status=='4'}">selected</c:if> >异常单</option>
      </select> 
      <shiro:hasRole name="R_ADMIN">
                  下单人:
      <select class="select" style="height:30px;width:79px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>
      <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:30px;width:160px" placeholder="关键字（标题或链接）"/> 
      &nbsp;&nbsp;<input type="submit" value="查询" style="height:20;width:70px"/>
      
      
      </div>
       
		<table width="90%" class="table"> 
		<tr> 
		<th width="5%" style="text-align:center;" >序号</th>
		<th width="25%">标题</th>
		<th width="9%" style="text-align:center;">计划阅读</th> 
		<th width="9%" style="text-align:center;">实际阅读</th> 
		<th width="7%" style="text-align:center;">状态</th>
		<th width="18%" style="text-align:center;">下单时间</th>
		<th width="18%" style="text-align:center;">完成时间</th>
		<th width="10%" style="text-align:center;">操作</th>
		
		</tr> 
		<c:forEach var="list" items="${list }"><c:set var="i" value="${i+1}"></c:set> 
			<tr> 
				<td align="center">${(pageNum-1)*10+i}</td>
				<td align="left" class="ellipsis"><a href="${list.url}" target="_Blank">${list.title }</a></td>
				<td align="center">${list.total_quantity }</td>
				<td align="center">${list.push_quantity }</td>
				
				<td align="center">
				<c:if test="${list.status=='0'}">进行中</c:if>
				<c:if test="${list.status=='1'}">已完成</c:if>
				<c:if test="${list.status=='2'}">已撤单</c:if>
				<c:if test="${list.status=='3'}">异常单</c:if></td>
			 
				<td align="center">${list.order_time }</td>
				<td align="center">${list.finish_time }</td>
				<td align="center"><a href="${pageContext.request.contextPath}/wxphone/link/input?id=${list.id}">修改</a> 
				&nbsp;&nbsp;&nbsp;
				<a href="${pageContext.request.contextPath}/wxphone/link/cancel?id=${list.id}">撤单</a> 
				</td>
			</tr>   
		</c:forEach> 
	
		</table> 
		
        <div class="control-group">
            <label class="laber_from" ></label>
		       <aside class="paging">查找到${count}条记录
		       <a href="?pageNum=1">第一页</a>
		       <c:forEach begin="1" end="${psize}" var="pageSize">
				 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
				  <c:if test="${pageSize==pageNum}">
				    <a style="background:red;color:#19a97b;padding:5px" href="?pageNum=${pageSize}" >${pageSize}</a>
				  </c:if>
				 <c:if test="${pageSize!=pageNum}"> 		      
				    <a style="padding:5px" onclick="loadtable(${pageSize})">${pageSize}</a> 
				 </c:if>
				 </c:if>
			  </c:forEach>
		       	<a  onclick="loadtable(${psize})">最后一页</a>
		      </aside>
		      </div>
    </form>
</div>
</body>
<script type="text/javascript">
   function loadtable(pageNum)
   {
	   
	   form1.action="${pageContext.request.contextPath}/wxphone/link?pageNum="+pageNum;
	   form1.submit();
	   
   }
 </script>  
</html>