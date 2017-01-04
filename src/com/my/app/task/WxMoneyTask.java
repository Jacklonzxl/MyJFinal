package com.my.app.task;


import java.util.List;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPayLog;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis; 

public class WxMoneyTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(20000);
		while(true)
		{   
			try{
		    Cache userCache= Redis.use("userc");
		    Jedis jedis = userCache.getJedis();
		    float follow_price =0.02f;          
		    if(jedis.get("follow_price")!=null)
		    {
		    	follow_price=Float.parseFloat(jedis.get("follow_price"));
		    } 
		    float read_price =0.004f;          
		    if(jedis.get("read_price")!=null)
		    {
		    	read_price=Float.parseFloat(jedis.get("read_price"));
		    } 
		    jedis.close();
			List<FollowTask> flist=FollowTask.dao.find("select * from follow_task where status=1 and settle<>1");
			for(int i=0;i<flist.size();i++)
			{
			    FollowTask followTask= flist.get(i);
				if(followTask.getInt("status")==1&&followTask.getInt("settle")!=1)
				{
				
					if(followTask.get("unit_price")!=null)
					{
						follow_price=followTask.getFloat("unit_price");
					}else{
						Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+followTask.get("user_id"));		
						if(channel!=null&&channel.getFloat("followprice")!=null&&channel.getFloat("followprice")>0)
						{
							 follow_price=channel.getFloat("followprice");
						}
					}
					UserPayLog userPayLog_db=UserPayLog.dao.findFirst("select * from user_money_pay_list where type=1 and rid='"+followTask.get("id")+"'");
					UserPayLog userPayLog=new UserPayLog();
					userPayLog.set("userid", followTask.get("user_id"));
					userPayLog.set("type",1);
					userPayLog.set("rid", followTask.get("id"));
					userPayLog.set("cnt", followTask.getInt("total_quantity"));
					userPayLog.set("price", follow_price);
					userPayLog.set("money",followTask.getInt("total_quantity") *1f*follow_price);
					userPayLog.set("status", 1);
					userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					
				//¿ÛÇ®
				if(followTask.getInt("user_id")>1)
				{
					if(userPayLog_db==null||userPayLog_db.get("id")==null)
					{
						UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+followTask.get("user_id"));
						float give =userMoney.getFloat("give")-userPayLog.getFloat("money");
						float balance =userMoney.getFloat("balance");
						if(give<0)
						{
							balance=balance+give;
							give=0;
						}
						userMoney.set("balance", balance);
						userMoney.set("give", give);
						userMoney.update();
						
						userPayLog.save();
					}
					followTask.set("settle", 1).update();
				}
			  }
			}
			sleep(1000);
			List<ReadTask> rlist=ReadTask.dao.find("select * from read_task where status=1 and settle<>1");
			for(int i=0;i<rlist.size();i++)
			{
			    ReadTask readTask= rlist.get(i);
				if(readTask.getInt("status")==1&&readTask.getInt("settle")!=1)
				{
					if(readTask.get("unit_price")!=null)
					{
						read_price=readTask.getFloat("unit_price");
					}else
					{
						Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+readTask.get("user_id"));	
						if(channel!=null&&channel.getFloat("readprice")!=null&&channel.getFloat("readprice")>0)
						 {
							 read_price=channel.getFloat("readprice");
						}
					}
					UserPayLog userPayLog_db=UserPayLog.dao.findFirst("select * from user_money_pay_list where type=2 and rid='"+readTask.get("id")+"'");
					
					System.out.println("money sql:"+"select * from user_money_pay_list where type=2 and rid='"+readTask.get("id")+"'");
					UserPayLog userPayLog=new UserPayLog();
					//int push_quantity=readTask.getInt("push_quantity");
					//int total_quantity=readTask.getInt("total_quantity");
					userPayLog.set("userid", readTask.get("user_id"));
					userPayLog.set("type",2);
					userPayLog.set("rid", readTask.get("id"));
					userPayLog.set("cnt", readTask.getInt("total_quantity"));
					userPayLog.set("price", read_price);
					userPayLog.set("money",readTask.getInt("total_quantity") *1f*read_price);
					userPayLog.set("status", 1);
					userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					
	                if(readTask.getInt("user_id")>1)
	                {
	                	if(userPayLog_db==null||userPayLog_db.get("id")==null)
	                	{
						//¿ÛÇ®
	                	//System.out.println("select * from user_money where userid="+readTask.get("user_id"));
						UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+readTask.get("user_id"));
						if(userMoney!=null){
						float give =userMoney.getFloat("give")-userPayLog.getFloat("money");
						float balance =userMoney.getFloat("balance");
						if(give<0)
						{
							balance=balance+give;
							give=0;
						}
						userMoney.set("balance", balance);
						userMoney.set("give", give);
						userMoney.update();
						System.out.println("userPayLog save ok  ok  ok  ok  ok  ok  ok  ok  ok  ok  ok  ok  ok  ok ");
						userPayLog.save();
						}
	                	}else
	                	{
	                		System.out.println("userPayLog no save fuck fuck fuck fuck fuck fuck fuck fuck fuck fuck fuck fuck");
	                	}
						readTask.set("settle", 1).update();
	                }
				}
			}
			sleep(1000);
		}catch(Exception ee){
			ee.printStackTrace();
		}
		}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 

}
