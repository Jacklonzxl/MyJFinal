package com.my.app;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.DES;
import com.my.util.TimeUtil;

public class DeviceController extends Controller {
    
	public void get()
	{
		String str="";
		if("sz".equals(getPara("fr")))
		{
			String cp=getPara("cp");
			String sql="select max(id) id from cp_device_"+cp;
			Record r=Db.findFirst(sql);
			if(r!=null&&r.getInt("id")!=null)
			{ 
				 

				String imei_Sql="select * from mongouserlog20160515 where id>"+r.getInt("id")+" limit 0,1 ";
				Record imeiR=Db.findFirst(imei_Sql);
				String imei=imeiR.getStr("imei");
				if(imei!=null&&imei.length()>=15)
				{
					String imsi=imeiR.getStr("imsi");
					if(imsi==null||imsi.length()==0)
					{
						imsi="null";
					 }
					 String model=imeiR.getStr("model");
					 if(model==null||model.length()==0)
					 {
						 model="null";
					 }
					 String mac=imeiR.getStr("mac");
					 if(mac==null||mac.length()==0)
					 {
						 mac="null";
					 }
					 if(imei.length()>15)
					 {
						 imei=DES.decryptDES(imei);
					 }
					 if(imsi.length()>15)
					 {
						 imsi=DES.decryptDES(imsi);
					 }
					 if(imei!=null&&imei.length()==15)
					 {
						 
					
					  str=imei+"|"+imsi+"|"+model+"|"+mac;
					  Record record=new Record();
					  record.set("id", imeiR.get("id"));
					  record.set("imei",imei);
					  record.set("imsi", imsi);
					  record.set("model", imeiR.get("model"));
					  record.set("thedate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					  Db.save("cp_device_"+cp, record);
					 }else
					 {
						 Db.delete("mongouserlog20160515", imeiR);
					 }
				  }else
				  {
					  Db.delete("mongouserlog20160515", imeiR);
				  }
				  
				
			}else
			{
 
				String imei_Sql="select * from mongouserlog20160515 where id>0 limit 0,1 ";
				Record imeiR=Db.findFirst(imei_Sql);
				String imei=imeiR.getStr("imei");
				if(imei!=null&&imei.length()>=15)
				{
					String imsi=imeiR.getStr("imsi");
					if(imsi==null||imsi.length()==0)
					{
						imsi="null";
					 }
					 String model=imeiR.getStr("model");
					 if(model==null||model.length()==0)
					 {
						 model="null";
					 }
					 String mac=imeiR.getStr("mac");
					 if(mac==null||mac.length()==0)
					 {
						 mac="null";
					 }
					 if(imei.length()>15)
					 {
						 imei=DES.decryptDES(imei);
					 }
					 if(imsi.length()>15)
					 {
						 imsi=DES.decryptDES(imsi);
					 }
					 if(imei!=null&&imei.length()==15)
					 {
						 
					  str=imei+"|"+imsi+"|"+model+"|"+mac;
					  Record record=new Record();
					  record.set("id", imeiR.get("id"));
					  record.set("imei",imei);
					  record.set("imsi", imsi);
					  record.set("model", imeiR.get("model"));
					  record.set("thedate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					  Db.save("cp_device_"+cp, record);
					 }else
					 {
						 Db.delete("mongouserlog20160515", imeiR);
					 }
				  }else
				  {
					  Db.delete("mongouserlog20160515", imeiR);
				  }
				 
				}
				
				
			
		}
		renderText(str);
	}
}