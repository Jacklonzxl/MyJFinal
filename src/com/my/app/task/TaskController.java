package com.my.app.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;

import redis.clients.jedis.Jedis;

public class TaskController  extends Controller{

 
	public void uploadWxAccount() { 
		uploadWxAccountInfo();
	}
	public void uploadWxAccountInfo() { 

		HttpServletRequest request=getRequest();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000); 
		// 设置临时文件存储位置
		String autoCreatedDateDirByParttern = "yyyy" + File.separatorChar + "MM" + File.separatorChar + "dd";
        String autoCreatedDateDir = DateFormatUtils.format(new java.util.Date(), autoCreatedDateDirByParttern);
        ServletContext sc=request.getSession().getServletContext();
        String base = sc.getRealPath("/")+"/files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir;
		File file = new File(base);
		if(!file.exists())
			file.mkdirs();
		factory.setRepository(file);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
		
		try {
			List<?> items = upload.parseRequest(request);
			Map<String,String> map=new HashMap<String,String>();
			for(int j=0;j< items.size();j++)
			{
				FileItem item_j = (FileItem) items.get(j);
				if(item_j.isFormField())
				{
					map.put(item_j.getFieldName(), item_j.getString());
					//System.out.println(item_j.getFieldName()+":"+item_j.getString());
				}
			}
			FileItem item = null;
			String fileName = null;
			for (int i = 0 ;i < items.size(); i++){
				item = (FileItem) items.get(i);
				fileName =   File.separator+map.get("account")+"_"+System.currentTimeMillis()+".xml";// File.separator +item.getName();
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0) {
					item.write(new File(base+fileName));					 
				}else
				{
					continue;
				}
				
				Record account=new Record();
				account.set("reg_ip", getIpAddr(request))
				.set("devicefile", "files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir+fileName)
				.set("create_time", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("last_use_time", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("account",map.get("account"))
				.set("wxid",map.get("wxid"))
				.set("org_wxid", map.get("org_wxid"))
				.set("password", map.get("password"))
				.set("imei", map.get("imei"))
				.set("imsi", map.get("imsi"))
				.set("model_id", map.get("model_id"))
				.set("city", map.get("city"))
				.set("info_nickname", map.get("info_nickname"))
				.set("info_gender", map.get("info_gender"))
				.set("info_province", map.get("info_province"))
				.set("info_city", map.get("info_city"))
				.set("info_sign", map.get("info_sign"))
				.set("info_photo", map.get("info_photo"))
				.set("iccid", map.get("iccid"))
				.set("androidid", map.get("androidid"))
				.set("mac", map.get("mac"))
				.set("bluetooth", map.get("bluetooth"))
				.set("serial", map.get("serial"))
				.set("guid", map.get("guid"))
				.set("language", map.get("language"))
				.set("brand", map.get("brand"))
				.set("model", map.get("model"))
				.set("android_os", map.get("android_os"))
				.set("status", 1)
				.set("cpu_arch", map.get("cpu_arch")) 
				.set("account_group_id", map.get("account_group_id")) ;
				Db.save("account_android", account);
					
			}
			posturl();
			renderText("ok");
		} catch (FileUploadException e) {
			e.printStackTrace();
			renderText("no");
		} catch (Exception e) {
			e.printStackTrace();
			renderText("no");
		}
		 
	}
	
	public void posturl() throws InterruptedException
	{
		new Thread()
		{
			@Override
			public void run() {
				String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-6000000).substring(0,10);
				String sql="select * from account_android where   create_time>='"+nowdate+"' and "+ "create_time<='"+nowdate+" 24' and last_status=0  order by id desc";
				List<Record> list =Db.find(sql);
				for(int i=0;i<list.size();i++)
				{
					try{
					Map<String,Object> map=new HashMap<String,Object>();
					Record req=list.get(i);
					String url="http://ss.58info.net:8090/ss/confm?aid=c102&apid=102&no="+req.getStr("account")+"&authkey=Fr5g9s";
					//System.out.println("post_url:"+url);
					String str=HttpUtil.doPost(url, map); 
					req.set("last_status",1);
					Db.update("account_android", req);
					System.out.println("post_status:"+str);
					sleep(1000);			        
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
		}.start();
		
	}
	public void isokph()
	{
		String sql=" select * from account_android_reg where ph='"+getPara("ph")+"' limit 1";
		//System.out.println(sql );
		if(Db.find(sql).size()==0&&getPara("ph")!=null)
		{
			//System.out.println(getPara("ph")+" ok" );
		    renderText("ok");		
		}else
		{
			//System.out.println("no" );
			renderText("no");
		}
	}
	public void isokimei()
	{
		if(getPara("type").equals("2"))
		{
			String sql=" select * from account_android where account='"+getPara("ph")+"' and imei='"+getPara("imei")+"' limit 1";
			System.out.println(sql);
			if(Db.find(sql).size()>0&&getPara("ph")!=null)
			{
				System.out.println("imei:"+getPara("imei")+" ok" );
			    renderText("ok");		
			}else
			{
				//System.out.println("no" );
				System.out.println("imei:"+getPara("imei")+" no" );
				long settimes=System.currentTimeMillis()-(1000*3600*24);
				String updateSql="update account_android set status=-107,last_use_time='"+TimeUtil.GetSqlDate(settimes)+"' where  account='"+getPara("ph")+"' ";
				Db.update(updateSql);
				renderText("no");
			}
		}else
		{
			String sql=" select * from account_android where  imei='"+getPara("imei")+"' limit 10";
			//System.out.println(sql );
			if(Db.find(sql).size()<8)
			{
				System.out.println("imei reg:"+getPara("imei")+" ok" );
			    renderText("ok");		
			}else
			{
				//System.out.println("no" );
				System.out.println("imei reg:"+getPara("imei")+" no" );
				renderText("no");
			}
		}
	}
	public void pushphinfo()
	{
		Record record=new Record();
		record.set("imsi", getPara("imsi"));
		record.set("iccid", getPara("iccid"));
		record.set("ap_mac", createRandomMacAddress(""));
		record.set("bluetooth",createRandomMacAddress(""));
		boolean isimsi=Db.findFirst("select * from b_phinfo where imsi='"+getPara("imsi")+"'")==null;
		boolean isiccid=Db.findFirst("select * from b_phinfo where iccid='"+getPara("iccid")+"'")==null;
		boolean ismac=Db.findFirst("select * from b_phinfo where ap_mac='"+getPara("ap_mac")+"' or bluetooth='"+getPara("bluetooth")+"'")==null;
		if(getPara("imsi")!=null&&getPara("imsi").length()>0&&isimsi&&isiccid&&ismac)
		{
			Db.save("b_phinfo", record);
		}
		renderText("ok");		
	}
 
	/***
	 * 脚本查询是否有任务
	 */
	public void getTask()
	{
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
        String channel=getPara("channel");
		int taskstatus=1;
		String sts="";
		String str="no";
		int wx_reg_day=10;
		int wx_active_hours=30;
	    Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();
		if(jedis.exists("wx_reg_day"))
	    {
	    	wx_reg_day=Integer.parseInt(jedis.get("wx_reg_day"));
	    }
	    taskstatus=getTaskstatus(jedis,channel);
	   
	    if(jedis.exists("wx_active_hours"))
	    {
	    	wx_active_hours=Integer.parseInt(jedis.get("wx_active_hours"));
	    }
	    jedis.close();
	    
	    
  
        	
        String sql="select * from account_android where account_group_id='"+channel+"'   and create_time>='"+nowdate+"' ";		 
		if(getHH()>=6||getHH()<3)
		{
			//System.out.println("taskstatus_2222:"+taskstatus+" v:"+getPara("v"));
			if(taskstatus==6)
			{
				sts="6";	
				str=liucTask(channel,wx_active_hours); 
				System.out.println("6:"+str);
				if(str.equals("no")){
					sts="1";
				}
								
			}else if(taskstatus==0)
			{
				
				
				List<Record> list=Db.find(sql);
				if(list.size()<wx_reg_day)
				{
					str="ok";
					sts="1";
				}else
				{
					str=liucTask(channel,wx_active_hours);
					if(str.equals("ok")){
						sts="2";
					}
				}
			}else if(taskstatus==1||taskstatus==3)
			{
				str=liucTask(channel,wx_active_hours);
				//System.out.println("liuc ok:"+str);
				if(str.equals("no"))
				{
				  	 
					//System.out.println("reg:"+sql);
					List<Record> list=Db.find(sql);
					if(list.size()<wx_reg_day)
					{
						str="ok";
						sts="1";
					}
				}else
				{					
					sts="2";					
				} 
			}else if(taskstatus==2)
			{
				long time=System.currentTimeMillis();
				if(time%2==1)
				{
					List<Record> list=Db.find(sql);
					if(list.size()<wx_reg_day)
					{
						str="ok";
						sts="1";
					}else
					{
						str=liucTask(channel,wx_active_hours);
						if(str.equals("ok")){
							sts="2";
						}
					}
				}else
				{
					str=liucTask(channel,wx_active_hours);
					if(str.equals("no"))
					{
					  	 
						List<Record> list=Db.find(sql);
						if(list.size()<wx_reg_day)
						{
							str="ok";
							sts="1";
						}
					}else
					{						
						sts="2";						
					}
					
				}
				
			}else if(taskstatus==4)
			{
				str=liucTask(channel,wx_active_hours);
				if(str.equals("no"))
				{
				  	 
					List<Record> list=Db.find(sql);
					if(list.size()<wx_reg_day)
					{
						str="ok";
						sts="1";
					}
				}else
				{					
					sts="4";					
				}
			}
		
		 }else
		 {
			 sts="6";
		 }
        
		renderText(sts);
		
		
	}
	/***
	 * 脚本查询是否有留存任务
	 */
	public String liucTask(String channel,int h)
	{
      	
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*(h*1L))); 
		//String cr_date=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*24*60L));
		String status="status<>-300 and status<>404 ";//status="status=1";
		if(getHH()>=2&&getHH()<=6)
		{
			status="status<>-300 and status<>404 ";
			 
		}
	   
	    Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();
	    int taskstatus=getTaskstatus(jedis,channel); 
	    jedis.close();
	    if(taskstatus==6)
	    {
	    	nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*8L));
			 
	    }
		if(channel.equals("123"))
		{
			nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*6L));
		}
		String liuc_sql="select * from account_android where account_group_id='"+channel+"' and "+status+"   and last_use_time<='"+nowdate+"' limit 2";// and create_time>'"+cr_date+"'";

		//System.out.println(liuc_sql);

		List<Record> list=Db.find(liuc_sql);
		if(list.size()>0)
		{
			return "ok";
		}else
		{
			return "no";
		}
			
		 
		
		
	}
	public int getTaskstatus(Jedis jedis,String channel)
	{
		int taskstatus=1;
        
	    if(jedis.exists("wx_taskstatus"))
	    {
	    	taskstatus=Integer.parseInt(jedis.get("wx_taskstatus"));
	    	if(channel.equals("131")||channel.equals("133")||channel.equals("135")||channel.equals("136")||channel.equals("138")||channel.equals("139"))
	    	{
	    		taskstatus=6;
	    	}else if(channel.indexOf("13")>-1||channel.indexOf("14")>-1||channel.indexOf("15")>-1)
	    	{
	    		taskstatus=6;
//	    		if(getHH()<=7||getHH()>=15)
//		    	{
//		    		taskstatus=6;
//		    	} 
	    	}else if(channel.indexOf("15")>-1)
	    	{
	    		taskstatus=1;
	    	}
	    	else if(channel.indexOf("108")>-1)
	        {
	    		taskstatus=1;
	    	}
	    	if(taskstatus==6)
	    	{
//	    		if(getHH()>1&&getHH()<6)
//		    	{
//		    		taskstatus=1;
//		    	}else if((getHH()>5&&getHH()<8)||(getHH()>15&&getHH()<18)||(getHH()>10&&getHH()<13))
//		    	{
//		    		taskstatus=0;
//		    	}
	    	}
	    	
	    	
	    }
	    
	    return taskstatus;
	}
	
	/***
	 * android手机端
	 * 取留存数据
	 */
	public void liuc()
	{
		String channel=getPara("channel");
	    Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();
	    int wx_active_hours=30;
	    if(jedis.exists("wx_active_hours"))
	    {
	    	wx_active_hours=Integer.parseInt(jedis.get("wx_active_hours"));
	    }
	    int wx_active_isreg=0;
	    if(jedis.exists("wx_active_isreg"))
	    {
	    	wx_active_isreg=Integer.parseInt(jedis.get("wx_active_isreg"));
	    }
	    int taskstatus=getTaskstatus(jedis,channel);   
	    jedis.close();
	    System.out.println("taskstatus:"+taskstatus);
		
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*(wx_active_hours*1L)));
		String cr_date=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*24*90L));
 
		if(channel.equals("123"))
		{
			nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*6L));
		}
		String order="last_use_time";
		if(wx_active_isreg==1)
		{
			cr_date=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*20L)).substring(0, 10);
		}
		String status="status<>-300 and status<>404";//status="status=1";
		if(getHH()>2&&getHH()<7)
		{
			status="status=-300 ";
 
		} 
		String where="account_group_id='"+channel+"'  and "+status+" and last_use_time<='"+nowdate+"' and create_time>'"+cr_date+"'";
 
		if(taskstatus==4)
		{
			order="create_time";
			where="account_group_id='"+channel+"'   and create_time>'"+cr_date+"' and create_time<='"+nowdate+"' and last_use_time<='"+nowdate+"' and status<>-300  and status<>404 and (wxid is null or wxid='')";
		}
		if(taskstatus==3)
		{
			order="create_time";
			where="account_group_id='"+channel+"'  and create_time>'"+cr_date+"'  and create_time<='"+nowdate+"' and last_use_time<='"+nowdate+"' and status<>-300 and status<>404  and wxid is not null and wxid<>''";
		}
		if(taskstatus==6)
		{		
			String cr_str="";
			if(getHH()>2&&getHH()<7)	
			{
				String cr_d=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*24*10L));
				cr_str=" and create_time>'"+cr_d+"'";
			}
			nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*8L));
			where="account_group_id='"+channel+"'  and last_use_time<='"+nowdate+"' and  "+status+cr_str;
		}
		String liuc_sql="select * from account_android where  "+where+" order by "+order+"  limit 2";
		//System.out.println("liuc :"+liuc_sql);
		List<Record> list=Db.find(liuc_sql);
		if(list!=null&&list.size()>0)
		{
			//Collections.shuffle(list);		
			Record record=list.get(0);		
			Db.update("update account_android set  last_use_time='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"' where id="+record.get("id"));
			Record log=new Record();
			log.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			log.set("userid", record.get("id"));
			log.set("account", record.get("account"));
			Db.save("account_liuc_log", log);
			String a_str=record.getStr("account");
			String wxid=record.getStr("wxid");
			if(taskstatus==3&&wxid!=null&&!wxid.equals(""))
			{
				a_str=wxid;
			}
			renderText(record.getStr("devicefile")+"|"+a_str+"|"+record.getStr("password")); 
		}else
		{
			renderText(""); 
		}
	}	
	/***
	 * 保存微信端update_account值
	 */
	public void update_account()
	{
      
		String dnum=getPara("dnum"); 
		String UserId=getPara("ph"); 
		String Result=getPara("status");
		String wxid=getPara("wxid");
		String wxidstr="";
		String sql="";
		if(wxid!=null&&wxid.length()>3)
		{
			Result="1";
			wxidstr=",wxid='"+wxid+"'";
			sql="update account_android set status='"+Result+"'"+wxidstr+" where account='"+UserId+"' and (wxid is null or wxid='')";
			Db.update("update wx_ph_id set status=1 where ph='"+UserId+"' and wxid='"+wxid+"'");
		}else 
		{
			sql="update account_android set status='"+Result+"' where account='"+UserId+"'";
		}
		if(UserId!=null&&Result!=null&&Result.length()>0&&UserId.length()>0)
		{
		Db.update(sql);
		Record record=new Record();
		record.set("userid", UserId);
		record.set("status", Result);
		record.set("serverid", 0);
		record.set("vpsid", 0);
		record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		record.set("dnum", dnum);
		Db.save("account_android_status", record);        
		renderText("true");		
		}else
		{
			System.out.println("t UserId:"+UserId+"t Result:"+Result);
			renderText("no");
		}
		
	}
	/***
	 * 微信端update_wxid值,用微信号登录时用
	 */
	public void update_wxid()
	{
  
		
		
		String wxid=getPara("wxid");  //微信id
		String password=getPara("password");   //密码
		String channel=getPara("channel"); //分组id
		String status=getPara("status");  //登录成功status=1,密码错误|封号status=0,其他不处理
        
		
		if(wxid!=null&&wxid.length()>3)
		{
           if("1".equals(status))
           {
     
				Record record=Db.findFirst("select * from wx_ids where wxid='"+wxid+"'");
				if(record!=null)
				{
					record.set("lastdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record.set("password", password);
					record.set("groupid", channel);
					Db.update("wx_ids", record);	
				}else
				{
					record=new Record();
					record.set("wxid", wxid);
					record.set("password", password);
					record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record.set("lastdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record.set("groupid", channel);
					Db.save("wx_ids", record);
				}
				Db.update("update wx_ph_id set status=1 where  wxid='"+wxid+"'");
           }else if("0".equals(status))
           {
        	   String sql="update account_android set wxid='' where wxid='"+wxid+"'";
        	   Db.update(sql);
           }

		}
 
		renderText("true");
		
	}
	
	/***
	 * 微信端update_wxid值,用微信号登录时用
	 */
	public void get_wxid()
	{
  
		
		
		String ph=getPara("ph");  
		String wxid="";
		String password="";
		if(ph.length()==11)
		{
            
				Record record=Db.findFirst("select * from wx_ph_id where ph='"+ph+"'");
				if(record!=null)
				{
					
					 
					wxid=record.getStr("wxid");	
					password=record.getStr("password");	
				}else
				{
					wxid=getStringRandom(10);
					password=getStringRandom(9);
					record=new Record();
					record.set("thedate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record.set("wxid", wxid);
					record.set("ph", ph);
					record.set("password", password);
					Db.save("wx_ph_id", record);
				}
           

		}
 
		renderText(wxid+"|"+password);
		
	}	
	/***
	 * 保存微信端注册失败值
	 */
	public void update_reg()
	{
  
		String dnum=getPara("dnum"); 
		String UserId=getPara("ph"); 
		String Result=getPara("status");
		if(Result==null||Result.length()==0)
		{
			renderText("no");
			return;
		}
 
		Record record=new Record();
		record.set("ph", UserId);
		record.set("status", Result);
		record.set("serverid", 0);
		record.set("vpsid", 0);
		record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		record.set("dnum", dnum);
		Db.save("account_android_reg", record);

		renderText("true");
		
	}
	
	/***
	 * android手机端
	 * 取留存数据
	 */
	public void account_info()
	{
 
		String UserId=getPara("ph"); 
		String sql="select * from account_android where account='"+UserId+"'";
		Record record=Db.findFirst(sql);
		UserInfo userInfo=new UserInfo();
		userInfo.setInfo_city("");
	}	
	
 

	/****
	 * 脚本是否通过账号ip
	 */
	public void verify_ip()
	{
		 
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-6000000L);
		String ip=getIpAddr(getRequest());
		if(ip==null||ip.equals("")||ip.equals("null")) {
				renderText("no");
	    }else{
			String sql="select * from vps_ip where ip='"+ip+"' and adddate>='"+nowdate+"' ";
			List<Record> list=Db.find(sql);
			if(list.size()==0)
			{
				renderText("ok");
				Record record=new Record();
				record.set("serverid", 0);
				record.set("vpsid", 0);
				record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				Db.save("vps_ip", record);
			}else
			{
//				nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
//				sql="select * from vps_ip where ip='"+ip+"' and adddate>='"+nowdate+"' ";
//				list=Db.find(sql);
//				if(list.size()<5)
//				{
//					renderText("ok");
//					Record record=new Record();
//					record.set("serverid", 0);
//					record.set("vpsid", 0);
//					record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
//					Db.save("vps_ip", record);
//				}else
//				{
//					renderText("no");
//				}
				renderText("no");
				
			}
		 }
		
	}
	 
	public void checkAccountIp()
	{
		 
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-6000000L);
		String ip=getIpAddr(getRequest());
		if(ip==null||ip.equals("")||ip.equals("null")) {
				renderText("no");
	    }else{
			String sql="select * from vps_ip where ip='"+ip+"' and adddate>='"+nowdate+"' ";
			List<Record> list=Db.find(sql);
			if(list.size()==0)
			{
				renderText("ok");
				Record record=new Record();
				record.set("serverid", 0);
				record.set("vpsid", 0);
				record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				Db.save("vps_ip", record);
			}else
			{
 
				renderText("no");
				
			}
		 }
		
	}
	
    public void getph()
    {
    	Map<String,String> map=new HashMap<String,String>();
    	//map.put("102","http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102");	
    	//map.put("105","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=henan&authkey=Fr5g9s");
    	//map.put("103","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=henan&authkey=Fr5g9s");
    	//map.put("101","http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102");
    	
    	map.put("111","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=guangxi"); 	
    	map.put("108","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=guangdong&authkey=Fr5g9s");
    	map.put("107","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=liaoning&authkey=Fr5g9s");   	
    	map.put("109","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=shandong&authkey=Fr5g9s"); 
    	map.put("106","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=shandong&authkey=Fr5g9s");    	
    	map.put("104","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=heilongjiang&authkey=Fr5g9s");
    	map.put("102","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=heilongjiang&authkey=Fr5g9s");
    	map.put("101","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=zhejiang&authkey=Fr5g9s");
    	map.put("100","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=henan&authkey=Fr5g9s");

    	
    	map.put("121","http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102&authkey=Fr5g9s");
    	map.put("122","http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102&authkey=Fr5g9s");
    	map.put("123","http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102&authkey=Fr5g9s");
    	
    	
    	map.put("131","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=hebei&authkey=Fr5g9s");
    	map.put("132","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=heilongjiang&authkey=Fr5g9s");
    	map.put("133","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=henan&authkey=Fr5g9s");
    	
    	map.put("134","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=yunnan&authkey=Fr5g9s");
    	map.put("234","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=yunnan&authkey=Fr5g9s");
    	
    	map.put("135","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=sichuan&authkey=Fr5g9s");
    	
    	map.put("136","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=shanxi&authkey=Fr5g9s");
    	map.put("235","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=shanxi&authkey=Fr5g9s");
    	
    	map.put("137","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=shanxi2&authkey=Fr5g9s");
    	map.put("138","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=jilin&authkey=Fr5g9s");
    	map.put("139","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=anhui&authkey=Fr5g9s");
    	map.put("140","http://ss.58info.net:8090/ss/getprovmobi?aid=c102&apid=102&prov=hebei&authkey=Fr5g9s");
    	
    	String channel=getPara("channel");
    	
    	String url=map.get(channel);
    	if(url==null||url.equals(""))
    	{
    		url="http://ss.58info.net:8090/ss/getmobi?aid=c102&apid=102&authkey=Fr5g9s";
    	}
    	renderText(url);
        
    }
    public void getcode()
    {
    	Map<String,String> map=new HashMap<String,String>();   
    	map.put("200","http://121.40.65.20:7654/phone.ashx?type=getMsgPhone&businessId=5&phone=");
    	map.put("111","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("109","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("108","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("107","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("106","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");    	
    	map.put("105","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("104","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("103","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("102","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("101","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("100","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	
    	map.put("121","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("122","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	map.put("123","http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=");
    	
    	String channel=getPara("channel");
    	String url=map.get(channel);
    	if(url==null||url.equals(""))
    	{
    		url="http://ss.58info.net:8090/ss/getcode?aid=c102&apid=102&no=";
    	}
    	renderText(url);
        
    }
    public void getpost()
    {
    	Map<String,String> map=new HashMap<String,String>();   
    	map.put("200","http://121.40.65.20:7654/phone.ashx?type=successPhone&businessId=5&phone=");
    	map.put("111","");
    	map.put("109","");
    	map.put("108","");
    	map.put("107","");
    	map.put("106","");    	
    	map.put("105","");
    	map.put("104","");
    	map.put("103","");
    	map.put("102","");
    	map.put("101","");
    	map.put("100","");
    	String channel=getPara("channel");
    	String url=map.get(channel);
    	if(url==null||url.equals(""))
    	{
    		url="http://121.40.65.20:7654/phone.ashx?type=successPhone&businessId=5&phone=";
    	}
    	renderText(url);
        
    }    
    public void xiufu()
    {
    	String sql="select * from account_android_bak where status<>404 and status<>-300";
    	List<Record> list=Db.find(sql);
    	
    	for(int i=0;i<list.size();i++)
    	{
    		Record record=list.get(i);
    		Db.update("update account_android set account_group_id="+record.get("account_group_id")+" where account='"+record.get("account")+"'");
    		System.out.println(list.size()+":"+i);
    	}
    }
    
    public void readtime()
    {
    	renderText("6800");
    }
    public void getregInfo()
    {
    	String sql="select * from account_info_1 order by rand() limit 1";
    	Record record=Db.findFirst(sql);
    	String nickname="娃娃";
    	int sex=record.getInt("sex");
    	String sign=" ";
    	if(record.getStr("sign")!=null&&record.getStr("sign").trim().length()>0)
    	{
    		sign=record.getStr("sign");
    	}
    	if(record.getStr("nickname")!=null&&record.getStr("nickname").trim().length()>0)
    	{
    		nickname=record.getStr("nickname");
    	}
    	String str="{\"info_province\":\"Sichuan\",\"info_sign\":\""+sign+"\",\"info_nickname\":\""+nickname+"\",\"info_gender\":\""+sex+"\",\"info_photo\":\"http://ww2.sinaimg.cn/bmiddle/6988dee2jw1ewexqozoc1j20zk0qo7b7.jpg\",\"info_city\":\"Deyang\"}";
    	renderText(str);
    }
    public void log()
    {
    	if(getPara("dnum")!=null){
    	Record record=new Record();
    	record.set("dnum", getPara("dnum"));
    	record.set("ph", getPara("ph"));
    	record.set("status", getPara("status"));
    	record.set("thetimes", getPara("thetimes"));
    	record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
    	Db.save("account_log", record);
    	renderText("true");
    	}else
    	{
    		renderText("false");
    	}
    	
     
    }
    public void getcollect()
    {
    	if(Db.findFirst("select * from wx_collect where ph='"+getPara("ph")+"'")==null)
    	{
    		  		
    	    renderText("no");
    	    
    	}else
    	{
    		renderText("ok");
    	}
    }
    public void setcollect()
    {
    	try{
    	Record record=new Record();
		record.set("ph", getPara("ph"));   
		Db.save("wx_collect", record);
    	}catch(Exception e){}
		renderText("ok");
    }
    public void getdotype()
    { 
    	
		renderText("1");
    }
	/***
	 * 取得当前小时
	 * @return
	 */
    public int getHH()
    {
    	Date time=new Date();
    	
    	SimpleDateFormat format=new SimpleDateFormat("HH");
    	int hh=Integer.parseInt(format.format(time));
    	//System.out.println("hh:"+hh);
    	return hh;
    }
	/***
	 * 取得当前分
	 * @return
	 */
    public int getMM()
    {
    	Date time=new Date();
    	
    	SimpleDateFormat format=new SimpleDateFormat("mm");
    	int mm=Integer.parseInt(format.format(time));
    	//System.out.println("mm:"+mm);
    	return mm;
    }
	/**********************
	 * 取得真实ip地址
	 * @param request
	 * @return
	 */
	public  String getIpAddr(HttpServletRequest request) {
		 String ip = request.getHeader("x-forwarded-for");  
		 
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			 
		     ip = request.getHeader("Proxy-Client-IP");  
		 }  
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			 
		     ip = request.getHeader("WL-Proxy-Client-IP");  
		 }  
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			 
		     ip = request.getRemoteAddr();  
//		     if("1001".equals(request.getParameter("channel"))||"1000".equals(request.getParameter("channel")))
//			 {
//				 System.out.println("333:ip:"+ip);
//			 }
		 }  
		 return ip;  
		}
	   //生成随机数字和字母,  
    public  String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum =i<2|| random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
//    public static void  main(String[] args) {  
//        //StringRandom test = new StringRandom();  
//        //测试  
//        System.out.println(getStringRandom(10));  
//    }  
    ///
	/**
     * 根据虚拟机类型生成随机Mac地址
     * 
     * @param hypervType 虚拟机类型
     *     KVM: QEMU虚拟机
     *     vmware: Vmware虚拟机
     *     其他： 生成随机的Mac地址
     * @return
     */
    private static String createRandomMacAddress(String hypervType){
        String macAddress = null;
        if ("QEMU".equalsIgnoreCase(hypervType)){//根据不同的虚拟化类型生成前缀
            String prefix = "52:54:00";
            macAddress = prefix.concat(":").concat(getRandChars(3));
        } else {
            macAddress = getRandChars(6);
        }
        return macAddress;
    }

    /**
     * 生成 2个随机的小写字母或者数字组成的串
     * 
     * @return
     */
    private static String getRandChars(int len){
        String multiChars = "";
        for (int i=0;i<len;i++){
            multiChars = multiChars.concat(":");
            String chars = getRandTwoChars();
            multiChars = multiChars.concat(chars);
        }
        if (len > 0){
            multiChars = multiChars.substring(1);
        }
        return multiChars;
    }
    
    
    /**
     * 生成2个随机的小写字母或者数字
     * 
     * @return
     */
    private static String getRandTwoChars(){
        String chars = createRandomChar();
        return chars.concat(createRandomChar());
    }
    
    /**
     * 生成随机的小写字母或者数字
     * 
     * @return 随机的小写字母或者数字
     */
    private static String createRandomChar(){
        String[] chars= new String[]{
            "a","b","c","d","e","f","0",
            "1","2","3","4","5","6","7","8","9",
        };
        Random rand = new Random();
        int rInt = rand.nextInt(chars.length);
        return chars[rInt];
    }    
    public void liucTask()
    {
    	renderText("ok");
    }
}
