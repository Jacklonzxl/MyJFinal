package com.my.app.wx; 

 

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.app.wx.bean.ReadTask;
import com.my.util.TimeUtil;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication  
public class FollowReviewContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="followreview";
	//当前请求路径
	private String path="review/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(FollowTask.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"value","name"};
	//唯一值
	private String onlyval="name";
 
	
	/***
	 * 主入口(列表)
	 */ 
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=10;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):-1;
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
		if(agent!=null)
		{

			List<Channel> clist=agent.getChannels();
			setAttr("clist", clist);
		}else if(subject.hasRole("R_ADMIN"))
		{
			channel_sql="select * from biz_channel where aid>0";
			List<Channel> clist=Channel.dao.find(channel_sql);
			setAttr("clist", clist);
		}
				
		User user=getSessionAttr("dbuser");
		long userid=user.getInt("group_id")==3?user.getInt("id"):-1;		
		userid=getParaToInt("userid")!=null?getParaToInt("userid"):userid;
		if(userid>0)
		{
			and+=" and user_id='"+userid+"'";
		}
		String tb="(select a.*,b.full_name from "+tableName+" a left join sec_user b on a.user_id=b.id) aa ";
		Page<FollowTask> page = FollowTask.dao.paginate(pageNum, pageSize, "select *", "from "+tb+" where status=-1 "+and+" order by id desc");
 
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
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
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");	
		FollowTask bean=getModel(FollowTask.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		FollowTask followTask=FollowTask.dao.findById(id);
		if(followTask!=null){
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
			followTask.update();
		}else
		{
			if(bean.get("status")==null)
			{
				bean.set("status", -1);
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
			bean.save();
		}

		renderJsp(root+"success.jsp");		
	}
	/***
	 *  审核
	 */	
	public void review() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			FollowTask.dao.findById(id).set("status", 0).update();
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   FollowTask.dao.findById(Long.parseLong(ids[i])).set("status", 0).update();
		   }
		}
		renderText("审核成功!");		
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
}