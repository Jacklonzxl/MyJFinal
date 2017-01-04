<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
<title>Insert title here</title>

</head>
<body>     
<div style="width: 700px;height: 300px;left:50%;margin-left:150px;top:10%;margin-top:10px"> 
        
 
       <!--title-->
       <h3>添加批量关注任务信息</h3>
       <!--content-->
       <form action="${pageContext.request.contextPath}/wxphone/${m_name}/save" method="post" id="form2">
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        
         <textarea class="textarea"  id="tasks" name="tasks"   placeholder="公众号+空格+关注数量"  style="height:300px;width:80%;font-size:8px;border-style:solid;border-color:#000000;"></textarea>
        <!-- 
        <ul>              
         <li style="text-align:center;">  
          <textarea class="textarea"  id="tasks" name="tasks"   placeholder="每行一个任务(公众号+空格+关注数量),可直接从Excel复制到这里粘贴"  style="height:300px;width:80%;"></textarea>
         </li> 
        </ul>
         -->
       
       </div>
       <br> 
       <!--以pop_cont_text分界--> 
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="submit" value="确认" style="width:80px;background-color:#CFCFCF;" />
     	<a href="${pageContext.request.contextPath}/wxphone/follow"><input type="button" value="返回" style="width:80px;background-color:#CFCFCF;"></input></a>
        
     
       </div> 
           </form>
 </div>
 </body>
 </html>    
  
