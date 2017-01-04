 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>权限信息 <small>编辑权限基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">权限信息</a></li>
<li class="active">编辑权限信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑权限信息</h2>
	 
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
					    <input type="hidden" id="permission.id" name="permission.id" value="${permission.id}" >
					    <input type="hidden" id="permission.created_at" name="permission.created_at" value="${permission.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="permission.name" name="permission.name" value="${permission.name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 编码 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="permission.value" name="permission.value" value="${permission.value}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 URL<span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="permission.url" name="permission.url" value="${permission.url}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 说明 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="permission.intro" name="permission.intro" value="${permission.intro}">
						</div>						 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="permission.status" class="grey" <c:if test="${permission==null||permission.id==0||permission.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="permission.status" class="grey" <c:if test="${permission!=null&&permission.status==0}">checked="checked"</c:if>>
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