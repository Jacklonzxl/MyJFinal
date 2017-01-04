package com.my.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.bean.wx.Data;
import com.my.app.bean.wx.Task;
import com.my.app.bean.wx.User;
import com.my.app.bean.wx.UserTask;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.util.MD5;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
import redis.clients.jedis.Jedis;

public class WxApiController extends Controller {
	public static boolean ispushUser=false;
	//private boolean isTest=false;
	public static String account_table="account";
	private String AuthKey="";
	private String ServerId="";
	private String VpsId="8"; 
	public static int usercnt=1; //日限关注
	public static int user_day_maxfollow=5; //日限关注
	public static int user_day_maxread=20;  //日限阅读
	//public static int maxTask=3;  //最大任务数
	//public static int maxFollowTask=1;  //关注最大任务数
	//public static int maxReadTask=1;    //阅读最大任务数

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
	public int rsize=0;
	public int fsize=0;
	public boolean isxw=true;
	//初始化公众参数
	private void getPublic()
	{
		AuthKey=getPara("AuthKey");		
		ServerId=getPara("ServerId");
		VpsId=getPara("VpsId"); 
		if(getPara("ReadCount")!=null)
		{
			//rsize=getParaToInt("ReadCount");
			//fsize=getParaToInt("FollowCount");
		}
		
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
	        	if(jedis.get("account_group_id_"+ServerId+"_follow_status")!=null)
	        	{
	        	int follow_status=Integer.parseInt(jedis.get("account_group_id_"+ServerId+"_follow_status"));
	        	if(follow_status==0)
	        	{
	        		user_day_maxfollow=0;
	        	} 
	        	}
	        }             
	        if(jedis.get("user_day_maxread")!=null)
	        {
	        	user_day_maxread=Integer.parseInt(jedis.get("user_day_maxread"));
	        	if(jedis.get("account_group_id_"+ServerId+"_read_status")!=null)
	        	{
	        	int read_status=Integer.parseInt(jedis.get("account_group_id_"+ServerId+"_read_status"));
	        	if(read_status==0)
	        	{
	        		user_day_maxread=0;
	        	}
	        	}
	        }
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

