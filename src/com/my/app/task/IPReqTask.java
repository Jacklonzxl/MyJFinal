package com.my.app.task;


import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.HttpUtil;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil; 

public class IPReqTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(3000);
		while(true)
		{
 	        List<Record>  list=Db.find("select * from ip_task");
 	        for(int i=0;list!=null&&i<list.size();i++)
 	        {
 	        	Record info=list.get(i);
				Record ipcount = Db.findFirst("select count(*) cnt from ip_info where status=1 and prov='"+info.getStr("prov")+"' and city='"+info.getStr("city")+"' and op='"+info.getStr("op")+"'");
				if(ipcount.getLong("cnt")<10)
				{
					String filter=URLEncoder.encode(info.getStr("city")+"@"+info.getStr("op"),"utf8");
					String url=info.getStr("url")+filter+"";
					String str=HttpUtil.doPost(url, new HashMap<String,String>()); 
					if(str!=null&&str.length()>5)
					{
						String iplist[]=TokenUtil.getStrngArray(str.replaceAll("\r|\n", "").replaceAll("<br>", ""), "|");
						for(int j=0;j<iplist.length;j++)
						{
							String passdate=TimeUtil.GetSqlDate(System.currentTimeMillis()-1000*60*60*24L);
							System.out.println(iplist[j]);
							String ips[]=TokenUtil.getStrngArray(iplist[j],":");
							Long cnt=Db.queryFirst("select count(*) cnt from ip_info where ip='"+ips[0]+"' and adddate>'"+passdate+"'");
							//System.out.println("???"+rcnt.get("cnt").toString());
							if(cnt==0)
							{
							try{	
							String ipstr=HttpUtil.doGet("http://121.196.224.72/ip.jsp", "utf-8", ips);	
							System.out.println("ipstr:"+ipstr);
							if(ipstr!=null&&ipstr.length()>5)
							{
							Record record=new Record()
							.set("prov", info.getStr("prov"))
							.set("city", info.getStr("city"))
							.set("op", info.getStr("op"))
							.set("ip", ips[0])
							.set("port", ips[1])
							.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()))
							.set("status", 1);
							 Db.save("ip_info", record);
							}
							}catch(Exception e)
							{
								System.out.println(e.toString());
							}
							}
							
						}
					}
				}
	 	        }
				sleep(5000);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String aa[]) throws Exception
	{
//		String filter=URLEncoder.encode("深圳@电信","utf8");
//		System.out.println(URLEncoder.encode("深圳@电信","utf8"));
//		String url="http://www.tkdaili.com/api/getiplist.aspx?vkey=5E094C581EBC3E84DEDD2A8F3BCE9994&num=1&password=fucking2016&filter="+filter+"&style=3";
//		String str=HttpUtil.doPost(url, new HashMap<String,String>()); 
//		String ips_str[]=TokenUtil.getStrngArray(str.replaceAll("\r|\n", ""), "|");
//		String ips[]=TokenUtil.getStrngArray(ips_str[0],":");
//		System.out.println("ip:"+ips[0]);
//	    System.out.println("port:"+ips[1]);
//		String ipstr=HttpUtil.doGet("http://121.196.224.72/ip.jsp", "utf-8", ips);
//		System.out.println(ipstr);
        String imeiString="35566778898256";//前14位  
        char[] imeiChar=imeiString.toCharArray();  
        int resultInt=0;  
        for (int i = 0; i < imeiChar.length; i++) {  
            int a=Integer.parseInt(String.valueOf(imeiChar[i]));  
            i++;  
            final int temp=Integer.parseInt(String.valueOf(imeiChar[i]))*2;  
            final int b=temp<10?temp:temp-9;  
            resultInt+=a+b;  
        }  
        resultInt%=10;  
        resultInt=resultInt==0?0:10-resultInt;  
        System.out.println("imei:"+imeiString+resultInt);  
	}
 

}
