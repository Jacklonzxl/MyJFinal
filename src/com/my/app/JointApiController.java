package com.my.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.bean.wx.Data;
import com.my.app.bean.wx.Task;
import com.my.app.wx.bean.ReadTask;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
import redis.clients.jedis.Jedis;

public class JointApiController extends Controller {
	public static boolean ispushUser=false;
	//private boolean isTest=false;
	public static String account_table="account";
	private String AuthKey="";
	private String ServerId="";
	private String UserId="8"; 
	public static int usercnt=1; //日限关注
	public static int user_day_maxfollow=5; //日限关注
	public static int user_day_maxread=20;  //日限阅读
	public static int maxTask=3;  //最大任务数
	public static int maxFollowTask=1;  //关注最大任务数
	public static int maxReadTask=1;    //阅读最大任务数

	public static int user_follow_cnt=1; // 
	public static int user_read_cnt=5;    
	public static int user_login_cnt=5;
	public static long data_time=0;
	public static long read_time=0;
	//public static long user_time=0;
	public static int readTemp=0;
	public static int read_order=0;
	
	public static int threadCount=5;
	public static int waitTime=10;
	public static int task_read_supplement=0;    //补量
	public static int accout_use_time=3000000;    //账号使用间隔3000000;
	public static int dlen=0;  //日志表
	public static int s_operation=0;
	public static int t_operation=0;
	
	private static int show_log=0;
	
	
	public long readlen=0;
	
