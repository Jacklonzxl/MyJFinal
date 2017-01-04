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
public class MoneypayController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="moneypay";
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
		String starttablename="";
		String enttablename="";
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

		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			String []usernamearrays=getPara("keyword").split(",");
			if(usernamearrays.length>1){
				and+=" and (";
				for(int i=0;i<usernamearrays.length;i++){
					and+=" username like '%"+usernamearrays[i]+"%'"+" Or";
				}
				and=and.substring(0, and.length()-2)+") ";
			}else{
				and+=" and username like '%"+getPara("keyword")+"%'";
			}
			
		}
		int groupstatus=getPara("groupstatus")!=null?getParaToInt("groupstatus"):-1;

		if(groupstatus==1){
			starttablename=" (select ad,sum(cnt) cnt ,sum(money) money,sum(money)/sum(cnt) price from(select * from ";
			enttablename=") dd group by ad) ff ";
		}else{
			groupstatus=0;
		}
		

		String tablename=starttablename+"(select aa.*,username from (select ad,userid,sum(cnt) cnt ,sum(money) money,sum(money)/sum(cnt) price from (select cnt,money,userid,left(adddate,10) ad from user_money_pay_list) aa group by ad,userid) aa left join sec_user bb on aa.userid=bb.id) cc where 1=1 "+and+enttablename;
		String sqlc="select * from"+tablename +" order by ad desc limit "+(pageNum-1)*20+","+pageSize;
		String sqlcount="select count(*) count from"+tablename;
		System.out.println(sqlc);
		long psizecount=Db.queryLong(sqlcount);
		long psize=psizecount/pageSize;
		if(psizecount%pageSize>0){
			psize+=1;
		}
		List<Record> list=Db.find(sqlc);
		
		
		setAttr("list",list);
		setAttr("psize",psize);
		setAttr("pageNum", pageNum);
		setAttr("count",psizecount);
		setAttr("groupstatus",groupstatus);
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
	public static void main(String[] args) {
		String C0001="C0001,111";
		String aaa[]=C0001.split(",");
		System.out.println(aaa.length);
	}
}