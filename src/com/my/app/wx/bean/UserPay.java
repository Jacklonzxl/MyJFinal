package com.my.app.wx.bean;


import com.jfinal.plugin.activerecord.Model;
import com.my.app.bean.sec.User;

public class UserPay extends Model<UserPay>{
	
	public static final UserPay dao = new UserPay() ;
	public User getUser()
	{
		if(get("userid")!=null)
		{
			return User.dao.findById(get("userid"));
		}else
			return null;
	}
 

}
