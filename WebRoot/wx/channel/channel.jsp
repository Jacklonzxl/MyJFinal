 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="get" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">渠道商列表</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
   
      <div style="float:right;padding-bottom:10px">
      	代理:
       <select class="select" style="height:32px;width:70px" id="aid" name="aid">
       <option value="-1">-选择-</option>
       	<c:forEach var="ulist" items="${agentlist }">
         		<option value="${ulist.id }"  <c:if test="${ulist.id==aid}">selected="selected"</c:if>> ${ulist.name }</option>
        </c:forEach>
      </select> 
                      关键字:<input type="text" class="textbox"  style="height:20px;width:80px" id="keyword" name="keyword"  value="${keyword }" />
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadsearch('${m_name}',1);"  value="查询"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <th>序号</th>
        <th>渠道名</th>
        <th>联系地址</th>
        <th>最大关注</th>
        <th>最大阅读</th>
        <th>单价(关注/阅读)</th>
        <th>日限(关注/阅读)</th>
        <th>说明</th>
        <th>操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td>${list.name}</td> 
        <td>${list.address}</td>
        
        <td>${list.maxfollow}</td>
        <td>${list.maxread}</td>   
        <td>${list.followprice}/${list.readprice}</td> 
        <td>${list.max_day_follow}/${list.max_day_read}</td>
        <td>${list.content}</td>      
        <!-- <td><c:if test="${list.status==0 }">开启</c:if><c:if test="${list.status==1 }">关闭</c:if></td>       
        -->
        <td style="text-align:center;">
         <a href="#" onclick="loadinput('${m_name}',${list.id})">设置</a>
          
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
 <script type="text/javascript">
<!--

//-->

     function loadsearch(url,pageNum)
    {   //alert(url)
    	gourl=url;
    	gopageNum=pageNum;
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"?pageNum="+gopageNum;
        //alert($('#keyword').val());
	   	$.ajax({
	   		cache: true,
	   		type: "get",
	   		url:ajaxCallUrl, 
	   		data:encodeURI($('#form1').serialize()),// 你的formid
			async: true,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   			$("#tablediv").html(data); 
	   		}
	   		});
    }
     </script>    