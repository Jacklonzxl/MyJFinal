<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>填写短信接口信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe1" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         <li>
          <span>号&nbsp;&nbsp;&nbsp;码</span>
          <input type="text" id="name" name="bean.getph" value="${bean.getph}" placeholder="" class="textbox"/>
         </li> 
         <li>
          <span>验&nbsp;证&nbsp;码</span>
          <input type="text" id="theindex" name="bean.getcode" value="${bean.getcode}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>上&nbsp;&nbsp;&nbsp;报</span>
          <input type="text" id="name" name="bean.getpost" value="${bean.getpost}" placeholder="" class="textbox"/>
         </li> 
         <li>
          <span>渠&nbsp;&nbsp;&nbsp;道</span>
          <input type="text" id="theindex" name="bean.channel" value="${bean.channel}" placeholder="" class="textbox"/>
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