package com.my.app.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil; 

public class LiucReqTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(30000);
		while(true)
		{
			try{
			//long nt=System.currentTimeMillis();
			String cptask_sql="select * from cp_task where status=0 and (liuccontinue is not null or liucday is not null)";
			List<Record> list=Db.find(cptask_sql);
			for(int i=0;i<list.size();i++)
			{
				Record record=list.get(i);
				String liuccontinue=record.getStr("liuccontinue");
				String liucday=record.getStr("liucday");
				if(liuccontinue!=null&&liuccontinue.length()>0)
				{
					
					String chs[]=TokenUtil.getStrngArray(record.getStr("cids"), ",");
					for(int j=0;j<chs.length;j++)
					{
						Record liuctask=new Record();
						String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()-36000000).substring(0, 10);
						String channel= chs[0].substring(1, chs[0].length());
						liuctask.set("thedate", thedate);
						liuctask.set("cpid", record.get("cpid"));
						liuctask.set("channel",channel);
						liuctask.set("packname", record.get("packname"));
						liuctask.set("status", 0);
						liuctask.set("liucs", liuccontinue);
						Record theliuc=Db.findFirst("select * from cp_liuc_task where thedate='"+thedate+"' and cpid='"+record.get("cpid")+"' and channel='"+channel+"'");
						if(theliuc==null||theliuc.get("id")==null)
						{
							Db.save("cp_liuc_task", liuctask);
						}
						
					}

					 
					
				}
				if(liucday!=null&&liucday.length()>0)
				{
					String chs[]=TokenUtil.getStrngArray(record.getStr("cids"), ",");
					for(int j=0;j<chs.length;j++)
					{
						String dls[]=TokenUtil.getStrngArray(liucday, ",");
						for(int k=0;k<dls.length;k++)
						{
							String dls_d_v[]=TokenUtil.getStrngArray(dls[k], "_");
							int day=Integer.parseInt(dls_d_v[0]);
							long longday=day*1000*3600*24L;
							String val=dls_d_v[1];
							Record liuctask=new Record();
							String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()-longday).substring(0, 10);
							String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-(1000*3600*24L)).substring(0, 10);
							//System.out.println(thedate);
							String channel= chs[0].substring(1, chs[0].length());
							liuctask.set("thedate", thedate);
							liuctask.set("cpid", record.get("cpid"));
							liuctask.set("channel",channel);
							liuctask.set("packname", record.get("packname"));
							liuctask.set("status", 0);
							liuctask.set("liucs", val);
							Record theliuc=Db.findFirst("select * from cp_liuc_task where addtime>'"+nowdate+"' and  thedate='"+thedate+"' and cpid='"+record.get("cpid")+"' and channel='"+channel+"'");
							if(theliuc==null||theliuc.get("id")==null)
							{
								Db.save("cp_liuc_task", liuctask);
							}	
							
						}
		
						
					}
					
				}
					
					
			}
			}catch(Exception e){e.printStackTrace();}
			String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
			String sql="select * from cp_liuc_task where status=0 and thedate<'"+nowdate+"'"; //and thedate='"+nowdate+"' 
			//System.out.println(sql);
			try{
			Record  task=Db.findFirst(sql);
			
			if(task!=null&&task.getStr("thedate")!=null&&task.getStr("thedate").length()>0)
			{
				 
				
				String liucs[]=TokenUtil.getStrngArray(task.getStr("liucs"), ",");
				int cpid=task.getInt("cpid");
				String channelstr=task.getStr("channel");
				String packname=task.getStr("packname");
				String chs[]=TokenUtil.getStrngArray(channelstr, ",");
				//System.out.println(channelstr);
 	        	for(int c=0;c<chs.length;c++)
 	        	{
 	        	long ts=1000*60*60*24L;	
 	        	long nt=TimeUtil.GetSqlMilsecTime(task.getStr("thedate")+" 23:59:59"); 
				for(int t=0;t<liucs.length;t++)
				{

					String thedate=TimeUtil.GetSqlDate(nt).substring(0,10);	
					double liuc=Double.parseDouble(liucs[t]);
					String sql_cp_log="select *  from cp_new_log where "
								+ "adddate>='"+thedate+"' and "
								+ "adddate<='"+thedate+" 24' and "
								+ "cpid='"+cpid+"' and "
								+ "channel='"+chs[c]+"' and "
								+ "packname='"+packname+"'";
					//System.out.println(sql_cp_log);
					List<Record> cplist =Db.find(sql_cp_log);
					if(cplist!=null&&cplist.size()>0)
					{
						Collections.shuffle(cplist);	
						int cpsz=cplist.size();
						int size=(int)(cpsz*1D*liuc/100D);
			 	        for(int i=0;i<size;i++)
			 	        {
			 	        	Record info=cplist.get(i);
			 	        	
				 	        	Record saveinfo=new Record();
				 	        	saveinfo.set("cpid", info.get("cpid"));
				 	        	saveinfo.set("channel",info.get("channel"));
				 	        	saveinfo.set("packname", info.get("packname"));
				 	        	saveinfo.set("phone", info.get("phone"));
				 	        	saveinfo.set("pwd", info.get("pwd"));
				 	        	saveinfo.set("userdate", info.get("adddate"));
				 	        	saveinfo.set("deviceinfo", info.get("deviceinfo"));
				 	        	saveinfo.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				 	        	saveinfo.set("status", 0);
				 	        	saveinfo.set("city", info.get("city"));
				 	        	saveinfo.set("dnum", info.get("dnum"));
				 	        	saveinfo.set("dmodel", info.get("dmodel"));
				 	        	saveinfo.set("lastdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
								Db.save("cp_liuc_log", saveinfo);
			 	        	 
																
			 	        }
					}
					nt=nt-ts;
				}
 	        	}
				task.set("status", "1");
				Db.update("cp_liuc_task", task);
	 	         
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			sleep(10000);
		}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String aa[]) throws Exception
	{
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
