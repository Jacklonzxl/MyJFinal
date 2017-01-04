package com.my.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.app.bean.cp.Phone;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;

public class ApiController  extends Controller {
	
	public void getphone()
	{
		Map<String,Object> map=new HashMap<String,Object>();
		String url="http://ss.58info.net:8090/ss/getmobi?aid=c001&apid=100";
		//System.out.println(url);
		//map.put("phone", req.get("phone"));
		///map.put("code", req.get("vcode"));
		String sqlc="select * from ph_info_3 where status=0";
		List<Record> list=Db.find(sqlc);
		if(list.size()<60)
		{
		 try{							
			String str=HttpUtil.doPost(url, map); 
			Gson gson=new Gson();
	        if(str!=null&&str.length()>0)
	        {
	        	Phone ph=gson.fromJson(str, Phone.class);
	        	if(ph!=null&&ph.getNo()!=null&&ph.getNo().length()>0)
	        	{       		
					Record record_save=new Record();	
					record_save.set("phone",ph.getNo());
					record_save.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record_save.set("status", 0);  
					String sp=ph.getNo().substring(0, 7);
					String sql="select * from tb_phone_info where prefix_phone='"+sp+"'";
					
					System.out.println(sql);
					Record r=Db.findFirst(sql);
					//record_save.set("city", "sz");
					if(r!=null)
					{
						if("上海".equals(r.getStr("city")))
						{
							record_save.set("city", "sh");
							
						}else if("北京".equals(r.getStr("city")))
						{
							record_save.set("city", "bj");
							
						}else if("广州".equals(r.getStr("city")))
						{
							record_save.set("city", "gz");
							
						}else if("深圳".equals(r.getStr("city")))
						{
							record_save.set("city", "sz");
						}
						else if("东莞".equals(r.getStr("city"))) 
						{
							record_save.set("city", "dg");
						}else if("湛江".equals(r.getStr("city")))
						{
							record_save.set("city", "gz");
						}else if("韶关".equals(r.getStr("city")))
						{
							record_save.set("city", "gz");
						}else if("中山".equals(r.getStr("city")))
						{
							record_save.set("city", "zs");
						}
						else
						{
							record_save.set("city", "sz");
						}						
					}
					Db.save("ph_info_3",record_save);
	        	}
	        		
	        }
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
	}
	//接收验证码
	public void receive()
	{
		String status="no";
		String type=getPara(0);
		String phone=getPara("phone");
		
		String code=getPara("code");
		String msmType=getPara("type");
		String table="ph_info";
		if("1".equals(msmType))
		{
			table="ph_info_1";
		}else if(msmType!=null)
		{
			table="ph_info_"+msmType;
		}

		if("1".equals(type)&&phone!=null&&phone.length()>0)
		{
			String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
			String phones[]=TokenUtil.getStrngArray(phone, ",");
			for(int i=0;i<phones.length;i++)
			{
				String sql="select * from "+table+" where phone='"+phones[i]+"'  and adddate>='"+nowdate+"'";
				Record record=Db.findFirst(sql);
				if(record==null){
					Record record_save=new Record();
					record_save.set("phone",phones[i] );
					record_save.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					record_save.set("status", 0);
					Db.save(table, record_save);
				}else if(1==(record.getInt("status"))||2==(record.getInt("status"))||3==(record.getInt("status")))
				{
					String thedate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0, 10);
					String dbdate=record.getStr("adddate").substring(0, 10);
					if(!thedate.equals(dbdate))
					{
						Record record_save=new Record();	
						record_save.set("phone",phones[i] );
						record_save.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						record_save.set("status", 0);
						//record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						//record_save.set("status", 0);
						Db.save(table,record_save);
					}
				}
			}
			status="ok";
		}else if("2".equals(type)&&phone!=null&&phone.length()>0&&code!=null&&code.length()>0)
		{
			String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10);
			String sql="select * from "+table+" where phone='"+phone+"' and adddate>='"+nowdate+"'";
			Record record=Db.findFirst(sql);
			if(record!=null)
			{
				record.set("status", 2);
				record.set("vcode", code);
				record.set("vdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				Db.update(table, record);
				status="ok";
			}
		}else if("3".equals(type))
		{
			String sql="select * from "+table+" where status=0";
			List<Record> list=Db.find(sql);
			if(list.size()>2)
			{
				status="{\"frequency\":1,\"status\":0}";
			}else
			{
				status="{\"frequency\":1,\"status\":1}";
			}
		}
		renderText(status);
	}
	public void get()
	{
		new Thread()
		{
			@Override
			public void run() {
				 
				super.run();
				for(int i=0;i<10;i++)
				{
					
					getphone();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		String nowdate=TimeUtil.GetSqlDate(System.currentTimeMillis()).substring(0,10); 
		String msmType=getPara("type");
		String city=getPara("city");
		if(city!=null&&city.length()>1)
		{
			city=" and city='"+city+"' ";
		}
		String table="ph_info";
		if("1".equals(msmType))
		{
			table="ph_info_1";
		}else if(msmType!=null)
		{
			table="ph_info_"+msmType;
		}
		boolean re=true;
		if("3".equals(msmType))
		{   
			System.out.println("ph 3");
			String sql="select count(*)  from `cp_new_log` where adddate>='"+nowdate+"' and channel=1 and pwd <>'123' and pwd not like '192%' ";
			Record cp=Db.findById("cp_task", 2);
			long recount=Db.queryLong(sql);
			System.out.println(cp.getInt("cnt")+"-reg-"+recount);
			if(recount>cp.getInt("cnt"))
			{
				//System.out.println("reg is ok");
				re=false;
			}
		}
		if(re)
		{
			String type=getPara(0);
			String phone=getPara("phone");
			String str="";
			if("1".equals(type))
			{
				String sql="select * from "+table+" where status=0 "+city+" order by id desc";
				Record record=Db.findFirst(sql);
				if(record!=null)
				{
					record.set("status", 1); 
					record.set("msgdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					Db.update(table, record);
				    str="{\"no\":\""+record.getStr("phone")+"\"}";
					
				}		 
			}else if(phone!=null&&phone.length()>0)
			{
				
				String sql="select * from "+table+" where phone='"+phone+"'  and adddate>='"+nowdate+"'";
				Record record=Db.findFirst(sql);
				
				if(record!=null&&record.getStr("vcode")!=null&&record.getStr("vcode").length()>0)
				{
					if(record.getInt("status")==2)
					{
						record.set("vdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
					}
					record.set("status", 4);
					Db.update(table, record);
					str="{\"code\":\""+record.getStr("vcode")+"\"}";
					
				}else if(record!=null)
				{
	
					if(record.getStr("msgdate")==null||record.getStr("msgdate").length()==0)
					{
						record.set("msgdate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
						record.set("status", 3);
						Db.update(table, record);
					}
					
				}
				
			}
			renderText(str);
		}else
		{
			renderText("");
		}
	}
	//接收硬件信息
	public void receiveDevice()
	{
		/* 
		String sql_head="CREATE TABLE `mb_info_1` ("
				  +"`id` int(11) NOT NULL AUTO_INCREMENT,";
		String sql_foot="PRIMARY KEY (`id`)" 
				      +") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手机设备信息表';";
		String sql_content="";
		Enumeration<String> en=getParaNames();
		while (en.hasMoreElements()) {
			 String f=en.nextElement().toLowerCase();
			 if(!f.equals("id"))
			 {
				 sql_content+="  `"+f+"` varchar(255) DEFAULT NULL COMMENT '',";
			 }else
			 {
				 sql_content+="  `bid` varchar(255) DEFAULT NULL COMMENT '',";
			 }
			 
	    }
		String sql=sql_head+sql_content+sql_foot;
		System.out.println(sql);
		Db.update(sql);
		*/ 
	    
		Record record=new Record();
		Enumeration<String> en=getParaNames();
		while (en.hasMoreElements()) {
			
			 String f=en.nextElement().toLowerCase();
			 System.out.println(f);
			 System.out.println(getPara(f.toUpperCase()));
			 
			 if(!f.equals("id"))
			 {
				 record.set(f, getPara(f.toUpperCase()));
			 }else
			 {
				 record.set("bid", getPara("ID"));
			 }
			 
	    }
		Db.save("device_lib", record);
		

		
	}

  public void getAccount()
  {
	  String st="st2";//getPara("st");
	  if(getPara("gost")!=null&&getPara("gost").length()>0)
	  {
		  st=getPara("gost");
	  }
	  //String st1=getPara("st");
	  //String st1=getPara("s_t");
	  //System.out.println("xxx"+st1);
	 //String st1where=st1!=null&&st1.length()>0?" and "+st1+"=3":"";
	  String sql="select * from ph_pwd where "+st+"=0";
	  System.out.println(sql);
	  Record record=Db.findFirst(sql);
	  if(record!=null&&record.getInt("id")!=null)
	  {
		  renderText(record.getStr("phone")+"|"+record.getStr("pwd"));
		  record.set(st, 1);
		  record.set(st+"date", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		  Db.update("ph_pwd", record);
	  }else
	  {
		  renderText("");
	  }
  }
  public void getAccountMoney()
  {
	  String st="st2";//getPara("st");
	  //String st1=getPara("st");
	  //String st1=getPara("s_t");
	  //System.out.println("xxx"+st1);
	 //String st1where=st1!=null&&st1.length()>0?" and "+st1+"=3":"";
	  String sql="select * from ph_pwd_money where "+st+"=0";
	  System.out.println(sql);
	  Record record=Db.findFirst(sql);
	  if(record!=null&&record.getInt("id")!=null)
	  {
		  renderText(record.getStr("phone")+"|"+record.getStr("pwd"));
		  record.set(st, 1);
		  record.set(st+"date", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		  Db.update("ph_pwd_money", record);
	  }else
	  {
		  renderText("");
	  }
  }
  public void setAccount()
  {
	  String phone=getPara("phone");
	  String st=getPara("st");
	  Record record=Db.findFirst("select * from ph_pwd where phone='"+phone+"'");
	  if(record!=null&&record.getInt("id")!=null)
	  {
		  //renderText(record.getStr("phone")+"|"+record.getStr("pwd"));
		  record.set(st, 3);
		  //record.set(st+"date", TimeUtil.GetSqlDate(System.currentTimeMillis()));
		  Db.update("ph_pwd", record);
		  renderText("ok");
	  }else
	  {
		  renderText("ok");
	  }
  }
  public void postAccount() throws Exception
  {
	  String phone=getPara("phone");
	  String pwd=getPara("pwd");
	  File file = new File("C:\\work\\svn\\phone\\20160713.txt");//Text文件
	  BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	  String s = null;
	  while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	      String ar[]=TokenUtil.getStrngArray(s+"{==}aa123123", "{==}");
	      if(ar.length==2)
		  {
	    	  System.out.println(ar[0]+"|"+ar[1]);
	    	  phone=ar[0];
	    	  pwd=ar[1];
	    	  Record record=Db.findFirst("select * from ph_pwd_money where phone='"+phone+"' and pwd='"+pwd+"'");
	    	  if(phone.length()>5&&pwd.length()>3&&(record==null||record.getInt("id")==null))
	    	  {
	    		  Record record2=new Record();
	    		  record2.set("phone", phone);
	    		  record2.set("pwd", pwd);
	    		  record2.set("type",1);
	    		  //renderText("ok"); 
	    		  Db.save("ph_pwd_money", record2);
	    	  }
		  }
	  
	  }
	  br.close();
  }
  
  public void updateimei() throws Exception
  {
	  String date=getPara("date");
	  String sql="select * from cp_new_log where adddate>='"+date+"' ";
	  List<Record> list=Db.find(sql);
	  for(int i=0;i<list.size();i++)
	  {
		  Record r=list.get(i);
		  String deviceinfo=r.getStr("deviceinfo");
		  //String adddate=r.getStr("adddate");
		  int id=r.getInt("id");
		  String imei="";
			try{ 
				File f=new File("/mnt/www/80/"+deviceinfo); 
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
				DocumentBuilder builder=factory.newDocumentBuilder(); 
				Document doc = builder.parse(f); 
				NodeList nl = doc.getElementsByTagName("item"); 
				for (int j=0;j<nl.getLength();j++){
					Element brandElement=(Element) nl.item(j);
					String kName=brandElement.getAttribute("key"); 
					String vName=brandElement.getAttribute("value");
					if("getDeviceId".equals(kName))
					{
						imei=vName;
					}
					System.out.println(kName+":"+vName);
				 } 
				}catch(Exception e){ 
				    e.printStackTrace();
				  }		
			Record record=new Record();
			record.set("id", id);
			record.set("imei", imei);
			record.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			try{
			Db.save("cp_new_log_device", record);
			}catch(Exception e)
			{
				//e.printStackTrace();
			}
		  
	  }
  }
  public  void androidinfo()throws Exception{
	//long lasting =System.currentTimeMillis(); 
	try{ 
	File files=new File("C:\\work\\xml"); 
	File flist[] =files.listFiles();
	for(int a=0;a<flist.length;a++)
	{
		File f1=flist[a];
		File flist1[] =f1.listFiles();
		for(int b=0;b<flist1.length;b++)
		{
			File f=flist1[b];	
		//	if(f.getName().startsWith("1")){
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder=factory.newDocumentBuilder(); 
		Document doc = builder.parse(f); 
		NodeList nl = doc.getElementsByTagName("item"); 
		
	//	String t="CREATE TABLE `android_info` ("
	//			  +"`id` int(11) NOT NULL AUTO_INCREMENT,";
	//			
	//    String c="";
		Record record=new Record();
		for (int i=0;i<nl.getLength();i++){
			
			Element brandElement=(Element) nl.item(i);
			String kName=brandElement.getAttribute("key");
			String vName=brandElement.getAttribute("value");
			if(kName.equals("ID"))
			{
				kName="aid";
			}
			record.set(kName, vName);
			
			
			//c+= "`"+kName+"` varchar(255) NOT NULL DEFAULT '',";
			
		 } 
		 System.out.println("a:"+f1.getAbsolutePath()+" b:"+f.getAbsolutePath()); 
		 String sql="select * from android_info where getDeviceId='"+record.getStr("getDeviceId")+"' ";
		 if(Db.findFirst(sql)==null)
		 {
			 Db.save("android_info", record);
		 }
		//	}
		}
	}
//	 String b=" PRIMARY KEY (`id`)"
//	 		+ ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;";
//	 String sql=t+c+b;
//	 System.out.println(sql);
	 System.out.println();
	}catch(Exception e){ 
	    e.printStackTrace();
	  }
  }
  public void gethotkey()
  {
	  String keyword=Db.queryStr("select keywords from `txt_keywords` order by rand() limit 1");
	  renderText(keyword);
  }
  public static void delxmlnode() throws Exception
  {
		// 1.得到DOM解析器的工厂实例
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 2.从DOM工厂里获取DOM解析器
		DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse("C:\\work\\svn\\jfinal\\MyJFinal\\WebRoot\\files\\admin\\2016\\10\\09\\18714684420_1475829310919.xml");
	    org.w3c.dom.NodeList list=doc.getElementsByTagName("info");
	    //org.w3c.dom.NodeList list=doc.getElementsByTagName("info");
	    //list.item(0).
	    System.out.println(list.getLength());
	    for(int i=0;i<list.getLength();i++){
			Element brandElement=(Element) list.item(i);
			//brandElement.r
			org.w3c.dom.NodeList item =brandElement.getElementsByTagName("item");
			for(int j=0;j<item.getLength();j++)
			{
				Element branditem =(Element) item.item(j);
				String key=branditem.getAttribute("key");
				String value=branditem.getAttribute("value");
				System.out.println(key+":"+value);
			}
			
			//System.out.println(brandName);
	    }
  }
  public void updatErrorImei()
  {
	  String ph=getPara("ph");
	  if("-1".equals(ph))
	  {
		 List<Record> list= Db.find("select * from account_android where status=-107");
		 for(int i=0;i<list.size();i++)
		 {
			  System.out.println("imei:"+i);
			  Record record=list.get(i);
			  if(record!=null)
			  {
				  try{
					  updatexml(record.getInt("id"),record.getStr("imei"),"/mnt/www/wx/"+record.getStr("devicefile"));	  
				  }catch(Exception e)
				  {
					  
				  }
				  
			  } 
		 }
		  
	  }else
	  {
	  Record record=Db.findFirst("select * from account_android where account='"+ph+"'");
	  if(record!=null)
	  {
		  try{
			  updatexml(record.getInt("id"),record.getStr("imei"),"/mnt/www/wx/"+record.getStr("devicefile"));	  
		  }catch(Exception e)
		  {
			  
		  }
		  
	  } 
	  }
	  renderText("ok");
	  
	  
  }
  public  void updatexml(int id,String frimei,String xmlpath) throws Exception
  {
	    
	    for(int a=0;a<10;a++)
	    {
			// 1.得到DOM解析器的工厂实例
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// 2.从DOM工厂里获取DOM解析器
			DocumentBuilder db = dbf.newDocumentBuilder();
		    Document doc = db.parse(xmlpath);
		    //org.w3c.dom.NodeList list=doc.getChildNodes();
		    org.w3c.dom.NodeList list0=doc.getElementsByTagName("info");
		    if(list0.getLength()>1)
		    {
			    for(int i=0;i<list0.getLength();i++){
					Element e1=(Element) list0.item(i);
					org.w3c.dom.NodeList item =e1.getElementsByTagName("item");
					String imei="";
					for(int j=0;j<item.getLength();j++)
					{
						Element e2 =(Element) item.item(j);
						String key=e2.getAttribute("key");
						String value=e2.getAttribute("value");
						//System.out.println(key+":"+value);
						if(key.equals("getDeviceId"))
						{
							imei=value;
							break;
						}
					} 
					System.out.println("imei:"+imei);
					
					
					
					if(!imei.equals(frimei))
					{
						Node catParent = e1.getParentNode();
					    catParent.removeChild(e1);
					    System.out.println("del");
						
					}else 
					{
						System.out.println("ok");
					}
			    }
			
			    TransformerFactory tFactory =TransformerFactory.newInstance(); 
			    Transformer transformer = tFactory.newTransformer(); 
			    DOMSource source = new DOMSource(doc); 
			    StreamResult result = new StreamResult(new java.io.File(xmlpath)); 
			    transformer.transform(source, result); 
		    }else
		    {
			    for(int i=0;i<list0.getLength();i++){
					Element e1=(Element) list0.item(i);
					org.w3c.dom.NodeList item =e1.getElementsByTagName("item");
					String imei="";
					for(int j=0;j<item.getLength();j++)
					{
						Element e2 =(Element) item.item(j);
						String key=e2.getAttribute("key");
						String value=e2.getAttribute("value");
						//System.out.println(key+":"+value);
						if(key.equals("getDeviceId"))
						{
							imei=value;
							break;
						}
					} 
					System.out.println("imei:"+imei);
					if(!imei.equals(frimei)&&list0.getLength()==1&&imei!=null&&imei.length()>10)
					{
						Db.update(" update account_android set imei='"+imei+"' where id="+id);
					}
			    }
		        
		    }
	    }

  }
  public  void createphone() throws Exception
  {
	 
	    Record record=Db.findById("device_model", 30);
		// 1.得到DOM解析器的工厂实例
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 2.从DOM工厂里获取DOM解析器
		DocumentBuilder db = dbf.newDocumentBuilder();
		// 3.解析XML文档，得到document，即DOM树
		Document doc = db.parse("C:\\work\\svn\\jfinal\\MyJFinal\\WebRoot\\files\\admin\\2016\\10\\09\\17181703820_1475960875019.xml");
		

		org.w3c.dom.NodeList list=doc.getElementsByTagName("item");
		for(int i=0;i<list.getLength();i++){
		Element brandElement=(Element) list.item(i);
		String brandName=brandElement.getAttribute("key");
		System.out.println(brandName);
		if(brandName.equals("getDeviceId")){	
		  brandElement.setAttribute("value",record.getStr("imei"));
		}else if(brandName.equals("android_id"))
		{
			brandElement.setAttribute("value",record.getStr("androidid"));
			
		}else if(brandName.equals("getSubscriberId"))
		{
			brandElement.setAttribute("value",record.getStr("imsi"));
		}else if(brandName.equals("getMacAddress"))
		{
			if(record.getStr("mac")!=null&&record.getStr("mac").length()>0)
			{
				brandElement.setAttribute("value",record.getStr("mac"));
			}
			
		}else if(brandName.equals("getSSID"))
		{
			if(record.getStr("ssid")!=null&&record.getStr("ssid").length()>0)
			{
				brandElement.setAttribute("value",record.getStr("ssid"));
			}
		}else if(brandName.equals("getWidth"))
		{
			brandElement.setAttribute("value",record.getStr("dw"));
		}else if(brandName.equals("getHeight"))
		{
			brandElement.setAttribute("value",record.getStr("dh"));
		}else if(brandName.equals("SDK"))
		{
			brandElement.setAttribute("value",record.getStr("sdk"));
		}else if(brandName.equals("RELEASE"))
		{
			brandElement.setAttribute("value",record.getStr("release"));
		}else if(brandName.equals("getRadioVersion"))
		{
			brandElement.setAttribute("value",record.getStr("radio"));
		}else if(brandName.equals("MODEL"))
		{
			brandElement.setAttribute("value",record.getStr("model"));
		}else if(brandName.equals("MANUFACTURER"))
		{
			brandElement.setAttribute("value",record.getStr("manufacturer"));
		}else if(brandName.equals("HARDWARE"))
		{
			brandElement.setAttribute("value",record.getStr("hardware"));
		}else if(brandName.equals("BRAND"))
		{
			brandElement.setAttribute("value",record.getStr("brand"));
		}else if(brandName.equals("FINGERPRINT"))
		{
			brandElement.setAttribute("value",record.getStr("fingerprint"));
		}else if(brandName.equals("BOARD"))
		{
			if(record.getStr("board")!=null&&record.getStr("board").length()>0)
			{
				brandElement.setAttribute("value",record.getStr("board"));
			}
		}else if(brandName.equals("DEVICE"))
		{
			brandElement.setAttribute("value",record.getStr("device"));
		}else if(brandName.equals("CPU_ABI"))
		{
			brandElement.setAttribute("value",record.getStr("cpu_abi"));
		}else if(brandName.equals("CPU_ABI2"))
		{
			brandElement.setAttribute("value",record.getStr("cpu_abi2"));
		}else if(brandName.equals("PRODUCT"))
		{
			brandElement.setAttribute("value",record.getStr("product"));
		}else if(brandName.equals("HOST"))
		{
			brandElement.setAttribute("value",record.getStr("host"));
		}else if(brandName.equals("USER"))
		{
			brandElement.setAttribute("value",record.getStr("user"));
		}else if(brandName.equals("DISPLAY"))
		{
			brandElement.setAttribute("value",record.getStr("display"));
		}else if(brandName.equals("ID"))
		{
			brandElement.setAttribute("value",record.getStr("bid"));
		}else if(brandName.equals("getSimSerialNumber")) //sim序列号
		{
			brandElement.setAttribute("value",record.getStr("simserialnumber"));
			
		}else if(brandName.equals("getSimState"))  //sim状态
		{
			brandElement.setAttribute("value","5");
		}else if(brandName.equals("getSimOperator"))  //运营商id
		{
			brandElement.setAttribute("value",record.getStr("simoperator"));
			
		}else if(brandName.equals("getSimOperatorName")) //运营商名
		{
			//brandElement.setAttribute("value",record.getStr("cn"));
		}else if(brandName.equals("getSimCountryIso")) //国家码
		{
			brandElement.setAttribute("value","cn");
			
		} 
		
		         
		          
		} 
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer former = factory.newTransformer();
        former.transform(new DOMSource(doc), new StreamResult(new File("d:\\2016-06-03-lishixinxi.xml")));

		 
  }
  public void getimeilist()
  {
	  String table="cp_device_mg";
	  String sql="select imei from "+table+" where     imei is not null and imei <>''";
	  List<String> list=Db.query(sql);
	  for(int i=0;i<list.size();i++)
	  {
		  System.out.println(list.get(i));
		  writerTxt(list.get(i));
	  }
  }
  private void writerTxt(String imei) {
		BufferedWriter fw = null;
		try {
			File file = new File("D://imei.txt");
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
			//fw.append("我写入的内容");
			fw.newLine();
			fw.append(imei);
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
  
	public static void main2(String aa[]) throws Exception
	{
		  //HttpGet httpGet = new HttpGet("http://127.0.0.1/ua.jsp");    
		  System.out.print(methodGet("http://127.0.0.1/ua.jsp"));
        
		    
//	        //設置httpGet的头部參數信息     
//	    
//	        httpGet.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");    
//	    
//	        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");    
//	    
//	        httpGet.setHeader("Accept-Encoding", "gzip, deflate");    
//	    
//	        httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");    
//	    
//	        httpGet.setHeader("Connection", "keep-alive");    
//	    
//	        httpGet.setHeader("Cookie", "__utma=226521935.73826752.1323672782.1325068020.1328770420.6;");    
//	    
//	        httpGet.setHeader("Host", "www.cnblogs.com");    
//	    
//	        httpGet.setHeader("refer", "http://www.baidu.com/s?tn=monline_5_dg&bs=httpclient4+MultiThreadedHttpConnectionManager");    
//	    
//	        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2"); 
//	     
	}
	   /** 
	    * @Title: methodPost  
	    * @Description: httpclient方法中post提交数据的使用  
	    * @param @return 
	    * @param @throws Exception    
	    * @return String     
	    * @throws 
	     */  
	    public static String methodPost() throws Exception {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        // // 代理的设置  
	        // HttpHost proxy = new HttpHost("10.60.8.20", 8080);  
	        // httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,  
	        // proxy);  
	  
	        // 目标地址  
	        HttpPost httppost = new HttpPost(  
	                "http://localhost:8011/testServlet");  
	        System.out.println("请求: " + httppost.getRequestLine());  
	   
	        // post 参数 传递  
	        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();  
	        nvps.add(new BasicNameValuePair("content", "11111111")); // 参数  
	        nvps.add(new BasicNameValuePair("path", "D:/file")); // 参数  
	        nvps.add(new BasicNameValuePair("name", "8")); // 参数  
	        nvps.add(new BasicNameValuePair("age", "9")); // 参数  
	        nvps.add(new BasicNameValuePair("username", "wzt")); // 参数  
	  
	        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); // 设置参数给Post  
	  
	        // 执行  
	        HttpResponse response = httpclient.execute(httppost);  
	        HttpEntity entity = response.getEntity();  
	        System.out.println(response.getStatusLine());  
	        if (entity != null) {  
	            System.out.println("Response content length: "  
	                    + entity.getContentLength());  
	        }  
	        // 显示结果  
	        BufferedReader reader = new BufferedReader(new InputStreamReader(  
	                entity.getContent(), "UTF-8"));  
	  
	        String line = null;  
	        while ((line = reader.readLine()) != null) {  
	            System.out.println(line);  
	        }  
	        if (entity != null) {  
	            entity.consumeContent();  
	        }  
	        return null;  
	  
	    }  
	      
	    /** 
	    * @Title: methodGet  
	    * @Description:  以get方法提交数的使用  
	    * @param @return 
	    * @param @throws Exception    
	    * @return String     
	    * @throws 
	     */  
	    public static  String methodGet(String url) throws  Exception {   
	           DefaultHttpClient httpclient = new DefaultHttpClient();  
	           
//	               // 代理的设置     
//	                HttpHost proxy = new HttpHost("10.60.8.20", 8080);     
//	                httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);     
	  
	  
	            // 目标地址     
	             HttpPost httpGet = new HttpPost(url);     
	             httpGet.setHeader("Referer", "http://www.baidu.com");
	             // 构造最简单的字符串数据     
	              StringEntity reqEntity = new StringEntity("name=test&password=test");     
	             // 设置类型     
	              reqEntity.setContentType("application/x-www-form-urlencoded");     
	             // 设置请求的数据     
	              httpGet.setEntity(reqEntity);     
	            
	             // 执行     
	             HttpResponse response = httpclient.execute(httpGet);     
	             HttpEntity entity = response.getEntity();     
	             System.out.println(response.getStatusLine());     
	               
	            if (entity != null) {     
	               System.out.println("Response content length: " + entity.getContentLength());  //得到返回数据的长度    
	             }     
	             // 显示结果     
	             BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));   
	               
	             String line = null;     
	            while ((line = reader.readLine()) != null) {     
	                 System.out.println(line);     
	             }     
	            if (entity != null) {     
	                  entity.consumeContent();     
	             }     
	            return null;   
	  
	        }   
	      
}
