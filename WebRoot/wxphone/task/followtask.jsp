 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
 <%@page import="com.my.app.wx.bean.Channel"%>
<%@page import="com.my.app.bean.sec.User"%>
<%@page import="com.jfinal.plugin.redis.Redis"%>
<%@page import="com.jfinal.plugin.redis.Cache"%>
<%@page import="redis.clients.jedis.Jedis"%>
<%@page import="com.my.util.TimeUtil"%>
<%@page import="com.my.app.WxApiController"%>
<%
Cache userCache= Redis.use("userc");
Jedis jedis = userCache.getJedis();
String notice=jedis.get("notice")!=null?jedis.get("notice"):"";
jedis.close();
User user=(User)session.getAttribute("dbuser");
Channel channel=Channel.dao.findFirst("select * from biz_channel where userid='"+user.get("id")+"'");
%>
<head>

<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/add.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/css/list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/wxphone/utilLib/bootstrap.min.css" type="text/css" media="screen" />
</head>

<body>

<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form  method="post" id="form1">

    <!--  
    	<div class="page_title">
       <h2 class="fl">关注任务列表</h2>
       <a class="fr top_rt_btn">右侧按钮</a> 
  		</div> 
      -->
             
     
     
      <a href="${pageContext.request.contextPath}/wxphone/followtask/input"><input style="height:30px;width:70px;color: black" type="button" value="添加" /></a>
      <a href="${pageContext.request.contextPath}/wxphone/followtask/batchinput"><input type="button" value="批量添加" style="height:30px;width:90px;color: black"/></a>
      
     <!--  <c:if test="${follow_order_status=='1'}">
      <button class="link_btn" id="showPopTxt" onclick=" loadinput('${m_name}',0)">添加</button>
      <input type="button" value="批量添加"   class="link_btn"  onclick="loadbatchinput('${m_name}',0)"/>
      </c:if>   -->
     
      <div style="float:right;padding-bottom:5px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:30px;width:100px" placeholder=""/> 
        - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:30px;width:100px" placeholder=""/> 
                  状态:
      <select class="select" style="height:30px;width:100px" id="status" name="status">
       <option value="-2">-选择-</option>
       <option value="-1" <c:if test="${param.status=='-1'}">selected</c:if> >待审核</option>
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >进行中</option>
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
      </select>
      <shiro:hasRole name="R_ADMIN"> 
                  下单人:      
      <select class="select" style="height:30px;width:100px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>
      <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:30px;width:130px" placeholder="关键字"/>
      &nbsp;&nbsp;<input type="submit" style="height:30px;width:70px;color: black" value="查询"/>
      </div>
     
     
     <br/>
 
      <table width="100%" class="table" >
       <tr>
        <th style="text-align:center" width="7%">序号</th>
        <th style="text-align:center" width="15%">公众号</th>
        <th style="text-align:center" width="10%">关注数</th>       
        <th style="text-align:center" width="15%">下单时间</th>
        <th style="text-align:center" width="10%">状态</th>
        <th style="text-align:center" width="15%">完成时间</th>
        <th style="text-align:center" width="10%">下单用户</th>
        <th style="text-align:center" width="10%">操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <td style="text-align:center;width:4%;">${(pageNum-1)*pageSize+i}</td>
        <td style="width:160px;text-align:center">${list.public_account}</td>
        <td style="text-align:center">${list.total_quantity}(${list.finish_quantity})</td>
        <td style="text-align:center;">${fn:substring(list.order_time,0,16)}</td>
       
       	<td  style="text-align:center;"> 
       	<c:if test="${list.settle!=1||list.finish_quantity==0 }">
	       <!--<c:if test="${list.status==-1 }">待审核</c:if> -->
	        <c:if test="${list.finish_quantity>=list.total_quantity}">
	        <c:if test="${list.status==0 }">校验中</c:if>
	        </c:if>
	        <c:if test="${list.finish_quantity<list.total_quantity}">
	        <c:if test="${list.status==0}">进行中</c:if>
	        </c:if>
	        <c:if test="${list.status==1}">已完成</c:if>
	        <c:if test="${list.status==2}">已撤单</c:if>
        </c:if>
        <c:if test="${list.settle==1&&list.finish_quantity!=0 }">已结算</c:if>
        </td>
        <td style="text-align:center;">${fn:substring(list.finish_time,0,16)}</td>
        <td style="text-align:center;">${list.full_name}</td>
        
        <td align="center">
        <a href="${pageContext.request.contextPath}/wxphone/followtask/input?id=${list.id}">修改</a>
        <a href="${pageContext.request.contextPath}/wxphone/followtask/cancel?id=${list.id}">撤单</a> 
  		</td>
 
       </tr>
       </c:forEach>    
      </table>
 
      
      <div class="control-group">
            <label class="laber_from" ></label>
		       <aside class="paging">查找到${count}条记录
		       <a href="?pageNum=1">第一页</a>
		       <c:forEach begin="1" end="${psize}" var="pageSize">
				 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
				  <c:if test="${pageSize==pageNum}">
				    <a style="background:red;color:#19a97b;padding:5px" href="?pageNum=${pageSize}" >${pageSize}</a>
				  </c:if>
				 <c:if test="${pageSize!=pageNum}"> 		      
				   <a style="padding:5px" href="?pageNum=${pageSize}">${pageSize}</a>
				 </c:if>
				 </c:if>
			  </c:forEach>
		      
		       <a  href="?pageNum=${psize}">最后一页</a>
		      </aside>
		      </div>
     
 </form>     
 </body>