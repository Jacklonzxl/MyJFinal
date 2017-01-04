package com.my.app.biz;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.biz.Channel;
import com.my.app.bean.biz.ChannelUrl;
import com.my.util.TimeUtil;
@RequiresRoles("R_ADMIN")
public class ChannelUrlController   extends Controller{

	//�����Ŀ¼
	private String root="/admin/";
	//��ǰ����·��
	private String path="biz/channelUrl";
	//����
	private  String tableName=TableMapping.me().getTable(ChannelUrl.dao.getClass()).getName();
	//ģ����ѯ
	private  String keywordArray[]={"name"};
	//Ψһֵ
	private String onlyval="name";
	//��λ���˵�
	private  String m1="m_biz";
	//��λ�Ӳ˵�
	private  String m2="m_biz_channelUrl";
	
	/***
	 * �����(�б�)
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
		int channelid=getPara("channelid")!=null?getParaToInt("channelid"):-1;
		if(channelid>-1)
		{
			and+=" and channelid="+channelid;
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
		List<Channel> clist=Channel.dao.find("select * from biz_channel");
		setAttr("clist", clist);
		tableName=" (select a.*,b.name channelname from "+tableName+" a left join biz_channel b on b.id=a.channelid) t";
		//int firstRow = (pageNum - 1) * pageSize ;	
		Page<ChannelUrl> page = ChannelUrl.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
		   
		setAttr("list", page.getList());
		setAttr("psize", page.getTotalPage());
		setAttr("pageNum", pageNum);
		setAttr("count", page.getTotalRow()); 
		initAttr();
		renderJsp(root+path+".jsp");		
	}
	/***
	 * �༭����
	 */
	public void input() {
		int id=getPara("id")!=null?getParaToInt("id"):0;
		ChannelUrl channelUrl=ChannelUrl.dao.findById(id);
		setAttr("channelUrl", channelUrl);
		List<Channel> clist=Channel.dao.find("select * from biz_channel ");
		setAttr("clist", clist);
		initAttr();
		renderJsp(root+path+"-input.jsp");		
	}
	/***
	 * ���浽DB
	 */
	public void save() {
		ChannelUrl channelUrl=getModel(ChannelUrl.class); 
		ChannelUrl dbchannelUrl=ChannelUrl.dao.findFirst("select * from "+tableName+" where "+onlyval+"='"+channelUrl.getStr(onlyval)+"'");
		if(channelUrl.getInt("id")!=null&&channelUrl.getInt("id")>0&&
		  (dbchannelUrl==null||dbchannelUrl.get(onlyval).equals(channelUrl.get(onlyval))))
		{   
			channelUrl.update();
		}else if(dbchannelUrl==null)
		{ 
			channelUrl.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
			channelUrl.save();
		}
		redirect(root+"?pg="+path+"&m1="+m1+"&m2="+m2+"&path=/"+path);		
	}
	/***
	 * ɾ��
	 */	
	public void del() {
		int id = getPara("id") != null ? getParaToInt("id") : -1;
		if(id>0)
		{
			ChannelUrl.dao.deleteById(id);
		}else
		{
		   String ids[]=getParaValues("ids");
		   for(int i=0;i<ids.length;i++)
		   {
			   ChannelUrl.dao.deleteById(ids[i]);
		   }
		}
		index() ;
		
	}
	public void initAttr()
	{
		setAttr("m1", m1);    
		setAttr("m2", m2);
		setAttr("bean", "channelUrl");
		setAttr("path", "/"+path);
	}
}