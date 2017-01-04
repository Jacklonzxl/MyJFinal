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
          <input type="text" id="public_account" name="bean.public_account" value="${bean.public_account}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>回&nbsp;&nbsp;&nbsp;复</span>
          <input type="text" id="reply_content" name="bean.reply_content"  value="${bean.reply_content}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>关注数</span>
          <input type="text" id="total_quantity" name="bean.total_quantity" value="${bean.total_quantity}" placeholder="0" class="textbox"/>
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
          <span>状&nbsp;&nbsp;&nbsp;态</span>
            <select class="select" style="height:32px;width:83.8%" id="status" name="bean.status">
		    <option value="-1" <c:if test="${bean.status=='-1'}">selected</c:if> >未开始</option>
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
        	
        	$(".pop_bg").fadeOut();
        	$("#form2").submit()
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
       </script>
  
     <!--结束：弹出框效果-->