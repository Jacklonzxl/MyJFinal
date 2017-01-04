<!DOCTYPE html>
<%@page import="com.my.app.bean.sec.User"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%

User user=(User)session.getAttribute("dbuser");
String username=user.getStr("username");
%>
<html>
<head>
<meta charset="utf-8"/>
<title>后台管理系统</title>
<meta name="author" content="DeathGhost" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/wx/css/style.css" />
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
<script src="${pageContext.request.contextPath}/wx/js/jquery.js"></script>
<script src="${pageContext.request.contextPath}/wx/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="${pageContext.request.contextPath}/wx/laydate/laydate.js"></script>
<script>
	(function($){
		$(window).load(function(){
			
			$("a[rel='load-content']").click(function(e){
				e.preventDefault();
				var url=$(this).attr("href");
				$.get(url,function(data){
					$(".content .mCSB_container").append(data); //load new content inside .mCSB_container
					//scroll-to appended content 
					$(".content").mCustomScrollbar("scrollTo","h2:last");
				});
			});
			
			$(".content").delegate("a[href='top']","click",function(e){
				e.preventDefault();
				$(".content").mCustomScrollbar("scrollTo",$(this).attr("href"));
			});
			
		});
	})(jQuery);
	function clickmenu(o)
	{
		
		if($("#status").length>0){ 
			//$("#status").val("");
			$("#status").remove();
		} 
		if($("#startdate").length>0){ 
			//$("#startdate").val("");
			$("#startdate").remove();
		}   
		if($("#enddate").length>0){ 
			//$("#enddate").val("");
			$("#enddate").remove();
		}   
		if($("#userid").length>0){ 
			//$("#status").val("");
			$("#userid").remove();
		}   
		$("#leftmenu a").removeClass("active");	
		$("#"+o).addClass("active");
		loadtable(o,1);
	}
	function clickmenu2(o,id)
	{
		
		if($("#status").length>0){ 
			//$("#status").val("");
			$("#status").remove();
		} 
		if($("#startdate").length>0){ 
			//$("#startdate").val("");
			$("#startdate").remove();
		}   
		if($("#enddate").length>0){ 
			//$("#enddate").val("");
			$("#enddate").remove();
		}   
		if($("#userid").length>0){ 
			//$("#status").val("");
			$("#userid").remove();
		}  
		$("#leftmenu a").removeClass("active");	
		$("#"+id).addClass("active");
		loadtable(o,1);
	}
	/**
	 *更新查询的数据
	 */
	function updatequery_order_time(o,date){
		$("#query_order_time").val(date);
		clickmenu(o);
	}
	
    function setall(o) {  
       
        	//alert(o.checked);
        	if(o.checked){ 
        		allCkb("ids")
        	}else{
        		unAllCkb("ids")
        		
        	}
       
  
    } 
    /**
     * 全选
     * 
     * items 复选框的name
     */
    function allCkb(items){
       $('[name='+items+']:checkbox').attr("checked", true);
       $("input[name='"+items+"']").prop("checked", true);
    }
       
    /**
     * 全不选
     * 
     */
    function unAllCkb(){
       $('[type=checkbox]:checkbox').attr('checked', false);
       $("input[name='"+items+"']").prop("checked", false);
    }
    var gourl="";
    var goid=0;
    var gopageNum=1;
    function loadtable(url,pageNum)
    {   //alert(url)
    	gourl=url;
    	gopageNum=pageNum;
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"?pageNum="+gopageNum;
        //alert($('#keyword').val());
	   	$.ajax({
	   		cache: true,
	   		type: "POST",
	   		url:ajaxCallUrl, 
	   		data:$('#form1').serialize(),// 你的formid
			async: true,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   		var longinindex = data.indexOf("用户登录");
	   		if(longinindex==-1)
	   	      {
	   			$("#tablediv").html(data); 
	   	      }else {
	   	    	location.href="/login/"  
	   	      }
	   		}
	   		});
    }
    function loadinput(url,id)
    {  
    	
    	gourl=url;
    	goid=id;
    	if(id==0){
    		gopageNum=1;
    	} 		
    	$(".pop_bg").fadeIn();
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"/input?pageNum="+gopageNum+"&id="+goid;
	    //alert(ajaxCallUrl);
        $.ajax({
	   		cache: true,
	   		type: "POST",
	   		url:ajaxCallUrl, 
	   		async: false,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   			var longinindex = data.indexOf("用户登录");
		   		if(longinindex==-1)
		   	      {
		   			$("#pop_div").html(data); 
		   	      }else
		   	    	  {
		   	    	location.href="/login/"  
		   	    	  }
	   	      	   	    
	   		}
	   		});
    }
    
    function loadinput2(url,id)
    {  
    	
    	gourl=url;
    	goid=id;
    	if(id==0){
    		gopageNum=1;
    	} 		
    	$(".pop_bg").fadeIn();
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"?pageNum="+gopageNum+"&id="+goid;
	    //alert(ajaxCallUrl);
        $.ajax({
	   		cache: true,
	   		type: "POST",
	   		url:ajaxCallUrl, 
	   		async: false,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   			var longinindex = data.indexOf("用户登录");
		   		if(longinindex==-1)
		   	      {
		   			$("#pop_div").html(data); 
		   	      }else
		   	    	  {
		   	    	location.href="/login/"  
		   	    	  }
	   	      	   	    
	   		}
	   		});
    }
    function loadbatchinput(url,id)
    {  
    	
    	gourl=url;
    	goid=id;
    	if(id==0){
    		gopageNum=1;
    	} 		
    	$(".pop_bg").fadeIn();
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"/batchinput?pageNum="+gopageNum+"&id="+goid;
	    //alert(ajaxCallUrl);
        $.ajax({
	   		cache: true,
	   		type: "POST",
	   		url:ajaxCallUrl, 
	   		async: false,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   			var longinindex = data.indexOf("用户登录");
		   		if(longinindex==-1)
		   	      {
		   			$("#pop_div").html(data); 
		   	      }else
		   	    	  {
		   	    	location.href="/login/"  
		   	    	  }
	   	      	   	    
	   		}
	   		});
    }
    function reloadTable()
    {
        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"?pageNum="+gopageNum;
	   	$.ajax({
	   		cache: true,
	   		type: "POST",
	   		url:ajaxCallUrl, 
	   		data:$('#form1').serialize(),// 你的formid
			async: true,
	   		error: function(request) {
	   		    alert(ajaxCallUrl);
	   			//$("#my-modal-loading").modal("close");
	   		},
	   		success: function(data) {
	   			var longinindex = data.indexOf("用户登录");
	   			if(longinindex==-1)
	   			{
	   				$("#tablediv").html(data); 
	   			}else
	   				{
	   				location.href="/login/"
	   				}
	   	      
	   		}
	   		});
    }
    function loaddel(id)
    {
    	if(id==0){
    		var checks=document.getElementsByName("ids");
		    var c=0;
		    for (i=0;i<checks.length;i++)
			{ 
			   if(checks[i].checked) 
			   {
			    c++;
			   } 
			 } 
	    }else{
		   c=id;
	    }
    	if(c==0){
		    alert("请选中一条记录选行删除！");
	    }else{	    		    
    	 if(confirm("确定删除您选择的记录吗？")){
    	    
    	    
		        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"/del?pageNum="+gopageNum+"&id="+id;
			   	$.ajax({
			   		cache: true,
			   		type: "POST",
			   		url:ajaxCallUrl, 
			   		data:$('#form1').serialize(),// 你的formid
					async: true,
			   		error: function(request) {
			   		    alert(ajaxCallUrl);
			   			//$("#my-modal-loading").modal("close");
			   		},
			   		success: function(data) {
			   	      //$("#tablediv").html(data); 
			   	      if(data.indexOf("用户登录")==-1)
			   	      {
			   	    	  reloadTable();
			   	      }else
			   	    	  {
			   	    	   location.href="/login/";
			   	    	  }
			   	      //alert(data);
			   		}
			   		});   
	      }
    	}
    }
    function loadcancel(id)
    {
    	if(id==0){
    		var checks=document.getElementsByName("ids");
		    var c=0;
		    for (i=0;i<checks.length;i++)
			{ 
			   if(checks[i].checked) 
			   {
			    c++;
			   } 
			 } 
	    }else{
		   c=id;
	    }
    	if(c==0){
		    alert("请选中一条记录选行撤单！");
	    }else{	    		    
    	 if(confirm("确定撤单吗？")){
    	    
    	    
		        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"/cancel?pageNum="+gopageNum+"&id="+id;
			   	$.ajax({
			   		cache: true,
			   		type: "POST",
			   		url:ajaxCallUrl, 
			   		data:$('#form1').serialize(),// 你的formid
					async: true,
			   		error: function(request) {
			   		    alert(ajaxCallUrl);
			   			//$("#my-modal-loading").modal("close");
			   		},
			   		success: function(data) {
			   	      //$("#tablediv").html(data); 
			   	      if(data.indexOf("用户登录")==-1)
			   	      {
			   	    	  reloadTable();
			   	      }else
			   	    	  {
			   	    	   location.href="/login/";
			   	    	  }
			   	      //alert(data);
			   		}
			   		});   
	      }
    	}    	
    	
    }
    function loadreview(id)
    {
    	if(id==0){
    		var checks=document.getElementsByName("ids");
		    var c=0;
		    for (i=0;i<checks.length;i++)
			{ 
			   if(checks[i].checked) 
			   {
			    c++;
			   } 
			 } 
	    }else{
		   c=id;
	    }
    	if(c==0){
		    alert("请选中一条记录选行审核！");
	    }else{	    		    
    	 if(confirm("确定审核吗？")){
    	    
    	    
		        var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+gourl+"/review?pageNum="+gopageNum+"&id="+id;
			   	$.ajax({
			   		cache: true,
			   		type: "POST",
			   		url:ajaxCallUrl, 
			   		data:$('#form1').serialize(),// 你的formid
					async: true,
			   		error: function(request) {
			   		    alert(ajaxCallUrl);
			   			//$("#my-modal-loading").modal("close");
			   		},
			   		success: function(data) {
			   	      //$("#tablediv").html(data); 
			   	      if(data.indexOf("用户登录")==-1)
			   	      {
			   	    	  reloadTable();
			   	      }else
			   	    	  {
			   	    	   location.href="/login/";
			   	    	  }
			   	      //alert(data);
			   		}
			   		});   
	      }
    	}    	
    	
    }
    
      function setonkeyup(o,max){
    	  
    	   if(o.value.length==1){
    	    o.value=o.value.replace(/[^1-9]/g,'');
    	    }else{
    	     o.value=o.value.replace(/\D/g,'');
    	    }
    	    
    	    if(parseInt(o.value)>max){
    	     
    	     o.value = max;//o.value.substr(0,3);//限制最大数不能超过3位数
    	   }
    	   
    	   if(o.value.length ==1){
    	    
    	    if(o.value==0){
    	     
    	     o.value ='';//限制第一位数不能为0
    	    }
    	   } 
      }
      function loadpage(url,hid)
      {  

          var ajaxCallUrl="${pageContext.request.contextPath}/wx/"+url;
          //alert(ajaxCallUrl)
  	   	$.ajax({
  	   		cache: true,
  	   		type: "POST",
  	   		url:ajaxCallUrl,  
  			async: true,
  	   		error: function(request) {
  	   		    alert(ajaxCallUrl);
  	   			//$("#my-modal-loading").modal("close");
  	   		},
  	   		success: function(data) {
  	   		var longinindex = data.indexOf("用户登录");
  	   		if(longinindex==-1)
  	   	      {
  	   			$("#"+hid).html(data); 
  	   	      }else {
  	   	    	location.href="/login/"  
  	   	      }
  	   		}
  	   		});
      }
