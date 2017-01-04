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
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
import redis.clients.jedis.Jedis;

public class JoinApiController extends Controller {
	
	public static boolean ispushUser=false;
	//private boolean isTest=false;
	public static String account_table="account";
	private String AuthKey="";
	private String ServerId="";
	private String UserId="8"; 
	//public static int usercnt=1; //日限关注
	//public static int user_day_maxfollow=5; //日限关注
	//public static int user_day_maxread=20;  //日限阅读
	//public static int maxTask=3;  //最大任务数
	//public static int maxFollowTask=1;  //关注最大任务数
	//public static int maxReadTask=1;    //阅读最大任务数
	public static int user_follow_cnt=1; // 
	public static int user_read_cnt=5;    
	//public static int user_login_cnt=5;
	public static long data_time=0;
	public static long read_time=0;
	//public static long user_time=0;
	public static int readTemp=0;
	public static int read_order=0;
	
	public static int threadCount=5;
	public static int waitTime=10;
	public static int task_read_supplement=0;    //补量
	public static int accout_use_time=3000000;    //账号使用间隔3000000;
	public static int dlen=3;  //日志表
	public static int s_operation=0;
	public static int t_operation=0;
	
	private static int show_log=0;
	
	
	public long readlen=0;
	public int type=-1;
	
