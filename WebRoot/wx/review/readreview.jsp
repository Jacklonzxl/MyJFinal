 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">阅读审核列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
       
      <shiro:hasRole name="R_ADMIN">
      <input type="button" value="审核"  class="link_btn" onclick="loadreview(0)" /> 
      </shiro:hasRole>
      <div style="float:right;">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <option value="-2">-选择-</option>
       <option value="-1" <c:if test="${param.status=='-1'}">selected</c:if> >未开始</option>
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >未完成</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
      </select> 
      <shiro:hasRole name="R_ADMIN">
                  下单人:
      <select class="select" style="height:32px;width:70px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('readtask',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <shiro:hasRole name="R_ADMIN">
        <th><input type="checkbox" value="-1" id="selectAll" onclick="setall(this)"></th>
        </shiro:hasRole>
        <th>序号</th>
        <th>标题</th>
        <th>阅读数</th>
        <th>点赞数</th>
        <th>下单时间</th> 
        <th>下单用户</th>
        <th>操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <shiro:hasRole name="R_ADMIN">
        <td><input type="checkbox" value="${list.id}" id="ids" name="ids"></td>
        </shiro:hasRole>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:20%;">
        <div class="cut_title ellipsis">
        <a href="${list.url}" target="_blank"><c:if test="${list.title!=null }">${list.title}</c:if><c:if test="${list.title==null }">${list.url}</c:if></a>     
        </div></td>
        <td>${list.total_quantity}</td>
        <td>${list.praise_quantity}</td>
      
        <td style="text-align:center;">${fn:substring(list.order_time,0,16)}</td> 
        <td style="text-align:center;">${list.full_name}</td>
        <td style="text-align:center;">
         <a href="#" onclick="loadreview(${list.id})" >审核</a>
        </td>
       </tr>
       </c:forEach>    
      </table>
      <aside class="paging">共${psize}页(${count}条记录)
       <a onclick="loadtable('readtask',1);">第一页</a>
       <c:forEach begin="1" end="${psize}" var="pageSize">
		 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
		  <c:if test="${pageSize==pageNum}">
		    <a style="background:none;color:#19a97b" onclick="loadtable('readtask',${pageSize});">${pageSize}</a>
		  </c:if>
		 <c:if test="${pageSize!=pageNum}"> 		      
		   <a onclick="loadtable('readtask',${pageSize});">${pageSize}</a>
		 </c:if>
		 </c:if>
	  </c:forEach>
      
       <a onclick="loadtable('readtask',${psize});">最后一页</a>
      </aside>
     </section>
 </form>     