</script>
</head>
<body> 
<!--header-->
<header>
 <h1><img src="${pageContext.request.contextPath}/wx/images/admin_logo.png"/></h1>
 <ul class="rt_nav">
  <!-- <li><a href="#" class="website_icon">站点首页</a></li>-->
  <li><a href="javascript:loadtable('user/center',1)" class="admin_icon">用户中心</a></li>
   
  <li><a href="javascript:loadinput('user',${user.id})" class="set_icon">账号设置</a></li>
  <li><a href="/login/loginout" class="quit_icon">安全退出</a></li>
 </ul>
</header>

<!--aside nav-->
<aside class="lt_aside_nav content mCustomScrollbar">
 <!--  --><h2><a href="#">功能列表</a></h2>
 <ul id="leftmenu">
  <c:if test="${user.group_id!=4}">
  <%if(!username.equals("caiwu")){ %>
  <li>
   <dl>
    <dt>任务管理</dt>
    <!--当前链接则添加class:active--> 
    <dd><a href="#" id="readtask"  class="active" onclick="clickmenu('readtask')">阅读任务</a></dd>
    <dd><a href="#" id="followtask" onclick="clickmenu('followtask')">关注任务</a></dd>
    <shiro:hasRole name="R_ADMIN">
    <!-- <dd><a href="#" id="followreview"  onclick="clickmenu('followreview')">关注审核</a></dd>
    <dd><a href="#" id="readreview"  onclick="clickmenu('readreview')">阅读审核</a></dd>
     -->
    </shiro:hasRole>
   </dl>
  </li>
  <%} %>
  </c:if>
  <%if(!username.equals("yunwei")) {%>
  <li>
   <dl>
    <dt>统计报表</dt>
    <shiro:hasRole name="R_ADMIN">
    <dd><a href="#" id="usercount"  onclick="clickmenu('usercount')">用户统计</a></dd>
    <!--<dd><a href="#" id="join"  onclick="clickmenu2('usercount/join','join')">接入统计</a></dd>-->
    </shiro:hasRole>
    <c:if test="${user.group_id!=4}">
    <!-- <dd><a href="#" id="readreport"  onclick="clickmenu('readreport')">阅读统计</a></dd>
    <dd><a href="#" id="taskreport"  onclick="clickmenu('taskreport')">关注统计</a></dd>
     -->
    <dd><a href="#" id="reporttask"  onclick="clickmenu('reporttask')">阅读统计</a></dd>
    <!--<dd><a href="#" id="readingDetail"  onclick="clickmenu('readingDetail')">阅读明细</a></dd>-->
    <dd><a href="#" id="concerntask"  onclick="clickmenu('concerntask')">关注统计</a></dd>
    <!--<dd><a href="#" id="followDetailed"  onclick="clickmenu('followDetailed')">关注明细</a></dd>
    -->
    </c:if>
    <c:if test="${user.group_id==4}">
    <dd><a href="#" id="join"  onclick="clickmenu2('usercount/join','join')">接入统计</a></dd>
    </c:if>
   </dl>
  </li>
  <%}%>
  <c:if test="${user.group_id!=4}">
  <shiro:hasRole name="R_ADMIN"> 
  <%if(!username.equals("caiwu")){ %>
  <li>
   <dl>
    <dt>数据监控</dt>
    <dd><a href="#" id="mdata"  onclick="clickmenu('mdata')">终端信息</a></dd>
    <dd><a href="#" id="mdatadaysum"  onclick="clickmenu2('mdata/daysum','mdatadaysum')">终端产能</a></dd>
    <dd><a href="#" id="daymonitor"  onclick="clickmenu2('mdata/daymonitor','daymonitor')">分组产能</a></dd>
    
    <!-- <dd><a href="#" id="monitor"  onclick="clickmenu('monitor')">终端监控</a></dd>
    <dd><a href="#" id="monitorcount"  onclick="clickmenu2('monitor/count','monitorcount')">终端统计</a></dd>
    <dd><a href="#" id="monitor2" onclick="clickmenu2('monitor/account','monitor2')">账号监控</a></dd>
    <dd><a href="#" id="monitor3" onclick="clickmenu2('monitor/nonaccount','monitor3')">封号明细</a></dd>
    <dd><a href="#" id="monitor4" onclick="clickmenu2('monitor/accountlist','monitor4')">账号列表</a></dd>
    -->
   </dl>
  </li>  
  <%} %> 
  <%if(!username.equals("yunwei")&&!username.equals("caiwu")){ %> 
  <li>
   <dl>
    <dt>渠道管理</dt>
    <dd><a href="#" id="agent"  onclick="clickmenu('agent')">代理商</a></dd>
    <dd><a href="#" id="channel" onclick="clickmenu('channel')">渠道商</a></dd>
   </dl>
  </li>      
  <li>
   <dl>
    <dt>用户管理</dt>
    
    <!-- <dd><a href="#" id="group" onclick="clickmenu('group')">用户分组</a></dd>  -->
    <dd><a href="#" id="user" onclick="clickmenu('user')">用户信息</a></dd>
    <dd><a href="#" id="role" onclick="clickmenu('role')">角色信息</a></dd>
    <!--<dd><a href="#" id="permission" onclick="clickmenu('permission')">权限信息</a></dd>--> 
   </dl>
  </li>
  <%} %>
  <%if(!username.equals("yunwei")){ %>
  <li>
   <dl>
    <dt>账务管理</dt>
    <dd><a href="#" id="usermoney"  onclick="clickmenu('usermoney')">账户查询</a></dd>
    <dd><a href="#" id="reviewmoney" onclick="clickmenu2('userpay','reviewmoney')">充值审核</a></dd>
    <!--<dd><a href="#" id="moneyin" onclick="clickmenu('moneyin')">充值统计</a></dd>
    <dd><a href="#" id="moneypay" onclick="clickmenu('moneypay')">消费统计</a></dd>  -->
   </dl>
  </li>
  <%} %>
  <!-- 
  <li>
   <dl>
    <dt>微信终端</dt>
    <dd><a href="#" id="sms"  onclick="clickmenu('sms')">短信接口</a></dd>
    <dd><a href="#" id="regcnt"  onclick="clickmenu('regcnt')">注册统计</a></dd>
    <dd><a href="#" id="reglist"  onclick="clickmenu('reglist')">账号名细</a></dd>
   </dl>
  </li>
   -->
   <%if(!username.equals("yunwei")&&!username.equals("caiwu")){ %>
  <li>
   <dl>
    <dt>运营控制</dt>
    <dd><a href="#" id="setting"  onclick="clickmenu('setting')">通用设置</a></dd>
    <dd><a href="#" id="setting1"  onclick="clickmenu('groupsetting')">分组设置</a></dd>
   </dl>
  </li> 
  <%} %> 
   </shiro:hasRole>
  <shiro:lacksRole name="R_ADMIN"> 
  <%if(!username.equals("yunwei")){ %>
  <li>
   <dl>
    <dt>我的账户</dt>
    <dd><a href="#" id="pay"  onclick="clickmenu2('userpay/pay','pay')">在线充值</a></dd>
    <dd><a href="#" id="userpay" onclick="clickmenu('userpay')">充值记录</a></dd>
    <dd><a href="#" id="paylog" onclick="clickmenu2('userpay/paylog','paylog')">支出明细</a></dd>
   </dl>
  </li>
  <%} %>
  </shiro:lacksRole> 
   </c:if>
 
  <!-- <li>
   <p class="btm_infor">© xxx 版权所有</p>
  </li>
   -->
 </ul>
