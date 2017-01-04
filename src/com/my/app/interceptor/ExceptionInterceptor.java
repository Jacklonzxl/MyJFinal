package com.my.app.interceptor;


import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;


/**
 * @title: �쳣����������
 * @className: ActionInterceptor
 * @description: TODO
 * @company: FOREVEROSS
 * @author: zcj
 * @createDate: 2014��4��2��
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
			//�ж��Ƿ�ajax����
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
	 * ��ʽ���쳣��Ϣ�������Ѻ���Ӧ�û�
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
	        //һЩ�����쳣��ʾ
	        if("java.lang.NumberFormatException".equals(eClassName)){
	            message = "��������ȷ������";
	        }else if (e instanceof RuntimeException) {
	            message = e.getMessage();
	            if(StringUtils.isBlank(message))message = e.toString();
	        }
	        
	        //��ȡĬ���쳣��ʾ
	        if (StringUtils.isBlank(message)){
	        	message = "ϵͳ��æ,���Ժ�����";
	        }
	        System.out.println(message);
	        //�滻�����ַ�
	        message = message.replaceAll("\"", "'");
	        return message;
	}

}