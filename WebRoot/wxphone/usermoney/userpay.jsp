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

<script type="text/javascript" src="js/jquery.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
<script src="${pageContext.request.contextPath}/wxphone/laydate/laydate.js"></script>
</head>
<body>
<div  style="width:100%;padding-left:20px">
    <form action="${pageContext.request.contextPath}/wxphone/userpay" method="post" id="form1">
	<div class="page_title">
       <h2 class="fl">充值明细<span style="color:red">(余额:${userMoney.balance},赚送:${userMoney.give})</span></h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px;">
 
      <div style="float:right;padding-bottom:10px">
      &nbsp;&nbsp;&nbsp;&nbsp; 充值时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:30px;width:120px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:30px;width:120px" placeholder=""/> 
      
      <shiro:hasRole name="R_ADMIN">  
                 状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status"> 
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >待审核</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已审核</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已作废</option>
      </select>
      
                  用户:      
      <select class="select" style="height:32px;width:70px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>           
       &nbsp;&nbsp;<button type="submit" class="link_btn" >查询</button>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table"  >
       <tr>
        <th style="width:30%;">交易号</th>
        <th style="width:10%;">用户名</th> 
        <th style="width:10%;">充值方式</th>
        <th style="width:10%;">充值金额</th> 
        <th style="width:20%;">入账时间</th>
        <th style="width:10%;">状态</th>
        <shiro:hasRole name="R_ADMIN"><th>操作</th></shiro:hasRole>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="width:500px;">${list.alyid}</td>
 
        <td style="width:50%">${list.full_name}</td> 
        <td style="width:10%;">
        <c:if test="${list.type==1 }">支付宝</c:if>
        <c:if test="${list.type==2 }">微信</c:if>
        <c:if test="${list.type==3 }">充值</c:if>
        <c:if test="${list.type==4 }">赠送</c:if>
       </td> 
        <td style="width:10%;">${list.money}</td>      
        <td style="width:10%;">${list.reviewdate}</td>      
        <td style="width:10%;"><c:if test="${list.status==0 }">待审核</c:if><c:if test="${list.status==1 }">已入账</c:if></td>
        <shiro:hasRole name="R_ADMIN">       
        <td  style="text-align:center;"> 
         <a href="#" onclick="loadinput('${m_name}',${list.id})">
         <c:if test="${list.status==0 }">审核</c:if>
         <c:if test="${list.status!=0 }">查看</c:if>
         </a>
          
        </td>
        </shiro:hasRole>
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