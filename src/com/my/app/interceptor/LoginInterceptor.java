package com.my.app.interceptor;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation; 

/** 
 *
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		 
		Subject cuurenUser = SecurityUtils.getSubject();
		if (!cuurenUser.isAuthenticated()&&!ai.getMethodName().endsWith("login")) {
			ai.getController().setAttr("erralert", "<script type='text/javascript'>window.location.href='/login/'</script>");
			ai.getController().renderError(401);
			return;
		}
		ai.invoke();
	}

	
}
