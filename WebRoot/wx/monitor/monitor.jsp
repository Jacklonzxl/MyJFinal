 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">终端监控</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
      <!--  <button class="link_btn" id="showPopTxt" onclick="loadinput('${m_name}',0,1)">添加</button>-->
 
       
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th>序号</th>
        <th>VpsId</th>
        <th>ServerId</th>
        <th>最后请求时间</th>
        <th>请求间隔</th>
        <th>状态</th>  
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td> 
        <td style="text-align:center;width:100px">${list.vpsid}</td>
        <td style="text-align:center;width:100px">${list.serverid}</td>
        <td style="text-align:center;">${list.reqdate}</td>
        <td style="text-align:center;">${list.st}分钟</td>
        <td style="text-align:center;"><c:if test="${list.status==0 }">停用</c:if><c:if test="${list.status==1 }">启用</c:if></td>
         
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