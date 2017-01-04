package com.my.app.wx;


import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.jfinal.core.Controller;
import com.my.app.bean.sec.User;
@RequiresAuthentication 
public class WxController extends Controller {

	public void index()
	{ 
		User user=getSessionAttr("dbuser");
		setAttr("user", user);
		//System.out.println(user.getInt("group_id"));
		//user
//		if(!user.getStr("username").equals("admin"))
//		{
//			redirect("/wxphone");
//			return;
//		}else
//		{
//			renderJsp("/wx/index.jsp");
//		}
//		
		renderJsp("/wx/index.jsp");
	}

}