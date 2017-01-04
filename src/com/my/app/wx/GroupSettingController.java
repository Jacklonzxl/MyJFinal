package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresRoles;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.my.app.wx.bean.GroupSetting;
import com.my.app.wx.bean.Sms;
import com.my.util.TimeUtil;

import redis.clients.jedis.Jedis;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class GroupSettingController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="groupsetting";
	//当前请求路径
	private String path="setting/"+m_name;
	//表名
	private  String tableName="wx_group_setting";
 
 
	
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
	 
 	
		Page<GroupSetting> page = GroupSetting.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id asc");
		   
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
		GroupSetting groupSetting=GroupSetting.dao.findById(id);
		setAttr("bean", groupSetting);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() { 
		GroupSetting bean=getModel(GroupSetting.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		GroupSetting groupSetting=GroupSetting.dao.findById(id);
		if(groupSetting!=null){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				groupSetting.set(f[i], bean.get(f[i]));
			}
			groupSetting.update();
		}else 
		{ 
			//bean.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			bean.save();
		}
		Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();
	    if(bean.get("maxread")!=null)
	    { 
	    	jedis.set("account_group_id_"+bean.get("serverid")+"_read", bean.get("maxread").toString());
	    }
	    if(bean.get("dcnt")!=null)
	    { 
	    	jedis.set("account_group_id_"+bean.get("serverid")+"_day", bean.get("dcnt").toString());
	    }	 
	    jedis.set("account_group_id_"+bean.get("serverid")+"_read_status", bean.get("readstatus").toString());
	    jedis.set("account_group_id_"+bean.get("serverid")+"_follow_status", bean.get("followstatus").toString());
	    jedis.close();
		renderJsp(root+"success.jsp");	
	}
	/***
	 * 删除
	 */	
	public void del() {
		Cache userCache= Redis.use("userc");
	    Jedis jedis = userCache.getJedis();
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			GroupSetting groupSetting=GroupSetting.dao.findById(id);
			GroupSetting.dao.deleteById(id);
		    jedis.set("account_group_id_"+groupSetting.get("serverid")+"_read_status", groupSetting.get("readstatus").toString());
		    jedis.set("account_group_id_"+groupSetting.get("serverid")+"_follow_status", groupSetting.get("followstatus").toString());
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   GroupSetting groupSetting=GroupSetting.dao.findById(ids[i]);
			   GroupSetting.dao.deleteById(ids[i]);
			   jedis.set("account_group_id_"+groupSetting.get("serverid")+"_read_status", groupSetting.get("readstatus").toString());
			   jedis.set("account_group_id_"+groupSetting.get("serverid")+"_follow_status", groupSetting.get("followstatus").toString());
		   }
		}
		jedis.close();
		renderText("删除成功!");	
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}