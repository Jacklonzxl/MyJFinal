package com.my.app.wxphone; 

 

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPay;
import com.my.app.wx.bean.UserPayLog;
import com.my.util.TimeUtil;
/**
 * 角色管理
 * @author zcj
 *
 */
@RequiresAuthentication 
public class UserPayContrller  extends Controller{
	//请求根目录
	private String root="/wxphone/";
	
	private String m_name="userpay";
	//当前请求路径
	private String path="usermoney/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(UserPay.dao.getClass()).getName();
 
 
	
	/***
	 * 主入口(列表)
	 */ 
	public void index() {
		Subject subject = SecurityUtils.getSubject();
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");
		int status=1;
		if(subject.hasRole("R_ADMIN"))
		{ 
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):-1;
			if(getPara(0)!=null)
			{
				userid=getParaToInt(0);
			}
			status=getPara("status")!=null&&getPara("status").length()>0?getParaToInt("status"):0;
		}
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=20;  

		String and=" and id>0";
		
		if(status>-1)
		{
			and+=" and status="+status;
		}
		String startdate=getPara("startdate")!=null?getPara("startdate"):"";
		if(startdate.length()>0)
		{
			and+=" and adddate>='"+startdate+"'";
		}
		String enddate=getPara("enddate")!=null?getPara("enddate"):"";
		if(enddate.length()>0)
		{
			and+=" and adddate<='"+enddate+" 24'";
		}
		if(userid>0)
		{
			and+=" and userid='"+userid+"'";
		}
		
	    tableName=" (select a.*,b.full_name,b.group_id,c.balance from "+tableName+" a left join sec_user b on a.userid=b.id left join user_money c on c.userid=a.userid) aaa";
		Page<UserPay> page = UserPay.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		
		setAttr("keyword",getPara("keyword"));
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		String channel_sql="select * from biz_channel where aid>0";
		List<Channel> clist=Channel.dao.find(channel_sql);
		setAttr("clist", clist);
		//User user=getSessionAttr("dbuser");
		//int userid=user.getInt("id"); 
		UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+userid);
		setAttr("user", user);
		setAttr("userMoney", userMoney);
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	/***
	 * 主入口(列表)
	 */ 
	public void paylog() {
		Subject subject = SecurityUtils.getSubject();
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");
 
		if(subject.hasRole("R_ADMIN"))
		{
			userid=getPara("userid")!=null&&getPara("userid").length()>0?getParaToInt("userid"):-1;
			 
		}
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
		int pageSize=20;  

		String and=" and id>0";
		
 
		String startdate=getPara("startdate")!=null?getPara("startdate"):"";
		if(startdate.length()>0)
		{
			and+=" and adddate>='"+startdate+"'";
		}
		String enddate=getPara("enddate")!=null?getPara("enddate"):"";
		if(enddate.length()>0)
		{
			and+=" and adddate<='"+enddate+" 24'";
		}
		if(userid>0)
		{
			and+=" and userid='"+userid+"'";
		}
		
	    tableName=" (select a.*,b.full_name,b.group_id,c.balance,d.title,e.public_account from user_money_pay_list a "
	    		+ "left join sec_user b on a.userid=b.id "
	    		+ "left join user_money c on c.userid=a.userid "
	    		+ "left join read_task_settlement d on a.rid=d.id and a.type=2 "
	    		+ "left join follow_task e on a.rid=e.id and a.type=1 ) aaa";
		Page<UserPayLog> page = UserPayLog.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		
		setAttr("keyword",getPara("keyword"));
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		String channel_sql="select * from biz_channel where aid>0";
		List<Channel> clist=Channel.dao.find(channel_sql);
		setAttr("clist", clist);
		
		setAttr("m_name", "userpay/paylog");
		renderJsp(root+path+"-log.jsp");		
	}	
	/***
	 * 编辑界面
	 */
	public void pay() {		 
		initAttr();	
		renderJsp(root+path+"-pay.jsp");		
	}
	public void paysave() {		
		User user=getSessionAttr("dbuser");
		int userid=user.getInt("id");
		String alyid=getPara("payid");
		String money=getPara("money");
		if(alyid.length()>=28&&money.length()>0){
		UserPay userPay=UserPay.dao.findFirst("select * from "+tableName+" where alyid='"+alyid+"'");
		
		if(userPay==null)
		{
			try{
			userPay=new UserPay();
			userPay.set("userid", userid);
			userPay.set("alyid", alyid);
			userPay.set("type", 1);
			userPay.set("money", Float.parseFloat(money));
			userPay.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			userPay.save();
			initAttr();	
			setAttr("msg", "提交充值信息成功，请等待审核！");
			setAttr("url", "userpay/pay");
			renderJsp(root+"msg.jsp");	
			}catch(Exception e)
			{
				setAttr("msg", "提交充值信息失败，充值信息填写不正确！"); 
				renderJsp(root+"msg.jsp");
			}
		}else
		{
			setAttr("msg", "提交充值信息失败，请不要重复提交！"); 
			renderJsp(root+"msg.jsp");	
		}
		}else
		{
			setAttr("msg", "提交充值信息失败，充值信息填写不正确！"); 
			renderJsp(root+"msg.jsp");
		}
	}
	/***
	 * 编辑界面
	 */
	public void input() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		UserPay money=UserPay.dao.findById(id);
		//User user=User.dao.findById(money.get("userid"));
		setAttr("user", money.getUser());
		setAttr("bean", money);
		initAttr();				
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 保存到DB
	 */
	@RequiresRoles("R_ADMIN")
	public void save() {
		UserPay bean=getModel(UserPay.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		UserPay userPay=UserPay.dao.findById(id);
		
		if(userPay!=null&&(userPay.getInt("status")==0)){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				userPay.set(f[i], bean.get(f[i]));
			}
			userPay.set("reviewdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			userPay.update();
			UserMoney userMoney=UserMoney.dao.findFirst("select * from user_money where userid="+userPay.get("userid"));
			if(userMoney!=null)
			{
				userMoney.set("balance", userMoney.getFloat("balance")+userPay.getFloat("money"));
				userMoney.update();
			}else
			{
				userMoney=new UserMoney();
				userMoney.set("balance", userPay.getFloat("money"));
				userMoney.set("userid", userPay.get("userid"));				
				userMoney.save();
			}
			
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
			UserPay.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   UserPay.dao.deleteById(ids[i]);
		   }
		}
		renderText("删除成功!");
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
}