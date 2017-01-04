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
             
     <section style="padding-top:10px;">
      <shiro:lacksRole name="R_ADMIN">
      <c:if test="${follow_order_status=='1'}">
      <button class="link_btn" id="showPopTxt" onclick=" loadinput('${m_name}',0)">添加</button>
      <input type="button" value="批量添加"  class="link_btn"  onclick="loadbatchinput('${m_name}',0)"/>
      </c:if> 
      </shiro:lacksRole>
      <div style="float:right;padding-bottom:5px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <!--<option value="-2">-选择-</option>  -->
       
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >进行中</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
       <option value="-3" <c:if test="${param.status=='-3'}">selected</c:if> >异常单</option>
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
      <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="关键字 "/> 
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th><input type="checkbox" value="-1" id="selectAll" onclick="setall(this)"></th>
        <th>序号</th>
        <th>公众号</th>
        <th>关注数</th>       
        <th>下单时间</th>
        <th>状态</th>
        <th>完成时间</th>
        <th>下单用户</th>
        <th>操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td><input type="checkbox" value="${list.id}" id="ids" name="ids"></td>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:160px;">${list.public_account}</td>
        <td>${list.total_quantity}(${list.real_quantity})</td>
        <td style="text-align:center;">${fn:substring(list.order_time,0,16)}</td>
        <td  style="text-align:center;">
        <c:if test="${list.settle!=1||list.finish_quantity==0 }">
	        <c:if test="${list.status==-1 }">待审核</c:if>
	        <c:if test="${list.finish_quantity>=list.total_quantity}">
	        <c:if test="${list.status==0 }">进行中</c:if>
	        </c:if>
	        <c:if test="${list.finish_quantity<list.total_quantity}">
	        <c:if test="${list.status==0 }">进行中</c:if>
	        </c:if>
	        <c:if test="${list.status==1 }">已完成</c:if>
	        <c:if test="${list.status==2 }">已撤单</c:if>
        </c:if>
        <c:if test="${list.settle==1&&list.finish_quantity!=0 }">已结算</c:if>
        </td>
        <td style="text-align:center;">${fn:substring(list.finish_time,0,16)}</td>
        <td style="text-align:center;">${list.full_name}</td>
        <td style="text-align:center;">
        <shiro:lacksRole  name="R_ADMIN"> 
         <c:if test="${list.status<1}" >
         <a href="#" onclick="loadinput('${m_name}',${list.id},1)">修改</a>
         </c:if>
         <c:if test="${list.status<1}" >
         <a href="#" onclick="loadcancel(${list.id})" >撤单</a>
         </c:if>
         </shiro:lacksRole>
         <shiro:hasRole name="R_ADMIN"> 
         <a href="#" onclick="loadinput('${m_name}',${list.id},1)">修改</a>
         <a href="#" onclick="<c:if test="${list.status==2}" >alert('该任务已撤单!')</c:if><c:if test="${list.status!=2}" >loadcancel(${list.id})</c:if>" >撤单</a>
          
         </shiro:hasRole>
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