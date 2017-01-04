<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.my.app.bean.sec.User"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>后台模板</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
<script src="${pageContext.request.contextPath}/wxphone/laydate/laydate.js"></script>
</head>
<body>
<div  style="width:100%;padding-left:20px">
    <form action="${pageContext.request.contextPath}/wxphone/userpay/paylog" method="post" id="form1">
	<div class="page_title">
       <h2 class="fl">消费明细</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
      </div>
     <section style="padding-top:10px;">
 
      <div style="float:right;padding-bottom:10px">
      &nbsp;&nbsp;&nbsp;&nbsp; 支出时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:30px;width:120px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:30px;width:120px" placeholder=""/> 
      
      <shiro:hasRole name="R_ADMIN">  
 
      
                  用户:      
      <select class="select" style="height:32px;width:70px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>  
                   
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th style="width:5%">序号</th>
        <th style="width:8%;text-align:center;">类别</th>
        <th style="width:30%;text-align:center;">名称</th>  
        <th style="width:10%;text-align:center;">数量</th>
        <th style="width:10%;text-align:center;">单价</th>
        <th style="width:10%;text-align:center;">金额</th>     
        <th style="width:22%;text-align:center;">时间</th>
        <th style="width:10%;text-align:center;">状态</th>  
        <shiro:hasRole name="R_ADMIN">  <th>操作</th></shiro:hasRole>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:5%;">${(pageNum-1)*pageSize+i}</td>
   
        <td style="width:8%;text-align:center;">
        <c:if test="${list.type==1 }">关注</c:if>
        <c:if test="${list.type==2 }">阅读</c:if>
       </td> 
        <td style="width:19%"><div class="cut_title ellipsis">${list.title}${list.public_account}</div></td>
        <td>${list.cnt}</td>
        <td>${list.price}</td> 
        <td>${list.money}</td>      
        <td>${list.adddate}</td>
        <td>
        <c:if test="${list.status==1 }">完成</c:if>
        <c:if test="${list.status==2 }">撤单</c:if>
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
				   <a style="padding:5px" href="?pageNum=${pageSize}">${pageSize}</a>
				 </c:if>
				 </c:if>
			  </c:forEach>
		      
		       <a  href="?pageNum=${psize}">最后一页</a>
		      </aside>
		      </div>
     </section>
     </form>
</div>
</body>
</html>