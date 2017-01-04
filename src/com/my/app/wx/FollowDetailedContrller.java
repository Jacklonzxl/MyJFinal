package com.my.app.wx; 

 

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.my.util.TimeUtil;
/**
 * 关注明细
 * @author zcj
 *
 */
@RequiresAuthentication  
public class FollowDetailedContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="followDetailed";
	//当前请求路径
	private String path="task/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(FollowTask.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"value","name"};
	//唯一值
	private String onlyval="name";
 
	
	/***
	 * 主入口(列表)
	 * @throws Exception 
	 */ 
	public void index() throws Exception {
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
		String startdate=getPara("query_order_time")!=null?getPara("query_order_time"):"";
		String enddate="";
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		if(startdate.length()>0)
		{
			enddate=TimeUtil.getSpecifiedDayAfter(startdate,dateFormat);
			and+=" and order_time>='"+startdate+"' and order_time <'"+enddate+"'";
		}else{
			
			enddate=dateFormat.format(new Date());
			startdate=TimeUtil.getSpecifiedDayBefore(enddate, dateFormat);
			and+=" and order_time>='"+startdate+"' and order_time <'"+enddate+"' ";
			//System.out.println(and);
		}
		String user_sql="select * from sec_user where id>0";
		List<User> clist=User.dao.find(user_sql);
		setAttr("clist", clist);
				
		User user=getSessionAttr("dbuser");
		Subject subject = SecurityUtils.getSubject();
		long userid=user.getInt("id");
		if(subject.hasRole("R_ADMIN"))
		{
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):-1;
		}
		if(userid>0)
		{
			and+=" and user_id='"+userid+"'";
		}
		String tb="(select a.*,b.full_name from "+tableName+" a left join sec_user b on a.user_id=b.id) aa ";
		//System.out.println(tb);
		Page<FollowTask> page = FollowTask.dao.paginate(1, 10000, "select *", "from "+tb+" where status>-1 "+and+" order by id desc");
		int total_quantity = 0;
	    int real_quantity=0;
	    for(FollowTask readt:page.getList()){	    	
	    	total_quantity=total_quantity+Integer.parseInt(readt.get("total_quantity")!=null||readt.get("total_quantity").toString().length()==0?readt.get("total_quantity").toString():"0");
	    	real_quantity=real_quantity+Integer.parseInt(readt.get("real_quantity")!=null||readt.get("real_quantity").toString().length()==0?readt.get("real_quantity").toString():"0");
	    }
		FollowTask red=new FollowTask();
	    red.set("total_quantity", total_quantity);
	    red.set("real_quantity", real_quantity);
	    List<FollowTask> list=page.getList();
	    list.add(red);
		setAttr("list",list);
		setAttr("query_order_time",startdate);	
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
 
}