 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">用户统计</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
     
      <div style="float:right;padding-bottom:10px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
       <input type="hidden" class="textbox laydate-icon" onclick="laydate()" id="query_order_time" name="query_order_time" value="${query_order_time}" style="height:20px;width:80px" placeholder=""/>        
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <option value="-3">-选择-</option>
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >未完成</option>
       <option value="1" <c:if test="${param.status=='1'||param.status==null||param.status==''}">selected</c:if> >已完成</option>
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
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        
        <th>序号</th>
        <th>下单日期</th>
        <th>用户名</th>
        <th>关注数(计划/实际)</th>
        <th>阅读数(计划/实际)</th>
        <th>点赞数(计划/实际)</th>
        <th>状态</th>  
        <th>操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:100px;">${list.thedate}</td>
        <td>${list.full_name}</td>
        <td>${list.ftcnt}/${list.frcnt}</td> 
        <td>${list.rtcnt}/${list.rrcnt}</td>
        <td>${list.rpcnt}/${list.rrpcnt}</td>
        <td>
        <c:if test="${param.status=='0'}">未完成</c:if>
        <c:if test="${param.status=='1'||param.status==null||param.status==''}">已完成</c:if>
        <c:if test="${param.status=='2'}">已撤单</c:if>
        <c:if test="${param.status=='-3'}">全部</c:if>
        </td>
        <td style="text-align:center;">
         <a href="#" onclick="loadpage('usercount/readlist?status=${param.status}&userid=${list.id}&thedate=${list.thedate}','tablediv')">阅读明细</a>
         <a href="#" onclick="loadpage('usercount/followlist?status=${param.status}&userid=${list.id}&thedate=${list.thedate}','tablediv')">关注明细</a>
        </td>  
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