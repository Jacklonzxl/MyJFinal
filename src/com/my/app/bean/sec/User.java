package com.my.app.bean.sec;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User>{
	
	//private static final long serialVersionUID = 1L;
	
	public static final User dao = new User() ;
	
	public List<Role> getRoles()
	{
		
		int id=get("id")!=null?getInt("id"):0;
		System.out.println(id);
		return Role.dao.find("select a.*,b.user_id from sec_role a LEFT join  sec_user_role b on a.id=b.role_id and b.user_id=?",id);
	}
	public Group getGroup()
	{
		int id=get("group_id")!=null?getInt("group_id"):0;
		return Group.dao.findById(id);
	}

}
