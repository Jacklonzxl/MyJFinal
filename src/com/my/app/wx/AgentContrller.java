package com.my.app.wx; 

 

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.my.app.bean.sec.User;
import com.my.app.wx.bean.Agent;
import com.my.app.wx.bean.Channel;
import com.my.app.wx.bean.FollowTask;
import com.my.util.TimeUtil;
/**
 * ��ɫ����
 * @author zcj
 *
 */
@RequiresRoles("R_ADMIN") 
public class AgentContrller  extends Controller{
	//�����Ŀ¼
		private String root="/wx/";
		
		private String m_name="agent";
		//��ǰ����·��
		private String path="channel/"+m_name;
		//����
		private  String tableName=TableMapping.me().getTable(Channel.dao.getClass()).getName();
		//ģ����ѯ
		private  String keywordArray[]={"name","prov","city","address","content","created_at"};


		//Ψһֵ
		private String onlyval="name"; 
		/***
		 * �����(�б�)
		 */
		public void index() {
			int pageNum=getPara("pageNum")!=null?getParaToInt("pageNum"):1;
			int pageSize=20;  
			String and=" and type=2";
			int status=getPara("status")!=null?getParaToInt("status"):-1;
			if(status>-1)
			{
				and+=" and status="+status;
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
			//System.out.println(and);
			//int firstRow = (pageNum - 1) * pageSize ;	
			Page<Channel> page = Channel.dao.paginate(pageNum, pageSize, "select *", "from "+tableName+" where id>0 "+and+" order by id desc");
			   
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
			Channel channel=Channel.dao.findById(id);
			setAttr("bean", channel);
			List<User> ulist=User.dao.find("select * from sec_user  where group_id=3 ");
			setAttr("ulist", ulist);
			initAttr();
			renderJsp(root+path+"-input.jsp");		
		}
		/***
		 * ���浽DB
		 */
		public void save() {
			Agent bean=getModel(Agent.class,"bean"); 
			long id=bean.getInt("id")!=null?Long.parseLong(String.valueOf(bean.getInt("id"))):0;
			Agent agent=Agent.dao.findById(id);
			if(agent!=null){
				String f[]=bean._getAttrNames();
				for(int i=0;i<f.length;i++)
				{
					agent.set(f[i], bean.get(f[i]));
				}
				agent.update();
			}else
			{
				bean.set("created_at", TimeUtil.GetSqlDate(System.currentTimeMillis()));
				bean.set("userid",1);
				bean.save();
			}
			renderJsp(root+"success.jsp");		
		}
		/***
		 * ɾ��
		 */	
		public void del() {
			int id = getPara("id") != null ? getParaToInt("id") : -1;
			if(id>0)
			{
				Channel.dao.deleteById(id);
			}else
			{
			   String ids[]=getParaValues("ids");
			   for(int i=0;i<ids.length;i++)
			   {
				   Channel.dao.deleteById(ids[i]);
			   }
			}
			renderText("ɾ���ɹ�!");
			
		}
		public void initAttr()
		{
			setAttr("m_name", m_name);
		}
}