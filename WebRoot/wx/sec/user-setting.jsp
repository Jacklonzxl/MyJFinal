<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>填写用户分组信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe1" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>名&nbsp;&nbsp;&nbsp;称</span>
          <input type="text" id="name" name="bean.name" value="${bean.name}" placeholder="" class="textbox"/>
         </li> 
         <li>
          <span>排&nbsp;&nbsp;&nbsp;序</span>
          <input type="text" id="theindex" name="bean.theindex" value="${bean.theindex}" placeholder="0" class="textbox"/>
         </li>
         <li>
          <span>状&nbsp;&nbsp;&nbsp;态</span>
          <select class="select" style="height:32px;width:83.8%" id="status" name="bean.status">
	       <option value="1" <c:if test="${bean.status=='1'}">selected</c:if> >启用</option>
	       <option value="0" <c:if test="${bean.status=='0'}">selected</c:if> >停有</option>
	      </select> 
         </li>          
         <li> 
          <span class="ttl">说&nbsp;&nbsp;&nbsp;明</span>
          <textarea class="textarea"  id="intro" name="bean.intro"  style="height:50px;width:80%;">${bean.intro}</textarea>
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