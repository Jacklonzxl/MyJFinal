 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>渠道短信接口信息 <small>编辑渠道短信接口基本信息</small></h1>
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
<li><a href="javascript:loadUrl('${path}?pageNum=1')">渠道短信接口信息</a></li>
<li class="active">编辑渠道短信接口信息</li>
</ol>
</div>
</div>
<!-- end: BREADCRUMB -->
<div class="row">
<div class="col-md-12">
	<!-- start: FORM VALIDATION 1 PANEL -->
	<div class="panel panel-white">
		<div class="panel-heading"> 
		<h2><i class="fa fa-pencil-square"></i> 编辑渠道短信接口信息</h2>
	 
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
					    <input type="hidden" id="channelSms.id" name="channelSms.id" value="${channelSms.id}" >
					    <input type="hidden" id="channelSms.created_at" name="channelSms.created_at" value="${channelSms.created_at}" >
						<div class="form-group">
							<label class="control-label">
							  所属渠道 <span class="symbol"></span>
							</label>
							<select  class="form-control search-select" id="channelSms.channelid" name="channelSms.channelid">
							<option value="-1">&nbsp;</option>
							<c:forEach items="${clist}" var="clist">
							<option value="${clist.id}" <c:if test ="${clist.id==channelSms.channelid}">selected</c:if> >${clist.name}</option>
							</c:forEach>
							</select>

						</div>
						
						<div class="form-group">
							<label class="control-label">
							广告主<span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channelSms.advertiser" name="channelSms.advertiser" value="${channelSms.advertiser}">
						</div>
						<div class="form-group">
							<label class="control-label">
							  接口名称 <span class="symbol required"></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required" id="channelSms.name" name="channelSms.name" value="${channelSms.name}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 获取地址 <span class="symbol required" ></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required"    id="channelSms.geturl" name="channelSms.geturl" value="${channelSms.geturl}">
						</div>
						<div class="form-group">
							<label class="control-label">
							  提交地址 <span class="symbol required" ></span>
							</label>
							<input type="text" placeholder="" class="form-control" required="required"    id="channelSms.posturl" name="channelSms.posturl" value="${channelSms.posturl}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 备注 <span class="symbol"></span>
							</label>
							<input type="text" placeholder="" class="form-control"   id="channelSms.content" name="channelSms.content" value="${channelSms.content}">
						</div>	
						<div class="form-group">
							<label class="control-label">
							 每日限量<span class="symbol"></span>
							</label>
							<input type="number" placeholder="" class="form-control"   id="channelSms.daycnt" name="channelSms.daycnt" value="${channelSms.daycnt}">
						</div>
						<div class="form-group">
							<label class="control-label">
							 排序<span class="symbol"></span>
							</label>
							<input type="number" placeholder="" class="form-control"   id="channelSms.theindex" name="channelSms.theindex" value="${channelSms.theindex}">
						</div>					 						
						<div class="form-group">
							<label class="control-label">
							状态<span class="symbol required"></span>
							</label>
							<div>
							<label style="margin-left:3.2%" >
							 <input  type="radio" value="1" name="channelSms.status" class="grey" <c:if test="${channelSms==null||channelSms.id==0||channelSms.status==1}">checked="checked"</c:if> >
							   打开
		                     </label>
							 <label  style="margin-left:6%"  >
							 <input type="radio" value="0" name="channelSms.status" class="grey" <c:if test="${channelSms!=null&&channelSms.status==0}">checked="checked"</c:if>>
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