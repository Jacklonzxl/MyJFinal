package com.my.app.shiro;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.my.app.bean.sec.Permission;
import com.my.app.bean.sec.Role;

public class MyJdbcAuthzService {
	
	/*
	  @Override
	  public Map<String, AuthzHandler> getJdbcAuthz() {
	    //加载数据库的url配置
	    Map<String, AuthzHandler> authzJdbcMaps = new HashMap<String, AuthzHandler>();
	    //遍历角色
	    //List<Role> roles = Role.dao.findAll();
	    List<Permission> permissions = null;
	    for (Role role : roles) {
	      //角色可用
	      if (role.getDate("daleted_at") == null) {
	        permissions = Permission.dao.findByRole("", role.get("id"));
	        //遍历权限
	        for (Permission permission : permissions) {
	          //权限可用
	          if (permission.getDate("daleted_at") == null) {
	            if (permission.getStr("url") != null && !permission.getStr("url").isEmpty()) {
	              authzJdbcMaps.put(permission.getStr("url"), new JdbcPermissionAuthzHandler(permission.getStr("value")));
	            }
	          }
	        }
	      }
	    }
	
	    return authzJdbcMaps;
	  }
	 */
	}