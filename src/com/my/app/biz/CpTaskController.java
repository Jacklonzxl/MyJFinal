package com.my.app.biz;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.biz.CpTask;
import com.my.util.TimeUtil;
@RequiresRoles("R_ADMIN")
public class CpTaskController extends Controller{

	//�����Ŀ¼
	private String root="/admin/";
	//��ǰ����·��
	private String path="biz/cpTask";
	//����
	private  String tableName=TableMapping.me().getTable(CpTask.dao.getClass()).getName();
	//ģ����ѯ
	private  String keywordArray[]={"name"};
	//Ψһֵ
	private String onlyval="name";
	//��λ���˵�
	private  String m1="m_biz";
	//��λ�Ӳ˵�
	private  String m2="m_biz_cpTask";
	
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
		int channelid=getPara("channelid")!=null?getParaToInt("channelid"):-1;
		if(channelid>-1)
		{
			and+=" and channelid="+channelid;
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
		//List<Channel> clist=Channel.dao.find("select * from biz_channel");
		//setAttr("clist", clist);
		//tableName=" (select a.*,b.name channelname from "+tableName+" a left join biz_channel b on b.id=a.channelid) t";
		//int firstRow = (pageNum - 1) * pageSize ;	
		Page<CpTask> page = CpTask.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
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
		CpTask cpTask=CpTask.dao.findById(id);
		setAttr("cpTask", cpTask);
		//List<Channel> clist=Channel.dao.find("select * from biz_channel ");
		//setAttr("clist", clist);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * ���浽DB
	 */
	public void save() {
		CpTask cpTask=getModel(CpTask.class); 
		CpTask dbcpTask=CpTask.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+cpTask.getStr(onlyval)+"'");
		if(cpTask.getInt("id")!=null&&cpTask.getInt("id")>0&&
		  (dbcpTask==null||dbcpTask.get(onlyval).equals(cpTask.get(onlyval))))
		{   
			cpTask.update();
		}else if(dbcpTask==null)
		{ 
			cpTask.set("cnt", 100000);
			cpTask.set("type", 1);
			cpTask.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			cpTask.save();
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
			CpTask.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   CpTask.dao.deleteById(ids[i]);
		   }
		}
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("bean", "cpTask");
		setAttr("path", "/"+path);
	}
}