	//初始化公众参数
	private void getPublic()
	{
		UserId=getPara("userid");
		AuthKey=getPara("AuthKey");		
		ServerId=getPara("jid"); 
	}
	private void initSetting(Jedis jedis)
	{
	       /***
         * 取得日限
         */  
		
		long nowtime=System.currentTimeMillis();
		if((nowtime-read_time)>5000)
		{
			readlen=Db.queryLong("select count(id) from read_task where status=0");
		    read_time=System.currentTimeMillis();
		}
		
		if((nowtime-data_time)>60000)
		{
	        if(jedis.get("user_day_maxfollow")!=null)
	        {
	        	user_day_maxfollow=Integer.parseInt(jedis.get("user_day_maxfollow"));
	        }             
	        if(jedis.get("user_day_maxread")!=null)
	        {
	        	user_day_maxread=Integer.parseInt(jedis.get("user_day_maxread"));
	        }
	        if(jedis.get("push_cnt")!=null)
	        {
	        	maxTask=Integer.parseInt(jedis.get("push_cnt"));
	        }
	        if(jedis.get("push_follow_cnt")!=null)
	        {
	        	maxFollowTask=Integer.parseInt(jedis.get("push_follow_cnt"));
	        }
	        if(jedis.get("push_read_cnt")!=null)
	        {
	        	maxReadTask=Integer.parseInt(jedis.get("push_read_cnt"));
	        } 
	        if(jedis.get("user_cnt")!=null)
	        {
	        	usercnt=Integer.parseInt(jedis.get("user_cnt"));
	        }
	        if(jedis.get("user_cnt")!=null)
	        {
	        	usercnt=Integer.parseInt(jedis.get("user_cnt"));
	        }
	        if(jedis.get("user_cnt")!=null)
	        {
	        	usercnt=Integer.parseInt(jedis.get("user_cnt"));
	        }
	 
	        if(jedis.get("user_follow_cnt")!=null)
	        {
	        	user_follow_cnt=Integer.parseInt(jedis.get("user_follow_cnt"));
	        }
	        if(jedis.get("user_read_cnt")!=null)
	        {
	        	user_read_cnt=Integer.parseInt(jedis.get("user_read_cnt"));
	        }
	        if(jedis.get("threadCount")!=null)
	        {
	        	threadCount=Integer.parseInt(jedis.get("threadCount"));
	        }
	        if(jedis.get("waitTime")!=null)
	        {
	        	waitTime=Integer.parseInt(jedis.get("waitTime"));
	        }
	        if(jedis.get("accout_use_time")!=null)
	        {
	        	accout_use_time=Integer.parseInt(jedis.get("accout_use_time"));
	        }
	        if(jedis.get("dlen")!=null)
	        {
	        	dlen=Integer.parseInt(jedis.get("dlen"));
	        }
	        if(jedis.get("s_operation")!=null)
	        {
	        	s_operation=Integer.parseInt(jedis.get("s_operation"));
	        }
	        if(jedis.get("t_operation")!=null)
	        {
	        	t_operation=Integer.parseInt(jedis.get("t_operation"));
	        }
	        if(jedis.get("show_log")!=null)
	        {
	        	show_log=Integer.parseInt(jedis.get("show_log"));
	        }
	        if(jedis.get("read_order")!=null)
	        {
	        	read_order=Integer.parseInt(jedis.get("read_order"));
	        }
	        data_time=System.currentTimeMillis();
	        List<Record> list=Db.find("select * from setting");
	        for(int i=0;i<list.size();i++)
	        {
	        	Record record=list.get(i);
	            jedis.set(record.getStr("name"), record.getStr("value"));
	        }
		}		
	}
	/***
	 * 取任务
	 * @throws Exception
	 */
	public void get_task() throws Exception
	{		
		getPublic();
		//缓存
        Cache userCache= Redis.use("userc");
        Jedis jedis = userCache.getJedis();
        String taskstr="";
        try{ 
        	
		if(!"Fifdsf32432fsdfsk".equals(AuthKey)||UserId==null||UserId.length()==0)
		{
			renderText("null"); 
			jedis.close();
			return ;
		} 
		 initSetting(jedis);
		 boolean isOp=false;
		 
		 if(t_operation>s_operation) 
	     {
	    	 if(getHH()>=s_operation&&getHH()<=t_operation)
		     {
	    	   isOp=true;
		     }
	    		
	     }else{
	         if(getHH()>=s_operation||getHH()<=t_operation){
	    	   isOp=true;
		     }
	    		
	     }
		 //System.out.println("isOP:"+isOp);
		 if(t_operation==0&&getHH()==1&&getMM()<23)
		 {
			 isOp=true;
		 }
		 if(dlen==0||isOp==false)
		 {
			 taskstr=""; 
		 }
		 else{
			 taskstr=search_task(jedis,UserId);
		 }
        }catch(Exception e){e.printStackTrace();}
        jedis.close();
        renderText(taskstr);
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
    	return hh;
    }	
    public int getMM()
    {
      Date time = new Date();

      SimpleDateFormat format = new SimpleDateFormat("mm");
      int mm = Integer.parseInt(format.format(time));

      return mm;
    }
	private String search_task(Jedis jedis,String userid) throws Exception
	{
		createlogtable(ServerId);
		long yst = TimeUtil.getFirstDayOfYear(new Date());
 
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen;
		long m=nowtimes/1000/60/60/24%dlen;
		boolean isWriteNext=m>=(dlen-1);
		String logtable="user_task_log_join_"+ServerId+"_"+week;
		String logtableNext="user_task_log_join_"+ServerId+"_"+(week+1);
 
	 
		boolean hasData=false;
 				
     	int taskcnt=0; //总任务数	
     	int followcnt=0;//总关注数
     	int readcnt=0;  //总阅读数
     	boolean isNotFull=followcnt<maxFollowTask&&readcnt<maxReadTask&&taskcnt<maxTask;
     	List<Task> tlist=new ArrayList<Task>();	 
		for(int i=0;isNotFull&&i<usercnt;i++)
		{
   			
	
			
			int urcnt=user_read_cnt;
 
 
			int plus=readlen<5?50:readlen<10?20:readlen<15?8:0;
			int plus50=plus+50;int plus40=plus+40;int plus30=plus+30;int plus1=plus;
			String where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus50+" ))";
			String orderby="a.level desc,a.id asc";  //按下单排序
			if(readTemp==0)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus40+" ))";
				orderby="a.lasttime asc "; //按最近没取数据排序
				
			}
			else if(readTemp==4)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus30+" ))";
				orderby="a.lasttime asc "; //按最近没取数据排序
				
			}
			else if(readTemp==8)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus1+" ))";
				orderby="a.level desc,a.lasttime asc "; //按最近没取数据排序
				
			}
			else if(readTemp>0&&readTemp<=3)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus50+" ))";
				orderby="a.level desc,a.id asc";  //按下单排序
				
			}else if(readTemp>4&&readTemp<=7)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+plus30+" ))";
				orderby="a.level desc,a.id asc "; //按下单排序
				
			}			
			else if(readTemp==9)
			{
				where="(a.status=0 and (total_quantity>finish_quantity or (push_quantity/total_quantity)<1 ))";
				orderby="a.level desc,a.id asc "; //按下单排序
			}			
			else if(readTemp==10) //补赞程序
			{
				long thetime=System.currentTimeMillis()-26000L;						
				where="(a.status=1 and (praise_quantity>push_praise ) and lasttime<"+thetime+"   )"; 
				orderby="a.level desc,a.id asc "; //按下单排序
			}
			if(read_order==1&&readTemp<8)
			{
				orderby="id desc "; //按最近没取数据排序
			}
			String search_logtable="(select id,sn from "+logtable+" where type=2 AND user='"+userid+"' and status=1 order by id desc limit 500) ";// AND b.type=2 AND b.user='"+user.getUserId()+"' 
			//type=2 and status=1 and
			String task_read_sql=" select a.* from read_task a left join "
					+ search_logtable+"  b    "  
					+ "on (a.sn=b.sn   ) where "+where+"  and b.id is null  "
					+ "order by "+orderby+"  limit 0,"+(urcnt+30);
			 
 		
			List<Record> t_r_list=Db.find(task_read_sql);
 
			//无补量数据使用默认查询
            if(readTemp==10&&t_r_list.size()<urcnt)
            {
            	 where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+(plus1+5)+" ))";
    			 orderby="a.level desc,a.lasttime asc";  //按下单排序
    			 task_read_sql=" select a.* from read_task a left join "
    						+search_logtable+"  b    "
    						+ " on (a.sn=b.sn)  where "+where+"  and b.id is null  "
    						+ "order by "+orderby+"  limit 0,"+(urcnt-t_r_list.size()+30);
    			 List<Record> t_r_list_1=Db.find(task_read_sql);
    			 t_r_list.addAll(t_r_list_1);
            } 
			readTemp++;
			if(readTemp>10)
			{
				readTemp=0;
			}
			if(t_r_list.size()==0&&show_log==1)
			{
				System.out.println("t_r_list ==0 "+ServerId);
			}
			if(t_r_list.size()>0)
			{
				Record taskRecord=new Record();
				taskRecord.set("type", "1");
				taskRecord.set("status", 0);
				Db.save("task", taskRecord);
				Task task=new Task();						
				task.setTaskid(taskRecord.get("id")+"");
				task.setTasktype("2");							
				List<Data> datalist=new ArrayList<Data>();
				int read_cnt=0; 
				for(int j=0;read_cnt<urcnt&&readcnt<maxReadTask&&taskcnt<maxTask&&j<t_r_list.size();j++)
				{
					Record record1=t_r_list.get(j);	
					String serverid=record1.getStr("serverid");  //任务定向省份用
					int id=record1.getInt("id");
					int finish_quantity =record1.getInt("finish_quantity"); //任务完成数
					int total_quantity =record1.getInt("total_quantity");   //任务数 
				    int praise_quantity=record1.getInt("praise_quantity");
				    int push_quantity=record1.getInt("push_quantity");
				    int push_praise=record1.getInt("push_praise");
				    int finish_praise=record1.getInt("finish_praise");
				    int start_quantity=record1.getInt("start_quantity");
 				    int status=record1.getInt("status");
 				    long nowtime=System.currentTimeMillis();
 				    if(status==1)
 				    {
 				    	//String order_time=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*24*1L)); 
 				    	long order_time=TimeUtil.GetSqlMilsecTime(record1.get("order_time").toString());
 				    	
 				    	if((nowtime-order_time)>(1000*60*60*24))
 				    	{
 				    		Db.update("update read_task set lasttime="+(System.currentTimeMillis()+3600000000L)+"  where id="+id+"");	
 				    		continue;
 				    		
 				    	}
 				    }
					if(serverid!=null&&serverid.length()>0)
					{
						if(!serverid.equals(ServerId))
						{
							continue;
						}
					}

					
					//速度控制
					int frequency=record1.get("frequency")!=null?record1.getInt("frequency"):10000;
					int thefrequency=0;					
					String frequency_key="fy_"+id+"_"+(TimeUtil.GetSqlDate(nowtime).substring(0, 15).replaceAll(" ", "-"));
					if(jedis.exists(frequency_key))
					{
						thefrequency=Integer.parseInt(jedis.get(frequency_key));
					}
					if(frequency>0&&thefrequency>frequency)
					{
						continue;
					}else
					{
						jedis.incr(frequency_key);
					}
					//end 速度控制
		   
				    //给客户端的任务数据
				    Data data=new Data();							    
				    data.setMessage(record1.getStr("reply_content"));
				    data.setWxid(record1.getStr("public_account"));				    
				    data.setUrl(record1.getStr("url"));	
 
			    	long inittime=record1.getLong("init_time");
			    	long st=nowtime-inittime;
			    	
				    float readRate=(push_quantity*1f)/(total_quantity*0.95f);
				    float praiseRate=praise_quantity>0?(push_praise*1f)/(praise_quantity*1f):0;
				    float readRate1=(finish_quantity*1f)/(total_quantity*0.95f);
				    float praiseRate1=praise_quantity>0?(finish_praise*1f)/(praise_quantity*1f):0;
				    boolean isRate=(readRate>praiseRate)&&(readRate1>praiseRate1);
				    boolean isTime=praise_quantity>19||st>1000;
				    //发命令更新初始值
				    if(record1.getInt("start_quantity")==0)
				    {
				    	
				    	if(st>18000)
				    	{
				    		data.setUpdate_count(1);
				    		data.setPraise(0);
				    		if(push_quantity>100)
				    		{
				    			status=3;
				    		}
				    		Db.update("update read_task set init_time="+nowtime+",status="+status+" where id="+id);
				    	}else
				    	{
				    		continue;
				    	}
				    }else if(st>120000)
				    {
				    	data.setUpdate_count(1); 
			    		Db.update("update read_task set init_time="+nowtime+" where id="+id);
				    }
				    //准备完成阅读时更新
				    else if(finish_quantity>=total_quantity-5)
				    {
				    	data.setUpdate_count(1);
				    }				    
				    //准备完成点赞时更新
				    else if((finish_praise>=praise_quantity||(push_praise>=praise_quantity))&&readRate>0.8)
				    {
				    	data.setUpdate_count(1);
				    }else if(push_quantity>=total_quantity-5)
				    {
				    	data.setUpdate_count(1);
				    }

				    //是否点赞 
				    if(status==1||(start_quantity>0&&(praise_quantity>finish_praise)&&isRate&&isTime))
				    {
				    	data.setPraise(1);
				    	finish_praise=finish_praise+1;
				    	record1.set("finish_praise", finish_praise);
				    }else
				    {
				    	data.setPraise(0);
				    }
				    String url =record1.get("url");
				 
				    //写日志
				    Record utl=new Record();
				    utl.set("type", 2);
				    utl.set("rid", id);
				    utl.set("tid", taskRecord.get("id"));
				    utl.set("user", userid);
				    utl.set("public_account", record1.get("public_account"));
				    utl.set("url",url);
				    utl.set("sn",record1.get("sn"));
				    utl.set("status",0);
				    utl.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("praise", data.getPraise());
				    utl.set("serverid", ServerId);
				    utl.set("vpsid", ServerId);
				     
				    //Db.save("user_task_log", utl);
				    Db.save(logtable, utl);
				    if(isWriteNext)
				    {
				    	Db.save(logtableNext, utl);
				    }
				    
				    //记录每日推送数据
				    
				    //设置数据id
				    data.setId(Integer.parseInt(utl.getLong("id")+""));
				    //加到数据列表中
				    datalist.add(data);
				    
				    String lasttime=",lasttime="+System.currentTimeMillis();
				    Db.update("update read_task set finish_quantity=finish_quantity+1,finish_praise="+finish_praise+lasttime+"  where id="+id+"");
				    hasData=true;
				    taskcnt++;  //任务的总数
				    readcnt++;  //阅读任务数
				    read_cnt++;
				    
				}

				task.setData(datalist);
				tlist.add(task); 
			}  
		}
	 
				
		if(hasData)
	    {
		 
			String str=new Gson().toJson(tlist);//JSON.toJSONString(JSON.toJSONString(ut));	
			return str;
 
	    }else
	    {
	    	 
	    	String str="";//new Gson().toJson(ut);    	
	    	return str;
	    }
 
 		
	}
	public static long cnt_time=0;
	public static int read_task_cnt=0;
	public static int read_task_cnts[]=new int[10];
	/****
	 * 更新任务状态
	 * @throws ParseException 
	 */
	public void update_task() throws ParseException
	{
		
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey)||UserId==null||UserId.length()==0)
		{
			renderText("");
			return ;
		}
		long yst = TimeUtil.getFirstDayOfYear(new Date());
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen;

		String logtable="user_task_log_join_"+ServerId+"_"+week;
		
        Cache userCache= Redis.use("userc");
        Jedis jedis = userCache.getJedis();
        try{
		String Type=getPara("Type");
		String Status=getPara("Status");
 
		//状态IDS
		String st_ids[]=TokenUtil.getStrngArray(Status, ",");
		String sts="";
		String tids="";
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
		for(int i=0;i<st_ids.length;i++)
		{
			String dataid=st_ids[i];
			if(dataid.indexOf("~")==-1)
			{
				Record user_task_log=Db.findById(logtable, dataid);
				sts+="or id="+dataid+" ";
				if(Type.equals("1")) //关注+1处理
				{	
					String key_follow_day_cnt="day_"+user_task_log.getStr("user")+"_follow_"+nowdate;
					jedis.incr(key_follow_day_cnt); 	
					tids+="or id="+user_task_log.get("rid")+" ";
				}else if(Type.equals("2")) //阅读+1处理
				{
					try{
					if(System.currentTimeMillis()-cnt_time>60000)
					{
						cnt_time=System.currentTimeMillis();
						
						read_task_cnts[1]=read_task_cnts[0];
						read_task_cnts[2]=read_task_cnts[1];
						read_task_cnts[3]=read_task_cnts[2];
						read_task_cnts[4]=read_task_cnts[3];
						read_task_cnts[0]=read_task_cnt;
						
						System.out.println(read_task_cnt+"/m");
						read_task_cnt=0;
					}else
					{
						read_task_cnt++;
					}
					String key_read_day_cnt="day_"+user_task_log.getStr("user")+"_read_"+nowdate;
					jedis.incr(key_read_day_cnt); 	
					saveUserDayRead(nowdate,user_task_log.getStr("user"),jedis.get(key_read_day_cnt));
					ReadTask readTask=ReadTask.dao.findById(user_task_log.getInt("rid"));
					if(readTask==null)
					{
						renderText("");
						jedis.close();
						return ;
					}
					if(readTask.get("push_quantity")!=null)
					{
						readTask.set("push_quantity", readTask.getInt("push_quantity")+1);
						if(user_task_log.get("praise")!=null&&user_task_log.getInt("praise")==1)
						{
							if(readTask.get("push_praise")!=null)
							{
								readTask.set("push_praise", readTask.getInt("push_praise")+1);
							}else
							{
								readTask.set("push_praise", 1);
							}
						}
						if(readTask.getInt("push_quantity")>(readTask.getInt("total_quantity")*2))
						{
							readTask.set("status", 3);
						}
						if(readTask.getInt("push_quantity")>((readTask.getInt("total_quantity")+6000)))
						{
							readTask.set("status", 3);
						}
						
					}else
					{
						readTask.set("push_quantity", 1);
						if(user_task_log.get("praise")!=null&&user_task_log.getInt("praise")==1)
						{
							if(readTask.get("push_praise")!=null)
							{
								readTask.set("push_praise", readTask.getInt("push_praise")+1);
							}else
							{
								readTask.set("push_praise", 1);
							}
						}
					}
					readTask.update();
					}catch(Exception e){e.printStackTrace();}
				}
				
				
			}else
			{
				//修改失败日志状态
				String status_error[]=TokenUtil.getStrngArray(dataid, "~");
				String id=status_error[0];
				String dataSql="update "+logtable+" set status="+status_error[1]+",donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+ServerId+"' where id="+id+" and type="+Type;
				if(show_log==1)
				{
					System.out.println("dataid-error:"+dataid+" server:"+ServerId+" vpsid:"+ServerId);
				}
				Db.update(dataSql);
				 
		 
			}
		}
		if(sts.length()>0)
		{
			//修改成功日志状态
			long s=System.currentTimeMillis();
			String dataSql="update "+logtable+" set status=1,donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+ServerId+"' where ("+sts.substring(2, sts.length())+")  and type="+Type;
		 
			Db.update(dataSql);
			long t=System.currentTimeMillis();
			if(t-s>100&&show_log==1)
			{
				System.out.println("dataSql:"+(t-s));
			}
			//修改真实关注值
			if(Type.equals("1")&&tids.length()>0)
			{
				String sql_update_follow="update follow_task set real_quantity=real_quantity+1 where ("+tids.substring(2, tids.length())+")";
				Db.update(sql_update_follow);
			}
				
			
		}
        }catch(Exception e){e.printStackTrace();}
		jedis.close();
		renderText("true");
		
	}
	public void saveUserDayRead(String thedate,String userid,String readcnt)
	{
		Record record=Db.findFirst("select * from account_day_cnt_join where thedate='"+thedate+"' and userid="+userid+"");
		if(record==null)
		{
			record=new Record();
			record.set("thedate", thedate);
			record.set("userid", userid);
			record.set("readcnt", readcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.save("account_day_cnt", record);
		}else
		{
			record.set("readcnt", readcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.update("account_day_cnt", record);
		}
	}
	/***
	 * 更新微信真实阅读数
	 * @throws ParseException 
	 */
	public void update_count() throws ParseException
	{
		
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}
		long yst = TimeUtil.getFirstDayOfYear(new Date());
		//int dlen=10;
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen; 
		String logtable="user_task_log_join_"+ServerId+"_"+week; 
		
		int DataId=getPara("DataId")!=null?getParaToInt("DataId"):0;
		int Follow=getPara("Follow")!=null?getParaToInt("Follow"):0;
		int Read=getPara("Read")!=null&&getPara("Read").length()>0?getParaToInt("Read"):-1;
		int Praise=getPara("Praise")!=null&&getPara("Praise").length()>0?getParaToInt("Praise"):-1;
		String Type=getPara("Type"); //type=1 关注，type=2 阅读
 	
		if("1".equals(Type))
		{
			Record record=Db.findById(logtable, DataId);
			if(record!=null)
			{
				Record follow_task=Db.findById("follow_task", record.get("rid"));
				if(follow_task.getInt("start_quantity")==0)
				{
					follow_task.set("start_quantity", Follow); 
				} 
				int real_follow= Follow-follow_task.getInt("start_quantity");
				follow_task.set("real_quantity",real_follow);
				//真实用户数》=完成数
				if(real_follow>=follow_task.getInt("total_quantity"))
				{					
					follow_task.set("status", 1); 
					follow_task.set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				}else if(follow_task.getInt("finish_quantity")>=follow_task.getInt("total_quantity"))
				{
					follow_task.set("finish_quantity", real_follow);
				}
			}
			
		}
		//取得阅读真实内容
		else if("2".equals(Type))
		{
			Record record=Db.findById(logtable, DataId);
			if(record!=null)
			{
				Record read_task=Db.findById("read_task", record.get("rid"));
				if(read_task==null)
				{
					renderText("");
					return ;
				}
				if(Read>0&&Praise>=0)
				{
					//第一次阅读
					if(read_task.getInt("start_quantity")==0)
					{
						read_task.set("start_quantity", Read);
						read_task.set("start_praise", Praise);
					}
					int total_quantity=read_task.getInt("total_quantity");
					int start_quantity=read_task.getInt("start_quantity");
					int real_read=Read-start_quantity;
					int push_quantity=read_task.getInt("push_quantity");
					int real_praise= Praise-read_task.getInt("start_praise");
					//int praise_quantity=read_task.getInt("praise_quantity");
					read_task.set("real_quantity", real_read);
					read_task.set("real_praise", real_praise);
					//read_task.set("finish_quantity", real_read);
					//真实用户数》=完成数
					if((real_read>=total_quantity||Read>=100000)&&push_quantity>=total_quantity)//&&push_quantity>=praise_quantity
					{					
						read_task.set("status", 1); 
						read_task.set("finish_quantity", read_task.getInt("total_quantity"));
						read_task.set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					}else if(read_task.getInt("finish_quantity")>=read_task.getInt("total_quantity"))
					{
						//read_task.set("finish_quantity", real_read);
					}
					if(read_task.getInt("praise_quantity")>=real_praise)
					{					
						read_task.set("finish_praise", real_praise);
					}
					//String lasttime=",lasttime="+System.currentTimeMillis();
				    int status=read_task.getInt("status");
				    if(status!=1)
				    {
				    	//read_task.set("lasttime", System.currentTimeMillis());
				    }
				    read_task.set("failedcnt",0);
				}else if(Read==-1)
				{                  
					read_task.set("failedcnt",Integer.parseInt(read_task.get("failedcnt").toString())+1);
					if(read_task.getInt("failedcnt")>10)
					{
						read_task.set("status", 3);
					}
					
				}
				
		        Db.update("read_task",read_task); 
			} 
		}
		
		Record reqRecord=Db.findFirst("select * from vps_status where vpsid='"+ServerId+"' and serverid='"+ServerId+"'");
		if(reqRecord!=null)
		{
			reqRecord.set("reportdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			Db.update("vps_status", reqRecord);
		}
		renderText("true");
		
	}
 
  
	/****
	 * 脚本是否通过账号ip
	 */
	public void verify_ip()
	{
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-60000L).substring(0,16);
		String ip=getIpAddr(getRequest());
		if(ip==null||ip.equals("")||ip.equals("null")) {
				renderText("false");
	    }else{
			String sql="select * from vps_ip where ip='"+ip+"' and adddate>='"+nowdate+"' ";
			List<Record> list=Db.find(sql);
			if(list.size()==0)
			{
				renderText("true");
				Record record=new Record();
				record.set("serverid", ServerId);
				record.set("vpsid", ServerId);
				record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				Db.save("vps_ip", record);
			}else
			{
				nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,15);
				sql="select * from vps_ip where ip='"+ip+"' and adddate>='"+nowdate+"' ";
				list=Db.find(sql);
				if(list.size()<5)
				{
					renderText("true");
					Record record=new Record();
					record.set("serverid", ServerId);
					record.set("vpsid", ServerId);
					record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.save("vps_ip", record);
				}else
				{
					renderText("false");
				}
				
			}
		 }
		
	}
 
	public void createlogtable(String serverid) throws Exception
	{
		long s= TimeUtil.getFirstDayOfYear(new Date());
		
		long t=System.currentTimeMillis();
		long ss=t-s;
		long week=ss/1000/60/60/24/dlen; 	
		String sql_1="create table  if not exists user_task_log_join_"+serverid+"_"+week+" like user_task_log";
		String sql_2="create table  if not exists user_task_log_join_"+serverid+"_"+(week+1)+" like user_task_log";
		Db.update(sql_1);
		Db.update(sql_2);

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
		 }  
		 return ip;  
		}	
 
}