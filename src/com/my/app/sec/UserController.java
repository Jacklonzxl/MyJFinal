package com.my.app.sec;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Group;
import com.my.app.bean.sec.User;
import com.my.app.bean.sec.UserRole;
import com.my.util.TimeUtil;
/***
 * 用户基本信息管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class UserController  extends Controller{
	
	//请求根目录
	private String root="/admin/";
	//当前请求路径
	private String path="sec/user";
	//表名
	private  String tableName=TableMapping.me().getTable(User.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"full_name","username"};
	//唯一值
	private String onlyval="username";
	//定位主菜单
	private  String m1="m_sec";
	//定位子菜单
	private  String m2="m_sec_user";
	
	/***
	 * 主入口(列表)
	 */
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
		System.out.println(and);
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
		int id=getPara("id")!=null?getParaToInt("id"):0;
		User user=User.dao.findById(id);
		if(user==null)
		{
			user=new User();
		}
		setAttr("user", user);
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
		String role_ids[]=getParaValues("role_ids");
		List<UserRole> rlist=new ArrayList<UserRole>();

		User user=getModel(User.class); 
		User dbuser=User.dao.findFirst("select * from "+tableName+" where  "+onlyval+"='"+user.getStr(onlyval)+"'");
		if(user.getInt("id")!=null&&user.getInt("id")>0&& (dbuser==null||dbuser.get(onlyval).equals(user.get(onlyval))))
		{   
			String f[]=user._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				dbuser.set(f[i], user.get(f[i]));
			}
			dbuser.update();
		}else if(dbuser==null)
		{ 
			user.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis())); 
			user.save(); 
		}
		//保存角色
		for(int i=0;role_ids!=null&&i<role_ids.length;i++)
		{
			UserRole userRole=new UserRole();
			userRole.set("user_id",user.get("id"));
			userRole.set("role_id", role_ids[i]);
			rlist.add(userRole);
		}
		Db.update("delete from sec_user_role where user_id="+user.get("id"));
		Db.batchSave(rlist,rlist.size());
		redirect(root+"?pg="+path+"&m1="+m1+"&m2="+m2+"&path=/"+path);		
	}
	/***
	 * 删除
	 */	
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
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("path", "/"+path);
	}

}
