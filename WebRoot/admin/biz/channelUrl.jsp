 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- start: PAGE HEADER -->
<!-- start: TOOLBAR -->
<div class="toolbar row">
	<div class="col-sm-6 hidden-xs">
		<div class="page-header">
			<h1>渠道链接信息 <small>查看及管理渠道链接信息</small></h1>
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
<li class="active">渠道链接信息</li>
</ol>
</div>
</div>
<!-- start: BREADCRUMB -->
<!-- end: BREADCRUMB -->
<div class="row">
	<div class="col-md-12">
		<!-- start: TABLE WITH IMAGES PANEL -->
		<div class="panel panel-white">
			<div class="panel-heading">
				 <p>
				<button class="btn btn-success " type="button" onclick="loadPage('${path}/input?id=0','${path}/save','${m1}','${m2}');">
				 <i class="glyphicon glyphicon-plus"></i><span> 添 加 </span>
				 </button>
				 <button class="btn btn-danger " type="button"  onclick="delinfos('${path}/del','${m1}','${m2}')">
				 <i class="glyphicon glyphicon-trash"></i><span> 删 除 </span> 
				 </button>
				</p>
				<div class="panel-tools">										
					<div class="dropdown">
					<a data-toggle="dropdown" class="btn btn-xs dropdown-toggle btn-transparent-grey">
				    <i class="fa fa-cog"></i>
					</a>
					<ul class="dropdown-menu dropdown-light pull-right" role="menu">
					<li>
						<a class="panel-collapse collapses" href="#"><i class="fa fa-angle-up"></i> <span>Collapse</span> </a>
					</li>
					<li>
						<a class="panel-refresh" href="#"> <i class="fa fa-refresh"></i> <span>Refresh</span> </a>
					</li> 
					<li>
						<a class="panel-expand" href="#"> <i class="fa fa-expand"></i> <span>Fullscreen</span></a>
					</li>										
					</ul>
					</div>
					 
				</div>
			</div>
			<div class="panel-body">
			
				<div class="row">
				<div class="col-md-12">
				<div id="sample_1_length" class="dataTables_length">
				<label>&nbsp;&nbsp;&nbsp;&nbsp;渠道:&nbsp;
				<select style="width:200px" id="channelid" name="channelid"  >
				<option value="-1"  selected="selected">全部</option>
				<c:forEach items="${clist}" var="clist">
				 <option value="${clist.id}" <c:if test ="${clist.id==param.channelid}">selected</c:if> >${clist.name}</option>
				</c:forEach>
				</select>
				</label>
				<label>&nbsp;&nbsp;&nbsp;&nbsp;状态:&nbsp;
				<select size="1" id="status" name="status"  >
				<option value="-1"  selected="selected">全部</option>
				<option value="1" <c:if test="${param.status=='1'}">selected</c:if> >正常</option>
				<option value="0" <c:if test="${param.status=='0'}">selected</c:if> >关闭</option> 
				</select> &nbsp;&nbsp;&nbsp;&nbsp;关键字:&nbsp;</label>
				<label><input type="text" aria-controls="sample_1" class="form-control input-sm"  name="keyword" id="keyword" placeholder="Search" value="${param.keyword}"></label>
				&nbsp;&nbsp;<button class="btn btn-default btn-sm " type="button" onclick="loadUrl('${path}?pageNum=1')">
				 <i class="glyphicon glyphicon-search"></i><span>搜索 </span>
				 </button>
				</div></div>
				</div>
				<input type="hidden" name="pageNum" id="pageNum" value="${pageNum}"> 
				<table class="table table-bordered table-hover" id="sample-table-2">
				<thead>
				<tr>
					<th class="center"><div class="checkbox-table"><label><input type="checkbox" id="checkids"   name="checkids" value="None" onclick="selectAll(this)" class="flat-grey selectall"></label></div></th>
					<th class="center">序号</th>  
					<th class="hidden-xs">渠道ID</th>
					<th class="hidden-xs">渠道名称</th>
					<th class="hidden-xs">广告主</th>
					<th class="hidden-xs">链接名称</th>
					<th class="hidden-xs">跳转地址</th>
					<th class="hidden-xs">状态</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<c:set var="i" value="0"></c:set>
				<c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set>  
				<tr>
					<td class="center" style="5%">
					<div class="checkbox-table"><label><input type="checkbox" class="flat-grey foocheck" name="ids" id="ids" value="${list.id}" ></label></div>
					</td>
					<td class="center" style="10%">${(pageNum-1)*pageSize+i}</td> 
					<td class="hidden-xs">${list.channelid}</td>
					<td class="hidden-xs">${list.channelname}</td>
					<td class="hidden-xs">${list.advertisers}</td>				
					<td class="hidden-xs">${list.name}</td>
					<td class="hidden-xs" style="width:20%;word-break:break-all;">${list.url}</td>
					<td class="hidden-xs">${list.status}</td>
					<td class="center">
					<div class="visible-md visible-lg hidden-sm hidden-xs">
						<a href="javascript:loadPage('${path}/input?id=${list.id}','${path}/save','${m1}','${m2}'); " class="btn btn-xs btn-blue tooltips" data-placement="top" data-original-title="Edit"><i class="fa fa-edit"></i></a>
						&nbsp;&nbsp;&nbsp;
						<a href="javascript:delinfo('${path}/del',${list.id},'${m1}','${m2}');" class="btn btn-xs btn-red tooltips" data-placement="top" data-original-title="Remove"><i class="fa fa-times fa fa-white"></i></a>
					    &nbsp;&nbsp;&nbsp;
					</div>
					 </td>
				</tr>
               </c:forEach>
					</tbody>
				</table>
				<!-- 分页 -->
										 <div class="row">
				<div class="col-md-6">
				<div class="dataTables_info" id="sample_1_info">共${psize}页(${count}条记录)</div>
				</div>
				<div class="col-md-6">
				<div class="dataTables_paginate paging_bootstrap">
				<ul class="pagination pagination-blue">
				<li class="prev"><a href="javascript:loadUrl('${path}?pageNum=1')"><i class="fa fa-chevron-left"></i> </a></li>
				<c:forEach begin="1" end="${psize}" var="pageSize">
			       <c:if test="${pageSize<pageNum+5&&pageSize>pageNum-5}">
				   <c:if test="${pageSize==pageNum}">
				   <li  class="active"><a href="javascript:loadUrl('${path}?pageNum=${pageSize}')">${pageSize}</a></li>
				   </c:if>
			       <c:if test="${pageSize!=pageNum}"> 		      
				   <li><a href="javascript:loadUrl('${path}r?pageNum=${pageSize}')">${pageSize}</a></li>
				   </c:if>
				  </c:if>
				  </c:forEach> 
				<li class="next"><a href="javascript:loadUrl('${path}?pageNum=${psize}')"> <i class="fa fa-chevron-right"></i></a></li>
				</ul>
				</div>
				</div>
				</div>
			</div>
		</div>

		<!-- end: TABLE WITH IMAGES PANEL -->
	</div>
</div>