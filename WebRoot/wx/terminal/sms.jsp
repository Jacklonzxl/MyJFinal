 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">短信接口列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
      <button class="link_btn" id="showPopTxt" onclick="loadinput('${m_name}',0,1)">添加</button>
      <!-- <input type="button" value="批量添加"  class="link_btn"/> -->
      <input type="button" value="批量删除"  class="link_btn" onclick="loaddel(0)" />
       
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th><input type="checkbox" value="-1" id="selectAll" onclick="setall(this)"></th>
        <th>号码</th>
        <th>验证码</th>
        <th>上报</th>
        <th>渠道</th> 
        <th>操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td><input type="checkbox" value="${list.id}" id="ids" name="ids"></td>
        <td style="word-break:break-all;width:20%;">${list.getph}</td>
        <td style="word-break:break-all;width:20%;">${list.getcode}</td>
        <td style="word-break:break-all;width:20%;">${list.getpost}</td>
        <td >${list.channel}</td>
        <td>
         <a href="#" onclick="loadinput('${m_name}',${list.id},1)">修改</a>
         <a href="#" onclick="loaddel(${list.id})"  >删除</a>
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