 <%@page import="com.my.app.wx.bean.Channel"%>
<%@page import="com.my.app.bean.sec.User"%>
<%@page import="com.jfinal.plugin.redis.Redis"%>
<%@page import="com.jfinal.plugin.redis.Cache"%>
<%@page import="redis.clients.jedis.Jedis"%>
<%@page import="com.my.util.TimeUtil"%>
<%@page import="com.my.app.WxApiController"%>
<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
Cache userCache= Redis.use("userc");
Jedis jedis = userCache.getJedis();
String notice=jedis.get("notice")!=null?jedis.get("notice"):"";
jedis.close();
User user=(User)session.getAttribute("dbuser");
Channel channel=Channel.dao.findFirst("select * from biz_channel where userid='"+user.get("id")+"'");
%>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
  
     <!--结束：高峰期下单，2点左右完成弹出框效果-->
     <div class="page_title">
       <h2 class="fl">阅读任务列表<font color="red" ><shiro:hasRole name="R_ADMIN">
       [<%=WxApiController.read_task_cnts[0] %>次/分--<%=TimeUtil.GetSqlDate(WxApiController.cnt_time) %>]
       </shiro:hasRole>&nbsp;</font>
       </h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
       <div style="width:100%;color:red;"><%=notice %>&nbsp;
       <shiro:lacksRole name="R_ADMIN">单价<%=Math.floor(channel.getFloat("readprice")*10000*1F+0.1)%>元/万</shiro:lacksRole>
       </div>      
     <section style="padding-top:10px">
      <shiro:lacksRole name="R_ADMIN">
      <c:if test="${read_order_status=='1'}">
      <button class="link_btn" id="showPopTxt" onclick="loadinput('readtask',0)">添加</button>
      <input type="button" value="批量添加"  class="link_btn"  onclick="loadbatchinput('${m_name}',0)"/>
      </c:if>
      <shiro:hasRole name="R_ADMIN">
      <input type="button" value="撤单"  class="link_btn" onclick="loadcancel(0)" />
      
      <input type="button" value="删除"  class="link_btn" onclick="loaddel(0)" />
      </shiro:hasRole>
      </shiro:lacksRole>
      <div style="float:right;padding-bottom:5px">
       &nbsp;&nbsp;&nbsp;&nbsp; 下单时间:
        <input type="text" class="textbox laydate-icon" onclick="laydate()" id="startdate" name="startdate" value="${param.startdate}" style="height:20px;width:80px" placeholder=""/> 
      - <input type="text" class="textbox laydate-icon" onclick="laydate()" id="enddate" name="enddate" value="${param.enddate}"  style="height:20px;width:80px" placeholder=""/> 
                  状态:
      <select class="select" style="height:32px;width:79px" id="status" name="status">
       <!-- <option value="-2">-选择-</option>
       <option value="-1" <c:if test="${param.status=='-1'}">selected</c:if> >未开始</option> --> 
       <option value="0" <c:if test="${param.status=='0'}">selected</c:if> >进行中</option>     
       <option value="1" <c:if test="${param.status=='1'}">selected</c:if> >已完成</option>
       <option value="2" <c:if test="${param.status=='2'}">selected</c:if> >已撤单</option>
       <shiro:hasRole name="R_ADMIN">
       <option value="3" <c:if test="${param.status=='3'}">selected</c:if> >异常单(未结算)</option>
       </shiro:hasRole>
       <option value="4" <c:if test="${param.status=='4'}">selected</c:if> >异常单</option>
      </select> 
      <shiro:hasRole name="R_ADMIN">
                  下单人:
      <select class="select" style="height:32px;width:70px" id="userid" name="userid">
       <option value="-1">-选择-</option>
       <c:forEach items="${clist}" var="clist">
       <option value="${clist.userid}" <c:if test="${param.userid==clist.userid}">selected</c:if> >${clist.name}</option>
       </c:forEach> 
      </select>
      </shiro:hasRole>
      <input type="text" class="textbox"  id="keyword" name="keyword" value="${param.keyword}"  style="height:20px;width:160px" placeholder="关键字（标题或链接）"/> 
      &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('readtask',1);"  value="查询"/>
      <input type="button"  class="link_btn"   onclick="exCSV()"  value="导出"/>
      </div>
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr>
        <shiro:hasRole name="R_ADMIN">
        <!-- <th><input type="checkbox" value="-1" id="selectAll" onclick="setall(this)"></th> -->
        </shiro:hasRole>
        <th style="font-size:0.8em">序号</th>
        <th style="font-size:0.8em">标题(红色表示撤单)</th>            
        <th style="font-size:0.8em">&nbsp;初始阅读/点赞&nbsp;</th>
        <th style="font-size:0.8em">&nbsp;计划阅读/点赞&nbsp;</th> 
        <th style="font-size:0.8em">&nbsp;实际阅读/点赞&nbsp;</th>
        <th style="font-size:0.8em">限速(10m)</th>
        <th style="font-size:0.8em">状态</th>
        <th style="font-size:0.8em">下单时间-完成时间</th> 
        <th style="font-size:0.8em">下单用户</th>
        <th style="font-size:0.8em">操作</th>
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr>
        <shiro:hasRole name="R_ADMIN">
        <!--<td><input type="checkbox" value="${list.id}" id="ids" name="ids"></td>-->
        </shiro:hasRole>
        <td style="text-align:center;width:4%;">${(pageNum-1)*10+i}</td>
        <td style="width:253px;font-size:0.89em">
        <div class="cut_title ellipsis" style="width:253px">
        <a href="${list.url}" target="_blank" <c:if test="${list.status==2 }">style="color:red"</c:if> >
        <c:if test="${list.title!=null }">${list.title}</c:if><c:if test="${list.title==null }">${list.url}</c:if>  
        </a>   
        </div></td>
        <td><span style="font-size:0.85em">${list.start_quantity}/${list.start_praise}</span>
        <td style="font-size:0.85em">${list.total_quantity}/${list.praise_quantity} </td>       
        
        <td style="font-size:0.85em">${list.push_quantity}/${list.push_praise}</td>
         <td style="font-size:0.85em">
        <c:if test="${list.frequency<8000&&list.frequency>0}">
         ${list.frequency/1000}k
        </c:if>
        <c:if test="${list.frequency>=8000||list.frequency==0}">
                        不限速
        </c:if>
        </td>
        <td  style="text-align:center;font-size:0.85em">
        <c:if test="${list.settle!=1||list.finish_quantity==0 }">
	        <c:if test="${list.status==-1 }">待审核</c:if>
	        <c:if test="${list.finish_quantity>=list.total_quantity&&list.push_quantity>=list.total_quantity}">
	        <c:if test="${list.status==0 }">校验中</c:if>
	        </c:if>
	        <c:if test="${list.finish_quantity<list.total_quantity||list.push_quantity<list.total_quantity}">
	        <c:if test="${list.status==0 }">进行中</c:if>
	        </c:if>
	        <c:if test="${list.status==1 }">已完成</c:if>
	        <c:if test="${list.status==2 }">已撤单</c:if>
        </c:if>
        <c:if test="${list.settle==1&&list.finish_quantity!=0 }">已结算</c:if>
         <c:if test="${list.status==3 &&list.settle!=1}">异常单</c:if>
        </td>
        <td style="text-align:left;font-size:0.85em">${fn:substring(list.order_time,0,16)}~${fn:substring(list.finish_time,5,16)}</td>
        <td style="text-align:center;font-size:0.85em;width:60px"><div class="cut_title ellipsis" style="width:60px">${list.full_name}</div></td>
        <td style="text-align:center;">
        <shiro:lacksRole  name="R_ADMIN"> 
         <c:if test="${list.status<1}" >
         <a href="#" onclick="loadinput('readtask',${list.id},1)">修改</a>
         </c:if>
         <c:if test="${(list.status<1||list.status==3)&&list.settle!=1}" >
         <a href="#" onclick="loadcancel(${list.id})" >撤单</a>
         </c:if>
         </shiro:lacksRole>
         <shiro:hasRole name="R_ADMIN"> 
         <a href="#" onclick="loadinput('readtask',${list.id},1)">修改</a>
         <c:if test="${list.status==3&&list.settle!=1}" >
         <a href="#" onclick="loadcancel(${list.id})" >结算</a>
         </c:if>
         <!-- <a href="#" onclick="<c:if test="${list.status==2}" >alert('该任务已撤单!')</c:if><c:if test="${list.status!=2}" >loadcancel(${list.id})</c:if>" >撤单</a>
         <a href="#" onclick="loaddel(${list.id})"  >删除</a>-->
         </shiro:hasRole>
        </td>
       </tr>
       </c:forEach>    
      </table>
      <aside class="paging">共${psize}页(${count}条记录)
       <a onclick="loadtable('readtask',1);">第一页</a>
       <c:forEach begin="1" end="${psize}" var="pageSize">
		 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
		  <c:if test="${pageSize==pageNum}">
		    <a style="background:none;color:#19a97b" onclick="loadtable('readtask',${pageSize});">${pageSize}</a>
		  </c:if>
		 <c:if test="${pageSize!=pageNum}"> 		      
		   <a onclick="loadtable('readtask',${pageSize});">${pageSize}</a>
		 </c:if>
		 </c:if>
	  </c:forEach>
      
       <a onclick="loadtable('readtask',${psize});">最后一页</a>
      </aside>
     </section>
 </form> 
 <script>
   function exCSV()
   {
	   form1.action="${pageContext.request.contextPath}/wx/readtask/1";
	   form1.submit();
	   
   }
 </script>    