package com.my.app.sec;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Permission;
import com.my.util.TimeUtil;
/**
 * ��ɫ����
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class PermissionController  extends Controller{
	//�����Ŀ¼
	private String root="/admin/";
	//��ǰ����·��
	private String path="sec/permission";
	//����
	private  String tableName=TableMapping.me().getTable(Permission.dao.getClass()).getName();
	//ģ����ѯ
	private  String keywordArray[]={"value","name"};
	//Ψһֵ
	private String onlyval="name";
	//��λ���˵�
	private  String m1="m_sec";
	//��λ�Ӳ˵�
	private  String m2="m_sec_permission";
	
	/***
	 * �����(�б�)
	 */
	@RequiresPermissions("P_D_ADMIN")
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
		System.out.println(and);
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
	 * �༭����
	 */
	public void input() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		Permission permission=Permission.dao.findById(id);
		setAttr("permission", permission);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * ���浽DB
	 */
	public void save() {
		Permission permission=getModel(Permission.class); 
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
		redirect(root+"?pg="+path+"&m1="+m1+"&m2="+m2+"&path=/"+path);		
	}
	/***
	 * ɾ��
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
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("bean", "permission");
		setAttr("path", "/"+path);
	}
}