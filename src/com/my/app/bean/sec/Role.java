package com.my.app.bean.sec;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class Role extends Model<Role>{
	
	public static final Role dao = new Role() ;
	public List<Permission> getPermissions()
	{
		int id=get("id")!=null?getInt("id"):0;
		return Permission.dao.find(" select a.*,b.role_id from sec_permission a LEFT join  sec_role_permission b on a.id=b.permission_id and b.role_id=?",id);
	}

}
