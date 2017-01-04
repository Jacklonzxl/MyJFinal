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
    <%@include file="common/head.jsp" %>
	<!-- end: HEAD -->
	<!-- start: BODY -->
	<body>
		<!-- start: SLIDING BAR (SB) -->
        <%@include file="common/slidingbar.jsp" %>
		<!-- end: SLIDING BAR -->
		<div class="main-wrapper">
			<!-- start: TOPBAR -->
			 <%@include file="common/header.jsp" %>
			<!-- end: TOPBAR -->
			<!-- start: PAGESLIDE LEFT -->
            <jsp:include page="common/pageslide-left.jsp"></jsp:include>
			<!-- end: PAGESLIDE LEFT -->
            <jsp:include page="common/pageslide-right.jsp"></jsp:include>
            <c:if test="${param.pg!=null}">
			<c:set var="url" value="/${fn:replace(param.pg,'-','/')}"></c:set>	 
			</c:if>
			<c:if test="${param.pg==null}">  
			<c:set var="url" value="/sec/user"></c:set>
			</c:if>
			<c:set var="m1" value="m_sec"></c:set>
			<c:set var="m2" value="m_sec_user"></c:set>
			<c:set var="path" value="/sec/user"></c:set>
			<c:if test="${param.m1!=null}">
			<c:set var="m1" value="${param.m1}"></c:set>
			<c:set var="m2" value="${param.m2}"></c:set>
			<c:set var="path" value="${param.path}"></c:set>	 
			</c:if> 
			<!-- start: MAIN CONTAINER -->
			<div class="main-container inner">
				<!-- start: PAGE -->
				<div class="main-content"> 
				<div class="container" id="maincontent">
               <form action="#" role="form" id="form" novalidate="novalidate" method="post">
                 
                </form>
				</div> 
				<div class="subviews">
				<div class="subviews-container"></div>
			   </div>
				</div>
				<!-- end: PAGE -->
			</div>
			<!-- end: MAIN CONTAINER -->
			<!-- start: FOOTER -->
			<footer class="inner">
				<div class="footer-inner">
					<div class="pull-left">
						2016 &copy; xxx by xxxx.
					</div>
					<div class="pull-right">
						<span class="go-top"><i class="fa fa-chevron-up"></i></span>
					</div>
				</div>
			</footer>
			<!-- end: FOOTER -->
            <jsp:include page="common/toolsbar-info.jsp"></jsp:include>
		</div>
		
		<%@include file="common/js.jsp"%>
        <%@include file="common/indexjs.jsp"%>
	</body>
	<!-- end: BODY -->
</html>