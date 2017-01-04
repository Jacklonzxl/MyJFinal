 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">账号明细列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
       <div style="float:right;margin-bottom:10px">

       &nbsp;&nbsp;&nbsp;&nbsp; 注册时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:70px" id="status" name="status">
       <option value="-1">-选择-</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >正常</option>
       <option value="-106" <c:if test="${param.status=='-106'}">selected</c:if>>身份证验证</option>
       <option value="-300" <c:if test="${param.status=='-300'}">selected</c:if> >封号</option>
       <option value="100">其它</option>
      </select>
      
   	   分组ID：
     <input type="text" class="textbox"  id="account_group_id" name="account_group_id" value="${param.account_group_id}"  style="height:20px;width:160px" placeholder="分组ID"/>   
     关键字：
     <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="关键字（号码或微信号或密码）"/>   
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th><input type="checkbox" value="-1" id="selectAll" onclick="setall(this)"></th>
        <th>手机号码</th>        
        <th>密码</th>
        <th>分组</th> 
        <th>状态</th>
        <th>注册时间</th>
        <th>最近访问时间</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td><input type="checkbox" value="${list.id}" id="ids" name="ids"></td>
        <td >${list.account}</td>
        <td >${list.password}</td>
        <td >${list.account_group_id}</td>
        <td >
        	<c:if test="${list.status==1}"><span style="color:green;">正常</span></c:if>
        	<c:if test="${list.status==-106}"><span style="color:gray;">验证</span></c:if>
        	<c:if test="${list.status==-300}"><span style="color:red">封号</span></c:if>
        	<c:if test="${list.status!=1&&list.status!=-106&&list.status!=-300}"><span style="color: ">其它</span></c:if>
        </td>
        <td >${list.create_time}</td>
        <td >${list.last_use_time}</td>
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