package com.my.app.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.my.app.AdminController;
import com.my.app.ApiController;
import com.my.app.DeviceController;
import com.my.app.ErrorController;
import com.my.app.GoController;
import com.my.app.IpController;
import com.my.app.JoinApiController;
import com.my.app.JointApiController;
import com.my.app.LoginController;
import com.my.app.WxApiController;  
import com.my.app.bean.sec.Group;
import com.my.app.bean.sec.Permission;
import com.my.app.bean.sec.Role;
import com.my.app.bean.sec.RolePermission;
import com.my.app.bean.sec.User;
import com.my.app.bean.sec.UserInfo;
import com.my.app.bean.sec.UserRole;
import com.my.app.bean.wx.Data;
import com.my.app.biz.ChannelController;
import com.my.app.biz.ChannelSmsController;
import com.my.app.biz.ChannelUrlController;
import com.my.app.biz.CpTaskController;
import com.my.app.biz.CpTaskLiucController;
import com.my.app.interceptor.ExceptionInterceptor;
import com.my.app.sec.GroupController;
import com.my.app.sec.PermissionController;
import com.my.app.sec.RoleController;
import com.my.app.sec.UserController;
import com.my.app.task.TaskController;
import com.my.app.task.WxMoneyTask;
import com.my.app.task.WxTask;
import com.my.app.task.WxUpdateTask;
import com.my.app.wx.MonitorContrller;
import com.my.app.wx.AgentContrller;
import com.my.app.wx.ChannelContrller;
import com.my.app.wx.FollowReviewContrller;
import com.my.app.wx.FollowTaskContrller;
import com.my.app.wx.GroupSettingController;
import com.my.app.wx.MdataController;
import com.my.app.wx.ReadReviewContrller;
import com.my.app.wx.ReadTaskContrller;
import com.my.app.wx.SmsController;
import com.my.app.wx.UserCountContrller;
import com.my.app.wx.WxController;
import com.my.app.wx.WxPhoneController;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.GroupSetting;
import com.my.app.wx.bean.Mdata;
import com.my.app.wx.bean.ReadTask;
import com.my.app.wx.bean.Setting;
import com.my.app.wx.bean.Sms;
import com.my.app.wx.money.UserMoneyContrller;
import com.my.app.wx.money.UserPayContrller;
import com.my.app.wxphone.LinkContrller;
import com.my.upload.UploadDeviceinfo;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPay;
import com.my.app.wx.bean.UserPayLog;


