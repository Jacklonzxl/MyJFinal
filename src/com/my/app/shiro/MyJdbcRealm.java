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
	   * 登录认证
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
	   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	   *
	   * @param principals 用户信息
	   * @return
	   */
	  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	    String loginName = ((User) principals.fromRealm(getName()).iterator().next()).get("username");
	    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	    Set<String> roleSet = new LinkedHashSet<String>(); // 角色集合
	    Set<String> permissionSet = new LinkedHashSet<String>();  // 权限集合
	    List<Role> roles = null;
	    User user = User.dao.findFirst("select * from `sec_user` where username =? AND `sec_user`.deleted_at is null", loginName);
	    if (user != null) {
	      //遍历角色
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
	    info.setRoles(roleSet); // 设置角色
	    info.setStringPermissions(permissionSet); // 设置权限
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
	      //角色可用
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
	    //遍历权限
	    for (Permission permission : permissions) {
	      //权限可用
	      if (permission.getDate("deleted_at") == null) {
	        permissionSet.add(permission.getStr("value"));
	      }
	    }
	  }

	  /**
	   * 更新用户授权信息缓存.
	   */

	  public void clearCachedAuthorizationInfo(Object principal) {
	    SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
	    clearCachedAuthorizationInfo(principals);
	  }

	  /**
	   * 清除所有用户授权信息缓存.
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