<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>审核充值信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li style="padding-top:10px;">
         	<span>用户名:</span>
         	${user.full_name} 
         	 
         </li>
        
         <li style="padding-top:10px;padding-bottom:10px">
          <span>交易号:</span>
           ${bean.alyid}        
         </li>
          <li>
          <span>渠&nbsp;&nbsp;&nbsp;道</span>
          <select class="select" style="height:32px;width:83.8%" id="type" name="bean.type">
	       <option value="1" <c:if test="${bean.type=='1'}">selected</c:if> >支付宝</option>
	       <option value="2" <c:if test="${bean.type=='2'}">selected</c:if> >微信支付</option>
	       <option value="3" <c:if test="${bean.type=='3'}">selected</c:if> >其他</option>
	      </select> 
         </li>       
         <li>
          <span>金&nbsp;&nbsp;&nbsp;额</span>
          <input type="text" id="borrow" name="bean.money" value="${bean.money}" placeholder="" class="textbox"  />
         </li>
         <li>
          <span>状&nbsp;&nbsp;&nbsp;态</span>
          <select class="select" style="height:32px;width:83.8%" id="level" name="bean.status">
	       <option value="1" <c:if test="${bean.status=='1'}">selected</c:if> >通过</option>
	       <option value="2" <c:if test="${bean.status=='2'}">selected</c:if> >作废</option>
	      </select> 
         </li> 
         <li>
          <span>说&nbsp;&nbsp;&nbsp;明</span>
          <input type="text" id="content" name="bean.content" value="${bean.content}" placeholder="" class="textbox"/>
         </li>                         
        </ul>
        </form>
       </div>
       <!--以pop_cont_text分界-->
       <div class="pop_cont_text">
                         
       </div>
       <!--bottom:operate->button-->
       <div class="btm_btn">
        <c:if test="${bean.status==0}">
        <input type="button" value="确认" class="input_btn trueBtn"  onclick="save_do()" />
        </c:if>
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