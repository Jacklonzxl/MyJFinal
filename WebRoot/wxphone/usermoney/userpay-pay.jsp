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
    <form action="${pageContext.request.contextPath}/wxphone/userpay/paysave" method="post" id="form1">
    
      <div class="page_title" style="">
       <h2 class="fl">线上充值</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
  
   
     <div style="padding-top:20px"> 
	  
	 <h3><strong style="color:#000000;font-size:0.8em">充值请直接转账到支付宝账号（ 18588438890 姓名：*承杰）转账完成号请填写以下信息</strong></h3> 
	 </div>


      <ul class="ulColumn2" style="padding-top:43px">
        </br>
        <div   style="font-size:1em;padding-bottom:10px">输入交易号：</div>
        <input name="payid"  id="payid" type="text" class="textbox textbox_295"  style="width:50%" placeholder="转账完成后，点击付款页面的“查看详情”，记录下 28 或 32 位的交易号。"/>
        </br></br>
        <div   style="font-size:1em;padding-bottom:10px">请输入金额：</div>
        <input name="money"  id="money" type="text" class="textbox textbox_295"  style="width:50%"  placeholder=""/>
        </br></br>
        <input type="submit" class="link_btn"/>
       
       </ul>
 
     </form>
</div>
</body>
</html>