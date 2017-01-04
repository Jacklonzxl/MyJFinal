<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>填写关注任务信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>公众号</span>
          <input type="text" <c:if test="${bean.id>0}">disabled="disabled"</c:if> id="public_account" name="bean.public_account" value="${bean.public_account}" placeholder="" class="textbox"/>
         </li>
         <c:if test="${bean.id>0}">
         <li>
          <span>已完成</span>
          <input type="text" id="finish_quantity" disabled="disabled" name="finish_quantity" value="${bean.finish_quantity}"  class="textbox"   />
         </li>
         </c:if>         
         <li>
          <span>关注数</span>
          <input type="text" id="total_quantity" name="bean.total_quantity" value="${bean.total_quantity}"  class="textbox"  placeholder="最大值为${maxfollow}"  onkeyup="setonkeyup(this,${maxfollow})" />
         </li>
         <li>
          <span>回&nbsp;&nbsp;&nbsp;复</span>
          <input type="text" id="reply_content" name="bean.reply_content"  value="${bean.reply_content}" placeholder="" class="textbox"/>
         </li>
          <shiro:hasRole name="R_ADMIN">
         <li>
          <span>优先级</span>
          <select class="select" style="height:32px;width:83.8%" id="level" name="bean.level">
	       <option value="0">默认任务</option>
	       <option value="1" <c:if test="${bean.level=='1'}">selected</c:if> >优先执行</option>
	       <option value="2" <c:if test="${bean.level=='2'}">selected</c:if> >紧急执行</option>
	      </select> 
         </li>
        
         <li>
          <span>状&nbsp;&nbsp;&nbsp;态${bean.finish_quantity}</span>
            <select class="select" style="height:32px;width:83.8%" id="status" name="bean.status">
		    <option value="-1" <c:if test="${bean.status=='-1'}">selected</c:if> >待审核</option>
		    <option value="0"  <c:if test="${bean.status=='0'}">selected</c:if> >未完成</option>
		    <option value="1"  <c:if test="${bean.status=='1'}">selected</c:if> >已完成</option>
		    <option value="2"  <c:if test="${bean.status=='2'}">selected</c:if> >已撤单</option>
		    </select>  
         </li> 
         </shiro:hasRole>                  
         <li> 
          <span class="ttl">说&nbsp;&nbsp;&nbsp;明</span>
          <textarea class="textarea"  id="note" name="bean.note"  style="height:50px;width:80%;">${bean.note}</textarea>
         </li> 
        </ul>
        </form>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
                         
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认" class="input_btn trueBtn"  onclick="save_do()" />
        <input type="button" value="关闭" class="input_btn falseBtn" onclick="save_no()"/>
       </div> 
       <script>
        function save_do()
        {
        	if(total_quantity.value=="")
        	{
        		alert("数据是不能为空");
        		return;
        		
        	}
        	<c:if test="${bean.id>0}">
        	if(total_quantity.value<${bean.finish_quantity})
        	{
        		alert("关注数不能小于已完成数");
        		return;
        		
        	}
        	</c:if>
        	if(confirm("已确认过填写正确？"))
        	{
        	$(".pop_bg").fadeOut();
        	$("#form2").submit();
        	}
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
       </script>
  
     <!--结束：弹出框效果-->