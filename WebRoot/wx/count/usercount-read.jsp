 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">用户统计明细</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
     <input type="button" value="返回"  class="link_btn" onclick="clickmenu('usercount')" />
      <div style="float:right;padding-bottom:10px;display:none">
      <!--
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
         
       <input type="text" class="textbox laydate-icon" onclick="laydate()" id="thedate" name="thedate" value="${param.thedate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <option value="-2">-选择-</option>
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
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      -->
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        
        <th>序号</th>
        <th>下单日期</th>
        <th>用户名</th>
        <th>标题</th> 
        <th>阅读数(计划/实际)</th>
        <th>点赞数(计划/实际)</th>  
        <th>状态</th>       
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:set var="c1" value="0"></c:set> 
       <c:set var="c2" value="0"></c:set> 
       <c:set var="c3" value="0"></c:set> 
       <c:set var="c4" value="0"></c:set> 
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set>
       <c:set var="c1" value="${c1+list.rtcnt}"></c:set> 
       <c:set var="c2" value="${c2+list.rrcnt}"></c:set> 
       <c:set var="c3" value="${c3+list.rpcnt}"></c:set> 
       <c:set var="c4" value="${c4+list.rrpcnt}"></c:set> 
       <tr>
        
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:100px;">${list.thedate}</td>
        <td>${list.full_name}</td>
        <td>${list.title}</td> 
        <td>${list.rtcnt}/${list.rrcnt}</td>
        <td>${list.rpcnt}/${list.rrpcnt}</td> 
        <td>
        <c:if test="${list.status=='0'}">未完成</c:if>
        <c:if test="${list.status=='1'||param.status==null||param.status==''}">已完成</c:if>
        <c:if test="${list.status=='2'}">已撤单</c:if>
        <c:if test="${list.status=='-3'}">全部</c:if>
        </td> 
       </tr>       
       </c:forEach>
       <tr>
         <td style="text-align:center;width:4%;"></td>
         <td style="width:100px;"></td>
         <td></td>
         <td>合计</td> 
         <td>${c1}/${c2}</td>
         <td>${c3}/${c4}</td> 
         <td></td> 
       </tr>    
      </table>
     
     </section>
 </form>     