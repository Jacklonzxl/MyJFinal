 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
 
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">用户中心</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
    
     <section>
     <br/>
     
      <!--左右分栏：左侧栏目-->
       <div class="cont_col_lt mCustomScrollbar" style="height:500px;width:43%;margin-top:28px;">
           <table class="table fl">
           
            <tr>
             <td style="text-align:left;width:40%"><strong>账号</strong></td>
             <td>${user.username}</td>
            </tr>
             <tr>
             <td style="text-align:left;width:40%"><strong>用户类别</strong></td>
             <td>
             <c:if test="${user.group_id==1}">管理员</c:if>
             <c:if test="${user.group_id==2}">渠道商</c:if>
             <c:if test="${user.group_id==3}">代理商</c:if> 
             </td>
            </tr>
             <tr>
             <td style="text-align:left;width:40%"><strong>账号余额</strong></td>
             <td>${userMoney.balance}</td>
            </tr>
            <tr>
             <td style="text-align:left;width:40%"><strong>赠送余额</strong></td>
             <td>${userMoney.give}</td>
            </tr>
            <tr>
             <td style="text-align:left;width:40%"><strong>透支额度</strong></td>
             <td>${userMoney.borrow}</td>
            </tr>

           </table>
           <table class="table fl" style="margin-top:28px;">
            <tr>
             <td style="text-align:left;width:40%"><strong>手机</strong></td>
             <td>${user.mobile}</td>
            </tr>
            <tr>
             <td style="text-align:left;width:40%"><strong>邮箱</strong></td>
             <td>${user.email}</td>
            </tr>                         
           </table>
       </div>

 </section>     