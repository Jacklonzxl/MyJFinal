<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<!-- Template Name: Rapido - Responsive Admin Template build with Twitter Bootstrap 3.x Version: 1.0 Author: ClipTheme -->
<!--[if IE 8]><html class="ie8" lang="zh-CN"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="zh-CN"><![endif]-->
<!--[if !IE]><!-->
<html lang="zh-CN">
	<!--<![endif]-->
	<!-- start: HEAD -->
    <%@include file="../head.jsp" %>
	<!-- end: HEAD -->
	<!-- start: BODY -->
	<body>
		<!-- start: SLIDING BAR (SB) -->
        <%@include file="../slidingbar.jsp" %>
		<!-- end: SLIDING BAR -->
		<div class="main-wrapper">
			<!-- start: TOPBAR -->
			 <%@include file="../header.jsp" %>
			<!-- end: TOPBAR -->
			<!-- start: PAGESLIDE LEFT -->
            <jsp:include page="${pageContext.request.contextPath}/admin/common/pageslide-left.jsp"></jsp:include>
			<!-- end: PAGESLIDE LEFT -->
            <jsp:include page="${pageContext.request.contextPath}/admin/common/pageslide-right.jsp"></jsp:include>
			<!-- start: MAIN CONTAINER -->
			<div class="main-container inner">
				<!-- start: PAGE -->
				<div class="main-content">
				 
					<div class="container" id="maincontent">