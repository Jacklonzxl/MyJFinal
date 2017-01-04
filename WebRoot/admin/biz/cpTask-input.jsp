 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>CP信息 <small>编辑CP基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">CP信息</a></li>
<li class="active">编辑CP信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑CP信息</h2>
	 
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
					    <input type="hidden" id="cpTask.id" name="cpTask.id" value="${cpTask.id}" >
					    <input type="hidden" id="cpTask.created_at" name="cpTask.created_at" value="${cpTask.created_at}" >
					    <input type="hidden" id="cpTask.cnt" name="cpTask.cnt" value="${cpTask.cnt}" >
					    <input type="hidden" id="cpTask.type" name="cpTask.type" value="${cpTask.type}" >
						<div class="form-group">
							<label class="control-label">
							  名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="cpTask.name" name="cpTask.name" value="${cpTask.name}">
						</div> 
						<div class="form-group">
							<label class="control-label">
							 cpid<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.cpid" name="cpTask.cpid" value="${cpTask.cpid}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 包名<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.packname" name="cpTask.packname" value="${cpTask.packname}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 apkurl<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.apkurl" name="cpTask.apkurl" value="${cpTask.apkurl}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 apkpath<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.apkpath" name="cpTask.apkpath" value="${cpTask.apkpath}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 日推送量 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.daycnt" name="cpTask.daycnt" value="${cpTask.daycnt}">
						</div>	
						<div class="form-group">
							<label class="control-label">
							 推送手机 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTask.cids" name="cpTask.cids" value="${cpTask.cids}">
						</div>					 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input type="radio" value="0" name="cpTask.status" class="grey" <c:if test="${cpTask!=null&&cpTask.status==0}">checked="checked"</c:if>>
							   启用
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input  type="radio" value="1" name="cpTask.status" class="grey" <c:if test="${cpTask==null||cpTask.id==0||cpTask.status==1}">checked="checked"</c:if> >
							   停用
							   							   
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