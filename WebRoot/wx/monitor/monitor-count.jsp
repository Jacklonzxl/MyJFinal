 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">终端统计</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
     
      <div style="float:right;padding-bottom:10px">
       &nbsp;&nbsp;&nbsp;&nbsp; 时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${nowdate}" style="height:20px;width:80px" placeholder=""/> 
         ServerId:
      <input type="text" class="textbox"   id="serverid" name="serverid" value="${param.serverid}"  style="height:20px;width:80px" placeholder=""/>      
           &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        
        <th>序号</th>
        <th>VpsId</th>
        <th>ServerId</th>
        <th>阅读成功数</th>
        <th>关注成功数</th>
        <th>时间</th> 
       </tr>
       <c:set var="i" value="0"></c:set><c:set var="st" value="0"></c:set><c:set var="st1" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set><c:set var="st" value="${st+list.st}"></c:set> <c:set var="st1" value="${st1+list.st1}"></c:set> 
       <tr>
        
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td>${list.vpsid}</td>
        <td>${list.serverid}</td>
        <td>${list.st}</td>
        <td>${list.st1}</td>
        <td>${nowdate}</td> 
       </tr>
       </c:forEach>
       <tr>      
        <td style="text-align:center;width:4%;"></td>
        <td></td>
        <td>合计</td>
        <td>${st}</td>
        <td>${st1}</td>
        <td> </td> 
       </tr>    
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