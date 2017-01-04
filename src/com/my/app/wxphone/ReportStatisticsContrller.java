package com.my.app.wxphone;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.ReadTask;
import com.my.util.TokenUtil;
/**
 * 阅读统计
 * @author Administrator
 *
 */

public class ReportStatisticsContrller  extends Controller {
	//请求根目录
		private String root="/wxphone/";
		//当前请求路径
		private String path="task/reporttask";
		//表名
		private  String tableName=TableMapping.me().getTable(ReadTask.dao.getClass()).getName();
		//模糊查询
		private  String keywordArray[]={"url","title"};
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
			if(status==1||status==2)
			{
				tableName="read_task_settlement";
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
			
			
			System.out.println("and="+and);
		    String tb=" (select thedate,sum(total_quantity) total_quantity,sum(push_quantity) real_quantity,sum(praise_quantity) praise_quantity,sum(push_praise) real_praise from  (select DATE_FORMAT(`order_time`,'%Y-%m-%d') thedate,a.* from "+tableName+" a where status>-1 "+and+") bb  group by thedate) ff ";	
		    Page<ReadTask> page = ReadTask.dao.paginate(1,31, "select *", "from "+tb+"order  by thedate desc");
		    long total_quantity = 0;
		    int real_quantity=0;
		    int praise_quantity=0;
		    int real_praise=0;
		    for(ReadTask readt:page.getList()){    	
		    	total_quantity=total_quantity+Integer.parseInt(readt.get("total_quantity")!=null||readt.get("total_quantity").toString().length()==0?readt.get("total_quantity").toString():"0");
		    	real_quantity=real_quantity+Integer.parseInt(readt.get("real_quantity")!=null||readt.get("real_quantity").toString().length()==0?readt.get("real_quantity").toString():"0");
		    	praise_quantity=praise_quantity+Integer.parseInt(readt.get("praise_quantity")!=null||readt.get("praise_quantity").toString().length()==0?readt.get("praise_quantity").toString():"0");
		    	real_praise=real_praise+Integer.parseInt(readt.get("real_praise")!=null||readt.get("real_praise").toString().length()==0?readt.get("real_praise").toString():"0");
		    }
		    ReadTask red=new ReadTask();
		    red.set("total_quantity", total_quantity);
		    red.set("real_quantity", real_quantity);
		    red.set("praise_quantity", praise_quantity);
		    red.set("real_praise", real_praise);
		    List<ReadTask> list=page.getList();
		    list.add(red);
		    setAttr("list",list);
		    setAttr("psize", page.getTotalPage());
			setAttr("pageNum", 1);
			setAttr("count", page.getTotalRow()); 
			renderJsp(root+path+".jsp");		
		}
}
