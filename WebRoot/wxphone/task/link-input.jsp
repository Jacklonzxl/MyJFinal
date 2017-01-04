<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <%@page import="com.my.app.wx.bean.Channel"%>
<%@page import="com.my.app.bean.sec.User"%>
<%@page import="com.jfinal.plugin.redis.Redis"%>
<%@page import="com.jfinal.plugin.redis.Cache"%>
<%@page import="redis.clients.jedis.Jedis"%>
<%@page import="com.my.util.TimeUtil"%>
<%@page import="com.my.app.WxApiController"%>
<%@page import="com.my.app.bean.sec.User"%>
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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
<title>Insert title here</title>

</head>
<body>
    
      
<div style="width: 700px;height: 300px;left:50%;margin-left:150px;top:50%;margin-top:100px">
    <FORM action="${pageContext.request.contextPath}/wxphone/link/save" method="post" Id="from" >
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
          	 <LABEL>链&nbsp;&nbsp;&nbsp;接&nbsp;:</LABEL>&nbsp;&nbsp;
          	 <input class="input_from" type=text name="bean.url" value="${bean.url}" style="width:500px">
        
        	<br><br>
        
            <LABEL>阅读数&nbsp;:</LABEL>&nbsp;&nbsp;
            <input class="input_from" type=text name="bean.total_quantity" value="${bean.total_quantity}"  style="width:500px" placeholder="最大值为${maxread}">
        
        	<br><br>
      
      	
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <LABEL class="laber_from" ></LABEL>
            <button class="btn btn-success" style="width:80px;background-color:#CFCFCF" type="submit" >确认</button>&nbsp;&nbsp;&nbsp;&nbsp;
           <a href="${pageContext.request.contextPath}/wxphone/link"><input type="button" value="返回" style="width:80px;background-color:#CFCFCF;"></input></a>
      	
      	 
    </FORM>
      <div style="width:100%;color: green;margin-top:20px">  
<shiro:lacksRole name="R_ADMIN"><b> 受市场影响，价格调整
&nbsp;&nbsp;&nbsp;当前阅读单价<%=Math.floor(channel.getFloat("readprice")*10000*1F+0.1)%>元/万</b></shiro:lacksRole> 
</div>
</div>
	
</body>
</html>