 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">消费统计列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      <div style="float:right;margin-bottom:10px">
	              分组： 
    · <select class="select" style="height:32px;width:70px" id="groupstatus" name="groupstatus">
       <option value="0">-选择-</option>
      <!--   <option value="0" <c:if test="${param.groupstatus=='0'}">selected</c:if> >按天分组</option> -->
       <option value="1" <c:if test="${param.groupstatus=='1'}">selected</c:if> >按天分组</option>
      </select> 
       &nbsp;&nbsp;&nbsp;&nbsp; 注册时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
   	   用户：
     <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="用户"/>
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
 
       
       
       </div>
     </section>  
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th >日期</th>
        <th <c:if test="${groupstatus==1}">style="display:none;"</c:if>  >用户</th>
        <th>数量</th>
        <th>金额</th>
        <th>比率</th> 
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td >${list.ad}</td>
        <td <c:if test="${groupstatus==1}">style="display:none;"</c:if>  >${list.username}</td>
        <td >${list.cnt}</td>
        <td >${list.money}</td>        
        <td >${list.price}</td>
        
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