 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>用户信息 <small>编辑用户基本信息</small></h1>
		</div>
	</div>
	<jsp:include page="../common/toolsbar.jsp"></jsp:include>
</div>
<!-- end: TOOLBAR -->
<!-- end: PAGE HEADER -->
<!-- start: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
<ol class="breadcrumb">
<li><a href="#"> 首页</a></li>
<li><a href="javascript:loadUrl('${path}?pageNum=1')">用户信息</a></li>
<li class="active">编辑用户信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑用户信息</h2>
	 
		</div>
		<div class="panel-body">
			
			 
			<hr>
			
				<div class="row">
					<div class="col-md-12">
						<div class="errorHandler alert alert-danger no-display">
							<i class="fa fa-times-sign"></i> You have some form errors. Please check below.
						</div>
						<div class="successHandler alert alert-success no-display">
							<i class="fa fa-ok"></i> Your form validation is successful!
						</div>
					</div>
					<div class="col-md-6">
					    <input type="hidden" id="usre.id" name="user.id" value="${user.id}" >
					    <input type="hidden" id="usre.created_at" name="user.created_at" value="${user.created_at}" >
					    <div class="form-group">
							<label class="control-label">
							姓名 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control"  required="required" id="user.full_name" name="user.full_name" value="${user.full_name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 账户名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="user.username" name="user.username" value="${user.username}">
						</div>
						<div class="form-group">
							<label class="control-label">
							密码 <span class="symbol required"></span>
							</label>
							<input type="password" class="form-control" required="required" name="user.password" id="password" value="${user.password}">
						</div>
						<div class="form-group">
							<label class="control-label">
							确认密码 <span class="symbol required"></span>
							</label>
							<input type="password" class="form-control" id="password_again" name="password_again"  value="${user.password}">
						</div>						
						<div class="form-group" >
							<label class="control-label">
							用户分组  <span class="symbol  "></span>
							</label>
							<div>
							<c:forEach items="${groupList}" var="list">
							<label class="checkbox-inline">
							<input type="radio" class="red" id="user.group_id" name="user.group_id" value="${list.id}" <c:if test="${((user==null||user.group_id==0)&&list.id==1)||list.id==user.group_id}">checked="checked"</c:if> >
							${list.name}
							</label>
							</c:forEach>
							</div>						 
						</div>
			    </div>
					<div class="col-md-6">					

						<div class="form-group">
							<label class="control-label">
							手机号码  <span class="symbol  "></span>
							</label>
							<input type="text" placeholder="" class="form-control" id="user.mobile" name="user.mobile" value="${user.mobile}">
						</div>
						<div class="form-group" >
							<label class="control-label">
							Email  <span class="symbol  "></span>
							</label>
							<input type="email" placeholder="" class="form-control" id="user.email" name="user.email" value="${user.email}">
						 

						</div>
						<div class="form-group" >
							<label class="control-label">
							用户角色  <span class="symbol  "></span>
							</label>
							<div>
							<c:forEach items="${roleList}" var="list">
							<label class="checkbox-inline">
							<input type="checkbox" class="red" id="role_ids" name="role_ids" value="${list.id}" <c:if test="${list.user_id!=null}">checked="checked"</c:if> >
							${list.name}
							</label>
							</c:forEach>
							</div>						 
						</div>
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol "></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="user.status" class="grey" <c:if test="${user==null||usre.id==0||user.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="user.status" class="grey" <c:if test="${user!=null&&user.status==0}">checked="checked"</c:if>>
							   关闭							   
							 </label>
							 </div>
						</div>
			    </div>					
				</div> 
				<div class="row"> 
					<div class="col-md-4">
						<button class="btn btn-yellow btn-block" type="submit">
						保存信息<i class="fa fa-arrow-circle-right"></i>
						</button>
					</div>
				</div>
		 
		</div>
	</div>
	<!-- end: FORM VALIDATION 1 PANEL -->
</div>
 </div>