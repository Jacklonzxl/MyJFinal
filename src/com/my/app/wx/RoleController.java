package com.my.app.wx;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Role;
import com.my.app.bean.sec.RolePermission; 
import com.my.util.TimeUtil;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication 
public class RoleController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="role";
	//当前请求路径
	private String path="sec/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(Role.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"value","name"};
	//唯一值
	private String onlyval="name"; 
	
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
		Page<Role> page = Role.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
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
		Role role=Role.dao.findById(id);
		if(role==null)
		{
			role=new Role();
		}
		setAttr("bean", role);
		setAttr("permissionList", role.getPermissions());
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		Role role=getModel(Role.class,"bean"); 
		Role dbrole=Role.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+role.getStr(onlyval)+"'");
		if(role.getInt("id")!=null&&role.getInt("id")>0&&(dbrole==null||dbrole.get(onlyval).equals(role.get(onlyval))))
		{   
			String f[]=role._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				dbrole.set(f[i], role.get(f[i]));
			}
			dbrole.update();
		}else if(dbrole==null)
		{ 
			role.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			role.save();
		}
		String permission_ids[]=getParaValues("permission_ids");
		List<RolePermission> rlist=new ArrayList<RolePermission>();
		//保存权限
		for(int i=0;permission_ids!=null&&i<permission_ids.length;i++)
		{
			RolePermission rolePermission=new RolePermission();
			rolePermission.set("role_id",role.get("id"));
			rolePermission.set("permission_id", permission_ids[i]);
			rlist.add(rolePermission);
		}
		Db.update("delete from sec_role_permission where role_id="+role.get("id"));
		Db.batchSave(rlist,rlist.size());
		renderJsp(root+"success.jsp");		
	}
	/***
	 * 删除
	 */	
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			Role.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Role.dao.deleteById(ids[i]);
		   }
		}
		renderText("删除记录成功！");
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name); ; 
	}
}