	//初始化公众参数
	private void getPublic()
	{
		UserId=getPara("userid");
		AuthKey=getPara("AuthKey");		
		ServerId=getPara("jid"); 
		type=getPara("type")!=null?getParaToInt("type"):-1; 
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
//	        if(jedis.get("user_day_maxfollow")!=null)
//	        {
//	        	user_day_maxfollow=Integer.parseInt(jedis.get("user_day_maxfollow"));
//	        }             
//	        if(jedis.get("user_day_maxread")!=null)
//	        {
//	        	user_day_maxread=Integer.parseInt(jedis.get("user_day_maxread"));
//	        }
//	        if(jedis.get("push_cnt")!=null)
//	        {
//	        	maxTask=Integer.parseInt(jedis.get("push_cnt"));
//	        }
//	        if(jedis.get("push_follow_cnt")!=null)
//	        {
//	        	maxFollowTask=Integer.parseInt(jedis.get("push_follow_cnt"));
//	        }
//	        if(jedis.get("push_read_cnt")!=null)
//	        {
//	        	maxReadTask=Integer.parseInt(jedis.get("push_read_cnt"));
//	        } 
	   
	 
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
			 taskstr=search_task(jedis,UserId,getPara("url")!=null);
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
    public static int ftemp=0;
	private String search_task(Jedis jedis,String userid,boolean isurl) throws Exception
	{
		updatednum(getPara("dnum"));
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
 				
     	//int taskcnt=0; //总任务数	
     	//int followcnt=0;//总关注数
     	//int readcnt=0;  //总阅读数
     	int x=(int)(Math.random()*5); 
     	//int maxf=maxFollowTask+x;
     	int ufc=user_follow_cnt+x;
     	user_read_cnt=Integer.parseInt(jedis.get("user_read_cnt"));
		//maxReadTask=Integer.parseInt(jedis.get("push_read_cnt"));
     	Record ac=Db.findFirst("select account,account_group_id from account_android where account='"+(userid.replaceAll("'", ""))+"'");
     	if(ac!=null)
     	{
//     		if(ac.get("account_group_id").toString().indexOf("13")>-1)
//     		{
//     			
//     			if(getHH()<16)
//     			{
//     				//maxf=10;
//         			ufc=10;
//     			}
//     			//user_read_cnt=user_read_cnt;
//     			//maxReadTask=maxReadTask;
//     			if(ac.get("account_group_id").toString().equals("131"))
//     			{
//     				//user_read_cnt=user_read_cnt;
//         			//maxReadTask=maxReadTask+6;
//     			}
     		  
//     		}
     		if(ac.get("account_group_id").toString().equals("108"))
     		{
     			ufc=0;
     		}
     		if(ac.get("account_group_id").toString().equals("138")||ac.get("account_group_id").toString().equals("139"))
     		{
     			user_read_cnt=8;
     		}
     		
     	}else
     	{
     		//maxf=5;
     		ufc=3;
     		//user_read_cnt=10;
 			//maxReadTask=20;
     	}
     	
     	//boolean isNotFull=taskcnt<maxTask;
        String public_log_table="user_task_log_join_public_"+ServerId;
     	List<Task> tlist=new ArrayList<Task>();	 
     	String flurl="";
//     	//isurl=true;
//     	if(isurl)
//     	{
//     		flurl=" url<>'' and url is not null and ";
//     	}
     	if((type==1||type==-1)&&ufc>0)
     	{
	     	List<Record> t_f_list=new ArrayList<Record>();
			String follow_table="(select id,public_account from "+public_log_table+" where type=1 AND user='"+userid+"' and status=1 order by id desc limit 500)";
			//用户找任务
			String task_follow_sql=" select b.*,a.* from follow_task a left join "
					+ follow_table+" b  "
					+ "on (a.public_account=b.public_account )  where  "+flurl
					+ "a.status=0 and total_quantity>finish_quantity and b.id is null "
					+ "order by a.level desc,a.id asc limit 0,"+ufc;
			
			if(ftemp<3)
			{
				task_follow_sql=" select b.*,a.* from follow_task a left join "
						+ follow_table+" b  "
						+ "on (a.public_account=b.public_account )  where  "+flurl
						+ "a.status=0 and total_quantity>finish_quantity and b.id is null "
						+ "order by a.level desc,a.lasttime limit 0,"+ufc;
				
			}
			ftemp++;
			if(ftemp==11)
			{
				ftemp=0;
			}
			t_f_list=Db.find(task_follow_sql);	
			System.out.println(" ufc:"+ufc+" t_f_list.size:"+t_f_list.size());
			if(t_f_list.size()>0)
			{
				Record taskRecord=new Record();
				taskRecord.set("type", "1");
				taskRecord.set("status", 0);
				Db.save("task", taskRecord);
				Task task=new Task();
				
				task.setTaskid(taskRecord.get("id")+"");
				task.setTasktype("1");
				
				List<Data> datalist=new ArrayList<Data>();
				for(int j=0;  j<t_f_list.size();j++)
				{
					Record record1=t_f_list.get(j); 
					
	
				    //写日志
				    Record utl=new Record();
				    utl.set("type", 1);
				    utl.set("rid", record1.get("id"));
				    utl.set("tid", taskRecord.get("id"));
				    utl.set("user",userid);
				    utl.set("public_account", record1.get("public_account"));
				    utl.set("status",0);
				    utl.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("donedate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("serverid", ServerId);
				    utl.set("vpsid", 0);
				    Db.save(public_log_table, utl);
//				    if(isWriteNext)
//				    {
//				    	Db.save(logtableNext, utl);
//				    }
//				    //给客户端的任务数据
				    Data data=new Data();
				    data.setMessage(record1.getStr("reply_content"));
				    data.setWxid(record1.getStr("public_account"));
				    data.setId(Integer.parseInt(utl.getLong("id")+""));	
				    if(isurl)
			     	{
				    	data.setUrl(record1.getStr("url"));
			     	}
				    datalist.add(data);
				    //记录每日推送数据
				    String update_follow_sql="update follow_task set finish_quantity=finish_quantity+1,lasttime="+System.currentTimeMillis()+" where id='"+ record1.get("id")+"'";
				    Db.update(update_follow_sql);
				    hasData=true;
				    //taskcnt++;
				    //followcnt++;
				    
				}
				task.setData(datalist);
				tlist.add(task);
			}
     	}
		if((type==2||type==-1)&&user_read_cnt>0)
		{
   			
	
			
			int urcnt=user_read_cnt;
//			if(ac!=null)
//	     	{
//	     		if(ac.get("account_group_id").toString().indexOf("13")>-1)
//	     		{
//	     			///urcnt=3;
//	     		}
//	     	}
 
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
					+ "order by "+orderby+"  limit 0,"+(urcnt);
			 
 		
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
				for(int j=0;read_cnt<urcnt&&j<t_r_list.size();j++)
				{
					Record record1=t_r_list.get(j);	 
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
  
 

					
					//速度控制
 				    /*
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
					*/
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
				    //taskcnt++;  //任务的总数
				    //readcnt++;  //阅读任务数
				    read_cnt++;
				    
				}

				task.setData(datalist);
				tlist.add(task); 
			}  
		}
	 
				
		if(hasData)
	    {
			String str="";
			if(type==-1)
			{
				 str=new Gson().toJson(tlist);
			}else
			{
			    for(int i=0;i<tlist.size();i++)
			    {
			    	Task t=tlist.get(i);
			    	if(t.getTasktype().equals("2")&&type==2)
			    	{
			    		List<Data> datalist=t.getData();
			    		for(int d=0;d<datalist.size();d++)
			    		{
			    			Data data=datalist.get(d);
			    			data.setType("2");
			    			str+=new Gson().toJson(data)+"|";
			    		}
			    	
			    		
			    	}else if(t.getTasktype().equals("1")&&type==1)
			    	{
			    		List<Data> datalist=t.getData();
			    		for(int d=0;d<datalist.size();d++)
			    		{
			    			Data data=datalist.get(d);
			    			data.setType("1");
			    			str+=new Gson().toJson(data)+"|";
			    		}
			    	}
			    			
			    	
			    }
			}
			//String str=new Gson().toJson(tlist);//JSON.toJSONString(JSON.toJSONString(ut));	
			return str;
 
	    }else
	    {
	    	 
	    	String str="";//new Gson().toJson(ut);    	
	    	return str;
	    }
 
 		
	}
	public void getwxid()
	{
		String ph=getPara("ph");
		String wxid="wxokread";
		List<Record> list=Db.find("select * from ph_wxid  ORDER BY RAND() ");
		for(int i=0;i<list.size();i++)
		{
			Record record=list.get(i);
			Record r=Db.findFirst("select * from ph_wxid_list where ph='"+ph+"' and wxid='"+record.getStr("wxid")+"' ");
			wxid=record.getStr("wxid");
			if(r==null)
			{
				
				break;
			}
			 
		}
		renderText(wxid);
	}
	//public static long cnt_time=0;
	public static int read_task_cnt=0;
	//public static int read_task_cnts[]=new int[10];
	/****
	 * 更新任务状态
	 * @throws ParseException 
	 */
	public void update_task() throws ParseException
	{
		
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey)||ServerId==null||ServerId.length()==0)
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
		//String sts="";
		String ets="";
		//String tids="";
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
		for(int i=0;i<st_ids.length;i++)
		{
			String dataid=st_ids[i];
			if(dataid.indexOf("~")==-1)
			{
				//Record user_task_log=Db.findById(logtable, dataid);
//				if(user_task_log==null||user_task_log.getInt("status")==1)
//				{
//					continue;
//				}
//				if(user_task_log.getInt("type")==2)
//				{
//					Type="2";
//				}
				if(Integer.parseInt(dataid)>81106320)
				{
					Type="1";
				}else
				{
					Type="2";
				}
				//System.out.println(sts+="or id="+dataid+" ");
				if(Type.equals("1")) //关注+1处理
				{	
					String public_log_table="user_task_log_join_public_"+ServerId;
					Record user_task_log=Db.findById(public_log_table, dataid);
					if(user_task_log==null||user_task_log.getInt("status")==1)
					{
						continue;
					}
					//sts+="or id="+dataid+" ";
					//System.out.println("dataid:"+dataid+" rid:"+ user_task_log.getInt("rid"));
					String key_follow_day_cnt="day_"+user_task_log.getStr("user")+"_follow_"+nowdate;
					jedis.incr(key_follow_day_cnt); 	
					//tids+="or id="+user_task_log.get("rid")+" ";
					saveUserDayFollow(nowdate,user_task_log.getStr("user"),jedis.get(key_follow_day_cnt));
					String dnum=getPara("dnum");
					saveDnumDayFollow(nowdate, dnum);
					FollowTask follow_task=FollowTask.dao.findById( user_task_log.getInt("rid"));
					
					System.out.println("dataid:"+dataid+" rid:"+ user_task_log.getInt("rid")+" public_account:"+follow_task.getStr("public_account"));
					if(follow_task.getInt("real_quantity")>=follow_task.getInt("total_quantity"))
					{					
						follow_task.set("status", 1); 
						follow_task.set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						follow_task.update();
					}
					String sql_update_follow="update follow_task set real_quantity=real_quantity+1 where id="+user_task_log.get("rid");
					Db.update(sql_update_follow);
					String dataSql="update "+public_log_table+" set status=1,donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+ServerId+"' where id="+dataid;
					//System.out.println("上报成功:"+dataSql);
					Db.update(dataSql);
				}else if(Type.equals("2")) //阅读+1处理
				{
					try{
				    Record user_task_log=Db.findById(logtable, dataid);
					if(user_task_log==null||user_task_log.getInt("status")==1)
					{
						continue;
					}	
					//sts+="or id="+dataid+" ";
					if(System.currentTimeMillis()-WxApiController.cnt_time>60000)
					{
						WxApiController.cnt_time=System.currentTimeMillis();
						
						WxApiController.read_task_cnts[1]=WxApiController.read_task_cnts[0];
						WxApiController.read_task_cnts[2]=WxApiController.read_task_cnts[1];
						WxApiController.read_task_cnts[3]=WxApiController.read_task_cnts[2];
						WxApiController.read_task_cnts[4]=WxApiController.read_task_cnts[3];
						WxApiController.read_task_cnts[0]=read_task_cnt;
						
						System.out.println(read_task_cnt+"/m");
						read_task_cnt=0;
					}else
					{
						read_task_cnt++;
					}
					String key_read_day_cnt="day_"+user_task_log.getStr("user")+"_read_"+nowdate;
					jedis.incr(key_read_day_cnt); 	
					saveUserDayRead(nowdate,user_task_log.getStr("user"),jedis.get(key_read_day_cnt));
					String dnum=getPara("dnum");
					saveDnumDayRead(nowdate, dnum);
					ReadTask readTask=ReadTask.dao.findById(user_task_log.getInt("rid"));
					
					if(readTask==null)
					{
						continue;
					}
					System.out.println("dataid:"+dataid+" rid:"+ user_task_log.getInt("rid")+" title:"+readTask.getStr("title"));
					if(readTask.get("push_quantity")!=null)
					{
						readTask.set("push_quantity", readTask.getInt("push_quantity")+1);
						if(readTask.getInt("push_quantity")>readTask.getInt("total_quantity"))
						{
							readTask.set("real_quantity",readTask.getInt("push_quantity"));		
							readTask.set("status", 1);
							readTask.set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
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
					 
					}
					 	readTask.update();
					 	String dataSql="update "+logtable+" set status=1,donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+ServerId+"' where id="+dataid;
						//System.out.println("上报成功:"+dataSql);
						Db.update(dataSql);
					}catch(Exception e){e.printStackTrace();}
				}
				
				
			}else
			{
				//修改失败日志状态
				String status_error[]=TokenUtil.getStrngArray(dataid, "~");
				String id=status_error[0];
				ets+="status_error["+status_error[0]+"]["+status_error[1]+"] type:"+Type;
				String update_tb=logtable;
				if(Type.equals("1"))
				{
					String public_log_table="user_task_log_join_public_"+ServerId;
					update_tb=public_log_table;
				}
				String dataSql="update "+update_tb+" set status="+status_error[1]+",donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+ServerId+"' where id="+id+" and type="+Type;
//				System.out.println("上报失败:"+dataSql);
 
				Db.update(dataSql);
				 
		 
			}
		}
 
		if(ets.length()>0)
		{
			 System.out.println("ets:"+ets);
			 
				
			
		}
        }catch(Exception e){e.printStackTrace();}
		jedis.close();
		renderText("true");
		
	}
	
	
	
 
	
	public void saveUserDayRead(String thedate,String userid,String readcnt)
	{
		Record record=Db.findFirst("select * from account_day_cnt_join where thedate='"+thedate+"' and userid='"+userid+"'");
		if(record==null)
		{
			record=new Record();
			record.set("thedate", thedate);
			record.set("userid", userid);
			record.set("readcnt", readcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.save("account_day_cnt_join", record);
		}else
		{
			record.set("readcnt", readcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.update("account_day_cnt_join", record);
		}
	}
	public void saveUserDayFollow(String thedate,String userid,String followcnt)
	{
		Record record=Db.findFirst("select * from account_day_cnt_join where thedate='"+thedate+"' and userid='"+userid+"'");
		if(record==null)
		{
			record=new Record();
			record.set("thedate", thedate);
			record.set("userid", userid);
			record.set("followcnt", followcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.save("account_day_cnt_join", record);
		}else
		{
			record.set("followcnt", followcnt);
			record.set("lasttime", System.currentTimeMillis());
			Db.update("account_day_cnt_join", record);
		}
	}
	
	
	public void saveDnumDayRead(String thedate,String dnum)
	{
		Record record=Db.findFirst("select * from account_day_cnt_dnum where thedate='"+thedate+"' and dnum='"+dnum+"'");
		if(record==null)
		{
			record=new Record();
			record.set("thedate", thedate);
			record.set("dnum", dnum);
			record.set("readcnt", 1);
			record.set("lasttime", System.currentTimeMillis());
			Db.save("account_day_cnt_dnum", record);
		}else
		{
			record.set("readcnt", record.getInt("readcnt")+1);
			record.set("lasttime", System.currentTimeMillis());
			Db.update("account_day_cnt_dnum", record);
		}
		updatednum(dnum);
	}
	public void saveDnumDayFollow(String thedate,String dnum)
	{
		Record record=Db.findFirst("select * from account_day_cnt_dnum where thedate='"+thedate+"' and dnum='"+dnum+"'");
		if(record==null)
		{
			record=new Record();
			record.set("thedate", thedate);
			record.set("dnum", dnum);
			record.set("followcnt", 1);
			record.set("lasttime", System.currentTimeMillis());
			Db.save("account_day_cnt_dnum", record);
		}else
		{
			record.set("followcnt",  record.getInt("followcnt")+1);
			record.set("lasttime", System.currentTimeMillis());
			Db.update("account_day_cnt_dnum", record);
			
		}
		updatednum(dnum);
	}
	public void updatednum(String dnum)
	{
		if(dnum!=null)
		{
			try{	
				Record record=Db.findFirst("select * from ph_dnum where   dnum='"+dnum+"'");
				if(record==null)
				{
					System.out.println("新机器："+dnum);
					record=new Record(); 
					record.set("dnum", dnum); 
					record.set("lasttime", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.save("ph_dnum", record);
				}else
				{ 
					System.out.println("旧机器："+dnum);
					String updateSQL="update  ph_dnum set lasttime='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"' where  dnum='"+dnum+"'";
					Db.update(updateSQL);
			    }
			}catch(Exception e){
				System.out.println("更新机器状态异常:"+e.toString());
			}
		}
	}
	/***
	 * 更新微信真实阅读数
	 * @throws ParseException 
	 */
	/*
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
 
   */
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
	public static void main(String aa[])
	{
		for(int a=0;a<100;a++){
		int t=0;
		for(int i=0;i<100;i++)
		{
			int x=(int)(Math.random()*6);
			t+=x;
			//System.out.println(x);
		}
		System.out.println(t);
		}
	}
}