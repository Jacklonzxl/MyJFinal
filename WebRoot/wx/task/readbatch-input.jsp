<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>添加批量阅读任务信息<span style="color:red">${tip}</span></h3>
       <!--content-->
       <div class="pop_cont_input" style="">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
                         
         <li style="text-align:center;">  
          <textarea class="textarea"  id="tasks" name="tasks"  placeholder="每行一个任务(链接+空格+阅读数量+空格+点赞数量),可直接从Excel复制到这里粘贴"  style="height:300px;width:80%;font-size:8px"></textarea>
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
        	$("#form2").submit()
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
       </script>
  
     <!--结束：弹出框效果-->