 <%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn" %>
<iframe style="display:none" id="iframe1" name="iframe1"></iframe>
<form action="${pageContext.request.contextPath}/wx/${m_name}/paysave" method="post" id="form1" name="form1" onsubmit="return onpaycheck()" target="iframe1" style="padding-left:20px">
     <!--结束：弹出框效果-->
     <div class="page_title">
       <h2 class="fl">支付宝充值</h2>
       <!-- <a class="fr top_rt_btn">右侧按钮</a> -->
 
      </div>
  
     <section>
 
     <div style="padding-top:20px">
     <h2><strong style="color:grey;">操作步骤</strong></h2>
	 <h3><strong style="color:#000000;font-size:1.2em">1、登录支付宝网站或手机客户端</strong></h3>
	 <h3><strong style="color:#000000;font-size:1.2em">2、付款到支付宝账号： wll3916@126.com 姓名：**琳</strong></h3>
	 <h3><strong style="color:#000000;font-size:1.2em">3、转帐时，请核对收款与姓名是否与上面一致。</strong></h3>
	 <h3><strong style="color:#000000;font-size:1.2em">4、转账完成后，点击付款页面的“查看详情”，记录下 28 或 32 位的交易号。</strong></h3>
	 <h3><strong style="color:#000000;font-size:1.2em">5、输入交易号，10分钟内即可自动入账，如果遇到较长延迟，请联系我们。</strong></h3> 
	 </div>


      <ul class="ulColumn2" style="padding-top:43px">
       <li>
        <div   style="font-size:1em;padding-bottom:10px">请输入交易号：</div>
        <input name="payid"  id="payid" type="text" class="textbox textbox_295" placeholder=""/>
       
       </li>
       <li> 
        <input type="submit" class="link_btn"/>
       </li>
       </ul>


     </section>
     
 </form>
 <script type="text/javascript">

    function onpaycheck()
    {
	  if(payid.value.length==0)
	  {
		  alert("请填写交易号!")
		  return false;
	  }else
		  {
		  return true;
		  }
    }

</script>     