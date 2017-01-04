<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>编辑用户信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe1" name="iframe1"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form1" target="iframe1">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>姓&nbsp;&nbsp;&nbsp;名</span>
          <input type="text" id="full_name" name="bean.full_name" <shiro:lacksRole name="R_ADMIN">disabled="disabled"</shiro:lacksRole> value="${bean.full_name}" placeholder="" class="textbox"/>
         </li> 
         <li>
          <span>账&nbsp;&nbsp;&nbsp;户</span>
          <input type="text" id="username" name="bean.username" <shiro:lacksRole name="R_ADMIN">disabled="disabled"</shiro:lacksRole>  value="${bean.username}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>密&nbsp;&nbsp;&nbsp;码</span>
          <input type="password" id="password" name="bean.password" value="${bean.password}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>确&nbsp;&nbsp;&nbsp;认</span>
          <input type="password" id="password_again" name="password_again" value="${bean.password}" placeholder="" class="textbox"/>
         </li>
          <shiro:hasRole name="R_ADMIN">
         <li style="padding-top:8px">
          <span>用户组</span>
          <c:set var="i" value="0"></c:set> 
          <c:forEach items="${groupList}" var="list"><c:set var="i" value="${i+1}"></c:set> 
			 <label class="checkbox-inline">
			  &nbsp;&nbsp;<input type="radio" class="red" id="bean.group_id" name="bean.group_id" value="${list.id}" <c:if test="${((bean==null||bean.group_id==0)&&list.id==1)||list.id==bean.group_id||(bean.id==null&&i==2)}">checked="checked"</c:if> >
			 ${list.name}
			 </label>
		   </c:forEach>
		   
         </li>                   
         <li  style="padding-top:8px"> 
          <span class="ttl">角&nbsp;&nbsp;&nbsp;色</span>
          <c:forEach items="${roleList}" var="list"><c:set var="i" value="${i+1}"></c:set> 
			 <label class="checkbox-inline">
			  &nbsp;&nbsp;<input type="checkbox" class="red" id="role_ids" name="role_ids" value="${list.id}" <c:if test="${list.user_id!=null||(bean.id==null&&i==7)}">checked="checked"</c:if> >
			 ${list.name}
			 </label>
		  </c:forEach>
         </li> 
         <li style="padding-top:6px">
          <span>状&nbsp;&nbsp;&nbsp;态</span>
          <select class="select" style="height:35px;width:83.8%" id="status" name="bean.status">
	       <option value="1" <c:if test="${bean.status=='1'}">selected</c:if> >启用</option>
	       <option value="0" <c:if test="${bean.status=='0'}">selected</c:if> >停有</option>
	      </select> 
         </li>
         </shiro:hasRole>
         <li style="padding-top:6px">
          <span>手&nbsp;&nbsp;&nbsp;机</span>
          <input type="text" id="mobile" name="bean.mobile" value="${bean.mobile}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>邮&nbsp;&nbsp;&nbsp;箱</span>
          <input type="text" id="email" name="bean.email" value="${bean.email}" placeholder="" class="textbox"/>
         </li>
        </ul>
        </form>
       </div>
       <!--以pop_cont_text分界--> 
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <input type="button" value="确认" class="input_btn trueBtn"  onclick="save_do()" />
        <input type="button" value="关闭" class="input_btn falseBtn" onclick="save_no()"/>
       </div> 
       <script>
        function save_do()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#form1").submit()
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
       </script>
  
     <!--结束：弹出框效果-->