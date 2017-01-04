package com.my.app.wx; 

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.WxApiController;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPayLog;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication  
public class FollowTaskContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="followtask";
	//当前请求路径
	private String path="task/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(FollowTask.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"public_account"};
 
 
	private boolean hasMoney(int cnt,int user_id,int group_id)
	{
		if(group_id==1)
		{
			return true;
		}else
		{
		  
		  Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+user_id);
		  
		  String fsql="  select sum(money) from( select  (total_quantity*unit_price) money   from follow_task where user_id="+user_id+" and settle<>1 and order_time>='2016-11-01') aa";
		  //BigDecimal fb=Db.queryBigDecimal(fsql);
//		  float f_cnt=0;
		  double fmoney=0;
		  try{
			  fmoney=Db.queryDouble(fsql);
		  }catch(Exception e)
		  {
			  fmoney=0;
		  }
		  fmoney=fmoney+(cnt*channel.getFloat("followprice"));
		  String rsql ="  select sum(money) from( "
		  		+ "select  (total_quantity*unit_price) money "
		  		+ "from read_task where user_id="+user_id+"  and settle<>1 and order_time>='2016-11-01' ) aa";
		  //String rsql="select  sum(total_quantity) cnt from read_task where   settle<>1  and user_id="+user_id;
		  double rmoney=0;
		  try{
			  rmoney=Db.queryDouble(rsql);
		  }catch(Exception e)
		  {
			  rmoney=0;
		  }
		  //rmoney=rmoney;//r_cnt*channel.getFloat("readprice")+(cnt*channel.getFloat("readprice"));
		  double allmoney=fmoney+rmoney;
		  UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+user_id);
		  double account_money=(userMoney.getFloat("balance")+userMoney.getFloat("borrow")+userMoney.getFloat("give"))-allmoney;
		  if(account_money<0)
		  {
			  return false;
		  }else
		  {
			  return true;
		  } 
		}
		  
	}	
	/***
	 * 主入口(列表)
	 */ 
	public void index() {
		Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();   
	    String follow_order_status="1";
	    if(jedis.exists("follow_order_status"))
	    {
	    	follow_order_status=jedis.get("follow_order_status");
	    }
	    setAttr("follow_order_status", follow_order_status);
	    jedis.close();
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;  
		String and="";
		int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):0;
		System.out.println("statusstatusstatusstatusstatus:"+status);
		and+=" and status="+status;
	 
		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			and+=" and (";
			for(int i=0;i<keywordArray.length;i++)
			{
				if(i==0)
				{
				  and+=keywordArray[i]+" like '%"+getPara("keyword")+"%'";
				}else
				{
	              and+=" or "+keywordArray[i]+" like '%"+getPara("keyword")+"%'";
				}
			}
			and+=" )";
		}
		String startdate=getPara("startdate")!=null?getPara("startdate"):"";
		if(startdate.length()>0)
		{
			and+=" and order_time>='"+startdate+"'";
		}
		String enddate=getPara("enddate")!=null?getPara("enddate"):"";
		if(enddate.length()>0)
		{
			and+=" and order_time<='"+enddate+" 24'";
		}
		String channel_sql="select * from biz_channel where aid>0";
		Subject subject = SecurityUtils.getSubject();
		Channel agent=getSessionAttr("agent");
		User user=getSessionAttr("dbuser");
		long userid=user.getInt("id");	
		if(agent!=null)
		{
			List<Channel> clist=agent.getChannels();
			setAttr("clist", clist);
		}else if(subject.hasRole("R_ADMIN"))
		{
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):-1;
			channel_sql="select * from biz_channel where aid>0";
			List<Channel> clist=Channel.dao.find(channel_sql);
			setAttr("clist", clist);
		}
				 
		if(userid>0)
		{
			and+=" and user_id='"+userid+"'";
		}
		String tb="(select a.*,b.full_name from "+tableName+" a left join sec_user b on a.user_id=b.id) aa ";
		Page<FollowTask> page = FollowTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+" where id>0 "+and+" order by id desc");
 
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		setAttr("pageSize",pageSize);
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	/***
	 * 编辑界面
	 */
	public void input() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		FollowTask followTask=FollowTask.dao.findById(id);
		setAttr("bean", followTask);
		Subject subject = SecurityUtils.getSubject(); 
		User user=getSessionAttr("dbuser");
		long userid=user.getInt("id");
		long cnt=Db.queryLong("select count(*) from "+WxApiController.account_table);
		if(subject.hasRole("R_ADMIN"))
		{			
			setAttr("maxfollow", cnt);
		}else
		{
			Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
			if(channel!=null&&channel.getInt("maxfollow")!=null&&channel.getInt("maxfollow")>0)
			{
				setAttr("maxfollow", channel.getInt("maxfollow"));
			}else
			{
				setAttr("maxfollow", cnt);
			}
		}
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 编辑界面
	 */
	public void batchinput() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		ReadTask readTask=ReadTask.dao.findById(id);
		setAttr("bean", readTask);
		initAttr();
		renderJsp(root+"task/followbatch"+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");	
		Subject subject = SecurityUtils.getSubject();  
		long cnt=Db.queryLong("select count(*) from "+WxApiController.account_table);
		int max_day_follow=999999999;
		if(!subject.hasRole("R_ADMIN"))
		{	 
			Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
			if(channel!=null&&channel.getInt("maxfollow")!=null&&channel.getInt("maxfollow")>0)
			{
				cnt=channel.getInt("maxfollow");
				max_day_follow=channel.getInt("max_day_follow");
			}
			
		} 
		
		
		if(getPara("tasks")==null){
		FollowTask bean=getModel(FollowTask.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		FollowTask followTask=FollowTask.dao.findById(id);
		 
		String public_account=bean.getStr("public_account");
		if(followTask!=null)
		{
			public_account=followTask.getStr("public_account");
		}
		//去掉所有空格
		if(public_account.length()<3)
		{
		   renderText("");
		   return;
		}else
		{
			public_account=public_account.replaceAll(" ", "");
		}		
		boolean isokPublic_account=Db.findFirst("select * from follow_task where public_account='"+public_account+"' and status<1")==null;
		if(followTask!=null){

			String public_account_old=followTask.getStr("public_account");
			//已审核的不能修改具体内容
			if(followTask.getInt("status")<1||subject.hasRole("R_ADMIN"))
			{
				String f[]=bean._getAttrNames();
				for(int i=0;i<f.length;i++)
				{
					followTask.set(f[i], bean.get(f[i]));
				}
				if(bean.get("reply_content")==null)
				{
					followTask.set("reply_content", "");
				}
				if(followTask.getInt("user_id")==null)
				{
					followTask.set("user_id", userid);
				}
				 
			}
			//是否够现金
			boolean hasmoney=hasMoney(followTask.getInt("total_quantity"),userid,user.getInt("group_id"));
			//狗日的关注数>完成数
			boolean isfuckdo=followTask.getInt("finish_quantity")>followTask.getInt("total_quantity");
			
			if(!isfuckdo&&hasmoney&&(public_account_old.equals(public_account)||isokPublic_account))
			{
				followTask.update();
				renderJsp(root+"success.jsp");
			}else if(isfuckdo)
			{
				setAttr("msg", "请填写正确的关注数！");
				renderJsp(root+"msg.jsp");
			}else if(!hasmoney)
			{
				setAttr("msg", "账户余额不够支付现有的任务，请充值再下单！");
				renderJsp(root+"msg.jsp");
			}else
			{
				setAttr("msg", "该公众号已推广中，请填写其他公众号！");
				renderJsp(root+"msg.jsp");
			}
		}else
		{
			long day_cnt=0;
			BigDecimal big_day_cnt=Db.queryBigDecimal("  select sum(total_quantity) cnt from follow_task where status<>2 and user_id="+userid+" and order_time>='"+TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10)+"' ");
			if(big_day_cnt!=null)
			{
				day_cnt=Integer.parseInt(big_day_cnt.toString());
			}
			if((day_cnt+bean.getInt("total_quantity"))<=max_day_follow)
			{
				//st 单价
			    Cache userCache= Redis.use("userc");
			    Jedis jedis = userCache.getJedis();
			    float follow_price =0.02f;          
			    if(jedis.get("follow_price")!=null)
			    {
			    	follow_price=Float.parseFloat(jedis.get("follow_price"));
			    } 
			    jedis.close();
			    Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);		
				if(channel!=null&&channel.getFloat("followprice")!=null&&channel.getFloat("followprice")>0)
				{
					 follow_price=channel.getFloat("followprice");
				}
				bean.set("unit_price", follow_price); 
				//end 单价
				if(bean.get("status")==null)
				{
					bean.set("status", 0);
				}
				if(bean.get("level")==null)
				{
					bean.set("level", 0);
				}
				
				if(bean.get("reply_content")==null)
				{
					bean.set("reply_content", "");
				}
				bean.set("user_id", userid);
				bean.set("order_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				//是否够现金
				boolean hasmoney=hasMoney(bean.getInt("total_quantity"),userid,user.getInt("group_id"));
	            if(isokPublic_account&&hasmoney)
	            {
	            	bean.save();
	            	renderJsp(root+"success.jsp");
	            }else if(!hasmoney)
				{
					setAttr("msg", "账户余额够支付现有的任务，请充值再下单！");
					renderJsp(root+"msg.jsp");
				}else
				{
					setAttr("msg", "该公众号已推广中，请填写其他公众号！");
					renderJsp(root+"msg.jsp");
				}
			}else
			{
				setAttr("msg", "您的账号已被限量！");
				renderJsp(root+"msg.jsp");
			}
		}
		}else
		{
			String tr[]=TokenUtil.getStrngArray(getPara("tasks"), "\n");

			for(int i=0;i<tr.length;i++)
			{
				String td[]=TokenUtil.getStrngArray(tr[i].replaceAll("\\\\r", ""), " ");
				if(td.length==1)
				{
					td=TokenUtil.getStrngArray(tr[i], "\t");
				}

				String public_account=td[0];
				String total_quantity=replaceBlank(td[1]);
				if(Integer.parseInt(total_quantity)>cnt)
				{
					total_quantity=cnt+"";
				}
				boolean isokPublic_account=Db.findFirst("select * from follow_task where public_account='"+public_account+"' and status<1")==null;
				
				long day_cnt=0;
				BigDecimal big_day_cnt=Db.queryBigDecimal("  select sum(total_quantity) cnt from follow_task where status<>2 and user_id="+userid+" and order_time>='"+TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10)+"' ");
				if(big_day_cnt!=null)
				{
					day_cnt=Integer.parseInt(big_day_cnt.toString());
				}
				
				if(public_account.length()>0&&isokPublic_account)
				{
					if((day_cnt+Integer.parseInt(total_quantity))<max_day_follow)
				    {
					FollowTask bean=new FollowTask();
					//st 单价
				    Cache userCache= Redis.use("userc");
				    Jedis jedis = userCache.getJedis();
				    float follow_price =0.02f;          
				    if(jedis.get("follow_price")!=null)
				    {
				    	follow_price=Float.parseFloat(jedis.get("follow_price"));
				    } 
				    jedis.close();
				    Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);		
					if(channel!=null&&channel.getFloat("followprice")!=null&&channel.getFloat("followprice")>0)
					{
						 follow_price=channel.getFloat("followprice");
					}
					bean.set("unit_price", follow_price); 
					//end 单价
					bean.set("public_account", public_account);
					bean.set("total_quantity", total_quantity);
					bean.set("reply_content", "");
					bean.set("status", 0);
					bean.set("user_id", userid);
					bean.set("order_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					boolean hasmoney=hasMoney(Integer.parseInt(total_quantity),userid,user.getInt("group_id"));
					if(hasmoney){
						bean.save();
					 }
					}
						
				}
					
			}
			renderJsp(root+"success.jsp");
			}			
	}
	   public static String replaceBlank(String str) {
		   
           String dest = "";
    
           if (str!=null) {
 
               Pattern p = Pattern.compile("\\s*|\t|\r|\n");
   
               Matcher m = p.matcher(str);
   
               dest = m.replaceAll("");
  
           }
   
           return dest;
  
       }	
	/***
	 * 删除
	 */	
	public void del() {
		Subject subject = SecurityUtils.getSubject();		
		if(subject.hasRole("R_ADMIN"))
		{
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			FollowTask followTask=FollowTask.dao.findById(id);
			if(followTask.getInt("status")<0||followTask.getInt("finish_quantity")<1){
				FollowTask.dao.deleteById(id);
			}
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   FollowTask followTask=FollowTask.dao.findById(ids[i]);
			   if(followTask.getInt("status")<0||followTask.getInt("finish_quantity")<1){
				   FollowTask.dao.deleteById(ids[i]);
			   }
		   }
		}
		renderText("删除成功!");	
		}
		
	}
	/***
	 * 撤单
	 */	
	public void cancel() {
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");	
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			FollowTask followTask= FollowTask.dao.findById(id);
			if(userid==followTask.getInt("user_id")&&followTask.getInt("status")==0&&followTask.getInt("settle")!=1)
			{
			//Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);			
			UserPayLog userPayLog=new UserPayLog();
			userPayLog.set("userid", userid);
			userPayLog.set("type",1);
			userPayLog.set("rid", id);
			userPayLog.set("cnt", followTask.getInt("real_quantity"));
			userPayLog.set("price", followTask.getFloat("unit_price"));
			userPayLog.set("money",followTask.getInt("real_quantity") *1f*followTask.getFloat("unit_price"));
			userPayLog.set("status", 2);
			userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			userPayLog.save();
			followTask.set("status", 2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
			//扣钱
			UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+userid);
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
			}
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			    FollowTask followTask= FollowTask.dao.findById(Long.parseLong(ids[i]));
				if(userid==followTask.getInt("user_id")&&followTask.getInt("status")==0&&followTask.getInt("settle")!=1)
				{
				//Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);			
				UserPayLog userPayLog=new UserPayLog();
				userPayLog.set("userid", userid);
				userPayLog.set("type",1);
				userPayLog.set("rid", followTask.get("id"));
				userPayLog.set("cnt", followTask.getInt("real_quantity"));
				userPayLog.set("price",followTask.getFloat("unit_price"));
				userPayLog.set("money",followTask.getInt("real_quantity") *1f*followTask.getFloat("unit_price"));
				userPayLog.set("status", 2);
				userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				userPayLog.save();
				followTask.set("status", 2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
				//扣钱
				UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+userid);
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
				}
		   }
		}
		renderText("撤单成功!");		
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
}