			if(!"Fifdsf32432fsdfsk".equals(AuthKey)||VpsId==null||VpsId.length()==0)
			{
				taskstr=("null");
			}else
			{
				Record reqRecord=Db.findFirst("select * from vps_status where vpsid='"+VpsId+"' and serverid='"+ServerId+"'");
				if(reqRecord!=null)
				{
					reqRecord.set("reqdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.update("vps_status", reqRecord);
				}else
				{
					reqRecord=new Record();
					reqRecord.set("vpsid", VpsId);
					reqRecord.set("status", 1);
					reqRecord.set("serverid", ServerId);
					reqRecord.set("reqdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.save("vps_status", reqRecord);
				}
				
				if(reqRecord==null||reqRecord.get("status")==null||reqRecord.get("status").toString().trim().equals("0"))
				{
					System.out.println("get task...is close vps---serverid:"+ServerId+" vpsid:"+VpsId);	
					renderText("null");
					return ;
				}else if(ispushUser==false&&(jedis.exists("user_"+ServerId)==false||jedis.llen("user_"+ServerId)<1))
				{
					long nowtime=System.currentTimeMillis();
					long serverid_time=0;
					if(jedis.exists("time_serverid_"+ServerId))
					{
						serverid_time=Long.parseLong(jedis.get("time_serverid_"+ServerId));
					}
				    if(nowtime-serverid_time>600000)
				    {
					ispushUser=true;
					try{
				    if(jedis.get("user_login_cnt")!=null)
				    {
				        	user_login_cnt=Integer.parseInt(jedis.get("user_login_cnt"));
				    }	
				    
					String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
					String a_sql="select * from "+account_table+" where account_group_id='"+ServerId+"' and (status<>-106 and status<>-105 and status<>-205 and status<>-213 and status<>-3) order by last_use_time";
					if(ServerId.equals("999"))
					{
						a_sql="select * from "+account_table+" where  status=-106 and ssid is not null order by last_use_time limit 2000";
					}
					System.out.println(a_sql);
					List<Record> u_list=Db.find(a_sql);
					String key="user_"+ServerId;
					jedis.del(key);
					for(int i=0;i<u_list.size();i++)
					{
						Record record=u_list.get(i);
						if(record.get("friends")!=null&&(record.getInt("friends")==1114||record.getInt("friends")==1115))
						{
							jedis.set("buy_"+record.getStr("account"), "1");
						}
						User user=new User();
						user.setAndroid_api_level(record.getStr("api_level"));
						user.setAndroid_id(record.getStr("androidid"));
						user.setAndroid_platform(record.getStr("android_os"));
						user.setBrand(record.getStr("brand"));
						user.setCpu_arch(record.getStr("cpu_arch"));
						user.setDevice_id(record.getStr("guid"));
						user.setImei(record.getStr("imei"));
						user.setImsi(record.getStr("imsi"));
						user.setIccid(record.getStr("iccid"));
						user.setSsid(record.getStr("ssid"));
						if(user.getSsid()==null)
						{
							continue;
						}
						user.setSerial(record.getStr("serial"));
						user.setAp_mac(record.getStr("ap_mac"));
						user.setBluetooth(record.getStr("bluetooth"));
						user.setMac(record.getStr("mac")); 
						user.setMd5Password(MD5.GetMD5Code(record.getStr("password")));
						user.setModel(record.getStr("model"));
						user.setUsername(record.getStr("account"));
						user.setUserId(record.get("id")+"");
						user.setSession(record.getStr("session"));					
						user.setNickNameList(record.getStr("nick_name_list"));
						long update_time=record.get("update_time")!=null?TimeUtil.GetSqlMilsecTime(record.get("update_time").toString()):0;
						int max=10000000;
						int min=10;
						Random random = new Random();
						int s = random.nextInt(max)%(max-min+1) + min;
						if((nowtime-update_time)>(1000*3600*18L-(s)))
						{
							user.setSession("");
						}
					    long last_use_time=record.get("last_use_time")!=null?TimeUtil.GetSqlMilsecTime(record.get("last_use_time").toString()):0;
					    
					    long thetime=nowtime-last_use_time;
						//登录次数控制
						String key_user_login_cnt="day_"+user.getUserId()+"_login_"+nowdate;
						String login_day_cnt=jedis.get(key_user_login_cnt);	
						
	//					String key_read_day_cnt="day_"+user.getUserId()+"_read_"+nowdate;
	//					int read_day_cnt=jedis.get(key_read_day_cnt)!=null?Integer.parseInt(jedis.get(key_read_day_cnt)):0;					
	//					if(read_day_cnt!=null&&Integer.parseInt(read_day_cnt)>=user_day_maxread)
	//					{
	//					boolean iscanread=user_day_maxread>read_day_cnt;
						
						if(isxw&&record.get("timezone")!=null){
						user.setVersion_incremental(record.get("version_incremental").toString());					
						user.setVersion_incremental_display(record.get("version_incremental_display").toString());					
						user.setCore_count(record.get("core_count").toString());	 
						user.setCpu(record.get("cpu").toString()); 
						user.setCpu_serial(record.get("cpu_serial").toString()); 
						user.setCpu_revision(record.get("cpu_revision").toString()); 
						user.setOs_build_serial(record.get("os_build_serial").toString());			 
						user.setK1(record.get("k1").toString());				 
						user.setK2(record.get("k2").toString());		 
						user.setK11(record.get("k11").toString());					 
						user.setK16(record.get("k16").toString());				 
						user.setK38(record.get("k38").toString());					 
						user.setK39(record.get("k39").toString());					 
						user.setRegion(record.get("region").toString());				 
						user.setCountry_code(record.get("country_code").toString());						 
						user.setTimezone(record.get("timezone").toString());					 
						user.setLanguage(record.get("language").toString());
						}	
						System.out.println(ServerId+" login_day_cnt:"+login_day_cnt+"user_login_cnt"+user_login_cnt+" thetime:"+thetime+" accout_use_time:"+accout_use_time+"   "+(thetime>accout_use_time&&(login_day_cnt==null||Integer.parseInt(login_day_cnt)<user_login_cnt)));
						//登录间隔>2000秒
						if(thetime>accout_use_time&&(login_day_cnt==null||Integer.parseInt(login_day_cnt)<user_login_cnt))
						{
							jedis.lpush(key, JSON.toJSONString(user)); 
						}									    
					}
				  }catch(Exception e){
					e.printStackTrace();
				  }
				  ispushUser=false;
				  jedis.set("time_serverid_"+ServerId,System.currentTimeMillis()+"");
				  //user_time=;
				 }
				}
				 initSetting(jedis);
//				 user_day_maxfollow=5; //日限关注
//				 user_day_maxread=20;  //日限阅读 
//				 maxFollowTask=1;  //关注最大任务数
//				 maxReadTask=1;    //阅读最大任务数
//
//				 user_follow_cnt=1; // 
//				 user_read_cnt=5;  
				 
				 
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
					 taskstr=search_task(jedis);
				 }
			}

        }catch(Exception e){
        	e.printStackTrace();
        }
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
	private String search_task(Jedis jedis) throws Exception
	{
		createlogtable(ServerId);
		long yst = TimeUtil.getFirstDayOfYear(new Date());
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen;
		long m=nowtimes/1000/60/60/24%dlen;
		boolean isWriteNext=m>=(dlen-1);
		String logtable="user_task_log_"+ServerId+"_"+week;
		String logtableNext="user_task_log_"+ServerId+"_"+(week+1);

		
		String nowdate=TimeUtil.GetSqlDate(thetimes).substring(0, 10);
		UserTask ut=new UserTask();	 
		ut.setThreadCount(threadCount);
		ut.setWaitTime(waitTime);
		boolean hasData=false;
		List<User> userlist=new ArrayList<User>();					
     	//int taskcnt=0; //总任务数	
     	//int followcnt=0;//总关注数
     	//int readcnt=0;  //总阅读数
     	//boolean isNotFull=followcnt<maxFollowTask&&readcnt<maxReadTask;//&&taskcnt<maxTask;
     	
		for(int i=0;i<usercnt;i++)//isNotFull
		{
 
			String key="user_"+ServerId; 
			String user_str=jedis.rpop(key);
 
			if(user_str==null||user_str.length()<10)
			{
				System.out.println(" account  no serverid:"+ServerId); 
				continue;
			}
          
			//设置用户硬件信息
			user_str= user_str.replaceAll("\\\\", "").replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}");
			if(ServerId.equals("1")&&VpsId.equals("100"))
			{
				System.out.println(user_str);
			}
			User user=JSON.parseObject(user_str, User.class);//new User();
			
			int FollowCount=fsize>0?fsize:user_follow_cnt;
	
			List<Task> tlist=new ArrayList<Task>();			
			List<Record> t_f_list=new ArrayList<Record>();
			String ft="user_task_log_public_"+ServerId;
			String follow_table="(select id,public_account from "+ft+" where type=1 AND user='"+user.getUserId()+"' and status=1 order by id desc limit 500)";
			//用户找任务
			String task_follow_sql=" select b.*,a.* from follow_task a left join "
					+ follow_table+" b  "
					+ "on (a.public_account=b.public_account )  where  "
					+ "a.status=0 and total_quantity>finish_quantity and b.id is null "
					+ "order by a.level desc,a.lasttime asc limit 0,"+FollowCount;
			long s=System.currentTimeMillis();
			//判断是否满足日限
			String key_follow_day_cnt="day_"+user.getUserId()+"_follow_"+nowdate;
			String follow_day_cnt=jedis.get(key_follow_day_cnt);
			//日限通过
			if(fsize==0&&follow_day_cnt!=null&&Integer.parseInt(follow_day_cnt)>=user_day_maxfollow)
			{
			 
				if(show_log==1)
				{
					System.out.println(key_follow_day_cnt+" follow:"+follow_day_cnt+" serverid:"+ServerId); 
			 
				}
				//continue;
			}else
			{
				t_f_list=Db.find(task_follow_sql);		
			}
			
			//关注任务列表			
			long t=System.currentTimeMillis();
			if(t-s>200&&show_log==1)
			{
				System.out.println("task_follow_sql:"+(t-s));
			}
			if(VpsId.equals("100"))
			{
				System.out.println("t_f_list:"+(t_f_list.size()));
			}
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
				for(int j=0;j<t_f_list.size();j++)//followcnt<=FollowCount&&&&taskcnt<maxTask
				{
					Record record1=t_f_list.get(j);
					if(record1.getStr("serverid")!=null&&record1.getStr("serverid").length()>0)
					{
						if(!record1.getStr("serverid").equals(ServerId))
						{
							continue;
						}
					}
					
   
				    //写日志
				    Record utl=new Record();
				    utl.set("type", 1);
				    utl.set("rid", record1.get("id"));
				    utl.set("tid", taskRecord.get("id"));
				    utl.set("user", user.getUserId());
				    utl.set("public_account", record1.get("public_account"));
				    utl.set("status",0);
				    utl.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("donedate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("serverid", ServerId);
				    utl.set("vpsid", VpsId);
				    Db.save(ft, utl);
//				    if(isWriteNext)
//				    {
//				    	Db.save(logtableNext, utl);
//				    }
				    //给客户端的任务数据
				    Data data=new Data();
				    data.setMessage(record1.getStr("reply_content"));
				    data.setWxid(record1.getStr("public_account"));
				    data.setId(Integer.parseInt(utl.getLong("id")+""));				    
				    datalist.add(data);
				    //记录每日推送数据
				    String update_follow_sql="update follow_task set finish_quantity=finish_quantity+1,lasttime="+System.currentTimeMillis()+" where id='"+ record1.get("id")+"'";
				    Db.update(update_follow_sql);
				    hasData=true;
				    //taskcnt++;
				    //followcnt++;
				    
				}
				//System.out.println("datalist:"+datalist.size()+" serverid:"+serv);
				task.setData(datalist);
				tlist.add(task);
			}
			//int urcnt=user_read_cnt;
			int ReadCount=rsize>0?rsize:user_read_cnt;
//			if(getHH()==7)
//			{
//				//user_day_maxread=user_day_maxread/3;
//				urcnt=urcnt/3;
//			}
//			if(getHH()==0)
//			{
//				//user_day_maxread=user_day_maxread/2;
//				urcnt=urcnt/2;
//			}
			//System.out.println("user_read_cnt:"+urcnt);
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
			String search_logtable="(select id,sn from "+logtable+" where type=2 AND user='"+user.getUserId()+"' and status=1 order by id desc limit 500) ";// AND b.type=2 AND b.user='"+user.getUserId()+"' 
			//type=2 and status=1 and
			String task_read_sql=" select a.* from read_task a left join "
					+ search_logtable+"  b    "  
					+ "on (a.sn=b.sn   ) where "+where+"  and b.id is null  "
					+ "order by "+orderby+"  limit 0,"+(ReadCount+30);
			 
			s=System.currentTimeMillis();		
			List<Record> t_r_list=new ArrayList<Record>();
			
			//"account_group_id_"+bean.get("serverid")+"_read"
			int udmr=user_day_maxread;
			//判断用户是否满足阅读日限
			String key_read_day_cnt="day_"+user.getUserId()+"_read_"+nowdate;
			String read_day_cnt=jedis.get(key_read_day_cnt);	
			String key_account_group_read="account_group_id_"+ServerId+"_read";
			String key_account_group_day="account_group_id_"+ServerId+"_day";
			if(jedis.exists(key_account_group_day))
			{
			
				//用户导入时间
				String key_create_time="create_time_"+user.getUserId();
				String create_time=null;
				if(jedis.get(key_create_time)!=null)
				{
					create_time=jedis.get("key_create_time");
				}else
				{
					Record uRecord=Db.findFirst("select id,create_time from account where account='"+user.getUserId()+"'");
					if(uRecord!=null&&uRecord.get("create_time")!=null)
					{
						create_time=uRecord.get("create_time").toString();
					}else
					{
						create_time=TimeUtil.GetSqlDate(System.currentTimeMillis()-(10*24*3600*1000L));
					}
					jedis.set(key_create_time, create_time);
				}
				long times=System.currentTimeMillis()-TimeUtil.GetSqlMilsecTime(create_time);
				int dcnt=Integer.parseInt(jedis.get(key_account_group_day));
				
				if(times<(dcnt*24*3600*1000L))
				{
					System.out.println("create_time:"+create_time);
					udmr=Integer.parseInt(jedis.get(key_account_group_read));
					//urcnt=user_read_cnt/2;
				}
				
			}else
			{
				//用户导入时间
				String key_create_time="create_time_"+user.getUserId();
				String create_time=null;
				if(jedis.get(key_create_time)!=null)
				{
					create_time=jedis.get("key_create_time");
				}else
				{
					Record uRecord=Db.findFirst("select id,create_time from account where account='"+user.getUserId()+"'");
					if(uRecord!=null&&uRecord.get("create_time")!=null)
					{
						create_time=uRecord.get("create_time").toString();
					}else
					{
						create_time=TimeUtil.GetSqlDate(System.currentTimeMillis()-(10*24*3600*1000L));
					}
					jedis.set(key_create_time, create_time);
				}
				if(create_time==null)
				{
					create_time=TimeUtil.GetSqlDate(System.currentTimeMillis()-(10*24*3600*1000L));
				}
				long times=System.currentTimeMillis()-TimeUtil.GetSqlMilsecTime(create_time);
				if(times<(7*24*3600*1000L))
				{
					System.out.println("create_time:"+create_time);
					udmr=udmr-38;
					//user_read_cnt=user_read_cnt/2;
				} 
				
			}
			if(ServerId.equals("1")&&VpsId.equals("100"))
			{
				System.out.println("read_day_cnt!=null&&Integer.parseInt(read_day_cnt)>=udmr:"+(read_day_cnt!=null&&Integer.parseInt(read_day_cnt)>=udmr));
			}
			if(rsize==0&&read_day_cnt!=null&&Integer.parseInt(read_day_cnt)>=udmr)
			{
				//日限+1
				//jedis.incr(key_read_day_cnt);
                if(show_log==1){
                	System.out.println(key_read_day_cnt+" read:"+read_day_cnt+" serverid:"+ServerId); 
                }
				//continue;
			}else
			{
				if(ServerId.equals("1")&&VpsId.equals("100"))
				{
					System.out.println(task_read_sql);
				}
				
				t_r_list=Db.find(task_read_sql);
			}			
			t=System.currentTimeMillis();
			if(t-s>200&&show_log==1)
			{
				System.out.println("sql task_read_sql time :"+(t-s));
			}
			 //AND  b.type=2 AND b.user='"+user.getUserId()+"' 
			//无补量数据使用默认查询
            if(readTemp==10&&t_r_list.size()<ReadCount)
            {
            	 where="(a.status=0 and (total_quantity>finish_quantity or (total_quantity-push_quantity)>"+(plus1+5)+" ))";
    			 orderby="a.level desc,a.lasttime asc";  //按下单排序
    			 task_read_sql=" select a.* from read_task a left join "
    						+search_logtable+"  b    "
    						+ " on (a.sn=b.sn)  where "+where+"  and b.id is null  "
    						+ "order by "+orderby+"  limit 0,"+(ReadCount-t_r_list.size()+30);
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
			
			String buyph=jedis.get("buy_"+user.getUserId());
			if(t_r_list.size()>0&&buyph==null)
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
				for(int j=0;read_cnt<ReadCount&&j<t_r_list.size();j++)
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
				    
				    
				    //long nowtime=System.currentTimeMillis();
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
				    //boolean isdopush=praise_quantity>push_praise&&(total_quantity-push_quantity)<(praise_quantity-push_praise-5);
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
				    utl.set("user", user.getUserId());
				    utl.set("public_account", record1.get("public_account"));
				    utl.set("url",url);
				    utl.set("sn",record1.get("sn"));
				    utl.set("status",0);
				    utl.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				    utl.set("praise", data.getPraise());
				    utl.set("serverid", ServerId);
				    utl.set("vpsid", VpsId);
				     
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
				    //System.out.println("read cnt:"+datalist.size());
				}
				//System.out.println("read datalist:"+datalist.size());
				task.setData(datalist);
				tlist.add(task); 
			}
			
