package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Group;
import com.my.app.wx.bean.FollowTask;
import com.my.util.TimeUtil;
/**
 * ��ɫ����
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class GroupController  extends Controller{
	//�����Ŀ¼
	private String root="/wx/";
	private String m_name="group";
	//��ǰ����·��
	private String path="sec/"+m_name;
	//����
	private  String tableName=TableMapping.me().getTable(Group.dao.getClass()).getName();
	//ģ����ѯ
	private  String keywordArray[]={"name"};
	//Ψһֵ
	private String onlyval="name"; 
	
	/***
	 * �����(�б�)
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
		Page<Group> page = Group.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by theindex asc");
		   
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
		Group group=Group.dao.findById(id);
		setAttr("bean", group);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * ���浽DB
	 */
	public void save() { 
		Group bean=getModel(Group.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		Group group=Group.dao.findById(id);
		if(group!=null){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				group.set(f[i], bean.get(f[i]));
			}
			group.update();
		}else 
		{ 
			bean.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			bean.save();
		}
		renderJsp(root+"success.jsp");	
	}
	/***
	 * ɾ��
	 */	
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			Group.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Group.dao.deleteById(ids[i]);
		   }
		}
		renderText("ɾ���ɹ�!");	
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}