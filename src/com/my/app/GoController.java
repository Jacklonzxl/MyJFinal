package com.my.app;


import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.TimeUtil; 

public class GoController extends Controller {
	public static int dlen=3;  //日志表
	public void index()throws Exception 
	{
		go();
	}
//	public void go() throws Exception { 
//		  String wxid=getPara("userid");
//		  String url="http://wap.moodlogs.net/gowx?userid="+wxid+"&tm="+System.currentTimeMillis();
//		  System.out.println(url);
//		  redirect(url);		
//	}
	public static int rate=0; 
	public void go() throws Exception { 
		String wxid=getPara("userid");
		if(wxid==null)
		{
			renderText("");
			return ;
		}
		createlogtable();
		long yst = TimeUtil.getFirstDayOfYear(new Date());
		long thetimes=System.currentTimeMillis();
		long nowtimes=thetimes-yst;
		long week=nowtimes/1000/60/60/24/dlen;
		long m=nowtimes/1000/60/60/24%dlen;
		boolean isWriteNext=m>=(dlen-1);
		String logtable="user_link_log_"+week;
		String logtableNext="user_link_log_"+(week+1);
		String where=" a.status=0";
		String order=" a.id desc";
		if(rate<4)
		{
			order=" a.id desc";
		}else
		{
			 order=" a.level desc ,a.id ";
		}
		String search_logtable="(select id,sn from "+logtable+" where type=2 AND user='"+wxid+"' and status=1 order by id desc limit 500) ";
		String task_read_sql=" select a.* from read_task a left join "
				+ search_logtable+"  b    "  
				+ "on (a.sn=b.sn   ) where "+where+"  and b.id is null  "
				+ "order by "+order+"  limit 20";
		

	    String gourl="";
	    List<Record> list=Db.find(task_read_sql);
	    for(int i=0;i<list.size();i++)
	    {
	    	  Record record=list.get(i);
	          gourl=gourl+"|"+record.getStr("url");	
	          int finish_quantity =record.getInt("finish_quantity"); //任务完成数
			  int total_quantity =record.getInt("total_quantity");   //任务数     
	          ///写日志
			  Record utl=new Record();
			  int id=record.getInt("id");
			  utl.set("type", 2);
			  utl.set("rid", id);
			  utl.set("tid", 0);
			  utl.set("user", wxid);
			  utl.set("public_account","");
			  utl.set("url",record.getStr("url"));
			  utl.set("sn",record.get("sn"));
			  utl.set("status",0);
			  utl.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			  utl.set("praise", 0);
			  utl.set("serverid", 0);
			  utl.set("vpsid", 0);
			      
			  Db.save(logtable, utl);
			  if(isWriteNext){
			    	Db.save(logtableNext, utl);
			  } 		
			  int status=0;
			  if(finish_quantity>total_quantity)
			  {
				  status=1;
			  }
			  long lasttime=System.currentTimeMillis();
			  Db.update("update read_task set finish_quantity=finish_quantity+1,real_quantity=real_quantity+1,push_quantity=push_quantity+1,lasttime="+lasttime+",status="+status+"  where id="+id+"");			  
	  	      
			  rate++;
			  if(rate>10)
			  {
				  rate=0;
			  }
			 
			 
	    }	    
	    renderText(gourl);
	    
	  }
	public void updatelink()
	{
		
	}
	public void createlogtable() throws Exception
	{
		long s= TimeUtil.getFirstDayOfYear(new Date());
		
		long t=System.currentTimeMillis();
		long ss=t-s;
		long week=ss/1000/60/60/24/dlen; 	
		String sql_1="create table  if not exists user_link_log_"+week+" like user_task_log";
		String sql_2="create table  if not exists user_link_log_"+(week+1)+" like user_task_log";
		Db.update(sql_1);
		Db.update(sql_2);

	}
}