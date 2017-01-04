package com.my.app.interceptor;


import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;


/**
 * @title: 异常处理拦截器
 * @className: ActionInterceptor
 * @description: TODO
 * @company: FOREVEROSS
 * @author: zcj
 * @createDate: 2014年4月2日
 * @version: 1.0
 */
public class ExceptionInterceptor implements Interceptor {

 
	@Override
	public void intercept(Invocation ai) {
	    Controller controller = (Controller)ai.getController();
		HttpServletRequest request = controller.getRequest();
 
		try {
			ai.invoke(); 
		} catch (Exception e) {
			//
            e.printStackTrace();
			//判断是否ajax请求
        	String header = request.getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
            String msg = formatException(e);
            if(isAjax){
            	msg = new StringBuilder().append("{\"status\":\"0\",\"msg\":\"")
        				.append(msg).append("\"}").toString();
            	controller.renderJson(msg);
            }else{
            	String redirctUrl = request.getHeader("referer");
            	if(StringUtils.isBlank(redirctUrl)){
            		redirctUrl = request.getRequestURI();
            	}
            	//controller.setAttr("message", msg);
            	//controller.setAttr("redirctUrl",redirctUrl);
            	//controller.render("../public/failed.ftl");
            	controller.redirect("/error/500.html");
            }
		} 
	}
	
 

 
	/**
	 * 格式化异常信息，用于友好响应用户
	 * @param e
	 * @return
	 */
    private static String formatException(Exception e){
		 String message = null;
	        Throwable ourCause = e;
	        while ((ourCause = e.getCause()) != null) {
	            e = (Exception) ourCause;
	        }
	        String eClassName = e.getClass().getName();
	        //一些常见异常提示
	        if("java.lang.NumberFormatException".equals(eClassName)){
	            message = "请输入正确的数字";
	        }else if (e instanceof RuntimeException) {
	            message = e.getMessage();
	            if(StringUtils.isBlank(message))message = e.toString();
	        }
	        
	        //获取默认异常提示
	        if (StringUtils.isBlank(message)){
	        	message = "系统繁忙,请稍后再试";
	        }
	        System.out.println(message);
	        //替换特殊字符
	        message = message.replaceAll("\"", "'");
	        return message;
	}

}