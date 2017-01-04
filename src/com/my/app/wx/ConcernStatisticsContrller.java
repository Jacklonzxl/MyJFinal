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
 * 关注统计
 * @author zcj
 *
 */
@RequiresAuthentication  
public class ConcernStatisticsContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="concerntask";
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
	 */ 
	public void index() {
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
		
		
		//String tb="(select a.*,b.full_name from "+tableName+" a left join sec_user b on a.user_id=b.id) aa ";
		String  tb=" (select thedate,sum(total_quantity) total_quantity, sum(real_quantity) real_quantity  from  (select DATE_FORMAT(`order_time`,'%Y-%m-%d') thedate,f.* from "+tableName+" f where status>-1 "+and+" ) aa group by thedate ) bb ";
		//System.out.println(tb);
		Page<FollowTask> page = FollowTask.dao.paginate(1, 31, "select *", "from "+tb+" order by thedate desc ");
 
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