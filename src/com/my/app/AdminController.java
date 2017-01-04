package com.my.app;


import com.jfinal.core.Controller; 

public class AdminController extends Controller {
	public static String marray1[]={"m_sec","m_biz"};
	public static String marray2[]={"m_sec_user","m_sec_role",
			"m_sec_permission","m_sec_group",
			"m_biz_channel","m_biz_channelUrl",
			"m_biz_channelSms","m_biz_adv","m_biz_cpTask","m_biz_cpTaskLiuc"};
	public void index()
	{
		setAttr("marray1", marray1);
		setAttr("marray2", marray2);
		renderJsp("/admin/index.jsp");
	}
	/*
	public void adlist() {

		int pageNum=getParaToInt(0);
		int pageSize=100;  
		//int firstRow = (pageNum - 1) * pageSize ;	
		Page<AdUrl> page = AdUrl.dao.paginate(pageNum, pageSize, "select *", "from ad_url where id > ?", 0);
		   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow());
		renderJsp("/admin/adurl-list.jsp"); 
	   }
	public void adinfo(){
		
		int id=getParaToInt(0);
		AdUrl adUrl=AdUrl.dao.findById(id);
		setAttr("adUrl",adUrl);
		Page<AdUrl> page = AdUrl.dao.paginate(1, 100, "select *", "from ad_url where id > ?", 0);
		setAttr("list", page.getList());
		renderJsp("/admin/adurl-input.jsp"); 
		
	}
	//@Before(POST.class)
	public void adsave(){
		AdUrl adUrl=getModel(AdUrl.class,"adUrl"); 
		if(adUrl.getInt("id")!=null)
		{
			adUrl.update();
		}else
		{
			adUrl.save();
		}
		redirect("/admin/adlist/1"); 
		
	}
	public void addel()
	{
		int id=getParaToInt(0);
		AdUrl.dao.deleteById(id);
		redirect("/admin/adlist/1");
	}
	
	public void adcount() {
		 
		int pageNum=getParaToInt(0);
		int pageSize=100;  
		//int firstRow = (pageNum - 1) * pageSize ;	
		Page<AdUrl> page = AdUrl.dao.paginate(pageNum, pageSize, "select *", "from ad_url_cnt_view where id > ?", 0);
		   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow());
		renderJsp("/admin/adurl-count.jsp"); 
	   }
  */
}