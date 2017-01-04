package com.my.app.wx;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.wx.bean.Mdata;
import com.my.app.wx.bean.Setting;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class MdataController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="mdata";
	//当前请求路径
	private String path="mdata/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(Mdata.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"dnum"};
 
	/***
	 * 主入口(列表)
	 * @throws ParseException 
	 */
	public void daysum() throws ParseException {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=100;  
		String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
		if(getPara("thedate")!=null&&getPara("thedate").length()>0)
		{
			thedate=getPara("thedate");
		}
		String and="";
		if(getPara("g")!=null&&!getPara("g").equals("-1"))
		{
			and+=" and dnum like '"+getPara("g")+"%' ";
		}
		if(getPara("grouping")!=null&&!getPara("grouping").equals("-1"))
		{
			and+=" and grouping like '"+getPara("grouping")+"%' ";
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
		String table="  (select a.thedate,a.followcnt,a.readcnt,b.* from (select * from account_day_cnt_dnum where thedate='"+thedate+"') a left join ph_dnum  b on  a.dnum=b.dnum ) aaa";
		Page<Record> page = Db.paginate(pageNum, pageSize, "select *", "from "+table+"  where dnum is not null  "+and+" order by  followcnt desc");
		 
		setAttr("list",page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
 
		initAttr();
		setAttr("m_name", "mdata/daysum");
		renderJsp(root+path+"-daysum.jsp");			
	}
	
	/***
	 * 主入口(列表)
	 * @throws ParseException 
	 */
	public void daymonitor() throws ParseException { 
		
		String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
		if(getPara("thedate")!=null&&getPara("thedate").length()>0)
		{
			thedate=getPara("thedate");
		}
 
		String sql="  select aaaa.*,c.cnt,aaaa.followcnt/c.cnt cc from ("
       +"select grouping,sum(followcnt) followcnt ,sum(readcnt) readcnt from ("
       +"(select a.thedate,a.followcnt,a.readcnt,b.grouping from "
       +"  (select * from account_day_cnt_dnum where thedate='"+thedate+"') a "
       +"    left join ph_dnum  b on  a.dnum=b.dnum     "
       +") aaa"
       +")    group by grouping order by followcnt desc"
       +") aaaa left join "
       +"(select grouping  ,count(*) cnt  from ph_dnum group by grouping order by cnt desc) c  "
       +" on c.grouping=aaaa.grouping order by cc desc"  ;
		 
		setAttr("list",Db.find(sql)); 
		setAttr("thedate",thedate);
		initAttr();
		setAttr("m_name", "mdata/daymonitor");
		renderJsp(root+path+"-daymonitor.jsp");			
	}
	/***
	 * 主入口(列表)
	 * @throws ParseException 
	 */
	public void index() throws ParseException {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=100;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):-1;
		if(status==1)
		{
			String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()-2400000);
			and+=" and lasttime>'"+thedate+"'";
		}else if(status==0)
		{
			String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()-2400000);
			and+=" and lasttime<='"+thedate+"'";
		}
		if(getPara("g")!=null&&!getPara("g").equals("-1"))
		{
			and+=" and dnum like '"+getPara("g")+"%' ";
		}
		if(getPara("grouping")!=null&&!getPara("grouping").equals("-1"))
		{
			and+=" and grouping like '"+getPara("grouping")+"%' ";
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
		Page<Mdata> page = Mdata.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id asc");
		List<Mdata> list=new ArrayList<Mdata>();
		for(int i=0;i< page.getList().size();i++)
		{
			Mdata m= page.getList().get(i);
			long lasttime=0;
			if(m.getStr("lasttime")!=null)
			{
				lasttime=(TimeUtil.GetSqlMilsecTime(m.getStr("lasttime")));
			} 
			m.set("btime", (System.currentTimeMillis()-lasttime)/1000/60);
			list.add(m);
		}
		setAttr("list",list);
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
		//int id=getPara("id")!=null?getParaToInt("id"):0;
		Mdata mdata=Mdata.dao.findFirst("select * from "+tableName+" where dnum='"+getPara("id")+"'");
		setAttr("bean", mdata);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() { 
		Mdata bean=getModel(Mdata.class,"bean"); 
	    //long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		Mdata mdata=Mdata.dao.findFirst("select * from "+tableName+" where dnum='"+bean.get("dnum")+"'");
		if(mdata!=null){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				mdata.set(f[i], bean.get(f[i]));
			}
			    
	        mdata.update();
		}else 
		{ 
 
			bean.save();
		}
		renderJsp(root+"success.jsp");	
	}
	/***
	 * 删除
	 */	
	public void del() {
		String id = getPara("id") != null ? getPara("id") : null;
		if(id!=null)
		{
			String sql="delete from "+tableName+" where dnum='"+id+"'";
			Db.update(sql);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   String sql="delete from "+tableName+" where dnum='"+ids[i]+"'";
			   Db.update(sql);
		   }
		}
		renderText("删除成功!");	
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}