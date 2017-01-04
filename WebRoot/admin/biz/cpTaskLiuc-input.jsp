 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>留存信息 <small>编辑留存基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">留存信息</a></li>
<li class="active">编辑留存信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑留存信息</h2>
	 
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
					    <input type="hidden" id="cpTaskLiuc.id" name="cpTaskLiuc.id" value="${cpTaskLiuc.id}" >
					    <input type="hidden" id="cpTaskLiuc.addtime" name="cpTaskLiuc.addtime" value="${cpTaskLiuc.addtime}" >
						<div class="form-group">
							<label class="control-label">
							  CPID <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="cpTaskLiuc.cpid" name="cpTaskLiuc.cpid" value="${cpTaskLiuc.cpid}">
						</div>
						<div class="form-group">
							<label class="control-label">
							  留存参数 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="cpTaskLiuc.liucs" name="cpTaskLiuc.liucs" value="${cpTaskLiuc.liucs}">
						</div> 
						<div class="form-group">
							<label class="control-label">
							 渠道<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTaskLiuc.channel" name="cpTaskLiuc.channel" value="${cpTaskLiuc.channel}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 包名<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTaskLiuc.packname" name="cpTaskLiuc.packname" value="${cpTaskLiuc.packname}">
						</div>
						<div class="form-group">
							<label class="control-label">
							日期<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="cpTaskLiuc.thedate" name="cpTaskLiuc.thedate" value="${cpTaskLiuc.thedate}">
						</div>						 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input type="radio" value="0" name="cpTaskLiuc.status" class="grey" <c:if test="${cpTaskLiuc!=null&&cpTaskLiuc.status==0}">checked="checked"</c:if>>
							  未开始
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input  type="radio" value="1" name="cpTaskLiuc.status" class="grey" <c:if test="${cpTaskLiuc==null||cpTaskLiuc.id==0||cpTaskLiuc.status==1}">checked="checked"</c:if> >
							 
							   已完成							   
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