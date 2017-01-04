 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">充值统计列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      <div style="float:right;margin-bottom:10px">

       &nbsp;&nbsp;&nbsp;&nbsp; 注册时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
   	   支付方式:
      <select class="select" style="height:32px;width:70px" id="type" name="type">
       <option value="0">-选择-</option>
       <option value="1" <c:if test="${param.type=='1'}">selected</c:if> >支付宝</option>
       <option value="2" <c:if test="${param.type=='2'}">selected</c:if> >微信</option>
       <option value="3" <c:if test="${param.type=='3'}">selected</c:if> >充值</option>
       <option value="4" <c:if test="${param.type=='4'}">selected</c:if> >赠送</option>
      </select>  
   	   用户：
     <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="用户"/>
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section>  
     <section>
     <br/>
 
      <table class="table" id="tablelist" >
       <tr >
        <th>日期</th>
        <th>用户</th>
        <th>支付方式</th>
        <th>金额</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td >${list.ad}</td>
        <td >${list.username}</td>
        <td >
        	<c:if test="${list.type==1}">支付宝</c:if>
        	<c:if test="${list.type==2}">微信</c:if>
        	<c:if test="${list.type==3}">充值</c:if>
        	<c:if test="${list.type==4}">赠送</c:if>
        </td>
        <td >${list.money}</td>
       </tr>
       </c:forEach>    
      </table>
      <aside class="paging">共${psize}页(${count}条记录)
       <a onclick="loadtable('${m_name}',1);">第一页</a>
       <c:forEach begin="1" end="${psize}" var="pageSize">
		 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
		  <c:if test="${pageSize==pageNum}">
		    <a style="background:none;color:#19a97b" onclick="loadtable('${m_name}',${pageSize});">${pageSize}</a>
		  </c:if>
		 <c:if test="${pageSize!=pageNum}"> 		      
		   <a onclick="loadtable('${m_name}',${pageSize});">${pageSize}</a>
		 </c:if>
		 </c:if>
	  </c:forEach>
      
       <a onclick="loadtable('${m_name}',${psize});">最后一页</a>
      </aside>
     </section>
 </form>     