<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
<title>Insert title here</title>

</head>
<body>

<div style="width: 700px;height: 300px;left:50%;margin-left:150px;top:10%;margin-top:10px">
    <h3>添加批量链接任务信息<span style="color:red">${tip}</span></h3>
       <!--content-->
       <div class="pop_cont_input" style="">
      <!--  <iframe style="display:none" id="iframe2" name="iframe2"></iframe>  -->
       <form action="${pageContext.request.contextPath}/wxphone/${m_name}/save" method="post" id="form2">
        <input type="hidden" id="id" name="bean.id" value="${bean.id}">
        
        <textarea  class="input_from"   id="tasks" name="tasks"  placeholder="链接+空格+阅读数量"  style="height:300px;width:80%;font-size:8px;border-style:solid;border-color:#000000;"></textarea>
     <!--    <ul>
                         
         <li style="text-align:center;">  
          <textarea class="textarea"  id="tasks" name="tasks"  placeholder="每行一个任务(链接+空格+阅读数量+),可直接从Excel复制到这里粘贴"  style="height:300px;width:80%;font-size:8px"></textarea>
         </li> 
        </ul>
          -->
       
       <!--以pop_cont_text分界--> 
       <!--bottom:operate->button-->
       <br>
       <div class="btm_btn">
        <input type="submit" value="确认" style="width:80px;background-color:#CFCFCF;" />
      <a href="${pageContext.request.contextPath}/wxphone/link"><input type="button" value="返回" style="width:80px;background-color:#CFCFCF;"></input></a>
       </div> 
       </form>
     </div>  

</div>
	
</body>
</html>