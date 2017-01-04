<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page contentType="text/html;charset=utf-8"%>  
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
       <!--title-->
        
       <!--content-->
        
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wxphone/followtask/save" method="post" id="form2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">  
       
          <span>公众号:</span>
          <input type="text" style="width:500px" <c:if test="${bean.id>0}">disabled="disabled"</c:if> id="public_account" name="bean.public_account" value="${bean.public_account}" placeholder="" class="textbox"/>
  		<br><br>
          <span>关注数:</span>
          <input type="text" style="width:500px" id="total_quantity" name="bean.total_quantity" value="${bean.total_quantity}"  class="textbox"  placeholder="最大值为${maxfollow}"  onkeyup="setonkeyup(this,${maxfollow})" />
        <br><br>
          <span>回&nbsp;&nbsp;&nbsp;复:</span>
          <input type="text" id="reply_content" style="width:500px" name="bean.reply_content"  value="${bean.reply_content}" placeholder="" class="textbox"/>
       <br><br>
       
        
        
     
       <!--以pop_cont_text分界-->
      
       <!--bottom:operate->button-->
    
        <!-- <input type="submit" value="确认" style="width:80px;background-color:#CFCFCF;"  />  -->
        <div style="padding-left:25%">
        <button class="btn btn-success" style="width:80px;background-color:#CFCFCF" type="submit" >确认</button>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="${pageContext.request.contextPath}/wxphone/followtask"><input type="button" value="返回" style="width:80px;background-color:#CFCFCF;"></input></a>
       </div>
      </form>
      <div style="width:100%;color: green;margin-top:20px">  
<shiro:lacksRole name="R_ADMIN"><b>&nbsp;&nbsp;&nbsp;当前关注单价<%=Math.floor(channel.getFloat("followprice")*10000*1F+0.1)%>元/万</b></shiro:lacksRole> 
</div>
     </div> 
   
  </body>
</html>