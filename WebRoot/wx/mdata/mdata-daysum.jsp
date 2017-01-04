 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="#" method="post" id="form1" name="form1" target="iframe1" >
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">终端产能</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
             
     <section style="padding-top:10px">
      
      <div style="float:right;padding-bottom:10px">
 
                      日期:<input type="text" class="textbox laydate-icon" onclick="laydate()" id="thedate" name="thedate" value="${param.thedate}" style="height:20px;width:80px" placeholder=""/> 
                    分组:
       <select class="select" style="height:32px;width:70px" id="grouping" name="grouping">
          <option value="-1">-选择-</option>
          <option value="100"  <c:if test="${param.grouping=='100'}">selected="selected"</c:if>>100</option>
          <option value="101"  <c:if test="${param.grouping=='101'}">selected="selected"</c:if>>101</option>
          <option value="102"  <c:if test="${param.grouping=='102'}">selected="selected"</c:if>>102</option>
          <option value="104"  <c:if test="${param.grouping=='104'}">selected="selected"</c:if>>104</option>
          <option value="106"  <c:if test="${param.grouping=='106'}">selected="selected"</c:if>>106</option>
          <option value="107"  <c:if test="${param.grouping=='107'}">selected="selected"</c:if>>107</option>
          <option value="108"  <c:if test="${param.grouping=='108'}">selected="selected"</c:if>>108</option>
          <option value="109"  <c:if test="${param.grouping=='109'}">selected="selected"</c:if>>109</option>
          <option value="121"  <c:if test="${param.grouping=='121'}">selected="selected"</c:if>>121</option>
          <option value="131"  <c:if test="${param.grouping=='131'}">selected="selected"</c:if>>131</option>
          <option value="132"  <c:if test="${param.grouping=='132'}">selected="selected"</c:if>>132</option>
          <option value="133"  <c:if test="${param.grouping=='133'}">selected="selected"</c:if>>133</option>
          <option value="134"  <c:if test="${param.grouping=='134'}">selected="selected"</c:if>>134</option>
          <option value="135"  <c:if test="${param.grouping=='135'}">selected="selected"</c:if>>135</option>
          <option value="136"  <c:if test="${param.grouping=='136'}">selected="selected"</c:if>>136</option>
          <option value="137"  <c:if test="${param.grouping=='137'}">selected="selected"</c:if>>137</option>
          <option value="138"  <c:if test="${param.grouping=='138'}">selected="selected"</c:if>>138</option>
          <option value="139"  <c:if test="${param.grouping=='139'}">selected="selected"</c:if>>139</option>
          <option value="140"  <c:if test="${param.grouping=='140'}">selected="selected"</c:if>>140</option>
          <option value="234"  <c:if test="${param.grouping=='234'}">selected="selected"</c:if>>234</option>
          <option value="235"  <c:if test="${param.grouping=='235'}">selected="selected"</c:if>>235</option>   
      </select>
      	编码:
       <select class="select" style="height:32px;width:70px" id="g" name="g">
       <option value="-1">-选择-</option>
        
          <option value="a"  <c:if test="${param.g=='a'}">selected="selected"</c:if>>a</option>
          <option value="b"  <c:if test="${param.g=='b'}">selected="selected"</c:if>>b</option>
          <option value="c"  <c:if test="${param.g=='c'}">selected="selected"</c:if>>c</option>
          <option value="g"  <c:if test="${param.g=='g'}">selected="selected"</c:if>>g</option>
          <option value="x"  <c:if test="${param.g=='x'}">selected="selected"</c:if>>x</option>
          <option value="y"  <c:if test="${param.g=='y'}">selected="selected"</c:if>>y</option>
          <option value="LL"  <c:if test="${param.g=='LL'}">selected="selected"</c:if>>LL</option>
      </select>                     
       &nbsp;&nbsp;<input type="button"  class="link_btn"   onclick="loadtable('${m_name}',1);"  value="查询"/>
      </div>
       
     </section> 
     <section>
     <br/>
 
      <table class="table" id="tablelist">
       <tr> 
        <th>日期</th>
        <th>编码</th>
        <th>分组</th>
        <th>机型</th>
        <th>vpn服务器</th>
        <th>vpn账号</th>
        <th>关注数</th>
        <th>阅读数</th>    
       </tr>
       <c:set var="i" value="0"></c:set>
       <c:forEach items="${list}" var="list"><c:set var="i" value="${i+1}"></c:set> 
       <tr> 
        <td style="text-align:center;width:100px">${list.thedate}</td>
        <td style="text-align:center;width:100px">${list.dnum}</td>
        <td style="text-align:center;width:80px">${list.grouping}</td>
        <td style="text-align:center;width:100px">${list.model}</td>
        <td style="text-align:center;width:100px">${list.vpnserver}</td>
        <td style="text-align:center;width:100px">${list.vpnuser}</td>
        <td style="text-align:center;width:150px">${list.followcnt}</td>
        <td style="text-align:center;width:150px">${list.readcnt}</td> 
       </tr>
       </c:forEach>    
      </table>
      <aside class="paging">共${psize}页(${count}条记录)
       <a onclick="loadtable('${m_name}',1);">第一页</a>
       <c:forEach begin="1" end="${psize}" var="pageSize">
		 <c:if test="${pageSize<pageNum+10&&pageSize>pageNum-10}">
		  <c:if test="${pageSize==pageNum}">
		    <a style="background:none;color:#19a97b" onclick="loadtable('${m_name}',${pageSize});">${pageSize}</a>
		  </c:if>
		 <c:if test="${pageSize!=pageNum}"> 		      
		   <a onclick="loadtable('${m_name}',${pageSize});">${pageSize}</a>
		 </c:if>
		 </c:if>
	  </c:forEach>
      
       <a onclick="loadtable('${m_name}',${psize});">最后一页</a>
      </aside>
     </section>
 </form>     