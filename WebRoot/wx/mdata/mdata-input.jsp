<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>编辑终端信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>编&nbsp;&nbsp;&nbsp;码</span>
          <input type="text" id="name" name="bean.dnum" value="${bean.dnum}" placeholder="" class="textbox"/>
         </li> 
         <li>
          <span>分&nbsp;&nbsp;&nbsp;组</span>
          <input type="text" id="grouping" name="bean.grouping" value="${bean.grouping}" placeholder="" class="textbox"/>
         </li>
         <li>
         <li>
          <span>机&nbsp;&nbsp;&nbsp;型</span>
          <input type="text" id="model" name="bean.model" value="${bean.model}" placeholder="" class="textbox"/>
         </li>
         <li>
         <li>
          <span>省&nbsp;&nbsp;&nbsp;份</span>
          <input type="text" id="prov" name="bean.prov" value="${bean.prov}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>域&nbsp;&nbsp;&nbsp;名</span>
          <input type="text" id="vpnserver" name="bean.vpnserver" value="${bean.vpnserver}" placeholder="" class="textbox"/>
         </li>
         <li>          
         <li>
          <span>账&nbsp;&nbsp;&nbsp;号</span>
          <input type="text" id="vpnuser" name="bean.vpnuser" value="${bean.vpnuser}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>网&nbsp;&nbsp;&nbsp;络</span>
          <input type="text" id="network" name="bean.network" value="${bean.network}" placeholder="" class="textbox"/>
         </li>  
         <li>
          <span>区&nbsp;&nbsp;&nbsp;域</span>
          <input type="text" id="area" name="bean.area" value="${bean.area}" placeholder="" class="textbox"/>
         </li>  
                           
         <li> 
          <span class="ttl">说&nbsp;&nbsp;&nbsp;明</span>
          <textarea class="textarea"  id="info" name="bean.info"  style="height:50px;width:80%;">${bean.info}</textarea>
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