			user.setTask(tlist);
		    if(hasData)
			{
		    	//判断是否满足日限
				String key_user_login_cnt="day_"+user.getUserId()+"_login_"+nowdate;
				jedis.incr(key_user_login_cnt);
		    	String nowtime=TimeUtil.GetSqlDate(System.currentTimeMillis());
				String update_user_sql="update  "+account_table+" set last_use_time ='"+nowtime+"' where id="+user.getUserId();
				Db.update(update_user_sql);		    	
		    	userlist.add(user);
			}
		}
		ut.setUserlist(userlist);
				
		if(hasData)
	    {
		 
			String str=new Gson().toJson(ut);//JSON.toJSONString(JSON.toJSONString(ut));	
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
		if(!"Fifdsf32432fsdfsk".equals(AuthKey)||VpsId==null||VpsId.length()==0)
		{
			renderText("");
			return ;
		}
		
		long yst = TimeUtil.getFirstDayOfYear(new Date());
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen;
		String logtable="user_task_log_"+ServerId+"_"+week;
		
        Cache userCache= Redis.use("userc");
        Jedis jedis = userCache.getJedis();
        boolean  isUpdate=true;
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
					String ft="user_task_log_public_"+ServerId;
					if(Type.equals("1"))
					{
						logtable= ft;
					}
					Record user_task_log=Db.findById(logtable, dataid);
					if(user_task_log==null||user_task_log.getInt("status")==1)
					{
						continue;
					}
					sts+="or id="+dataid+" ";
					if(Type.equals("1")) //关注+1处理
					{	
						String key_follow_day_cnt="day_"+user_task_log.getStr("user")+"_follow_"+nowdate;
						jedis.incr(key_follow_day_cnt);
						jedis.incr("t_f_"+ServerId+"_"+VpsId+"_"+nowdate);	
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
							jedis.incr("t_r_"+ServerId+"_"+VpsId+"_"+nowdate);	
							saveUserDayRead(nowdate,user_task_log.getStr("user"),jedis.get(key_read_day_cnt));
							ReadTask readTask=ReadTask.dao.findById(user_task_log.getInt("rid"));
							if(readTask==null)
							{
								isUpdate=false;
							}else
							{
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
							 }
						}catch(Exception e) {
							e.printStackTrace();
						 }
					}
					
					
				}else
				{
					//修改失败日志状态
					String status_error[]=TokenUtil.getStrngArray(dataid, "~");
					String id=status_error[0];
					String ft="user_task_log_public_"+ServerId;
					if(Type.equals("1"))
					{
						logtable= ft;
						try{
							Record user_task_log=Db.findById(logtable, dataid);
							String ffkey="failedfllow_cnt"+user_task_log.get("rid");
							FollowTask fttask=FollowTask.dao.findById(user_task_log.get("rid"));
							jedis.incr(ffkey);
							if(jedis.exists(ffkey)&&Integer.parseInt(jedis.get(ffkey))>30&&fttask.getInt("real_quantity")==0)
							{
								Db.update("update follow_task set status=-3,settle=1 where id="+user_task_log.get("rid"));
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					String dataSql="update "+logtable+" set status="+status_error[1]+",donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+VpsId+"' where id="+id+" and type="+Type;
					if(show_log==1)
					{
						System.out.println("dataid-error:"+dataid+" server:"+ServerId+" vpsid:"+VpsId);
					}
					Db.update(dataSql);
					 
					Record user_task_log=Db.findById(logtable, id);
					String sql="update "+account_table+" set session=''  where id="+user_task_log.get("user")+"";
					Db.update(sql);
				 
				}
			 }
			if(sts.length()>0)
			{
				//修改成功日志状态
				long s=System.currentTimeMillis();
				String ft="user_task_log_public_"+ServerId;
				if(Type.equals("1"))
				{
					logtable= ft;
				}
				String dataSql="update "+logtable+" set status=1,donedate='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"', vpsid='"+VpsId+"' where ("+sts.substring(2, sts.length())+")  and type="+Type;
			 
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
        }catch(Exception e){
        	e.printStackTrace();
        	}
  
		jedis.close();
		if(isUpdate)
		{
			renderText("true");
		}else
		{
			renderText("false");
		}
		
	}
	public void saveUserDayRead(String thedate,String userid,String readcnt)
	{
		Record record=Db.findFirst("select * from account_day_cnt where thedate='"+thedate+"' and userid="+userid+"");
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
		String logtable="user_task_log_"+ServerId+"_"+week; 
		
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
		
		Record reqRecord=Db.findFirst("select * from vps_status where vpsid='"+VpsId+"' and serverid='"+ServerId+"'");
		if(reqRecord!=null)
		{
			reqRecord.set("reportdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			Db.update("vps_status", reqRecord);
		}
		renderText("true");
		
	}
	/***
	 * 保存微信端session值
	 */
	public void save_session()
	{
		
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}
		String UserId=getPara("UserId"); 
		String Result=getPara("Result"); 
 
		String sql="update "+account_table+" set session='"+Result+"',update_time='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"' where id="+UserId+"";
		Db.update(sql);
		renderText("true");
		
	}
	/***
	 * 保存微信端session值
	 */
	public void update_account()
	{
 	
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}
		
		String UserId=getPara("UserId"); 
		String Result=getPara("Result");
 
		String sql="update "+account_table+" set status='"+Result+"' where id="+UserId+"";
		Db.update(sql);
		Record record=new Record();
		record.set("userid", UserId);
		record.set("status", Result);
		record.set("serverid", ServerId);
		record.set("vpsid", VpsId);
		record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		Db.save("account_status", record);
		String session="";
		if(!Result.equals("1"))
		{
             session="session='',";
		}
		sql="update "+account_table+" set "+session+" seesion_update_time='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"' where id="+UserId+"";
		Db.update(sql);
		renderText("true");
		
	}
	public void check_Version()
	{
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}else
		{
			Record record=Db.findFirst("select * from wx_version where serverid="+ServerId+" and vpsid="+VpsId);
			if(record!=null)
			{
				String Version=getPara("Version");
				String version=record.getStr("version");
				if(Version.equals(version))
				{
					record.set("url", "");
					Db.update("wx_version", record);
					renderText("false");
				}else
				{
					renderText("true");
					return ;	
				}
				
			}else
			{
				record=new Record();
				record.set("vpsid", VpsId);
				record.set("serverid", ServerId);
				record.set("version", "");
				record.set("url", "");
				Db.save("wx_version", record);
				renderText("false");
				return ;
			}
		}
	}
	public void get_UpdateUrl()
	{
		getPublic();
		if(!"Fifdsf32432fsdfsk".equals(AuthKey))
		{
			renderText("");
			return ;
		}else
		{
			Record record=Db.findFirst("select * from wx_version where serverid="+ServerId+" and vpsid="+VpsId);
			if(record!=null)
			{
				renderText(record.getStr("url"));
				return ;
				
			}else
			{
				renderText("");
				return ;
			}
		}		
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
				record.set("vpsid", VpsId);
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
					record.set("vpsid", VpsId);
					record.set("ip", ip).set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.save("vps_ip", record);
				}else
				{
					renderText("false");
				}
				
			}
		 }
		
	}
	public void del_session()
	{
		int serverid=getParaToInt("serverid");
		String sql="update account set session='' where account_group_id="+serverid;
		if(serverid==-1)
		{
			sql="update account set session=''";
		}
		Db.update(sql);
        Cache userCache= Redis.use("userc");
        Jedis jedis = userCache.getJedis();
        
        if(serverid==-1)
		{
          for(int i=0;i<100;i++)
          {
        	  jedis.del("user_"+(i+1));
          }
		}else
		{
			jedis.del("user_"+serverid);
		}
        jedis.close();
        renderText("ok");
	}
	public void update_account_info()
	{
		int serverid=getParaToInt("serverid");
		String sql="select * from  account  where account_group_id="+serverid+" and status<>-106 and status<>-105 and status<>-205 and status<>-3 and status<>-213";
		if(serverid==-1)
		{
			for(int s=0;s<60;s++)
			{
				sql="select * from  account  where account_group_id="+(s+1)+" and status<>-106 and status<>-105 and status<>-205 and status<>-3 and status<>-213";
			    List<Record> list=Db.find(sql);
				for(int i=0;i<list.size();i++)
				{
					Record record=list.get(i);
					if(record.getStr("ap_mac")==null&&record.getStr("ssid")==null)
					{
					     Record r=Db.findFirst("select * from b_phinfo where status=0 limit 1");
					     Record r_ssid=Db.findFirst("SELECT * FROM ssid ORDER BY RAND() LIMIT 1");
					     record.set("imsi", r.get("imsi"));
					     record.set("iccid", r.get("iccid"));
					     record.set("ap_mac", r.get("ap_mac"));
					     record.set("ssid", r_ssid.get("ssid"));
					     record.set("bluetooth", r.get("bluetooth"));
					     Db.update("account", record);
					     r.set("status", 1);
					     Db.update("b_phinfo",r);
					}
				}
			}
		}else
		{
		    List<Record> list=Db.find(sql);
			for(int i=0;i<list.size();i++)
			{
				Record record=list.get(i);
				if(record.getStr("ap_mac")==null&&record.getStr("ssid")==null)
				{
				     Record r=Db.findFirst("select * from b_phinfo where status=0 limit 1");
				     Record r_ssid=Db.findFirst("SELECT * FROM ssid ORDER BY RAND() LIMIT 1");
				     record.set("imsi", r.get("imsi"));
				     record.set("iccid", r.get("iccid"));
				     record.set("ap_mac", r.get("ap_mac"));
				     record.set("ssid", r_ssid.get("ssid"));
				     record.set("bluetooth", r.get("bluetooth"));
				     Db.update("account", record);
				     r.set("status", 1);
				     Db.update("b_phinfo",r);
				}
			}
		}
         
       
        renderText("ok");
	}
	public void pushAccount()
	{
		String table=getPara("table");
		String sql=  "select"
				  +" `account`,"
				  +" `password`,"
				  +" `imei`  ,"
				  +" `model_id` ,"
				  +" `friends` ,"
				  +" `email`  ,"
				  +" `email_password` ," 
				  +" `email_regist_info`, " 
				  +" `status` ,"
				  +" `last_status` ,"
				  +" `group_share` ,"
				  +" `account_group_id` ,"
				  +" `friend_share` ,"
				  +" `last_share_date` ,"
				  +" `last_friends` ,"
				  +" `update_type` ,"
				  +" `update_time` ,"
				  +" `update_result`,"
				  +" `add_friend_date`  ,"
				  +" `city`  ,"
				  +" `info_nickname`  ,"
				  +" `info_gender` ,"
				  +" `info_province`  ,"
				  +" `info_city`  ,"
				  +" `info_sign`  ,"
				  +" `info_photo`  ,"
				  +" `last_use_time`  ,"
				  +" `nick_name_list`  ,"
				  +" `imsi`  ,"
				  +" `iccid`  ,"
				  +" `androidid` ,"
				  +" `mac`  ,"
				  +" `bluetooth`  ,"
				  +" `serial`  ,"
				  +" `guid`  ,"
				  +" `language`  ,"
				  +" `mobile`  ,"
				  +" `qq`  ,"
				  +" `qq_password`  ,"
				  +" `wxid`  ,"
				  +" `org_wxid`  ,"
				  +" `amount`,"
				  +" `coordinates_id` ,"
				  +" `heart_time` ,"
				  +" `create_time` ,"
				  +" `set_logo` ,"
				  +" `set_profile` ,"
				  +" `brand`  ,"
				  +" `model`  ,"
				  +" `android_os`  ,"
				  +" `api_level`  ,"
				  +" `cpu_arch`  ,"
				  +" `new_password`  ,"
				  +" `last_password`  ,"
				  +" `model_user_id` ,"
				  +" `session`"
				  +" from "+table+" where status=1";
		List<Record> list=Db.find(sql);
		for(int i=0;i<list.size();i++)
		{
			Record record=list.get(i);
			if(record.get("guid")==null||record.getStr("guid").length()==0)
			{
				String guid=Db.queryStr(" SELECT guid FROM account where status-106 ORDER BY RAND()  LIMIT 1");
				record.set("guid", guid);
			}
			record.set("create_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			String account=record.getStr("account");
			Record r=Db.findFirst("select * from "+account_table+" where account='"+account+"'");
			if(r==null||r.get("account")==null)
			{
				Db.save(""+account_table+"", record);
			}else
			{
				//r.set("status", record.get("status"));
				//Db.update("update "+account_table+" set status=1 where  id="+r.get("id"));
			}
		}
		renderText("ok");
				 

		
	}
	public void pushReg()
	{
		String len=getPara("len");
		String table=getPara("table");
		String sid=getPara("serverid");
	    String sql="select * from "+table+" limit "+len;
	    List<Record> list=Db.find(sql);
	    System.out.println("size:"+list.size());
	    for(int i=0;i<list.size();i++)
	    {
	    	Record record=list.get(i);
	        String ph=record.getStr("ph");
	        String password=record.getStr("password");
	        String imei=record.getStr("imei");
	        String android_id=record.getStr("android_id");
	        String mac=record.getStr("mac");
	        String guid=record.getStr("guid");
	        
			String frsql=  "select"
					  +" `account`,"
					  +" `password`,"
					  +" `imei`  ,"
					  +" `model_id` ,"
					  +" `friends` ,"
					  +" `email`  ,"
					  +" `email_password` ," 
					  +" `email_regist_info`, " 
					  +" `status` ,"
					  +" `last_status` ,"
					  +" `group_share` ,"
					  +" `account_group_id` ,"
					  +" `friend_share` ,"
					  +" `last_share_date` ,"
					  +" `last_friends` ,"
					  +" `update_type` ,"
					  +" `update_time` ,"
					  +" `update_result`,"
					  +" `add_friend_date`  ,"
					  +" `city`  ,"
					  +" `info_nickname`  ,"
					  +" `info_gender` ,"
					  +" `info_province`  ,"
					  +" `info_city`  ,"
					  +" `info_sign`  ,"
					  +" `info_photo`  ,"
					  +" `last_use_time`  ,"
					  +" `nick_name_list`  ,"
					  +" `imsi`  ,"
					  +" `iccid`  ,"
					  +" `androidid` ,"
					  +" `mac`  ,"
					  +" `bluetooth`  ,"
					  +" `serial`  ,"
					  +" `guid`  ,"
					  +" `language`  ,"
					  +" `mobile`  ,"
					  +" `qq`  ,"
					  +" `qq_password`  ,"
					  +" `wxid`  ,"
					  +" `org_wxid`  ,"
					  +" `amount`,"
					  +" `coordinates_id` ,"
					  +" `heart_time` ,"
					  +" `create_time` ,"
					  +" `set_logo` ,"
					  +" `set_profile` ,"
					  +" `brand`  ,"
					  +" `model`  ,"
					  +" `android_os`  ,"
					  +" `api_level`  ,"
					  +" `cpu_arch`  ,"
					  +" `new_password`  ,"
					  +" `last_password`  ,"
					  +" `model_user_id` ,"
					  +" `session`"
					  +" from account_android where  account='"+ph+"'";
			Record fromRecord=Db.findFirst(frsql);
	        if (fromRecord==null) {
	        	String nosql="SELECT * FROM account where status=1 ORDER BY RAND() limit 1";
	        	//Record noRecord=
	        	fromRecord=Db.findFirst(nosql);
	        	fromRecord.set("id", 0);
	        	fromRecord.set("email", "");
	        	fromRecord.set("account", ph);
	        	fromRecord.set("password", password);
	        	
	        }
	        	if(imei!=null&&imei.length()>9)
	        	{
	        		int L=15-imei.length();
	        		for(int c=0;c<L;c++)
	        		{
	        			imei="0"+imei;
	        		}
	        		fromRecord.set("imei", imei);
	        		
	        	}
	        	
	        	fromRecord.set("androidid", android_id);
	        	fromRecord.set("guid", guid);
	        	fromRecord.set("mac", mac);
	        	fromRecord.set("account_group_id", sid);
	        	fromRecord.set("last_use_time", TimeUtil.GetSqlDate(System.currentTimeMillis()-36000000));

				
	        	Record r=Db.findFirst("select * from "+account_table+" where account='"+ph+"'");

				if(r==null||r.get("account")==null)
				{
		        	if(fromRecord.get("api_level")==null||fromRecord.get("api_level").equals(""))
		        	{
		        		//fromRecord.set("api_level","android-19");
		        		fromRecord.set("api_level","android-19");
		        	}
		        	if(fromRecord.get("android_os")==null||fromRecord.get("android_os").equals(""))
		        	{
		        		fromRecord.set("android_os","4.4.4");
		        	}
		        	if(fromRecord.get("brand")==null||fromRecord.get("brand").equals(""))
		        	{
		        		fromRecord.set("brand","SKYHON");
		        	}
		        	if(fromRecord.get("cpu_arch")==null||fromRecord.get("cpu_arch").equals(""))
		        	{
		        		fromRecord.set("cpu_arch","armeabi-v7a");
		        	}
		        	if(fromRecord.get("model")==null||fromRecord.get("model").equals(""))
		        	{
		        		fromRecord.set("model","H888-M2");
		        	}
		        	fromRecord.set("model_id", TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10).replaceAll("-", ""));
		        	fromRecord.set("create_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));	 
		        	fromRecord.set("status", 1);
					Db.save(""+account_table+"", fromRecord);
					Db.update("update account_android set status=404 where account='"+ph+"' ");
				} 
			//}
	    	
	    }
	    renderText("ok");
		
	}
	public void PushGuid()
	{
		String len=getPara("len");
		String table=getPara("table");
		String sid=getPara("serverid");
	    String sql="select * from "+table+" limit "+len;
	    List<Record> list=Db.find(sql);
	    System.out.println("size:"+list.size());
	    for(int i=0;i<list.size();i++)
	    {
	    	Record record=list.get(i);
	        String ph=record.getStr("ph");
	        String guid=record.getStr("guid");
	        
			String frsql=  "select"
					  +" `account`,"
					  +" `password`,"
					  +" `imei`  ,"
					  +" `model_id` ,"
					  +" `friends` ,"
					  +" `email`  ,"
					  +" `email_password` ," 
					  +" `email_regist_info`, " 
					  +" `status` ,"
					  +" `last_status` ,"
					  +" `group_share` ,"
					  +" `account_group_id` ,"
					  +" `friend_share` ,"
					  +" `last_share_date` ,"
					  +" `last_friends` ,"
					  +" `update_type` ,"
					  +" `update_time` ,"
					  +" `update_result`,"
					  +" `add_friend_date`  ,"
					  +" `city`  ,"
					  +" `info_nickname`  ,"
					  +" `info_gender` ,"
					  +" `info_province`  ,"
					  +" `info_city`  ,"
					  +" `info_sign`  ,"
					  +" `info_photo`  ,"
					  +" `last_use_time`  ,"
					  +" `nick_name_list`  ,"
					  +" `imsi`  ,"
					  +" `iccid`  ,"
					  +" `androidid` ,"
					  +" `mac`  ,"
					  +" `bluetooth`  ,"
					  +" `serial`  ,"
					  +" `guid`  ,"
					  +" `language`  ,"
					  +" `mobile`  ,"
					  +" `qq`  ,"
					  +" `qq_password`  ,"
					  +" `wxid`  ,"
					  +" `org_wxid`  ,"
					  +" `amount`,"
					  +" `coordinates_id` ,"
					  +" `heart_time` ,"
					  +" `create_time` ,"
					  +" `set_logo` ,"
					  +" `set_profile` ,"
					  +" `brand`  ,"
					  +" `model`  ,"
					  +" `android_os`  ,"
					  +" `api_level`  ,"
					  +" `cpu_arch`  ,"
					  +" `new_password`  ,"
					  +" `last_password`  ,"
					  +" `model_user_id` ,"
					  +" `session`"
					  +" from account_android where  account='"+ph+"'";
			Record fromRecord=Db.findFirst(frsql);
	        if (fromRecord!=null) {
	        	 
	       
	        	fromRecord.set("guid", guid); 
	        	fromRecord.set("account_group_id", sid);
	        	fromRecord.set("last_use_time", TimeUtil.GetSqlDate(System.currentTimeMillis()-36000000));

				
	        	Record r=Db.findFirst("select * from "+account_table+" where account='"+ph+"'");

				if(r==null||r.get("account")==null)
				{
					 Record b=Db.findFirst("select * from b_phinfo where status=0 limit 1");
				     Record r_ssid=Db.findFirst("SELECT * FROM ssid ORDER BY RAND() LIMIT 1");
				     fromRecord.set("iccid", b.get("iccid"));
				     fromRecord.set("ap_mac", b.get("ap_mac"));
				     fromRecord.set("ssid", r_ssid.get("ssid"));
				     fromRecord.set("bluetooth", b.get("bluetooth"));
		 
				     b.set("status", 1);
				     Db.update("b_phinfo",b);
		        	
		        	fromRecord.set("model_id", TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10).replaceAll("-", ""));
		        	fromRecord.set("create_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));	 
		        	fromRecord.set("status", 1);
					Db.save(""+account_table+"", fromRecord);
					Db.update("update account_android set status=404 where account='"+ph+"' ");
				} 
			}
	    	
	    }
	    renderText("ok");		
	}
	public void bakLog()
	{
		int sz=getParaToInt("sz");
		int t=getParaToInt("t");
		long ss=System.currentTimeMillis();

 		for(int a=0;a<t;a++)
 		{
		String sql_insert=" INSERT INTO user_task_log_bak1(id,rid,tid,type,user,public_account,url,sn,status,adddate,donedate,praise,vpsid,serverid) SELECT id,rid,tid,type,user,public_account,url,sn,status,adddate,donedate,praise,vpsid,serverid FROM user_task_log order by id limit "+sz;
		Db.update(sql_insert);
		String sql_del="delete from user_task_log order by id limit "+sz;
		Db.update(sql_del);
 		}
		long tt=System.currentTimeMillis();
		renderText("ok:"+(tt-ss));
		
	}
	public void  update_reg_account()
	{
		//AuthKey=xxx&ServerId=xx&VpsId=xx&UserId=xx&UserName=xx&DeviceID=xxx&Status=xxx
		if(!"Fifdsf32432fsdfsk".equals(AuthKey)||VpsId==null||VpsId.length()==0)
		{
        String UserId=getPara("UserId");
        String UserName=getPara("UserName");
        String guid=getPara("DeviceID");
        getPublic();
        Db.update("update account set account='"+UserName+"',guid='"+guid+"',status="+getPara("Status")+" where id="+UserId);
        renderText("true");
		}else
		{
			
			renderText("false");
		}
	}
 
	public void createlogtable(String serverid) throws Exception
	{
		long s= TimeUtil.getFirstDayOfYear(new Date());
		
		long t=System.currentTimeMillis();
		long ss=t-s;
		long week=ss/1000/60/60/24/dlen; 	
		String sql_1="create table  if not exists user_task_log_"+serverid+"_"+week+" like user_task_log";
		String sql_2="create table  if not exists user_task_log_"+serverid+"_"+(week+1)+" like user_task_log";
		String sql_3="create table  if not exists user_task_log_public_"+serverid+" like user_task_log";
		Db.update(sql_1);
		Db.update(sql_2);
		Db.update(sql_3);

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