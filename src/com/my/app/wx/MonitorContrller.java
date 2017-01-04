package com.my.app.wx; 

 

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.WxApiController;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Agent;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN") 
public class MonitorContrller  extends Controller{
	//请求根目录
		private String root="/wx/";
		
		private String m_name="monitor";
		//当前请求路径
		private String path="monitor/"+m_name;
		//表名
		private  String tableName="vps_status";
 
 
		/***
		 * 主入口(列表)
		 * @throws ParseException 
		 */
		public void index() throws ParseException {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=20;  
			String and="";
//			int status=getPara("status")!=null?getParaToInt("status"):-1;
//			if(status>-1)
//			{
//				and+=" and status="+status;
//			}
		  
			Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by status desc ,reqdate ");			   
			
			List<Record> list=page.getList();
			List<Record> list2=new ArrayList<Record>();
			for(int i=0;i<list.size();i++)
			{
				Record r=list.get(i);
				long thetime=System.currentTimeMillis();
				long reqtime=TimeUtil.GetSqlMilsecTime(r.getStr("reqdate"));
				long st=(thetime-reqtime)/60000;
				r.set("st", st);
				list2.add(r);
				
			}
			setAttr("list", list2);
			setAttr("psize", page.getTotalPage());
			setAttr("pageNum", pageNum);
			setAttr("count", page.getTotalRow()); 
			setAttr("pageSize", pageSize);
			initAttr();
			renderJsp(root+path+".jsp");		
		}
		/***
		 * 主入口(列表)
		 * @throws ParseException 
		 */
		public void count() throws ParseException {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=500;  
			String and=""; 
			int serverid=getPara("serverid")!=null&&getPara("serverid").length()>0?getParaToInt("serverid"):-1;
			if(serverid>-1)
			{
				and+=" and serverid="+serverid;
			}
			long thetime=System.currentTimeMillis();
			String nowdate=TimeUtil.GetSqlDate(thetime).substring(0,10);
			String startdate=getPara("startdate")!=null?getPara("startdate"):"";
			if(startdate.length()>0)
			{
				nowdate=startdate;
			} 
	        Cache userCache= Redis.use("userc");
	        Jedis jedis = userCache.getJedis();
			
		  
			Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by status desc ,serverid ");			   			
			List<Record> list=page.getList();
			List<Record> list2=new ArrayList<Record>();
			for(int i=0;i<list.size();i++)
			{
				Record r=list.get(i);
				String key="t_r_"+r.getStr("serverid")+"_"+r.getStr("vpsid")+"_"+nowdate;
				String key1="t_f_"+r.getStr("serverid")+"_"+r.getStr("vpsid")+"_"+nowdate;
				int cnt=jedis.get(key)!=null?Integer.parseInt(jedis.get(key)):0;
				int cnt1=jedis.get(key1)!=null?Integer.parseInt(jedis.get(key1)):0;
				r.set("st", cnt);
				r.set("st1", cnt1);
				list2.add(r);				
			}
			jedis.close();
			setAttr("list", list2);
			setAttr("psize", page.getTotalPage());
			setAttr("pageNum", pageNum);
			setAttr("count", page.getTotalRow()); 
			setAttr("pageSize", pageSize);
			setAttr("nowdate", nowdate);
			initAttr();
			setAttr("m_name", "monitor/count");
			renderJsp(root+path+"-count.jsp");			
		}		
		public void account() throws ParseException {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=20;  
			String and="";
			int status=getPara("status")!=null&&getPara("status").trim().length()>0?getParaToInt("status"):-1;
			if(status!=-1)
			{
				and+=" and status="+status;
			}
			int serverid=getPara("serverid")!=null&&getPara("serverid").trim().length()>0?getParaToInt("serverid"):-1;
			String serveridgroup="";
			if(serverid>0)
			{
				and+=" and serverid="+serverid;
				
			} else if(serverid==0)
			{
				serveridgroup="serverid,";
			}
			String startdate=getPara("startdate")!=null?getPara("startdate"):"";
			if(startdate.length()>0)
			{
				and+=" and adddate>='"+startdate+"'";
			}
			String enddate=getPara("enddate")!=null?getPara("enddate"):"";
			if(enddate.length()>0)
			{
				and+=" and adddate<='"+enddate+" 24'";
			}
		    String tb="( select ad,"+serveridgroup+"status,count(*) cnt from (select left(adddate,10)  ad,"+serveridgroup+"status from account_status where id>0 "+and+"    ) aaa group by ad,"+serveridgroup+"status order by ad desc ,cnt desc ) ggg";
			Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from "+tb+"    ");			   
			
		 
			setAttr("list", page.getList());
			setAttr("psize", page.getTotalPage());
			setAttr("pageNum", pageNum);
			setAttr("count", page.getTotalRow()); 
			setAttr("pageSize", pageSize);
			setAttr("m_name", "monitor/account");
			renderJsp(root+path+"-account.jsp");		
		}
		public void nonaccount() throws ParseException {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=20;  
			String and="";
			int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):-1;
			if(status!=-1)
			{
				and+=" and status="+status;
			}else
			{
				and=" and status=-106";
			}
			String startdate=getPara("startdate")!=null?getPara("startdate"):"";
			if(startdate.length()>0)
			{
				and+=" and adddate>='"+startdate+"'";
			}
			String enddate=getPara("enddate")!=null?getPara("enddate"):"";
			if(enddate.length()>0)
			{
				and+=" and adddate<='"+enddate+" 24'";
			}
			int serverid=getPara("serverid")!=null&&getPara("serverid").trim().length()>0?getParaToInt("serverid"):-1;
			if(serverid>0)
			{
				and+=" and serverid="+serverid;
				
			}
			Cache userCache= Redis.use("userc");
		    Jedis jedis = userCache.getJedis();
			Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from account_status where id>0 "+and+" order by id desc ");			   
			
