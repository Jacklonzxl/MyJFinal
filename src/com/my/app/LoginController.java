package com.my.app;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import com.jfinal.core.Controller;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel; 
 

public class LoginController extends Controller {
	/**
	 * µÇÂ½
	 */
 
	public void index() {
        renderJsp("/login.jsp");
		//dologin();
	   }
	public void dologin() {
		//String remember=getPara("remember");
		String username=getPara("username")!=null?getPara("username"):"admin";
		String password=getPara("password")!=null?getPara("password"):"admin";
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken userToken=new UsernamePasswordToken(username,password,false);//"1".equals(remember)
		//userToken.setRememberMe(true);
		ThreadContext.bind(subject); 
		try{
		subject.login(userToken);
		User user=User.dao.findFirst("select * from sec_user where username='"+username+"' and password='"+password+"'");
		if(user.getInt("group_id")==3)
		{
			Channel agent=Channel.dao.findFirst("select * from biz_channel where userid='"+user.getLong("id")+"'");
			getSession().setAttribute("agent", agent);
		}
		getSession().setAttribute("dbuser", user);
		}catch(AuthenticationException e)
		{
			redirect("/login/?error=1"); 
			return;
		}
		redirect("/wx/"); 
	   }
	public void loginout() { 
		Subject subject = SecurityUtils.getSubject();
		 
		try{
		subject.logout();
		}catch(AuthenticationException e)
		{
			redirect("/login/"); 
			return;
		}
		redirect("/login/"); 
	   }	
}