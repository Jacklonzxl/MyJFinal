package com.my.app.wx; 

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
import com.my.app.wx.bean.ReadTask;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPayLog;
import com.my.util.CSVUtils;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication 
public class ReadTaskContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="readtask";
	//当前请求路径
	private String path="task/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(ReadTask.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"title","url"};

 
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
		  rmoney=rmoney+(cnt*channel.getFloat("readprice"));//r_cnt*channel.getFloat("readprice")+(cnt*channel.getFloat("readprice"));
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
	 * @throws Exception 
	 */ 
	
	public void index() throws Exception {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;
		boolean isCSV="1".equals(getPara(0));
		if(isCSV)
		{
			pageNum=1;
			pageSize=5000;
		}
		Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();   
	    String read_order_status="1";
	    if(jedis.exists("read_order_status"))
	    {
	    	read_order_status=jedis.get("read_order_status");
	    }
	    setAttr("read_order_status", read_order_status);
	    
	    jedis.close();
		String and="";
		int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):0;
		if(status!=-1)
		{
			
			if(status==4)
			{
				and+=" and status=3";
			}else
			{
				and+=" and status="+status;
			}
		}
		if(status==1||status==2||status==4)
		{
			tableName="read_task_settlement";
			if(status==4)
			{
				status=3;
			}
		}
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
		//代理用户
		if(agent!=null)
		{
			 
			List<Channel> clist=agent.getChannels();
			setAttr("clist", clist);
		}
		//管理员
		else if(subject.hasRole("R_ADMIN"))
		{
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):-1;
			channel_sql="select * from biz_channel where aid>0";
			List<Channel> clist=Channel.dao.find(channel_sql);
			setAttr("clist", clist);
		}
				
		 
		if(userid>0)
		{
			and+=" and user_id="+userid+"";
		}
	    String tb="(select a.*,b.full_name from "+tableName+" a left join sec_user b on a.user_id=b.id   ) aa ";
 
		Page<ReadTask> page = ReadTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+" where id>0 "+and+"  order by id desc");
		List<ReadTask> plist=page.getList();
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		setAttr("pageSize", pageSize); 
	    initAttr();
	    if(isCSV)
	    {
	    	String h[]={"标题","链接","计划阅读","实际阅读","下单日期","完成时间","状态","下单人"};
	    	//List<String[]> list=new ArrayList<String[]>();
	    	List<List<String>> list=new ArrayList<List<String>>();
	    	List<String> clist=new ArrayList<String>();
    		for(int j=0;j<h.length;j++)
    		{
    			clist.add(h[j]);
    		}
    		list.add(clist);
	    	for(int i=0;i<plist.size();i++)
	    	{
	    		ReadTask readTask=plist.get(i);
	    		String[] txt=new String[8];
	    		txt[0]=  readTask.get("title")!=null?readTask.get("title").toString().replaceAll(",", "，").replaceAll("&nbsp;", " "):"[无标题]";
	    		txt[1]=readTask.get("url").toString();
	    		txt[2]=readTask.get("total_quantity").toString();
	    		txt[3]=readTask.get("push_quantity").toString();
	    		txt[4]=readTask.get("order_time").toString();
	    		txt[5]=readTask.get("finish_time")!=null?readTask.get("finish_time").toString():"";
	    		String ststr="";
	    		if(readTask.getInt("status")==0)
	    		{
	    			ststr="进行中";
	    		}else if(readTask.getInt("status")==1)
	    		{
	    			ststr="已完成";
	    		}else if(readTask.getInt("status")==2)
	    		{
	    			ststr="已撤单";
	    		}
	    		txt[6]=ststr;
	    		txt[7]=readTask.get("full_name").toString();
	    		clist=new ArrayList<String>();
	    		for(int j=0;j<txt.length;j++)
	    		{
	    			clist.add(txt[j]);
	    		}
	    		list.add(clist);
	    	}
	    	//CSVUtils.csv("/www/wx/wx/upload/"+user.get("id")+".csv", h, list);
	    	//getResponse().setContentType("application/csv;charset=UTF-8");
	    	CSVUtils.writeCsv(list, "/www/wx/wx/upload/"+user.get("id")+".csv");
	    	renderFile(new File("/www/wx/wx/upload/"+user.get("id")+".csv"));
	    	//renderText("ok");	
	    }else
	    {
	    	renderJsp(root+path+".jsp");	
	    }
			
	}
	
	/***
	 * 主入口(列表)
	 */ 
	
	public void list() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=1000;

		String and="";
		int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):-1;
		if(status>-1)
		{
			and+=" and status="+status;
		}
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
		//代理用户
		if(agent!=null)
		{
			 
			List<Channel> clist=agent.getChannels();
			setAttr("clist", clist);
		}
		//管理员
		else if(subject.hasRole("R_ADMIN"))
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
		Page<ReadTask> page = ReadTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+" where id>0 "+and+" order by id desc");
		
		for(int i=0;i<page.getList().size();i++)
		{
			ReadTask bean=page.getList().get(i);
			String url=bean.getStr("url");
			String url_str;
			try {
				url_str = HttpUtil.doGet(url, "utf-8", null);
				String title=url_str.substring(url_str.indexOf("<title>")+7, url_str.indexOf("</title>"));
				String update="update read_task set title='"+title+"' where id="+bean.get("id");
				Db.update(update);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		renderText("ok");	
	}
	/***
	 * 编辑界面
	 */
	public void input() {
		
		int fr_0=0;
		int fr_1=0;
		int fr_2=0;
		int fr_3=0;
		int fr_4=0;
		
        //int hh=getHH();
	    Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();   
	    String tj_title="";
	    if(jedis.exists("tj_title"))
	    {
	    	tj_title=(jedis.get("tj_title"));
	    }

	    setAttr("tip", tj_title);
		int id=getPara("id")!=null?getParaToInt("id"):0;
		ReadTask readTask=ReadTask.dao.findById(id);
		setAttr("bean", readTask);
		Subject subject = SecurityUtils.getSubject(); 
		User user=getSessionAttr("dbuser");
		long userid=user.getInt("id");
		long cnt=Db.queryLong("select count(*) from "+WxApiController.account_table);
		if(subject.hasRole("R_ADMIN"))
		{			
			setAttr("maxread", cnt);
		}else
		{
			Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
			if(channel!=null&&channel.getInt("maxread")!=null&&channel.getInt("maxread")>0)
			{
				setAttr("maxread", channel.getInt("maxread"));
			}else
			{
				setAttr("maxread", cnt);
			}
		}
	    if(jedis.exists("fr_0_"+id))
	    {
	    	fr_0=Integer.parseInt(jedis.get("fr_0_"+id));
	    }
	    if(jedis.exists("fr_1_"+id))
	    {
	    	fr_1=Integer.parseInt(jedis.get("fr_1_"+id));
	    }
	    
	    if(jedis.exists("fr_2_"+id))
	    {
	    	fr_2=Integer.parseInt(jedis.get("fr_2_"+id));
	    }
	    if(jedis.exists("fr_3_"+id))
	    {
	    	fr_3=Integer.parseInt(jedis.get("fr_3_"+id));
	    }
	    if(jedis.exists("fr_4_"+id))
	    {
	    	fr_4=Integer.parseInt(jedis.get("fr_4_"+id));
	    }
	   
	    setAttr("fr_0", fr_0);
	    setAttr("fr_1", fr_1);
	    setAttr("fr_2", fr_2);
	    setAttr("fr_3", fr_3);
	    setAttr("fr_4", fr_4);
		jedis.close();
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 编辑界面
	 */
	public void batchinput() {
	        //int hh=getHH();
		    Cache userCache= Redis.use("userc");
		    Jedis jedis = userCache.getJedis();   
 
		    String tj_title="";
  
		    if(jedis.exists("tj_title"))
		    {
		    	tj_title=(jedis.get("tj_title"));
		    }
		    jedis.close();
		    setAttr("tip", tj_title);
		int id=getPara("id")!=null?getParaToInt("id"):0;
		ReadTask readTask=ReadTask.dao.findById(id);
		setAttr("bean", readTask);
		initAttr();
		renderJsp(root+"task/readbatch"+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 * @throws Exception 
	 */
	public void save() throws Exception {
		Cache userCache= Redis.use("userc");
		Jedis jedis = userCache.getJedis(); 
		try{
			int fr_0=0;
			int fr_1=0;
			int fr_2=0;
			int fr_3=0;
			int fr_4=0;
			if(getPara("fr_0")!=null&&getPara("fr_0").length()>0)
			{
				fr_0=Integer.parseInt(getPara("fr_0"));
			}
			if(getPara("fr_1")!=null&&getPara("fr_1").length()>0)
			{
				fr_1=Integer.parseInt(getPara("fr_1"));
			}
			if(getPara("fr_2")!=null&&getPara("fr_2").length()>0)
			{
				fr_2=Integer.parseInt(getPara("fr_2"));
			}
			if(getPara("fr_3")!=null&&getPara("fr_3").length()>0)
			{
				fr_3=Integer.parseInt(getPara("fr_3"));
			}
			if(getPara("fr_4")!=null&&getPara("fr_4").length()>0)
			{
				fr_4=Integer.parseInt(getPara("fr_4"));
			}			
		    int hh=getHH();
  
			int tj_st=-1;
			int tj_et=-1;
			float tj_price=0f;
			if(jedis.exists("tj_st"))
			{
			    tj_st=Integer.parseInt(jedis.get("tj_st"));
		    }
			if(jedis.exists("tj_et"))
			{
			    	tj_et=Integer.parseInt(jedis.get("tj_et"));
		    }
			if(jedis.exists("tj_price"))
			{
			    	tj_price=Float.parseFloat(jedis.get("tj_price"));
		    }
	 
			boolean istj=false;
			if(tj_st>-1&&tj_et>-1)
		    {
			    	if(tj_et>tj_st)
			    	{
			    		if(hh>=tj_st&&hh<=tj_et)
				    	  {
				    		  istj=true;
				    	  }
			    		
			    	}else
			    	{
			    		if(hh>=tj_st||hh<=tj_et)
				    	{
				    		  istj=true;
				    	}
			    		
			    	}
			    	  
		 }
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");
		Subject subject = SecurityUtils.getSubject();  
		long cnt=Db.queryLong("select count(*) from "+WxApiController.account_table);
		int max_day_read=999999999;
		if(!subject.hasRole("R_ADMIN"))
		{	 
			Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
			if(channel!=null&&channel.getInt("maxread")!=null&&channel.getInt("maxread")>0)
			{
				cnt=channel.getInt("maxread");
				max_day_read=channel.getInt("max_day_read");
			}		 
		}
		if(getPara("tasks")==null){
		ReadTask bean=getModel(ReadTask.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		ReadTask readTask=ReadTask.dao.findById(id);
		//取网址信息
		if(readTask==null)
		{
			///单价：重中之中
		    
		    float read_price =0.004f;          
		    if(jedis.get("read_price")!=null)
		    {
		    	read_price=Float.parseFloat(jedis.get("read_price"));
		    } 
		    //jedis.close();
		    Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);	
			if(channel!=null&&channel.getFloat("readprice")!=null&&channel.getFloat("readprice")>0)
			{
				 read_price=channel.getFloat("readprice");
			}
			if(istj)
			{
				read_price=read_price-tj_price;
			}
			bean.set("unit_price", read_price);
			///end 单价
			String url=bean.getStr("url");
			String url_str=HttpUtil.doGet(url, "utf-8", null);
			String title=url_str.substring(url_str.indexOf("<title>")+7, url_str.indexOf("</title>"));
			//title=new String(title.getBytes(), "gbk");
			title=filter(title);
			//System.out.println(title);
			//title=title.replaceAll("\\\\","\\\\\\\\"); 
			if(title==null||title.length()==0)
			{
				jedis.close();
				renderText("");				
				return;
			}
			//去掉所有空格
			if(url.length()<20||url.startsWith("http")==false)
			{
			   jedis.close();
			   renderText("");
			   return;
			}else
			{
				url=url.replaceAll(" ", "");
			}
			if(url.indexOf("sn")==-1||url.indexOf("weixin")==-1)
			{
				//bean.set("start_quantity", 1);
			}
			if(bean.get("title")==null||bean.getStr("title").length()==0)
			{
				bean.set("title", title);
			}			
		}
		if(readTask!=null){  
			if(readTask.getInt("status")<1||subject.hasRole("R_ADMIN"))
			{
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				readTask.set(f[i], bean.get(f[i]));
				
			}
			if(readTask.getInt("praise_quantity")==null)
			{
				readTask.set("praise_quantity", 0);
			}
			if(readTask.getInt("user_id")==null)
			{
				readTask.set("user_id", userid);
			}
			}
			//是否够现金
			boolean hasmoney=hasMoney(readTask.getInt("total_quantity"),userid,user.getInt("group_id"));
			//狗日的关注数>完成数
			boolean isfuckdo=cnt<readTask.getInt("total_quantity")&&readTask.getInt("push_quantity")>readTask.getInt("total_quantity")&&!subject.hasRole("R_ADMIN");
			if(isfuckdo)
			{
				setAttr("msg", "请填写正确的阅读数！");
				renderJsp(root+"msg.jsp");
			}else if(!hasmoney)
			{
				setAttr("msg", "账户余额不够支付现有的任务，请充值再下单！");
				renderJsp(root+"msg.jsp");
			}else{
				int total_quantity=Integer.parseInt(readTask.get("total_quantity").toString());
				int push_quantity=Integer.parseInt(readTask.get("push_quantity").toString());
				if(total_quantity<push_quantity&&!subject.hasRole("R_ADMIN"))
				{
					setAttr("msg", "阅读数不能小于已完成数");
					renderJsp(root+"msg.jsp");
				}else
				{
					
					readTask.update(); 
				    jedis.set("fr_0_"+readTask.get("id"), fr_0+"");
				    jedis.set("fr_1_"+readTask.get("id"), fr_1+"");
				    jedis.set("fr_2_"+readTask.get("id"), fr_2+"");
				    jedis.set("fr_3_"+readTask.get("id"), fr_3+"");
				    jedis.set("fr_4_"+readTask.get("id"), fr_4+"");
				    //jedis.close();
					renderJsp(root+"success.jsp");						
				}

			}

		}else
		{
			long day_cnt=0;
			BigDecimal big_day_cnt=Db.queryBigDecimal("  select sum(total_quantity) cnt from read_task where status<>2 and user_id="+userid+" and order_time>='"+TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10)+"' ");
			if(big_day_cnt!=null)
			{
				day_cnt=Integer.parseInt(big_day_cnt.toString());
			}
			if((day_cnt+bean.getInt("total_quantity"))<=max_day_read)
			{
				if(bean.get("status")==null)
				{
					bean.set("status", 0);
				}
				if(bean.get("level")==null)
				{
					bean.set("level", 0);
				}
				if(bean.getInt("praise_quantity")==null)
				{
					bean.set("praise_quantity", 0);
				}
				//是否够现金
				boolean hasmoney=hasMoney(bean.getInt("total_quantity"),userid,user.getInt("group_id"));
				String wxurl=bean.getStr("url");
				String str[]=TokenUtil.getStrngArray(wxurl.substring(wxurl.indexOf("sn")+3,wxurl.length()),"&");
				String sn=str[0];
				ReadTask snReadTask=ReadTask.dao.findFirst("select * from read_task where url='"+bean.getStr("url")+"' order by id desc limit 1 ");
				int donecnt=0;
				boolean isPushing=false;
				if(snReadTask!=null)
				{
//					donecnt=snReadTask.getInt("push_quantity");
//					if(snReadTask.getInt("status")<1)
//					{
//						isPushing=true;
//					}
					isPushing=true;
					
				}
				if(bean.getStr("url").indexOf("sn")==-1)
				{
					UUID uuid = UUID.randomUUID();  
			         
					sn=uuid.toString(); 
				}
				if((!wxurl.startsWith("http://mp.weixin.qq.com")))//if(bean.getStr("url").indexOf("sn")==-1||(!wxurl.startsWith("http://mp.weixin.qq.com/s?__biz=")))
				{
					jedis.close();
					setAttr("msg", "本平台暂不支持该类型的链接，请填写微信文章原始链接！");
					renderJsp(root+"msg.jsp");	
					return ;
				}else if((bean.getInt("total_quantity")+donecnt)>cnt)
				{			
					setAttr("msg", "同一链接单次或多次下单阅读总数不能超过"+cnt+"！");
					renderJsp(root+"msg.jsp");	
				}else if(hasmoney&&isPushing==false)
				{
					bean.set("sn", sn);
					bean.set("user_id", userid);
					bean.set("order_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					bean.save();
				    jedis.set("fr_0_"+bean.get("id"), fr_0+"");
				    jedis.set("fr_1_"+bean.get("id"), fr_1+"");
				    jedis.set("fr_2_"+bean.get("id"), fr_2+"");
				    jedis.set("fr_3_"+bean.get("id"), fr_3+"");
				    jedis.set("fr_4_"+bean.get("id"), fr_4+"");
				    //jedis.close();
					renderJsp(root+"success.jsp");	
				}else if(!hasmoney)
				{			
					setAttr("msg", "账户余额不够支付现有的任务，请充值再下单！");
					renderJsp(root+"msg.jsp");	
				}else
				{
					setAttr("msg", "该链接已经被推广过，不能重复添加！");
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
			int row=0; 
			String error="";
			for(int i=0;i<tr.length;i++)
			{
				row++;
				try{
					String td[]=TokenUtil.getStrngArray(tr[i].replaceAll("\\\\r", ""), " ");
					if(td.length==1)
					{
						td=TokenUtil.getStrngArray(tr[i], "\t");
					}
	
					String url=td[0];
					int total_quantity=0;
					int praise_quantity=0;//(total_quantity/1000)*8;
					if(td.length==2)
					{
					   String tc[]=TokenUtil.getStrngArray(td[1], "\t");
					   if(tc.length==2)
					   {
						   total_quantity=Integer.parseInt(replaceBlank(tc[0]));
						   praise_quantity=Integer.parseInt(replaceBlank(tc[1]));
					   }else
					   {
						   total_quantity=Integer.parseInt(replaceBlank(tc[0]));
						   praise_quantity=(total_quantity/1000)*8;
					   }
						
					}
					total_quantity=total_quantity==0?Integer.parseInt(replaceBlank(td[1])):total_quantity;
					if(total_quantity>cnt)
					{
						total_quantity=Integer.parseInt(String.valueOf(cnt));
					}
					if(praise_quantity==0){
						
						if(td.length>2&&td[2]!=null&&td[2].length()>0)
						{
							praise_quantity=Integer.parseInt(replaceBlank(td[2]));
						}else
						{
							praise_quantity=(total_quantity/1000)*8;
						}
				    }
					 
					if(praise_quantity>cnt)
					{
						praise_quantity=Integer.parseInt(String.valueOf(cnt));
					}
					
					String str[]=TokenUtil.getStrngArray(url.substring(url.indexOf("sn")+3,url.length()),"&");
					String sn=str[0];
					if(url.indexOf("sn")==-1)
					{
						UUID uuid = UUID.randomUUID();  
				         
						sn=uuid.toString(); 
					}
					ReadTask snReadTask=ReadTask.dao.findFirst("select * from read_task where url='"+url+"'  order by id desc limit 1  ");
					int donecnt=0;
					boolean isPushing=false;
					if(snReadTask!=null)
					{
//						donecnt=snReadTask.getInt("push_quantity");
//						if(snReadTask.getInt("status")<1)
//						{
//							isPushing=true;
//						}
						isPushing=true;
					}
					
					boolean isoksn=isPushing==false;
					long day_cnt=0;
					BigDecimal big_day_cnt=Db.queryBigDecimal("  select sum(total_quantity) cnt from read_task where status<>2 and user_id="+userid+" and order_time>='"+TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10)+"' ");
					if(big_day_cnt!=null)
					{
						day_cnt=Integer.parseInt(big_day_cnt.toString());
					}
					if(url.length()>0&&isoksn&&(day_cnt+total_quantity)<max_day_read)
					{
						try{
						ReadTask bean=new ReadTask();
						///单价：重中之中       
					    float read_price =0.004f;          
					    if(jedis.get("read_price")!=null)
					    {
					    	read_price=Float.parseFloat(jedis.get("read_price"));
					    } 
					    Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);	
						if(channel!=null&&channel.getFloat("readprice")!=null&&channel.getFloat("readprice")>0)
						{
							 read_price=channel.getFloat("readprice");
						}
						if(istj)
						{
							read_price=read_price-tj_price;
						}
						bean.set("unit_price", read_price);
						//end 单价
						bean.set("url", url);					
						String url_str=HttpUtil.doGet(url, "utf-8", null);
						String title=url_str.substring(url_str.indexOf("<title>")+7, url_str.indexOf("</title>"));
						title=filter(title);
						bean.set("title", title);											
						bean.set("sn", sn);
						bean.set("total_quantity", total_quantity);
						bean.set("praise_quantity", praise_quantity);
						bean.set("status", 0);
						bean.set("user_id", userid);
						bean.set("order_time", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						boolean hasmoney=hasMoney(total_quantity,userid,user.getInt("group_id"));
						String wxurl=bean.getStr("url");
						boolean isokurl=(!wxurl.startsWith("http://mp.weixin.qq.com")); //wxurl.indexOf("sn")==-1||
						if(hasmoney&&((Integer.parseInt(bean.get("total_quantity").toString())+donecnt)<=cnt)&&!isokurl)
						{
							bean.save();
						}else
						{
							error+="\n第"+row+"行不请允许导入(金额不足或单链超量或非正常链接)";
						}
						}catch(Exception e){
							e.printStackTrace();
							error+="\n第"+row+"行发生错误";
						}
							
					}else
					{
						error+="\n第"+row+"行导入失败";
					}
			    }catch(Exception e){
				  error+="\n第"+row+"行发生错误";
			    }
		   }
			//renderJsp(root+"success.jsp");
			setAttr("msg", "导入任务完成\n"+error);
			renderJsp(root+"msg.jsp");
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			setAttr("msg", "编辑任务失败:"+e.toString());
			renderJsp(root+"msg.jsp");
		}
		jedis.close();
			
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
			ReadTask readTask=ReadTask.dao.findById(id);
			if(readTask.getInt("status")<0){
				ReadTask.dao.deleteById(id);
			}
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   ReadTask readTask=ReadTask.dao.findById(ids[i]);
			   if(readTask.getInt("status")<0){
				   ReadTask.dao.deleteById(ids[i]);
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
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");
		if(id>0)
		{
			Subject subject = SecurityUtils.getSubject();
			ReadTask readTask= ReadTask.dao.findById(id);
			if(subject.hasRole("R_ADMIN"))
			{
				userid=readTask.getInt("user_id");
			}
			if(userid==readTask.getInt("user_id")&&(readTask.getInt("status")==0||readTask.getInt("status")==3)&&readTask.getInt("settle")!=1)
			{
				UserPayLog userPayLog_db=UserPayLog.dao.findFirst("select * from user_money_pay_list where type=2 and rid='"+readTask.get("id")+"'");
				if(userPayLog_db==null||userPayLog_db.get("id")==null)
				{
					float readprice=0.005f;
					if(readTask.get("unit_price")!=null)
					{
						readprice=readTask.getFloat("unit_price");
					}else
					{
						Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
						readprice=channel.getFloat("readprice");
					}
					int cnt_t=readTask.getInt("total_quantity");
					int cnt_f=readTask.getInt("finish_quantity");
					int cnt_p=readTask.getInt("push_quantity");
					int cnt=cnt_f>cnt_p?cnt_p:cnt_f;
					cnt=cnt>cnt_t?cnt_t:cnt;
					//日志
					UserPayLog userPayLog=new UserPayLog();
					userPayLog.set("userid", userid);
					userPayLog.set("type",2);
					userPayLog.set("rid", id);
					userPayLog.set("cnt", cnt);
					userPayLog.set("price", readprice);
					userPayLog.set("money",cnt *1f*readprice);
					userPayLog.set("status", 2);
					userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					userPayLog.save();
					readTask.set("status", readTask.getInt("status")==3?3:2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
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
				}else
				{
					readTask.set("status", readTask.getInt("status")==3?3:2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
				}
			}else
			{
				
				//readTask.set("status", readTask.getInt("status")==3?3:2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
			}
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			    ReadTask readTask= ReadTask.dao.findById(Long.parseLong(ids[i]));
				if(userid==readTask.getInt("user_id")&&(readTask.getInt("status")==0||readTask.getInt("status")==3)&&readTask.getInt("settle")!=1)
				{
					float readprice=0.005f;
					if(readTask.get("unit_price")!=null)
					{
						readprice=readTask.getFloat("unit_price");
					}else
					{
						Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);
						readprice=channel.getFloat("readprice");
					}	
					//Channel channel=Channel.dao.findFirst("select * from biz_channel where userid="+userid);	
					UserPayLog userPayLog_db=UserPayLog.dao.findFirst("select * from user_money_pay_list where type=2 and rid='"+readTask.get("id")+"'");
					if(userPayLog_db==null||userPayLog_db.get("id")==null)
					{
						int cnt_f=readTask.getInt("finish_quantity");
						int cnt_p=readTask.getInt("push_quantity");
						int cnt=cnt_f>cnt_p?cnt_p:cnt_f;
						
						UserPayLog userPayLog=new UserPayLog();
						userPayLog.set("userid", userid);
						userPayLog.set("type",2);
						userPayLog.set("rid", readTask.get("id"));
						userPayLog.set("cnt", cnt);
						userPayLog.set("price", readprice);
						userPayLog.set("money",cnt *1f*readprice);
						userPayLog.set("status", 2);
						userPayLog.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						userPayLog.save();
						readTask.set("status", 2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
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
					}else
					{
						readTask.set("status", 2).set("settle", 1).set("finish_time", TimeUtil.GetSqlDate(System.currentTimeMillis())).update();
					}
				}
		   }
		}
		renderText("撤单成功!");		
		
	}
	
	public void initAttr()
	{
		setAttr("m_name", m_name);
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
    
    public  String filter(String str) {  
        
        if(str.trim().isEmpty()){  
            return str;  
        }  
        String pattern="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";  
        String reStr="";  
        Pattern emoji=Pattern.compile(pattern);  
        Matcher  emojiMatcher=emoji.matcher(str);  
        str=emojiMatcher.replaceAll(reStr);  
        str=removeFourChar(str);
        return str;
    } 
    public static String removeFourChar(String content) {
        byte[] conbyte = content.getBytes();
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < 4; j++) {                          
                    conbyte[i+j]=0x30;                     
                }  
                i += 3;
            }
        }
        content = new String(conbyte);
        return content.replaceAll("0000", "");
    }
    public static void main(String aa[])
    {
    	UUID uuid = UUID.randomUUID();  
        String str = uuid.toString();  
        System.out.println(str);
    }
}