			List<Record> list=page.getList();
			List<Record> list2=new ArrayList<Record>();
			for(int i=0;i<list.size();i++)
			{
				Record r=list.get(i);
				String key_read_day_cnt="day_"+r.get("userid")+"_read_"+(r.getStr("adddate").substring(0,10));
				int read_day_cnt=jedis.get(key_read_day_cnt)!=null?Integer.parseInt(jedis.get(key_read_day_cnt)):0;
				r.set("readcnt", read_day_cnt);
				list2.add(r);
				
			}
			jedis.close();
			setAttr("list", list2);
			setAttr("psize", page.getTotalPage());
			setAttr("pageNum", pageNum);
			setAttr("count", page.getTotalRow()); 
			setAttr("pageSize", pageSize);
			setAttr("m_name", "monitor/nonaccount");
			renderJsp(root+path+"-nonaccount.jsp");		
		}	
		public void accountlist() throws ParseException {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=100;  
			String and=" and (status<>-106 and status<>-105 and status<>-205 and status<>-3)";
			 
			String startdate=getPara("startdate")!=null&&getPara("startdate").length()>0?getPara("startdate"):TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
			 
			int serverid=getPara("serverid")!=null&&getPara("serverid").trim().length()>0?getParaToInt("serverid"):-1;
			if(serverid>0)
			{
				and+=" and account_group_id="+serverid;
				
			}
			Cache userCache= Redis.use("userc");
		    Jedis jedis = userCache.getJedis();
			Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from account where id>0 "+and+" order by last_use_time desc ");			   
			
			List<Record> list=page.getList();
			List<Record> list2=new ArrayList<Record>();
			for(int i=0;i<list.size();i++)
			{
				Record r=list.get(i);
				String key_read_day_cnt="day_"+r.get("id")+"_read_"+startdate;
				int read_day_cnt=jedis.get(key_read_day_cnt)!=null?Integer.parseInt(jedis.get(key_read_day_cnt)):0;
				r.set("readcnt", read_day_cnt);
				list2.add(r);
				
			}
			jedis.close();
			setAttr("list", list2);
			setAttr("startdate", startdate);
			setAttr("psize", page.getTotalPage());
			setAttr("pageNum", pageNum);
			setAttr("count", page.getTotalRow()); 
			setAttr("pageSize", pageSize);
			setAttr("m_name", "monitor/accountlist");
			renderJsp(root+path+"-accountlist.jsp");		
		}		
		public void initAttr()
		{
			setAttr("m_name", m_name);
		}
}