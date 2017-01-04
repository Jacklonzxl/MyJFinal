<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.my.app.bean.sec.User"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%

User user=(User)session.getAttribute("dbuser");
String username=user.getStr("username");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手刷管理后台</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/index.css" type="text/css" media="screen" />

<script type="text/javascript" src="${pageContext.request.contextPath}/wxphone/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/wxphone/js/tendina.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/wxphone/js/common.js"></script>

</head>
<body>
    <!--顶部-->
    <div class="layout_top_header">
            <div style="float: left"><span style="font-size: 16px;line-height: 45px;padding-left: 20px;color: #8d8d8d">手刷管理后台 &nbsp; &nbsp; &nbsp;<span style="color:green;"><%=user.getStr("username")%>，欢迎您！</span><a href="/login/loginout" style="color:blue;" >退出后台</a></span></div>
            <!-- <div id="ad_setting" class="ad_setting">
                <a class="ad_setting_a" href="javascript:; ">
                    <i class="icon-user glyph-icon" style="font-size: 20px"></i>
                    <span>管理员</span>
                    <i class="icon-chevron-down glyph-icon"></i>
                </a>
                <ul class="dropdown-menu-uu" style="display: none" id="ad_setting_ul">
                    <li class="ad_setting_ul_li"> <a href="javascript:;"><i class="icon-user glyph-icon"></i> 个人中心 </a> </li>
                    <li class="ad_setting_ul_li"> <a href="javascript:;"><i class="icon-cog glyph-icon"></i> 设置 </a> </li>
                    <li class="ad_setting_ul_li"> <a href="javascript:;"><i class="icon-signout glyph-icon"></i> <span class="font-bold">退出</span> </a> </li>
                </ul>
            </div> -->
    </div>
    <!--顶部结束-->
    <!--菜单-->
    <div class="layout_left_menu">
        <ul id="menu">
            <li   class="childUlLi selected opened" onclick="menuFrame.location.href='${pageContext.request.contextPath}/wxphone/link'">
                <a href="${pageContext.request.contextPath}/wxphone/link"  target="menuFrame"> <i class="glyph-icon icon-location-arrow"></i>阅读管理</a>
                <ul><li></li></ul>
            </li>
            <li   class="childUlLi " onclick="menuFrame.location.href='${pageContext.request.contextPath}/wxphone/followtask'">
                <a href="${pageContext.request.contextPath}/wxphone/followtask"  target="menuFrame"> <i class="glyph-icon icon-location-arrow"></i>关注管理</a>
                <ul><li></li></ul>
             </li>
            <li   class="childUlLi "  onclick="menuFrame.location.href='${pageContext.request.contextPath}/wxphone/reporttask'">
                <a href="${pageContext.request.contextPath}/wxphone/reporttask"  target="menuFrame"> <i class="glyph-icon icon-location-arrow"></i>统计报表</a>
                <ul><li></li></ul>
             </li>            
            <li class="childUlLi">
                <a href="#">   <i class="glyph-icon  icon-reorder"></i>账户管理</a>  
                <ul>
                    <li><a href="${pageContext.request.contextPath}/wxphone/userpay/pay" target="menuFrame"><i class="glyph-icon icon-chevron-right"></i>线上充值</a></li>
                    <li><a href="${pageContext.request.contextPath}/wxphone/userpay" target="menuFrame"><i class="glyph-icon icon-chevron-right"></i>充值明细</a></li>
                    <li><a href="${pageContext.request.contextPath}/wxphone/userpay/paylog" target="menuFrame"><i class="glyph-icon icon-chevron-right"></i>消费明细</a></li> 
                </ul>
            </li>
        </ul>
    </div>
    <!--菜单-->
    <div id="layout_right_content" class="layout_right_content">

        <!--<div class="route_bg">
            <a href="#">主页</a><i class="glyph-icon icon-chevron-righ<t"></i>
            <a href="#">菜单管理</a>
        </div>-->
        <div class="mian_content">
            <div id="page_content">
               <iframe id="menuFrame" name="menuFrame" src="${pageContext.request.contextPath}/wxphone/link" style="overflow:visible;"
                        scrolling="yes" frameborder="no" width="100%" height="100%"></iframe>
            </div> 
        </div>
    </div>
    <div class="layout_footer">
        <p>Copyright © 2016</p>
    </div>
    
   
</body>
</html>