 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">分组产能</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
      <div style="float:right;padding-bottom:10px">
 
                      日期:<input type="text" class="textbox laydate-icon" onclick="laydate()" id="thedate" name="thedate" value="${param.thedate}" style="height:20px;width:80px" placeholder=""/> 
                             
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
       
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr> 
        <th>日期</th> 
        <th>分组</th> 
        <th>机器数</th>  
        <th>关注数</th>
        <th>阅读数</th> 
        <th>单机器关注数</th>
        <th>单机器阅读数</th>   
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr> 
        <td style="text-align:center;width:100px">${thedate}</td> 
        <td style="text-align:center;width:80px"><c:if test="${list.grouping!=null }">${list.grouping}</c:if><c:if test="${list.grouping==null||list.grouping==''  }">空分组</c:if></td>
        <td style="text-align:center;width:100px">${list.cnt}</td>
        <td style="text-align:center;width:100px">${list.followcnt}</td>
        <td style="text-align:center;width:100px">${list.readcnt}</td>
        <td style="text-align:center;width:150px"><c:if test="${list.cnt>0 }">${list.followcnt/list.cnt}</c:if><c:if test="${list.cnt==0 }">未知</c:if></td>
        <td style="text-align:center;width:150px"><c:if test="${list.cnt>0 }">${list.readcnt/list.cnt}</c:if><c:if test="${list.cnt==0 }">未知</c:if></td> 
       </tr>
       </c:forEach>    
      </table>
 
     </section>
 </form>     