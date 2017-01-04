package com.my.app.wx;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.wx.bean.AccountAndroid;
import com.my.app.wx.bean.Sms;
/**
 * 账号列表 
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class MoneyinController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="moneyin";
	//当前请求路径
	private String path="terminal/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(AccountAndroid.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"account","password","wxid"};
 
	
	/***
	 * 主入口(列表)
	 */
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		if(pageNum<1){
			pageNum=1;
		}
		int pageSize=20;  
		String and="";


		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			and+=" and username like '%"+getPara("keyword")+"%'";

		}
		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			and+=" and username like '%"+getPara("keyword")+"%'";

		}
		int type=getPara("type")!=null?getParaToInt("type"):0;
		if(type!=0){
			and+=" and type="+type;
		}
		
		if(getPara("startdate")!=null&&getPara("startdate").trim().length()>0)
		{
			System.out.println(getPara("startdate"));
			and=and+" and ad>='"+getPara("startdate")+"'";
		}
		if(getPara("enddate")!=null&&getPara("enddate").trim().length()>0)
		{
			System.out.println(getPara("enddate"));
			and=and+" and ad<='"+getPara("enddate")+"'";
		}
		
		String sqlc="select * from  (select aa.*,bb.username from (select ad,type,userid,sum(money) money from (select money,type,userid,left(adddate,10) ad from user_money_in_list ) aa   group by ad desc)  aa  left join sec_user bb  on aa.userid=bb.id)  dddd where 1=1 "+and  +" limit "+(pageNum-1)*20+","+pageSize;
		String sqlcount="select count(*)  count from  (select aa.*,bb.username from (select ad,type,userid,sum(money) money from (select money,type,userid,left(adddate,10) ad from user_money_in_list ) aa   group by ad desc)  aa  left join sec_user bb  on aa.userid=bb.id)  dddd where 1=1 "+and;

		long psizecount=Db.queryLong(sqlcount);
		System.out.println(psizecount);
		System.out.println(sqlc);
		List<Record> list=Db.find(sqlc);
		long psize=psizecount/pageSize;
		if(psizecount%pageSize>0){
			psize+=1;
		}
		
		setAttr("list",list);
		setAttr("psize",psize);
		setAttr("pageNum", pageNum);
		setAttr("count",psizecount);		
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}