 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">注册统计列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      <div style="float:right;margin-bottom:10px">

       &nbsp;&nbsp;&nbsp;&nbsp; 注册时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      
   	   分组ID：
     <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="分组ID"/>
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section>  
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th>日期</th>
        <th>总注册</th>
        <th>注册成功</th>
        <th>成功率</th> 
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td >${list.createtime}</td>
        <td >${list.allcnt}</td>
        <td >${list.successcnt}</td>
        <td >${list.successratio}</td>
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