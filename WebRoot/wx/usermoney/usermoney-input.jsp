<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>设置账户信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li style="padding-top:10px;">
         	<span>&nbsp;用&nbsp;户&nbsp;名:</span>
         	${user.full_name} 
         	 
         </li>
        
         <li style="padding-top:10px;padding-bottom:10px">
          <span>账户余额:</span>
           <input type="text" style="margin-left:0px" id="balance" disabled="disabled" name="bean.balance" value="${bean.balance}" placeholder="" class="textbox"  /> 
         </li>
         <li style="padding-top:10px;padding-bottom:10px">
          <span>赠送余额:</span>
           <input type="text" style="margin-left:0px" id="give"  disabled="disabled"  name="bean.give" value="${bean.give}" placeholder="" class="textbox"  />    
         </li>         
         <li>
          <span>透支额度:</span>
          <input type="text" id="borrow" name="bean.borrow" value="${bean.borrow}" placeholder="" class="textbox"  />
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