﻿ <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>渠道链接信息 <small>编辑渠道链接基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">渠道链接信息</a></li>
<li class="active">编辑渠道链接信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑渠道链接信息</h2>
	 
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
					    <input type="hidden" id="channelUrl.id" name="channelUrl.id" value="${channelUrl.id}" >
					    <input type="hidden" id="channelUrl.created_at" name="channelUrl.created_at" value="${channelUrl.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  所属渠道 <span class="symbol"></span>
							</label>
							<select  class="form-control search-select" id="channelUrl.channelid" name="channelUrl.channelid">
							<option value="-1">&nbsp;</option>
							<c:forEach items="${clist}" var="clist">
							<option value="${clist.id}" <c:if test ="${clist.id==channelUrl.channelid}">selected</c:if> >${clist.name}</option>
							</c:forEach>
							</select>

						</div>
						
						<div class="form-group">
							<label class="control-label">
							广告主<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channelUrl.advertisers" name="channelUrl.advertisers" value="${channelUrl.advertisers}">
						</div>
						<div class="form-group">
							<label class="control-label">
							  链接名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="channelUrl.name" name="channelUrl.name" value="${channelUrl.name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 链接地址 <span class="symbol required" ></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required"    id="channelUrl.url" name="channelUrl.url" value="${channelUrl.url}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 备注 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channelUrl.content" name="channelUrl.content" value="${channelUrl.content}">
						</div>	
						<div class="form-group">
							<label class="control-label">
							 每日限量<span class="symbol"></span>
							</label>
							<input type="number" placeholder="" class="form-control"   id="channelUrl.daycnt" name="channelUrl.daycnt" value="${channelUrl.daycnt}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 排序<span class="symbol"></span>
							</label>
							<input type="number" placeholder="" class="form-control"   id="channelUrl.theindex" name="channelUrl.theindex" value="${channelUrl.theindex}">
						</div>					 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="channelUrl.status" class="grey" <c:if test="${channelUrl==null||channelUrl.id==0||channelUrl.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="channelUrl.status" class="grey" <c:if test="${channelUrl!=null&&channelUrl.status==0}">checked="checked"</c:if>>
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