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
	    //�������ݿ��url����
	    Map<String, AuthzHandler> authzJdbcMaps = new HashMap<String, AuthzHandler>();
	    //������ɫ
	    //List<Role> roles = Role.dao.findAll();
	    List<Permission> permissions = null;
	    for (Role role : roles) {
	      //��ɫ����
	      if (role.getDate("daleted_at") == null) {
	        permissions = Permission.dao.findByRole("", role.get("id"));
	        //����Ȩ��
	        for (Permission permission : permissions) {
	          //Ȩ�޿���
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