package com.my.app.wx;


import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.jfinal.core.Controller;
import com.my.app.bean.sec.User;
@RequiresAuthentication 
public class WxPhoneController extends Controller {

	public void index()
	{ 
		User user=getSessionAttr("dbuser");
		setAttr("user", user);
		//System.out.println(user.getInt("group_id"));
		//user
		renderJsp("/wxphone/index.jsp");
	}

}