package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Permission;
import com.my.util.TimeUtil;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class PermissionController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="permission";
	//当前请求路径
	private String path="sec/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(Permission.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"value","name"};
	//唯一值
	private String onlyval="name"; 
	
	/***
	 * 主入口(列表)
	 */
	//@RequiresPermissions("P_D_ADMIN")
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=20;  
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
		//System.out.println(and);
		//int firstRow = (pageNum - 1) * pageSize ;	
		Page<Permission> page = Permission.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
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
		Permission permission=Permission.dao.findById(id);
		setAttr("bean", permission);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		Permission permission=getModel(Permission.class,"bean"); 
		Permission dbpermission=Permission.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+permission.getStr(onlyval)+"'");
		if(permission.getInt("id")!=null&&permission.getInt("id")>0&&
		  (dbpermission==null||dbpermission.get(onlyval).equals(permission.get(onlyval))))
		{   
			permission.update();
		}else if(dbpermission==null)
		{ 
			permission.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			permission.save();
		}
		redirect(root+"success.jsp");		
	}
	/***
	 * 删除
	 */	
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			Permission.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Permission.dao.deleteById(ids[i]);
		   }
		}
		renderText("删除记录成功！");
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}