public class MyConfig extends JFinalConfig{
	 /**
     * 供Shiro插件使用。
     */
    Routes routes;
	/**
	 * 此方法用来配置 JF inal 常量
	 * @param me
	 */
	@Override
	public void configConstant(Constants me) {
		//设置是开发者模式
		loadPropertyFile("init.properties");
		String debug_test=getProperty("debug.test");
		if("true".equals(debug_test))			
		{
			me.setDevMode(true);
		}else
		{
			me.setDevMode(false);
		}
		me.setViewType(ViewType.JSP);
		me.setError404View("/error/404.html");
		me.setError500View("/error/500.html");
	}
	/**
	 * 设置访问路由(路径)
	 * @param me
	 */
	@Override
	public void configRoute(Routes me) {
		this.routes = me;
		me.add("/error", ErrorController.class);
		//第一个参数为路径名，第二个参数为控制器
		me.add("/", GoController.class);
		

		//me.add("/admin", AdminController.class);
		//me.add("/hello", HelloController.class);
		//me.add("/user", UserController.class);
		//用户权限
		me.add("/login", LoginController.class);
		me.add("/admin", AdminController.class);
		me.add("/sec/user", UserController.class);
		me.add("/sec/role", RoleController.class);
		me.add("/sec/permission", PermissionController.class);
		me.add("/sec/group", GroupController.class);
		//客户商务
		me.add("/biz/channel", ChannelController.class);
		me.add("/biz/channelUrl", ChannelUrlController.class);
		me.add("/biz/channelSms", ChannelSmsController.class);
		me.add("/biz/cpTask", CpTaskController.class);
		me.add("/biz/cpTaskLiuc", CpTaskLiucController.class);
		
		me.add("/upload", UploadDeviceinfo.class);
		me.add("/ip", IpController.class);
		
		me.add("/task", TaskController.class);
		
		me.add("/api", ApiController.class);
		me.add("/device", DeviceController.class);
		
		/**
		 * 微信用
		 */
		me.add("/wx", WxController.class); 
		me.add("/wx/api", WxApiController.class);
		me.add("/wx/join", JoinApiController.class);
		me.add("/wx/joint", JointApiController.class);
		me.add("/wx/agent", AgentContrller.class);
		me.add("/wx/channel", ChannelContrller.class);
		me.add("/wx/readtask", ReadTaskContrller.class);
		me.add("/wx/followtask", FollowTaskContrller.class);
		
		me.add("/wx/reporttask", com.my.app.wx.ReportStatisticsContrller.class);//阅读统计
		me.add("/wx/readingDetail", com.my.app.wx.ReadingDetailContrller.class);//阅读明细
		me.add("/wx/concerntask", com.my.app.wx.ConcernStatisticsContrller.class);//关注统计
		me.add("/wx/followDetailed", com.my.app.wx.FollowDetailedContrller.class);//关注明细
		
		me.add("/wx/readreview", ReadReviewContrller.class);
		me.add("/wx/followreview", FollowReviewContrller.class);
		me.add("/wx/usercount", UserCountContrller.class);
		
		me.add("/wx/setting", com.my.app.wx.SettingController.class);
		
		me.add("/wx/user", com.my.app.wx.UserController.class);
		me.add("/wx/role", com.my.app.wx.RoleController.class);
		me.add("/wx/permission", com.my.app.wx.PermissionController.class);
		me.add("/wx/group", com.my.app.wx.GroupController.class);
		
		me.add("/wx/usermoney", UserMoneyContrller.class);
		me.add("/wx/userpay", UserPayContrller.class);
		me.add("/wx/monitor", MonitorContrller.class);
		
		me.add("/wx/sms", SmsController.class);
		me.add("/wx/groupsetting", GroupSettingController.class); 
		 
		me.add("/wxphone", WxPhoneController.class);
		me.add("/wxphone/link", LinkContrller.class);
		me.add("/wxphone/reporttask",com.my.app.wxphone.ReportStatisticsContrller.class);//阅读统计
		me.add("/wxphone/followtask", com.my.app.wxphone.FollowTaskContrller.class);
	    me.add("/wxphone/userpay", com.my.app.wxphone.UserPayContrller.class);
	    
	    
	    me.add("/wx/mdata", MdataController.class);
		/*
		
		SmsReqTask srt=new SmsReqTask();
		srt.start();
		SmsVerifyTask svt=new SmsVerifyTask();
		svt.start();		
		
		IPReqTask ipt=new IPReqTask();
		ipt.start();
		
		//LiucReqTask liuct=new LiucReqTask();
		//liuct.start();
		
		//PostTask postTask=new PostTask();
		//postTask.start();
		
		//访问方法 http://xxxx/hello/[方法名(默认不填为index)]/参数1-参数2-参数3...
		 */
		//WxTask wx=new WxTask();
		//wx.start();	
		loadPropertyFile("init.properties");
		String debug_test=getProperty("debug.run");
		if(!"local".equals(debug_test))
		{
		WxUpdateTask wxupdate=new WxUpdateTask();
		wxupdate.start();	
		WxMoneyTask wxMoneyTask=new WxMoneyTask();
		wxMoneyTask.start();
		}
		
	}
	
	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * JFinalJFinalJFinal JFinal的全局 拦截器(non-Javadoc) 
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		//me.add(new LoginInterceptor());
		me.add(new ShiroInterceptor());
		me.add(new ExceptionInterceptor());

	}
	/*
	//此方法用来配置 JFinalJFinalJFinal 的 PluginPlugin Plugin ，
	//如下代码配置了 ，如下代码配置了 C3p0C3p0 数据库连接池插件与    ActiveRecord 数据库访问插件 。
	//通过以下的配置， 可以 在应用 中使用 ActiveRecord非常方便地操作数据库 。
	 */
	@Override
	public void configPlugin(Plugins me) {
		 
		loadPropertyFile("init.properties");
		System.out.println("mysql.jdbcUrl:"+getProperty("mysql.jdbcUrl")); 
		System.out.println("mysql.userName:"+getProperty("mysql.userName"));
		System.out.println("mysql.passWord:"+getProperty("mysql.passWord")); 
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("mysql.jdbcUrl"),
		getProperty("mysql.userName"), getProperty("mysql.passWord")); 
		
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		 
//		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://127.0.0.1/jf", "root", "sasa");
//		me.add(cp);
//		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
//		me.add(arp);
		
		//arp.addMapping("t_user", User.class);   
		arp.addMapping("biz_channel", com.my.app.wx.bean.Channel.class); 
		arp.addMapping("biz_channel", com.my.app.wx.bean.Agent.class); 
		arp.addMapping("sec_user", User.class);
		
		arp.addMapping("sec_user", User.class);
		arp.addMapping("sec_user_info", UserInfo.class); 
		arp.addMapping("sec_user_role", UserRole.class); 
		arp.addMapping("sec_role", Role.class); 
		arp.addMapping("sec_permission", Permission.class); 
		arp.addMapping("sec_role_permission", RolePermission.class); 
		arp.addMapping("sec_group", Group.class); 
		arp.addMapping("setting", Setting.class); 
		
		arp.addMapping("ph_dnum", Mdata.class); 
		
		/*
		arp.addMapping("biz_channel", Channel.class); 
		arp.addMapping("biz_channel_url", ChannelUrl.class); 
		arp.addMapping("biz_channel_url_cnt", ChannelUrlCnt.class); 
		arp.addMapping("biz_channel_url_log", ChannelUrlLog.class); 
		
		arp.addMapping("biz_channel_sms", ChannelSms.class);
		arp.addMapping("biz_channel_sms_cnt", ChannelSmsCnt.class); 
		arp.addMapping("biz_channel_sms_log", ChannelSmsLog.class); 
		arp.addMapping("biz_channel_sms_req", ChannelSmsReq.class); 
		
		arp.addMapping("cp_new_log", CpLog.class); 
		
		arp.addMapping("cp_task", CpTask.class); 
		arp.addMapping("cp_liuc_task", CpTaskLiuc.class); 
		*/
		/***
		 * 微信用
		 */             
		arp.addMapping("biz_channel", com.my.app.wx.bean.Channel.class); 
		arp.addMapping("read_task", ReadTask.class); 
		arp.addMapping("follow_task", FollowTask.class); 
		arp.addMapping("user_money", UserMoney.class);
		arp.addMapping("user_money_in_list", UserPay.class);
		arp.addMapping("user_money_pay_list", UserPayLog.class);
		arp.addMapping("wx_sms", Sms.class);
		arp.addMapping("wx_group_setting", GroupSetting.class);
		
		//加载Shiro插件
		//me.add(new ShiroPlugin(routes));
		me.add(new EhCachePlugin());
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
		shiroPlugin.setLoginUrl("/login/");
		shiroPlugin.setSuccessUrl("/admin/");
		shiroPlugin.setUnauthorizedUrl("/error/auth");
		me.add(shiroPlugin);
		
		//缓存user模块 到 redis
		String redis_ip=getProperty("redis.ip");
		String redis_pwd=getProperty("redis.pwd");
		String redis_port=getProperty("redis.port");
		int port=6379;
		if(redis_port!=null)
		{
			port=Integer.parseInt(redis_port);
		}
		if(redis_pwd!=null&&redis_pwd.length()>0){
			System.out.println("redis pwd");
	        RedisPlugin userRedis=new RedisPlugin("userc",redis_ip,port,1800,redis_pwd);
	        me.add(userRedis);			
		}else
		{
			System.out.println("redis no pwd");
			RedisPlugin userRedis=new RedisPlugin("userc",redis_ip,port,1800);
	        me.add(userRedis);	
		}

 
		//me.add(new EhCachePlugin()); 
		//me.add(new ShiroPlugin(routes));
		

	}



}
