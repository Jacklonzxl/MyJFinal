package com.my.app;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil;

public class IpController extends Controller {
    
	public void get() throws Exception
	{
		if("sz".equals(getPara("fr")))
		{
			
			String city="";
			if(getPara("ip")!=null)
			{
				String sql="select * from ip_info where ip='"+getPara("ip")+"'";
				System.out.println(sql);
				Record ipinfo=Db.findFirst(sql);
				if(ipinfo!=null&&ipinfo.getStr("city")!=null&&ipinfo.getStr("city").length()>0)
				{
					city=" and city='"+ipinfo.getStr("city")+"'";
				}
			}

			Record r=new Record();
			r.set("cpid", getPara("cpid"));
			r.set("channel", getPara("channel"));
			r.set("packname", getPara("packname"));
			r.set("type", getPara("type"));
			r.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			Db.save("ip_log", r);
			String sql="select * from ip_info where status=1"+city;
			System.out.println(sql);
			Record ipinfo=Db.findFirst(sql);
			if(ipinfo!=null)
			{
				String ips[]={ipinfo.getStr("ip"),ipinfo.getStr("port")};
				String ipstr=HttpUtil.doGet("http://121.196.224.72/ip.jsp", "utf-8", ips);

				ipinfo.set("status", 0);
				Db.update("ip_info", ipinfo);
				if(ipstr!=null&&ipstr.length()>5&ipstr.length()<20)
				{
				renderText(ipinfo.getStr("ip")+":"+ipinfo.getStr("port"));
				}
			}else
			{
				renderText("");
			}
			 
			
		}else
		{
			renderText("");
		}
	}
}