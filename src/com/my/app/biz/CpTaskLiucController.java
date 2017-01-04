package com.my.app.biz;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.biz.CpTaskLiuc;
import com.my.util.TimeUtil;
@RequiresRoles("R_ADMIN")
public class CpTaskLiucController extends Controller{

	//请求根目录
	private String root="/admin/";
	//当前请求路径
	private String path="biz/cpTaskLiuc";
	//表名
	private  String tableName=TableMapping.me().getTable(CpTaskLiuc.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"channel"};
	//唯一值
	private String onlyval="addtime";
	//定位主菜单
	private  String m1="m_biz";
	//定位子菜单
	private  String m2="m_biz_cpTaskLiuc";
	
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
//		int channelid=getPara("channelid")!=null?getParaToInt("channelid"):-1;
//		if(channelid>-1)
//		{
//			and+=" and channelid="+channelid;
//		}
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
		Page<CpTaskLiuc> page = CpTaskLiuc.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
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
		CpTaskLiuc cpTaskLiuc=CpTaskLiuc.dao.findById(id);
		setAttr("cpTaskLiuc", cpTaskLiuc);
		//List<Channel> clist=Channel.dao.find("select * from biz_channel ");
		//setAttr("clist", clist);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		CpTaskLiuc cpTaskLiuc=getModel(CpTaskLiuc.class); 
		CpTaskLiuc dbcpTaskLiuc=CpTaskLiuc.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+cpTaskLiuc.getStr(onlyval)+"'");
		if(cpTaskLiuc.getInt("id")!=null&&cpTaskLiuc.getInt("id")>0&&
		  (dbcpTaskLiuc==null||dbcpTaskLiuc.get(onlyval).equals(cpTaskLiuc.get(onlyval))))
		{   
			cpTaskLiuc.update();
		}else if(dbcpTaskLiuc==null)
		{ 
			 
			cpTaskLiuc.set("addtime", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			cpTaskLiuc.save();
		}
		redirect(root+"?pg="+path+"&m1="+m1+"&m2="+m2+"&path=/"+path);		
	}
	/***
	 * 删除
	 */	
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			CpTaskLiuc.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   CpTaskLiuc.dao.deleteById(ids[i]);
		   }
		}
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("bean", "cpTaskLiuc");
		setAttr("path", "/"+path);
	}
}