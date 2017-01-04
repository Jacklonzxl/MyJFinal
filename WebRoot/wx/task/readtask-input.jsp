<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@page import="com.jfinal.plugin.redis.Redis"%>
<%@page import="com.jfinal.plugin.redis.Cache"%>
<%@page import="redis.clients.jedis.Jedis"%>

     <!--弹出框效果-->
 
       <!--title-->
       <h3>填写阅读任务信息<span style="color:red">${tip}</span></h3>
       <!--content-->
       <div class="pop_cont_input">
       <iframe style="display:none" id="iframe2" name="iframe2"></iframe>
       <form action="${pageContext.request.contextPath}/wx/readtask/save" method="post" id="form2" target="iframe2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        <ul>
         
         <li>
          <span>链&nbsp;&nbsp;&nbsp;接</span>
          <input type="text" id="url" name="bean.url" <c:if test="${bean.id>0}">disabled="disabled"</c:if>  value="${bean.url}" placeholder="" class="textbox"/>
         </li>
         <li style="display:none" >
          <span>标&nbsp;&nbsp;&nbsp;题</span>
          <input type="text" id="title" name="bean.title" value="${bean.title}" placeholder="正常情况下无须填写,下单失败时手动填写用!!!" class="textbox"/>
         </li> 
         <c:if test="${bean.id>0}">
         <li>
          <span>已完成</span>
          <input type="text" id="finish_quantity" disabled="disabled" name="finish_quantity" value="${bean.push_quantity}"  class="textbox"   />
         </li>
         </c:if>         
         <li>
          <span>阅读数</span>
          <input type="text" id="total_quantity" name="bean.total_quantity" value="${bean.total_quantity}" class="textbox" placeholder="最大值为${maxread}" onkeyup="setonkeyup(this,${maxread});setpraise(this)" onchange="setpraise(this)"/>
         </li>
          <li>
          <span>点赞数</span>
            <input type="text"  style="width:20%" id="praise_quantity" name="bean.praise_quantity" value="${bean.praise_quantity}"  class="textbox" placeholder="最大值为${maxread}"   onkeyup="setonkeyup(this,${maxread})"/>
            <select class="select" style="height:35px;width:60%" id="praise" name="praise" onchange="praise_quantity.value=parseInt(this.value*${bean.total_quantity})"> 
            <option value="0" <c:if test="${beanbean.praise_quantity=='0'}">selected</c:if> >无</option>
		    <option value="0.001" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.001&&bean.praise_quantity/bean.total_quantity<0.003}">selected</c:if> >1/1000</option>
		    <option value="0.003" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.003&&bean.praise_quantity/bean.total_quantity<0.006}">selected</c:if> >3/1000</option>
		    <option value="0.006" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.006&&bean.praise_quantity/bean.total_quantity<0.008}">selected</c:if> >6/1000</option>
		    <option value="0.008" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.008&&bean.praise_quantity/bean.total_quantity<0.010||bean.id==null}">selected</c:if> >8/1000</option>
		    <option value="0.010" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.010&&bean.praise_quantity/bean.total_quantity<0.020}">selected</c:if> >10/1000</option>
		    <option value="0.020" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.020&&bean.praise_quantity/bean.total_quantity<0.030}">selected</c:if> >20/1000</option>
		    <option value="0.030" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.030&&bean.praise_quantity/bean.total_quantity<0.050}">selected</c:if> >30/1000</option>
		    <option value="0.050" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.050&&bean.praise_quantity/bean.total_quantity<0.080}">selected</c:if> >50/1000</option>
		    <option value="0.080" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.080&&bean.praise_quantity/bean.total_quantity<0.100}">selected</c:if> >80/1000</option>
		    <option value="0.100" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.100&&bean.praise_quantity/bean.total_quantity<0.300}">selected</c:if> >100/1000</option>
		    <option value="0.300" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.300&&bean.praise_quantity/bean.total_quantity<0.500}">selected</c:if> >300/1000</option>
		    <option value="0.500" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.500&&bean.praise_quantity/bean.total_quantity<0.700}">selected</c:if> >500/1000</option>
		    <option value="0.700" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.700&&bean.praise_quantity/bean.total_quantity<0.900}">selected</c:if> >700/1000</option>
		    <option value="0.900" <c:if test="${bean.praise_quantity/bean.total_quantity>=0.900&&bean.praise_quantity/bean.total_quantity<1.000}">selected</c:if> >900/1000</option>
		    <option value="1.000" <c:if test="${bean.praise_quantity/bean.total_quantity>=1.000&&bean.praise_quantity/bean.total_quantity<2.000}">selected</c:if> >1000/1000</option>
		    </select>  
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
         </shiro:hasRole>
         <shiro:hasRole name="R_ADMIN">
         <li>
          <span>状&nbsp;&nbsp;&nbsp;态</span>
            <select class="select" style="height:32px;width:83.8%" id="status" name="bean.status">
		    <option value="-1" <c:if test="${bean.status=='-1'}">selected</c:if> >待审核</option>
		    <option value="0" <c:if test="${bean.status=='0'}">selected</c:if> >未完成</option>
		    <option value="1" <c:if test="${bean.status=='1'}">selected</c:if> >已完成</option>
		    <option value="2" <c:if test="${bean.status=='2'}">selected</c:if> >已撤单</option>
		    </select>  
         </li>
         </shiro:hasRole>  
          <li>
          <span>速&nbsp;&nbsp;&nbsp;度</span>
            <select class="select" style="height:32px;width:83.8%" id="frequency" name="bean.frequency"> 
		    <option value="10000" <c:if test="${bean.frequency=='10000'}">selected</c:if> >不限速</option>
		    <option value="6000" <c:if test="${bean.frequency=='6000'}">selected</c:if> >6000次/10分钟</option>
		    <option value="4000" <c:if test="${bean.frequency=='4000'}">selected</c:if> >4000次/10分钟</option>
		    <option value="2000" <c:if test="${bean.frequency=='2000'}">selected</c:if> >2000次/10分钟</option>
		    <option value="1000" <c:if test="${bean.frequency=='1000'}">selected</c:if> >1000次/10分钟</option>
		    <option value="500" <c:if test="${bean.frequency=='500'}">selected</c:if> >500次/分10分钟</option>
		    <option value="300" <c:if test="${bean.frequency=='300'}">selected</c:if> >300次/分10分钟</option>
		    </select>  
         </li> 
         <!-- <li style="padding-top:10px">
         <span>来&nbsp;&nbsp;&nbsp;源&nbsp;&nbsp;---------------------------------------------------------------</span>
         </li>
         <li style="padding-left:8%;padding-top:10px">
         <span>&nbsp;公&nbsp;众&nbsp;号</span>
         <input type="text" id="fr_0" name="fr_0" value="${fr_0}" onkeyup="setonkeyup(this,100);" class="textbox" style="height:20px;width:38px;margin-left:3px" placeholder="" >%
          <span>&nbsp;&nbsp;&nbsp;好友转发</span>
          <input type="text" id="fr_1" name="fr_1" value="${fr_1}" onkeyup="setonkeyup(this,100);" class="textbox" style="height:20px;width:38px" placeholder="" >%
          <span>&nbsp;&nbsp;&nbsp;朋&nbsp;友&nbsp;圈</span>
          <input type="text" id="fr_2" name="fr_2" value="${fr_2}" onkeyup="setonkeyup(this,100);" class="textbox" style="height:20px;width:38px" placeholder="" >%
         </li> 
         <li style="padding-left:8%;">

          <span>历史消息</span>
          <input type="text" id="fr_3" name="fr_3" value="${fr_3}" onkeyup="setonkeyup(this,100);" class="textbox" style="height:20px;width:38px" placeholder="" >%
          <span>&nbsp;&nbsp;&nbsp;未知来源 </span>
          <input type="text" id="fr_4" name="fr_4" value="${fr_4}" onkeyup="setonkeyup(this,100);" class="textbox" style="height:20px;width:38px" placeholder="" >%
         </li>   -->                         
         <li style="display:none">
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
        	if(total_quantity.value=="")
        	{ 
        		alert("阅读数是不能为空");
        		return;
        		
        	}
        	var rc=parseInt(praise_quantity.value);
        	var tc=parseInt(total_quantity.value);
        	if(rc>tc)
        	{
        		alert("点赞不能大于阅读");
        		return;
        		
        	}
        	if(praise_quantity.value=='')
        	{
        		praise_quantity.value=parseInt(total_quantity.value*praise.value)
        	}
 
        	//if(confirm("已确认过填写正确？"))
        	//{        	
        	$(".pop_bg").fadeOut();
        	$("#form2").submit();
        	//}
        }
        function save_no()
        {
        	
        	$(".pop_bg").fadeOut();
        	$("#pop_div").html(""); 
        }
        function setpraise(o)
        {
        	praise_quantity.value=parseInt(o.value*praise.value)
        }
       </script>
  
     <!--结束：弹出框效果-->