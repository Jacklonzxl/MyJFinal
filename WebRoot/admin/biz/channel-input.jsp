 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>渠道信息 <small>编辑渠道基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">渠道信息</a></li>
<li class="active">编辑渠道信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑渠道信息</h2>
	 
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
					    <input type="hidden" id="channel.id" name="channel.id" value="${channel.id}" >
					    <input type="hidden" id="channel.created_at" name="channel.created_at" value="${channel.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="channel.name" name="channel.name" value="${channel.name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							  用户名称 <span class="symbol"></span>
							</label>
							<select  class="form-control search-select" id="channel.userid" name="channel.userid">
							<option value="-1">&nbsp;</option>
							<c:forEach items="${ulist}" var="ulist">
							<option value="${ulist.id}" <c:if test ="${ulist.id==channel.userid}">selected</c:if> >${ulist.full_name}</option>
							</c:forEach>
							</select>

						</div>
						<div class="form-group">
							<label class="control-label">
							 省份<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channel.prov" name="channel.prov" value="${channel.prov}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 城市<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channel.city" name="channel.city" value="${channel.city}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 地址 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channel.address" name="channel.address" value="${channel.address}">
						</div>	
						<div class="form-group">
							<label class="control-label">
							 备注 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channel.content" name="channel.content" value="${channel.content}">
						</div>					 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="channel.status" class="grey" <c:if test="${channel==null||channel.id==0||channel.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="channel.status" class="grey" <c:if test="${channel!=null&&channel.status==0}">checked="checked"</c:if>>
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