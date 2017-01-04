package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.wx.bean.Setting;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class SettingController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="setting";
	//当前请求路径
	private String path="setting/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(Setting.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"name"};
 
	
	/***
	 * 主入口(列表)
	 */
	public void index() {
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=100;  
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
		Page<Setting> page = Setting.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id asc");
		   
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
		Setting setting=Setting.dao.findById(id);
		setAttr("bean", setting);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() { 
		Setting bean=getModel(Setting.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		Setting setting=Setting.dao.findById(id);
		if(setting!=null){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				setting.set(f[i], bean.get(f[i]));
			}
			Cache userCache= Redis.use("userc");
	        Jedis jedis = userCache.getJedis(); 
	        if(bean.getInt("status")==0)
	        {
	        	jedis.del(bean.getStr("name"));
	        }else
	        {
	        	jedis.set(bean.getStr("name"), bean.getStr("value"));
	        }   
	        jedis.close();
			setting.update();
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
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			Setting.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Setting.dao.deleteById(ids[i]);
		   }
		}
		renderText("删除成功!");	
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}