package com.my.app.wx;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.wx.bean.AccountAndroid;
import com.my.app.wx.bean.Sms;
/**
 * 账号列表 
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN")
public class RegListController  extends Controller{
	//请求根目录
	private String root="/wx/";
	private String m_name="reglist";
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
		int pageSize=20;  
		String and="";
		int status=getPara("status")!=null?getParaToInt("status"):-1;
		System.out.println(status+"----"+getPara("status"));
		if(status!=-1)
		{
			if(status==100){
				and+=" and status!=1  And status!=-106 And status!=-300 ";
			}else{
			  and+=" and status="+status;
			}
			
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
		
		
		if(getPara("account_group_id")!=null&&getPara("account_group_id").trim().length()>0)
		{
			and=and+" and account_group_id='"+getPara("account_group_id")+"' ";
			System.out.println(getPara("account_group_id"));
		}
		if(getPara("startdate")!=null&&getPara("startdate").trim().length()>0)
		{
			System.out.println(getPara("startdate"));
			and=and+" and create_time>='"+getPara("startdate")+"'";
		}
		if(getPara("enddate")!=null&&getPara("enddate").trim().length()>0)
		{
			System.out.println(getPara("enddate"));
			and=and+" and create_time<='"+getPara("enddate")+"'";
		}
 	
		System.out.println("执行sql之前" +tableName);
		String tablename1=" (select id,account,password,imei,model_id,friends,email,email_password,email_regist_info,status,last_status,group_share,account_group_id,friend_share,last_friends,update_type,update_result,city,info_nickname,info_gender,info_province,info_city,info_sign,info_photo,last_use_time,nick_name_list,imsi,iccid,androidid,mac,bluetooth,serial,guid,language,mobile,qq,qq_password,wxid,org_wxid,amount,coordinates_id,create_time,set_logo,set_profile,brand,model,android_os,api_level,cpu_arch,new_password,last_password,model_user_id,session,page_pic,devicefile,reg_ip from account_android) aaa ";
		System.out.println(and);
		Page<AccountAndroid> page = AccountAndroid.dao.paginate(pageNum, pageSize, "select *", "from "+tablename1+" where 1=1 "+and+" order by id  ");
		System.out.println("执行sql之后");   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		setAttr("status", status);
		
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	public void initAttr()
	{
		setAttr("m_name", m_name);     
	}
}