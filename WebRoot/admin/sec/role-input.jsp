 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>角色信息 <small>编辑角色基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">角色信息</a></li>
<li class="active">编辑角色信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑角色信息</h2>
	 
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
					    <input type="hidden" id="role.id" name="role.id" value="${role.id}" >
					    <input type="hidden" id="role.created_at" name="role.created_at" value="${role.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="role.name" name="role.name" value="${role.name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 编码 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="role.value" name="role.value" value="${role.value}">
						</div>
						<div class="form-group" >
							<label class="control-label">
							用户角色  <span class="symbol  "></span>
							</label>
							<div>
							<c:forEach items="${permissionList}" var="list">
							<label class="checkbox-inline">
							<input type="checkbox" class="red" id="permission_ids" name="permission_ids" value="${list.id}" <c:if test="${list.role_id!=null}">checked="checked"</c:if> >
							${list.name}
							</label>
							</c:forEach>
							</div>						 
						</div>
						<div class="form-group">
							<label class="control-label">
							 说明 <span class="symbol  "></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="role.value" name="role.value" value="${role.intro}">
						</div>						 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="role.status" class="grey" <c:if test="${role==null||role.id==0||role.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="role.status" class="grey" <c:if test="${role!=null&&role.status==0}">checked="checked"</c:if>>
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