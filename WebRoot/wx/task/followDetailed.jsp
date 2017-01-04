 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">关注任务列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> --> 		     
      </div>             
     <section style="padding-top:10px">
       <input type="button" value="返回"  class="link_btn" onclick="clickmenu('concerntask')" />
      <div style="float:right;">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="query_order_time" name="query_order_time" value="${query_order_time}" style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <option value="-2">-选择-</option>
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >未完成</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='1'}">selected</c:if> >已撤单</option>
      </select>
      <shiro:hasRole name="R_ADMIN"> 
                  下单人:      
      <select class="select" style="height:32px;width:70px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.id}" <c:if test="${param.userid==clist.id}">selected</c:if> >${clist.full_name}</option>
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
        <th>下单时间</th>
        <th>公众号</th>
        <th>计划关注数</th>
        <th>实际关注数</th> 
        <th>状态</th>     
        <th>下单用户</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="text-align:center;width:60px;">
       	 <c:if test="${count==i-1}">合计</c:if>
         <c:if test="${count!=i-1}">${fn:substring(list.order_time,10,16)}</c:if>
        </td>
        <td style="width:160px;">${list.public_account}</td>
        <td>${list.total_quantity}</td>
        <td>${list.real_quantity}</td> 
        <td style="text-align:center;">
        <c:if test="${list.status==-1 }">待审核</c:if>
        <c:if test="${list.status==0 }">未完成</c:if>
        <c:if test="${list.status==1 }">已完成</c:if>
        <c:if test="${list.status==2 }">已撤单</c:if>
        </td> 
        <td style="text-align:center;">${list.full_name}</td>
       </tr>
       </c:forEach>    
      </table>
     </section>
 </form>     