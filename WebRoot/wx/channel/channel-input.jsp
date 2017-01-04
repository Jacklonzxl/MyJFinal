<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
     <!--弹出框效果-->
 
       <!--title-->
       <h3>填写渠道商信息</h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe1" name="iframe1"></iframe>
       <form action="${pageContext.request.contextPath}/wx/${m_name}/save" method="post" id="form1" target="iframe1">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <input type="hidden" id="type" name="bean.type" value="1">
        <ul>
         <!--
       <li>
         	<span>用&nbsp;&nbsp;&nbsp;户</span>
         	<select  class="select"  id="userid" name="bean.userid" style="height:32px;width:83.8%"  >
         	<c:forEach var="ulist" items="${ulist }">
         		<option value="${ulist.id }"  <c:if test="${ulist.id==bean.userid}">selected="selected"</c:if>> ${ulist.username }</option>
         	</c:forEach>
         	</select>
         </li>-->
         <li>
         	<span>代理商</span>
         	<select id="aid" name="bean.aid" class="select" style="height:32px;width:83.8%"  >
         	<c:forEach var="alist" items="${alist }">
         		<option value="${alist.id }"  <c:if test="${alist.id==bean.aid}">selected="selected"</c:if>> ${alist.name }</option>
         	</c:forEach>
         	</select>
         </li>
        
         <li>
          <span>渠道名</span>
          <input type="text" id="name" name="bean.name" value="${bean.name}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>关注数</span>
          <input type="text" id="maxfollow" name="bean.maxfollow" value="${bean.maxfollow}" placeholder="设置单个公众号最大关注数" class="textbox" onkeyup="setonkeyup(this,100000)"/>
         </li>
         <li>
          <span>阅读数</span>
          <input type="text" id="maxread" name="bean.maxread" value="${bean.maxread}" placeholder="设置单个条链接最大阅读数" class="textbox" onkeyup="setonkeyup(this,1000000)"/>
         </li>
          <li>
          <span>日&nbsp;&nbsp;&nbsp;限</span>
          <input type="text" id="max_day_follow" name="bean.max_day_follow" value="${bean.max_day_follow}" placeholder="" class="textbox" style="width:39%"   onkeyup="setonkeyup(this,100000000)"/>
          <input type="text" id="max_day_read" name="bean.max_day_read" value="${bean.max_day_read}" placeholder="" class="textbox" style="width:39%"   onkeyup="setonkeyup(this,100000000)"/>
         </li>
         <li>
          <span>单&nbsp;&nbsp;&nbsp;价</span>
          <input type="text" id="followprice" name="bean.followprice" value="${bean.followprice}" placeholder="" class="textbox" style="width:39%"    />
          <input type="text" id="readprice" name="bean.readprice" value="${bean.readprice}" placeholder="" class="textbox" style="width:39%"   />
         </li>   
         <!-- <li>
          <span>省&nbsp;&nbsp;&nbsp;份</span>
          <input type="text" id="prov" name="bean.prov"  value="${bean.prov}" placeholder="" class="textbox"/>
         </li>
         <li>
        
          <span>城&nbsp;&nbsp;&nbsp;市</span>
          <input type="text" id="city" name="bean.city" value="${bean.city}" placeholder="" class="textbox"/>
         </li>
          -->
         <li>
          <span>地&nbsp;&nbsp;&nbsp;址</span>
          <input type="text" id="address" name="bean.address" value="${bean.address}" placeholder="" class="textbox"/>
         </li>
         <li>
          <span>说&nbsp;&nbsp;&nbsp;明</span>
          <input type="text" id="content" name="bean.content" value="${bean.content}" placeholder="" class="textbox"/>
         </li>
          <!-- <li>
          <span>费&nbsp;&nbsp;&nbsp;率</span>
          <input type="text" id="rate" name="bean.rate" value="${bean.rate}" placeholder="0" class="textbox"/>
         </li> -->
         <li>
          <span>状&nbsp;&nbsp;&nbsp;态</span>
          <select class="select" style="height:32px;width:83.8%" id="status" name="bean.status">
	       <option value="0" <c:if test="${bean.status=='0'}">selected</c:if> >开启</option>
	       <option value="1" <c:if test="${bean.status=='1'}">selected</c:if> >关闭</option>
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
        	$("#form1").submit()
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
       </script>
  
     <!--结束：弹出框效果-->