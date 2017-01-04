package com.my.app.sec;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.Group;
import com.my.util.TimeUtil;
/**
 * ��ɫ����
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class GroupController  extends Controller{
	//�����Ŀ¼
	private String root="/admin/";
	//��ǰ����·��
	private String path="sec/group";
	//����
	private  String tableName=TableMapping.me().getTable(Group.dao.getClass()).getName();
	//ģ����ѯ
	private  String keywordArray[]={"name"};
	//Ψһֵ
	private String onlyval="name";
	//��λ���˵�
	private  String m1="m_sec";
	//��λ�Ӳ˵�
	private  String m2="m_sec_group";
	
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
		System.out.println(and);
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
		setAttr("group", group);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * ���浽DB
	 */
	public void save() {
		Group group=getModel(Group.class); 
		Group dbgroup=Group.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+group.getStr(onlyval)+"'");
		if(group.getInt("id")!=null&&group.getInt("id")>0&&
		  (dbgroup==null||dbgroup.get(onlyval).equals(group.get(onlyval))))
		{   
			group.update();
		}else if(dbgroup==null)
		{ 
			group.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			group.save();
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
			Group.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Group.dao.deleteById(ids[i]);
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