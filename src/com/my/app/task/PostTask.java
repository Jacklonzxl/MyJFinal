package com.my.app.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil; 

public class PostTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(10000);
		while(true)
		{
			postPush();
			sleep(5000);
			posturl();
			sleep(5000);
			postPush2();
			sleep(5000);
			
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void posturl() throws InterruptedException
	{
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-6000000).substring(0,10);
		String enowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
		String sql="select * from cp_new_log where (channel='31' or channel='32' or channel='1001') and status=1 and adddate>='"+nowdate+"' and "
				+ "adddate<='"+enowdate+" 24'  order by id desc";
		//System.out.println(sql);
		List<Record> list =Db.find(sql);
		for(int i=0;i<list.size();i++)
		{
			try{
			Map<String,Object> map=new HashMap<String,Object>();
			Record req=list.get(i);
			String url="http://114.119.5.218:7539/code-platform/platform/synStatus.do?number="+req.getStr("phone")+"&contentNo=CN032024&status=2";
			//System.out.println(url);
			//map.put("phone", req.get("phone"));
			///map.put("code", req.get("vcode"));
			String str=HttpUtil.doPost(url, map); 
			System.out.println("poststatus:"+str);
			if(str.length()>0&&!str.equals("error"))
			{
			 req.set("status",2);
			 Db.update("cp_new_log", req);
			}else if(str.length()>0&&str.equals("error"))
			{
				req.set("status",3);
				Db.update("cp_new_log", req);	
			}
			
	        
			}catch(Exception e){
				e.printStackTrace();
			}
			sleep(10);
		}		
	}
	public void postPush2() throws InterruptedException
	{
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*15)).substring(0,10);
		String enowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
		String sql="select * from ph_info_2 where  status=4 and vdate>='"+nowdate+"' and "
				+ "vdate<='"+enowdate+" 24'  order by id desc";
		List<Record> list =Db.find(sql);
		//System.out.println("ph_info_2");
		for(int i=0;i<list.size();i++)
		{
			Record req=list.get(i);
			Record record=Db.findFirst("select * from cp_new_log where  phone='"+req.getStr("phone")+"' and adddate>='"+nowdate+"'");
			if(record!=null&&record.getStr("phone")!=null)
			{
				try{
				//Map<String,Object> map=new HashMap<String,Object>();
				
				String url="http://api.zyy6688.com:8181/web/rest/vc/report?phone="+req.getStr("phone")+"&code="+req.getStr("vcode");
				//System.out.println(url);
				//map.put("phone", req.get("phone"));
				///map.put("code", req.get("vcode"));
				String str=HttpUtil.doGet(url, "utf-8", null); 
				System.out.println("pushstatus:"+str);
				if(str.length()>0&&str.equals("{\"status\":1}"))
				{
				 req.set("status",5);
				 Db.update("ph_info_2", req);
				}
				sleep(10);
				}catch(Exception e){
					e.printStackTrace();
				}		
			}
		}
		
	}
	public void postPush() throws InterruptedException
	{
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
		String sql="select * from ph_info where  status=4 and vdate>='"+nowdate+"' and "
				+ "vdate<='"+nowdate+" 24'  order by id desc";
		List<Record> list =Db.find(sql);
		//System.out.println("ph_info_2");
		for(int i=0;i<list.size();i++)
		{
			Record req=list.get(i);
			Record record=Db.findFirst("select * from cp_new_log where  phone='"+req.getStr("phone")+"' and adddate>='"+nowdate+"'");
			if(record!=null&&record.getStr("phone")!=null)
			{
				try{
				//Map<String,Object> map=new HashMap<String,Object>();
				
				String url="http://api.zyy6688.com:8181/web/rest/vc/report?phone="+req.getStr("phone")+"&code="+req.getStr("vcode");
				//System.out.println(url);
				//map.put("phone", req.get("phone"));
				///map.put("code", req.get("vcode"));
				String str=HttpUtil.doGet(url, "utf-8", null); 
				System.out.println("pushstatus:"+str);
				if(str.length()>0&&str.equals("{\"status\":1}"))
				{
				 req.set("status",5);
				 Db.update("ph_info", req);
				}
				sleep(10);
				}catch(Exception e){
					e.printStackTrace();
				}		
			}
		}
		
	}
}
