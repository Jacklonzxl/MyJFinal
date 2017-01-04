package com.my.app;


import com.jfinal.core.Controller; 

public class ErrorController extends Controller {
 	
	public void auth()
	{
		redirect("/error/auth.html");
 		
	}
	public void e500()
	{
		redirect("/error/500.html");
 		
	}
	public void e404()
	{
		redirect("/error/404.html");
 		
	}

}