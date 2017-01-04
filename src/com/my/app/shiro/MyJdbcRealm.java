package com.my.app.shiro;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.my.app.bean.sec.User;
import com.my.app.bean.sec.Permission;
import com.my.app.bean.sec.Role;

public class MyJdbcRealm extends AuthorizingRealm {

	  /**
	   * ��¼��֤
	   *
	   * @param token
	   * @return
	   * @throws org.apache.shiro.authc.AuthenticationException
	   */
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	    UsernamePasswordToken userToken = (UsernamePasswordToken) token;
	    User user = null;
	    String username = userToken.getUsername();
	      user = User.dao.findFirst("select * from sec_user where username=? and deleted_at is null", username);
	    if (user != null) {
	      SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getStr("password"), getName());
	      return info;
	    } else {
	    	
	      return null;
	    }
	  }

	  /**
	   * ��Ȩ��ѯ�ص�����, ���м�Ȩ�����������û�����Ȩ��Ϣʱ����.
	   *
	   * @param principals �û���Ϣ
	   * @return
	   */
	  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	    String loginName = ((User) principals.fromRealm(getName()).iterator().next()).get("username");
	    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	    Set<String> roleSet = new LinkedHashSet<String>(); // ��ɫ����
	    Set<String> permissionSet = new LinkedHashSet<String>();  // Ȩ�޼���
	    List<Role> roles = null;
	    User user = User.dao.findFirst("select * from `sec_user` where username =? AND `sec_user`.deleted_at is null", loginName);
	    if (user != null) {
	      //������ɫ
	      roles = user.getRoles();
	      for(int i=0;i<roles.size();i++)
	      {
	    	  if(roles.get(i).getInt("user_id")!=null)
	    	  {
	    		  //System.out.println(roles.get(i).getStr("value"));
	    	  }else
	    	  {
	    		  roles.remove(i);
	    	  }
	      }
	      
	    } else {
	    	return info;
	    }

	    loadRole(roleSet, permissionSet, roles);
	    info.setRoles(roleSet); // ���ý�ɫ
	    info.setStringPermissions(permissionSet); // ����Ȩ��
	    return info;
	  }

	  /**
	   * @param roleSet
	   * @param permissionSet
	   * @param roles
	   */
	  private void loadRole(Set<String> roleSet, Set<String> permissionSet, List<Role> roles) {
	    List<Permission> permissions;
	    for (Role role : roles) {
	      //��ɫ����
	      if (role.getDate("deleted_at") == null) {
	        roleSet.add(role.getStr("value"));
	        permissions = Role.dao.getPermissions();
	        loadAuth(permissionSet, permissions);
	      }
	    }
	  }

	  /**
	   * @param permissionSet
	   * @param permissions
	   */
	  private void loadAuth(Set<String> permissionSet, List<Permission> permissions) {
	    //����Ȩ��
	    for (Permission permission : permissions) {
	      //Ȩ�޿���
	      if (permission.getDate("deleted_at") == null) {
	        permissionSet.add(permission.getStr("value"));
	      }
	    }
	  }

	  /**
	   * �����û���Ȩ��Ϣ����.
	   */

	  public void clearCachedAuthorizationInfo(Object principal) {
	    SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
	    clearCachedAuthorizationInfo(principals);
	  }

	  /**
	   * ��������û���Ȩ��Ϣ����.
	   */
	  public void clearAllCachedAuthorizationInfo() {
	    Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
	    if (cache != null) {
	      for (Object key : cache.keys()) {
	        cache.remove(key);
	      }
	    }
	  }
	}