package com.my.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.app.bean.biz.ChannelUrl;
import com.my.app.bean.cp.CpLog;
import com.my.util.TimeUtil;
import com.my.util.TokenUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException; 
 

public class UploadDeviceinfo extends  Controller {
 
	@SuppressWarnings("deprecation")
	public void uploadDeviceinfo() { 

		HttpServletRequest request=getRequest();
		//System.out.println("channel:"+getPara("channel"));
		//HttpServletResponse response=getResponse();
		//List<String[]> list=new ArrayList<String[]>();DiskFileItemFactory 
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000); 
		// 设置临时文件存储位置
		String autoCreatedDateDirByParttern = "yyyy" + File.separatorChar + "MM" + File.separatorChar + "dd";
        String autoCreatedDateDir = DateFormatUtils.format(new java.util.Date(), autoCreatedDateDirByParttern);
        String base = request.getRealPath("/")+"files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir;
		File file = new File(base);
		if(!file.exists())
			file.mkdirs();
		factory.setRepository(file);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
		
		try {
			List<?> items = upload.parseRequest(request);
			FileItem item = null;
			String fileName = null;
			for (int i = 0 ;i < items.size(); i++){
				item = (FileItem) items.get(i);
				fileName =   File.separator +item.getName();
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0) {
					item.write(new File(base+fileName));					 
				}
				String cpinfos[]=TokenUtil.getStrngArray(item.getName().replace(".xml", ""), "-");
				if(cpinfos.length<2)
				{
					continue;
				}
				CpLog cplog=new CpLog();
				cplog.set("ip", getIpAddr(request))
				.set("deviceinfo", "files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir+fileName)
				.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("lastdate", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("phone", cpinfos[0])
				.set("pwd", cpinfos[1])
				.set("channel", getPara("channel"))
				.set("cpid", getPara("cpid"))
				.set("packname", getPara("packname"))
				.set("status", 1)
				.set("logincnt", 1)
				.set("city", getPara("city"))
				.set("dnum", getPara("dnum"))
				.set("dmodel", getPara("dmodel"));
				
				
				if(cpinfos.length>2)
				{
					cplog.set("ip",getIpAddr(request));
				}
				cplog.set("ip",getIpAddr(request));
				cplog.save();
				
				
				
			}
			renderText("ok");
		} catch (FileUploadException e) {
			e.printStackTrace();
			renderText("no");
		} catch (Exception e) {
			e.printStackTrace();
			renderText("no");
		}
		 
	}
	@SuppressWarnings("deprecation")
	public void uploadNewCp() { 
		HttpServletRequest request=getRequest();
		//System.out.println("channel:"+getPara("channel"));
		//HttpServletResponse response=getResponse();
		//List<String[]> list=new ArrayList<String[]>();DiskFileItemFactory 
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(10240000); 
		// 设置临时文件存储位置
		String autoCreatedDateDirByParttern = "yyyy" + File.separatorChar + "MM" + File.separatorChar + "dd";
        String autoCreatedDateDir = DateFormatUtils.format(new java.util.Date(), autoCreatedDateDirByParttern);
        String base = request.getRealPath("/")+"files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir;
		File file = new File(base);
		if(!file.exists())
			file.mkdirs();
		factory.setRepository(file);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(10002400000l);
		// 设置整个request的最大值
		upload.setSizeMax(10002400000l);
		upload.setHeaderEncoding("UTF-8");
		
		try {
			List<?> items = upload.parseRequest(request);
			FileItem item = null;
			String fileName = null;
			for (int i = 0 ;i < items.size(); i++){
				item = (FileItem) items.get(i);
				fileName =   File.separator +item.getName();
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0) {
					item.write(new File(base+fileName));					 
				}
				String cpinfos[]=TokenUtil.getStrngArray(item.getName().replace(".xml", ""), "-");
				if(cpinfos.length<2)
				{
					continue;
				}
				Record cplog=new Record();
				cplog.set("ip", getIpAddr(request))
				.set("deviceinfo", "files"+File.separatorChar+"admin"+File.separatorChar+autoCreatedDateDir+fileName)
				.set("adddate", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("lastdate", TimeUtil.GetSqlDate(System.currentTimeMillis()))
				.set("channel", getPara("channel"))
				.set("cpid", getPara("cpid"))
				.set("packname", getPara("packname"))
				.set("status", 1)
				.set("logincnt", 1)
				.set("city", getPara("city"))
				.set("dnum", getPara("dnum"))
				.set("dmodel", getPara("dmodel"));
				cplog.set("ip",getIpAddr(request));
				Db.save("cp_install_log", cplog);
				
				
				
			}
			renderText("ok");
		} catch (FileUploadException e) {
			e.printStackTrace();
			renderText("no");
		} catch (Exception e) {
			e.printStackTrace();
			renderText("no");
		}
		 
	}

 
	/**********************
	 * 取得真实ip地址
	 * @param request
	 * @return
	 */
	public  String getIpAddr(HttpServletRequest request) {
		 String ip = request.getHeader("x-forwarded-for");  
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		     ip = request.getHeader("Proxy-Client-IP");  
		 }  
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		     ip = request.getHeader("WL-Proxy-Client-IP");  
		 }  
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
		     ip = request.getRemoteAddr();  
		 }  
		 return ip;  
		}
}
