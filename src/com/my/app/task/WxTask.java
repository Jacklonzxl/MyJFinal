package com.my.app.task;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.bean.wx.User;
import com.my.util.MD5;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis; 

public class WxTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(90000);
		/*
		while(true)
		{
			//String serverids[]={"10","12","13","15","16","17","18","19","20"};
			Cache userCache= Redis.use("userc");
			Jedis jedis = userCache.getJedis();
			List<Record> u_list=Db.find("select * from account_8 order by last_use_time");
			Long s1= jedis.llen("user_"+1); 
			Long s10= jedis.llen("user_"+10); 
			Long s12= jedis.llen("user_"+12); 
			Long s13= jedis.llen("user_"+13); 
			Long s15= jedis.llen("user_"+15); 
			Long s16= jedis.llen("user_"+16); 
			Long s17= jedis.llen("user_"+17); 
			Long s18= jedis.llen("user_"+18); 
			Long s19= jedis.llen("user_"+19); 
			Long s20= jedis.llen("user_"+20);  
			for(int i=0;i<u_list.size();i++)
			{
				Record record=u_list.get(i);
				String key="user_"+record.getInt("account_group_id");
				//Long size = jedis.llen(key);  
				if(record.getInt("account_group_id")==1&&s1<3)
				{
					
				}
				if(record.getInt("account_group_id")==10&&s10<3)
				{
					
				}else if(record.getInt("account_group_id")==12&&s12<3)
				{
					
				}else if(record.getInt("account_group_id")==13&&s13<3)
				{
					
				}else if(record.getInt("account_group_id")==15&&s15<3)
				{
					
				}else if(record.getInt("account_group_id")==16&&s16<3)
				{
					
				}else if(record.getInt("account_group_id")==17&&s17<3)
				{
					
				}else if(record.getInt("account_group_id")==18&&s18<3)
				{
					
				}else if(record.getInt("account_group_id")==19&&s19<3)
				{
					
				}else if(record.getInt("account_group_id")==20&&s20<3)
				{
					
				}else
				{
					continue;
				}
					
				 
					User user=new User();
					user.setAndroid_api_level(record.getStr("api_level"));
					user.setAndroid_id(record.getStr("androidid"));
					user.setAndroid_platform(record.getStr("android_os"));
					user.setBrand(record.getStr("brand"));
					user.setCpu_arch(record.getStr("cpu_arch"));
					user.setDevice_id(record.getStr("guid"));
					user.setImei(record.getStr("imei"));
					user.setMac(record.getStr("mac")); 
					user.setMd5Password(MD5.GetMD5Code(record.getStr("password")));
					user.setModel(record.getStr("model"));
					user.setUsername(record.getStr("account"));
					user.setUserId(record.get("id")+"");
					user.setSession(record.getStr("session"));
					//user.setImsi(record.getStr("imsi"));
					user.setNickNameList(record.getStr("nick_name_list"));
					
				    //System.out.println(JSON.toJSONString(JSON.toJSONString(user)));
				    jedis.lpush(key, JSON.toJSONString(user));
				
			        
				
//				String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
//				if(Db.findFirst("select * from account_day_cnt where userid='"+u_list.get(i).get("id")+"' and thedate='"+nowdate+"'")==null)
//				{
//				record.set("thedate", nowdate);
//				record.set("readcnt", 0);
//				record.set("followcnt", 0);
//				record.set("userid", u_list.get(i).get("id"));
//				Db.save("account_day_cnt", record);
//				}
			}
			jedis.close();
			sleep(900000);
		}
		*/
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String aa[]) throws Exception
	{
		long thetime1=System.currentTimeMillis()-60000L;	
		System.out.println(TimeUtil.GetSqlDate(thetime1));
		System.out.println(TimeUtil.GetSqlDate(1470150418848L));
		String sql="adddate>=";
//		String filter=URLEncoder.encode("深圳@电信","utf8");
//		System.out.println(URLEncoder.encode("深圳@电信","utf8"));
//		String url="http://www.tkdaili.com/api/getiplist.aspx?vkey=5E094C581EBC3E84DEDD2A8F3BCE9994&num=1&password=fucking2016&filter="+filter+"&style=3";
//		String str=HttpUtil.doPost(url, new HashMap<String,String>()); 
//		String ips[]=TokenUtil.getStrngArray(str, "|");
//		System.out.println(str);
		String thedate="2016-04-15 00:00:00";
		String nowdate="2016-04-30 23:59:59";
		long thetime=TimeUtil.GetMilsecTime(thedate);
		long nowtime=TimeUtil.GetMilsecTime(nowdate);
		System.out.println(thetime);
		long t=TimeUtil.GetMilsecTime(nowdate)-thetime;
		System.out.println(t);
		long d=t/(1000*60*60*24);
		System.out.println(d);
		long st=d/4;
		System.out.println(st);
		List<String[]> list=new ArrayList<>();
		//String lastdate="";
		for(int i=0;i<st;i++)
		{
			String sdate=TimeUtil.GetSqlDate(nowtime-((i+1)*4*1000*60*60*24));
			String tdate=TimeUtil.GetSqlDate(nowtime-(i*4*1000*60*60*24));
			
			if(i==0)
			{
				tdate=nowdate;
			}
			
			if(i==st-1)
			{
				sdate=thedate;
			}
			String dstr[]={sdate,tdate};
			System.out.println(sdate+"-"+tdate);
			list.add(dstr);
		}
		 
		
	}

}
