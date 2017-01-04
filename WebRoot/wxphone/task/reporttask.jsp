<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.my.app.bean.sec.User"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>链接统计</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<link rel="stylesheet" href="css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="utilLib/bootstrap.min.css" type="text/css" media="screen" />


<script src="${pageContext.request.contextPath}/wxphone/laydate/laydate.js"></script>
</head>
<body>
<div  style="width:100%">
    <form action="${pageContext.request.contextPath}/wxphone/reporttask" method="post" id="form3">
    
      
      <div style="float:right;padding-bottom:5px;padding-top: 10;px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:30px;width:120px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:30px;width:120px" placeholder=""/> 
                  状态:
      <select class="select" style="height:30px;width:79px" id="status" name="status">
       <!-- <option value="-2">-选择-</option>
       <option value="-1" <c:if test="${param.status=='-1'}">selected</c:if> >未开始</option> --> 
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >进行中</option>     
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
       <shiro:hasRole name="R_ADMIN">
       <option value="3" <c:if test="${param.status=='3'}">selected</c:if> >异常单(未结算)</option>
       </shiro:hasRole>
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
      &nbsp;&nbsp;
      <button type="submit">查询</button>
      </div>      
		<table width="90%" class="table"> 
		<tr> 
		<th>序号</th>
        <th>日期</th>
        <th>计划阅读</th>
        <th>实际阅读</th>
        <th>计划点赞</th>
        <th>实际点赞</th>
        <th>状态</th>
		</tr> 
	   <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:100px;text-align:center;">
        <c:if test="${count==i-1}">合计</c:if>
        <c:if test="${count!=i-1}">${list.thedate}</c:if>
        </td>             
        <td>${list.total_quantity}</td>
        <td>${list.real_quantity}</td>
        <td>${list.praise_quantity}</td>
        <td>${list.real_praise}</td>
        <td style="text-align:center;">
        <c:if test="${param.status=='0'}">未完成</c:if>
        <c:if test="${param.status=='1'||param.status==null||param.status==''}">已完成</c:if>
        <c:if test="${param.status=='2'}">已撤单</c:if>
        <c:if test="${param.status=='-3'}">全部</c:if>
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
				   <a style="padding:5px" href="?pageNum=${pageSize}">${pageSize}</a>
				 </c:if>
				 </c:if>
			  </c:forEach>
		      
		       <a  href="?pageNum=${psize}">最后一页</a>
		      </aside>
		      </div>
    </form>
</div>
</body>
</html>