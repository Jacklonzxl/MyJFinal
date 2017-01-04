package com.my.app.wx; 

import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.WxApiController;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication  
public class UserCountContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="usercount";
	//当前请求路径
	private String path="count/"+m_name;
	//模糊查询
	private  String keywordArray[]={"value","name"};
 
 
	
	/***
	 * 主入口(列表)
	 */ 
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):0;
		if(status>-1)
		{
			and+=" and status="+status;
		}
		String read_table="read_task";
		if(status==1||status==2)
		{
			read_table="read_task_settlement";
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
		String tb="(   select cc.thedate,aa.id,aa.full_name,"
          +" bb.total_quantity ftcnt,bb.real_quantity frcnt,"
          +" cc.total_quantity rtcnt,cc.real_quantity rrcnt,cc.praise_quantity rpcnt,cc.real_praise rrpcnt"
          +" from "
	      +" ("
	      +" select thedate ,user_id,sum(total_quantity) total_quantity,sum(real_quantity) real_quantity from ("
	      +" select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,f.* from follow_task f where status>-1"+and 
	      +"  ) aa group by thedate,user_id"
	      +" ) bb "
	      +" right join  "
	      +" ("
	      +" select thedate ,user_id,sum(total_quantity) total_quantity,sum(push_quantity) real_quantity,sum(praise_quantity) praise_quantity,sum(push_praise) real_praise from ("
	      +" select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,r.* from "+read_table+" r where status>-1"+and 
	      +" )  aa group by thedate,user_id"
	      +" ) cc  on bb.user_id=cc.user_id and bb.thedate=cc.thedate"
	      +" left join "
	      +" sec_user aa  on cc.user_id=aa.id or bb.user_id=aa.id) aaa ";
		Page<FollowTask> page = FollowTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+"  order by thedate desc");
 
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	
	/***
	 * 主入口(列表)
	 */ 
	public void join() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):1;
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
		 
		Subject subject = SecurityUtils.getSubject();
		//Channel agent=getSessionAttr("agent");
		User user=getSessionAttr("dbuser");
		long userid=user.getInt("id");	
		if(subject.hasRole("R_JOIN"))
		{
			userid=user.getInt("id");	 
			
		}else if(subject.hasRole("R_ADMIN"))
		{
			
			String user_sql="select * from sec_user where group_id=4";
			List<User> clist=User.dao.find(user_sql);
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):clist.get(0).getInt("id");
			
			setAttr("clist", clist);
		} 
		user=User.dao.findById(userid);
		String jid=user.get("avatar_url").toString();
		String tb="( select aaa.thedate,aaa.push_cnt rcnt,bbb.push_cnt  fcnt  from( "
				  +" select thedate,sum(push_cnt) push_cnt from ( "
				  +" select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,a.* from `read_task_join_"+jid+"` a left join read_task  b on a.id=b.id  where status>-1"+and  
				  +" ) aa group by thedate "
				  +" ) aaa  "
				  +" left join  "
				  +" ( "
				  +"  select thedate,sum(push_cnt) push_cnt from ( "
				  +"  select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,a.* from `follow_task_join_"+jid+"` a left join follow_task  b on a.id=b.id where status>-1"+and  
				  +"  ) bb group by thedate "
				  +"  )  bbb  on aaa.thedate=bbb.thedate) aaaa ";
		Page<FollowTask> page = FollowTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+"  order by thedate desc");
		
		setAttr("user", user);
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		m_name="usercount/join";
		initAttr();
		renderJsp(root+path+"-join.jsp");		
	}	
	/***
	 * 主入口(列表)
	 */ 
	public void readlist() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=1000;  
		String and="";
		int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):1;
		if(status>-1)
		{
			and+=" and status="+status;
		}
		String read_table="read_task";
		if(status==1||status==2)
		{
			read_table="read_task_settlement";
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
		String thedate=getPara("thedate")!=null?getPara("thedate"):"";
		if(thedate.length()>0)
		{
			and+=" and order_time>='"+thedate+"' and order_time<='"+thedate+" 24'";
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
		String tb="(   select cc.status,cc.url,cc.title,cc.thedate,aa.id,aa.full_name,"
          
          +" cc.total_quantity rtcnt,cc.real_quantity rrcnt,cc.praise_quantity rpcnt,cc.real_praise rrpcnt"
          +" from "
	      +" ("
	      +" select status,url,title,thedate ,user_id,sum(total_quantity) total_quantity,sum(push_quantity) real_quantity,sum(praise_quantity) praise_quantity,sum(push_praise) real_praise from ("
	      +" select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,r.* from "+read_table+" r where status>-1"+and 
	      +" )  aa group by thedate,user_id,url,title,status"
	      +" ) cc  "
	      +" left join "
	      +" sec_user aa  on cc.user_id=aa.id) aaa ";
		Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from "+tb+"  order by thedate desc");
 
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+"-read.jsp");		
	}
	/***
	 * 主入口(列表)
	 */ 
	public void followlist() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;  
		String and="";
		int status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):1;
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
		String thedate=getPara("thedate")!=null?getPara("thedate"):"";
		if(thedate.length()>0)
		{
			and+=" and order_time>='"+thedate+"' and order_time<='"+thedate+" 24'";
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
		String tb="(   select bb.status,bb.public_account,bb.thedate,aa.id,aa.full_name,"
          +" bb.total_quantity ftcnt,bb.real_quantity frcnt"
          +" from "
	      +" ("
	      +" select status,public_account,thedate ,user_id,sum(total_quantity) total_quantity,sum(real_quantity) real_quantity from ("
	      +" select DATE_FORMAT(order_time,'%Y-%m-%d') thedate,f.* from follow_task f where status>-1"+and 
	      +"  ) aa group by thedate,user_id,public_account,status"
	      +" ) bb "	
	      +" left join "
	      +" sec_user aa  on   bb.user_id=aa.id) aaa ";
		Page<FollowTask> page = FollowTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+"  order by thedate desc");
 
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+"-follow.jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
}