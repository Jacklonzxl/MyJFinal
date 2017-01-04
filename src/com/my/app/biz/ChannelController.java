package com.my.app.biz;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.biz.Channel;
import com.my.app.bean.sec.User;
import com.my.util.TimeUtil;
@RequiresRoles("R_ADMIN")
public class ChannelController   extends Controller{

	//请求根目录
	private String root="/admin/";
	//当前请求路径
	private String path="biz/channel";
	//表名
	private  String tableName=TableMapping.me().getTable(Channel.dao.getClass()).getName();
	//模糊查询
	private  String keywordArray[]={"name"};
	//唯一值
	private String onlyval="name";
	//定位主菜单
	private  String m1="m_biz";
	//定位子菜单
	private  String m2="m_biz_channel";
	
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
		Page<Channel> page = Channel.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
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
		Channel channel=Channel.dao.findById(id);
		setAttr("channel", channel);
		List<User> ulist=User.dao.find("select * from sec_user where group_id=2 ");
		setAttr("ulist", ulist);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	public void save() {
		Channel channel=getModel(Channel.class); 
		Channel dbchannel=Channel.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+channel.getStr(onlyval)+"'");
		if(channel.getInt("id")!=null&&channel.getInt("id")>0&&
		  (dbchannel==null||dbchannel.get(onlyval).equals(channel.get(onlyval))))
		{   
			channel.update();
		}else if(dbchannel==null)
		{ 
			channel.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			channel.save();
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
			Channel.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   Channel.dao.deleteById(ids[i]);
		   }
		}
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("bean", "channel");
		setAttr("path", "/"+path);
	}
}