</aside>

<section class="rt_wrap content mCustomScrollbar">
 <div class="rt_content">
     <!--点击加载-->
     <script>
     $(document).ready(function(){
		$("#loading").click(function(){
			$(".loading_area").fadeIn();
             $(".loading_area").fadeOut(1500);
			});
		 });
     </script>
     <section class="loading_area">
      <div class="loading_cont">
       <div class="loading_icon"><i></i><i></i><i></i><i></i><i></i></div>
       <div class="loading_txt"><mark>数据正在加载，请稍后！</mark></div>
      </div>
     </section>
     <!--结束加载-->
     <!--弹出框效果-->
     <script>
     $(document).ready(function(){
		 //弹出文本性提示框
		 /*$("#showPopTxt").click(function(){
			 $(".pop_bg").fadeIn();
			 loadinput(1);
			 });
		 */
		 //弹出：确认按钮
		 $(".trueBtn").click(function(){
			 //alert("你点击了确认！");//测试
			 $(".pop_bg").fadeOut();
			 });
		 //弹出：取消或关闭按钮
		 $(".falseBtn").click(function(){
			 //alert("你点击了取消/关闭！");//测试
			 $(".pop_bg").fadeOut();
			 });
		 });
     </script>
     <section class="pop_bg">
      <div class="pop_cont">
       <div id="pop_div">
       </div>
        
      </div>
     
     </section>
     <!--结束：弹出框效果-->
     <!-- 表格 -->
     <div id="tablediv"></div>
     <%if(username.equals("yunwei")){ %>
     <script type="text/javascript">
     
     loadtable("readtask",1);
     </script>
     <%}else if(username.equals("caiwu")){ %>
     <script type="text/javascript">
     
     loadtable("usercount",1);
     </script>    
     <%}else{ %>
     <c:if test="${user.group_id!=4}">
     <script type="text/javascript">
     
     loadtable("readtask",1);
     </script>
     </c:if>
     <c:if test="${user.group_id==4}">
     <script type="text/javascript">
     
     clickmenu2('usercount/join','join');
     </script>
     </c:if>
     <%} %>
 </div>
</section>
<%
 session.setMaxInactiveInterval(3600);
%>
</body>
</html>
