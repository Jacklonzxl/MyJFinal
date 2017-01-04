package com.my.app.wx;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Group;
import com.my.app.bean.sec.User;
import com.my.app.bean.sec.UserRole;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.UserMoney;
import com.my.util.TimeUtil;
/***
 * 用户基本信息管理
 * @author zcj
 *
 */

public class UserController  extends Controller{
	
	//请求根目录
	private String root="/wx/";
	private String m_name="user";
	//当前请求路径
	private String path="sec/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(User.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"full_name","username"};
	//唯一值
	private String onlyval="username"; 
	private int df_aid=8;  //默认代理商
	
	/***
	 * 主入口(列表)
	 */
	@RequiresRoles("R_ADMIN")
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=20;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):-1;
		if(status>-1)
		{
			and+=" and status="+status;
		}
		int group_id=getPara("group_id")!=null?getParaToInt("group_id"):-1;
		if(group_id>-1)
		{
			and+=" and group_id="+group_id;
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
		//System.out.println(and);
		//int firstRow = (pageNum - 1) * pageSize ;
		String tableName="(select a.*,b.name group_name from sec_user a left join sec_group b on a.group_id=b.id) aa ";
		Page<User> page = User.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		
		List<Group> groupList=Group.dao.find("select * from sec_group");
		setAttr("groupList", groupList);
		
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	/***
	 * 编辑界面
	 */
	public void input() {
		 
		Subject subject = SecurityUtils.getSubject();
		User user=getSessionAttr("dbuser");
		int id=user.getInt("id");
		if(subject.hasRole("R_ADMIN"))
		{
			id=getPara("id")!=null?getParaToInt("id"):id;
			user=User.dao.findById(id);
		}
		if(user==null)
		{
			user=new User();
		}
		setAttr("bean", user);
		setAttr("roleList", user.getRoles());
		List<Group> groupList=Group.dao.find("select * from sec_group");
		setAttr("groupList", groupList);
		
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		
		Subject subject = SecurityUtils.getSubject();
		User user=getSessionAttr("dbuser");
		String role_ids[]=getParaValues("role_ids");
		List<UserRole> rlist=new ArrayList<UserRole>();

		User userbean=getModel(User.class,"bean"); 
		
		if(userbean.getInt("id")!=null&&userbean.getInt("id")>0)
		{   
			if(subject.hasRole("R_ADMIN"))
			{
				User dbuser=User.dao.findFirst("select * from "+tableName+" where  "+onlyval+"='"+userbean.getStr(onlyval)+"'");
				if(dbuser==null||dbuser.get(onlyval).equals(userbean.get(onlyval)))
				{
					userbean.update();
				}
			}else
			{
				user.set("password", userbean.get("password"));
				user.set("email", userbean.get("email"));
				user.set("mobile", userbean.get("mobile"));
				user.update();
				
			}
		}else if(subject.hasRole("R_ADMIN")&&(userbean.getInt("id")==null||userbean.getInt("id")==0))
		{   
			userbean.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis())); 
			userbean.save(); 
			if(userbean.getInt("group_id")==2)
			{
				Channel channel=new Channel();
				channel.set("userid", userbean.get("id"));
				channel.set("name", userbean.get("full_name"));
				channel.set("aid", df_aid);
				channel.set("type", 1);
				channel.save();
				UserMoney userMoney=new UserMoney(); 
				userMoney.set("userid", userbean.get("id"));				
				userMoney.save();
				
			}else if(userbean.getInt("group_id")==3)
			{
				Channel channel=new Channel();
				channel.set("userid", userbean.get("id"));
				channel.set("name", userbean.get("full_name"));
				channel.set("type", 2);
				channel.set("aid", 0);
				channel.save();
				UserMoney userMoney=new UserMoney(); 
				userMoney.set("userid", userbean.get("id"));				
				userMoney.save();
			}
		}
		//保存角色
		if(subject.hasRole("R_ADMIN"))
		{
			for(int i=0;role_ids!=null&&i<role_ids.length;i++)
			{
				UserRole userRole=new UserRole();
				userRole.set("user_id",userbean.get("id"));
				userRole.set("role_id", role_ids[i]);
				rlist.add(userRole);
			}
			Db.update("delete from sec_user_role where user_id="+userbean.get("id"));
			Db.batchSave(rlist,rlist.size()); 	
			renderJsp(root+"success.jsp");	
		}else
		{
			setAttr("msg", "设置账号信息成功！");
			renderJsp(root+"msg.jsp");
		}
	}
	/***
	 * 删除
	 */	
	@RequiresRoles("R_ADMIN")
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			User.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   User.dao.deleteById(ids[i]);
		   }
		} 
		renderText("删除成功!");	
		
	}
	public void center()
	{
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id"); 
		UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+userid);
		setAttr("user", user);
		setAttr("userMoney", userMoney);
		renderJsp(root+path+"-center.jsp");
	}
	
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}

}
