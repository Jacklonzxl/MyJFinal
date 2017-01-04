 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>用户分组 <small>编辑权限用户分组</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">用户分组</a></li>
<li class="active">编辑用户分组</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑用户分组</h2>
	 
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
					    <input type="hidden" id="group.id" name="group.id" value="${group.id}" >
					    <input type="hidden" id="group.created_at" name="group.created_at" value="${group.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="group.name" name="group.name" value="${group.name}">
						</div> 
						<div class="form-group">
							<label class="control-label">
							 说明 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="group.intro" name="group.intro" value="${group.intro}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 排序 <span class="symbol required"></span>
							</label>
							<input type="number" placeholder="" class="form-control" required="required"  id="group.theindex" name="group.theindex" value="<c:if test="${group!=null&&group.id>0}">${group.theindex}</c:if><c:if test="${group==null||group.id==0}">0</c:if>">
						</div>						 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="group.status" class="grey" <c:if test="${group==null||group.id==0||group.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="group.status" class="grey" <c:if test="${group!=null&&group.status==0}">checked="checked"</c:if>>
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