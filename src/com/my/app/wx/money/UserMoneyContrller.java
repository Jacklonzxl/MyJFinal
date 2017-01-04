package com.my.app.wx.money; 

 

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.UserMoney;
import com.my.app.wx.bean.UserPay;
import com.my.util.TimeUtil;
/**
 * 角色管理
 * @author zcj
 *
 */

public class UserMoneyContrller  extends Controller{
	//请求根目录
	private String root="/wx/";
	
	private String m_name="usermoney";
	//当前请求路径
	private String path="usermoney/"+m_name;
	//表名
	private  String tableName=TableMapping.me().getTable(UserMoney.dao.getClass()).getName();
 
 
	
	/***
	 * 主入口(列表)
	 * @throws UnsupportedEncodingException 
	 */ 
	@RequiresRoles("R_ADMIN")
	public void index() throws UnsupportedEncodingException {
		
		int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
//		Enumeration<String> en= getParaNames();
//		while (en.hasMoreElements()) {
//			System.out.println(en.nextElement());
//        }
		
		int pageSize=20;  
		User user=getSessionAttr("dbuser");
		//int userid=user.getInt("id");
		String and=" and id>0";
		int status=getPara("status")!=null?getParaToInt("status"):-1;
		if(status>-1)
		{
			and+=" and status="+status;
		}
		if(getPara("keyword")!=null&&getPara("keyword").trim().length()>0)
		{
			and+=" and full_name like '%"+java.net.URLDecoder.decode(getPara("keyword"), "utf-8")+"%'";
		}
 
	    tableName=" (select a.*,b.full_name,b.group_id from "+tableName+" a left join sec_user b on a.userid=b.id ) aaa";
	    //System.out.println("keyword:"+and+getPara("keyword"));
		Page<UserMoney> page = UserMoney.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		
		if(getPara("keyword")!=null){
			setAttr("keyword",java.net.URLDecoder.decode(getPara("keyword"), "utf-8"));
	        }
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
	@RequiresRoles("R_ADMIN")
	public void input() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		UserMoney money=UserMoney.dao.findById(id);
		User user=User.dao.findById(money.get("userid"));
		setAttr("user", user);
		setAttr("bean", money);
		initAttr();
		
		
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * 编辑界面
	 */
	@RequiresRoles("R_ADMIN")
	public void in() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		UserMoney money=UserMoney.dao.findById(id);
		User user=User.dao.findById(money.get("userid"));
		setAttr("user", user);
		setAttr("bean", money);
		initAttr();
		
		
		renderJsp(root+path+"-in.jsp");		
	}
	/***
	 * 保存到DB
	 */
	@RequiresRoles("R_ADMIN")
	public void save() {
		UserMoney bean=getModel(UserMoney.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		UserMoney money=UserMoney.dao.findById(id);
		if(money!=null){
			String f[]=bean._getAttrNames();
			for(int i=0;i<f.length;i++)
			{
				money.set(f[i], bean.get(f[i]));
			}
			money.update();
		}
		initAttr();
		renderJsp(root+"success.jsp");		
	}
	
	/***
	 * 保存到DB
	 */
	@RequiresRoles("R_ADMIN")
	public void insave() {
		UserMoney bean=getModel(UserMoney.class,"bean"); 
		long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
		UserMoney money=UserMoney.dao.findById(id);
		if(money!=null){
			UserPay userPay=new UserPay();
			userPay.set("userid", money.get("userid"));
			userPay.set("alyid", ""+System.currentTimeMillis()+System.currentTimeMillis());
			userPay.set("type", getPara("type"));
			userPay.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			userPay.set("reviewdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			userPay.set("status", 1);
			userPay.set("money", getPara("money"));
			userPay.save();
			if("3".equals(getPara("type")))
			{
				money.set("balance", money.getFloat("balance")+Float.parseFloat(getPara("money")));
			}else
			{
				money.set("give", money.getFloat("give")+Float.parseFloat(getPara("money")));
			}
			money.update();
		}
		setAttr("msg", "充值成功");
		setAttr("url", "usermoney");
		renderJsp(root+"msg.jsp");		
	}
	/***
	 * 删除
	 */	
	@RequiresRoles("R_ADMIN")
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			UserMoney.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   UserMoney.dao.deleteById(ids[i]);
		   }
		}
		renderText("删除成功!");
		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);
	}
}