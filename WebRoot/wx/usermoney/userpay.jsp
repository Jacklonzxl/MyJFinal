 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">充值记录</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px;">
 
      <div style="float:right;padding-bottom:10px">
      &nbsp;&nbsp;&nbsp;&nbsp; 充值时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
      
      <shiro:hasRole name="R_ADMIN">  
                 状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status"> 
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >待审核</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已审核</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已作废</option>
      </select>
      
                  用户:      
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
        <th>交易号</th>
        <th>用户名</th>
        <th>账户余额</th>
        <th>充值方式</th>
        <th>充值金额</th>
        <th>充值时间</th>
        <th>审核时间</th>
        <th>状态</th>
        <shiro:hasRole name="R_ADMIN">  <th>操作</th></shiro:hasRole>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:15%;">${list.alyid}</td>
 
        <td>${list.full_name}</td>
        <td>${list.balance}</td>
        <td>
        <c:if test="${list.type==1 }">支付宝</c:if>
        <c:if test="${list.type==2 }">微信</c:if>
        <c:if test="${list.type==3 }">充值</c:if>
        <c:if test="${list.type==4 }">赠送</c:if>
       </td> 
        <td>${list.money}</td>      
        <td>${list.adddate}</td>
        <td>${list.reviewdate}</td>      
        <td><c:if test="${list.status==0 }">待审核</c:if><c:if test="${list.status==1 }">已入账</c:if></td>
        <shiro:hasRole name="R_ADMIN">       
        <td  style="text-align:center;"> 
         <a href="#" onclick="loadinput('${m_name}',${list.id})">
         <c:if test="${list.status==0 }">审核</c:if>
         <c:if test="${list.status!=0 }">查看</c:if>
         </a>
          
        </td>
        </shiro:hasRole>
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