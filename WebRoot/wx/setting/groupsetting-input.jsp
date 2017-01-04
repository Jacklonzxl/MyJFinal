<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>编辑分组设置</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>分&nbsp;&nbsp;&nbsp;组</span>
          <input type="text" id="serverid" name="bean.serverid" value="${bean.serverid}" placeholder="" class="textbox"/>
         </li> 
         <!--  
         <li>
          <span>阅读数</span>
          <input type="text" id="value" name="bean.maxread" value="${bean.maxread}" placeholder="" class="textbox"/>
         </li>
         
         <li>
          <span>天&nbsp;&nbsp;&nbsp;数</span>
          <input type="text" id="value" name="bean.dcnt" value="${bean.dcnt}" placeholder="" class="textbox"/>
         </li>
         -->
         <li>
          <span>阅&nbsp;&nbsp;&nbsp;读</span>
          <select class="select" style="height:32px;width:83.8%" id="readstatus" name="bean.readstatus">
	       <option value="1" <c:if test="${bean.readstatus=='1'}">selected</c:if> >打开</option>
	       <option value="0" <c:if test="${bean.readstatus=='0'}">selected</c:if> >关闭</option>
	      </select>
         </li>
         <li>
          <span>关&nbsp;&nbsp;&nbsp;注</span>
          <select class="select" style="height:32px;width:83.8%" id="followstatus" name="bean.followstatus">
	       <option value="1" <c:if test="${bean.followstatus=='1'}">selected</c:if> >打开</option>
	       <option value="0" <c:if test="${bean.followstatus=='0'}">selected</c:if> >关闭</option>
	      </select>
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