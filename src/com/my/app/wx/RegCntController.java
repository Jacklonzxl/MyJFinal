package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.wx.bean.AccountAndroid;
/**
 * 注册管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class RegCntController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="regcnt";
	//当前请求路径
	private String path="terminal/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(AccountAndroid.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"name"};
 
	
	/***
	 * 主入口(列表)
	 */
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=20;  
		String and="";

		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			and+=" and account_group_id="+getPara("keyword").toString()+"";
		}
 	
		if(getPara("startdate")!=null&&getPara("startdate").trim().length()>0)
		{
			System.out.println(getPara("startdate"));
			and=and+" and create_time>='"+getPara("startdate")+"'";
		}
		if(getPara("enddate")!=null&&getPara("enddate").trim().length()>0)
		{
			System.out.println(getPara("enddate"));
			and=and+" and create_time<='"+getPara("enddate").toString()+" 23:59:59'";
		}
		
		String tamenale=" (select aa.cd  createtime,aa.cnt  allcnt,bb.cnt successcnt,bb.cnt/aa.cnt  successratio  from (select cd, count(*) cnt  from (select DATE_FORMAT(create_time,'%Y-%m-%d') cd from account_android   where id>0  "+and+" ) a group by cd ) aa left join (select cd, count(*) cnt  from (select DATE_FORMAT(create_time,'%Y-%m-%d') cd from account_android  where status<>-300) a group by cd ) bb on aa.cd=bb.cd) ffff ";
		System.out.println(tamenale);
		Page<AccountAndroid> page = AccountAndroid.dao.paginate(pageNum, pageSize, "select *", "from "+tamenale+" where 1=1  order by createtime desc ");
		   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}