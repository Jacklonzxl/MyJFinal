<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html >
	<!--<![endif]-->
	<!-- start: HEAD -->
	<head>
		<title>用户登录</title> 
		<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
				
	</head>
	<!-- end: HEAD -->
	<!-- start: BODY -->
	<body style="padding-top:10%">
		<div style="width:60%;padding-left:35%" >
			 
			    <p><b>请输入用户名和密登录</b><span  <c:if test="${param.error!='1'}">style="display:none"</c:if> >
							<i class="fa fa-remove-sign"></i>
							<span id="errordiv" style="color:red">(
							<c:if test="${param.error=='1'}" >用户名或密码错误!</c:if>
							<c:if test="${param.error!='1'}">用户名和密码填写不规范，请您正确填写.</c:if>)
							</span> 
						</span> </p>
				<div class="box-login">
				 
					<form class="form-login" action="/login/dologin">
						
							<div class="form-group">
								<span class="input-icon">
									<input type="text" class="form-control" name="username" placeholder="用户名">
									<i class="fa fa-user"></i> </span>
							</div>
							<div class="form-group form-actions">
								<span class="input-icon">
									<input type="password" class="form-control password" name="password" placeholder="密 码">
									 
									</span>
							</div>
							<div class="form-actions"> 
								<button type="submit" class="btn btn-green pull-left" style="width:30%">
									登录 <i class="fa fa-arrow-circle-right"></i>
								</button>
							</div> 
					</form>
					<!-- start: COPYRIGHT -->
					<div class="copyright">
						 
					</div>
					<!-- end: COPYRIGHT -->
				</div>
  
	 
		</div>
	 <script>
	 if(top.location!=self.location){
		 top.location = self.location;
		 }
	 </script>
	</body>
	<!-- end: BODY -->
</html>