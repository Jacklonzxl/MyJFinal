package com.my.app.wx.bean;


import com.jfinal.plugin.activerecord.Model;
import com.my.app.bean.sec.User;

public class UserPayLog extends Model<UserPayLog>{
	
	public static final UserPayLog dao = new UserPayLog() ;
	public User getUser()
	{
		if(get("userid")!=null)
		{
			return User.dao.findById(get("userid"));
		}else
			return null;
	}
 

}
