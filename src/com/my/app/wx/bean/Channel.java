package com.my.app.wx.bean;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class Channel extends Model<Channel>{
	
	public static final Channel dao = new Channel() ;
	public List<Channel> getChannels()
	{
		 List<Channel> clist=new ArrayList<Channel>();
		 if(getLong("id")!=null)
		 {
			 clist=Channel.dao.find("select * from biz_channel where aid="+getLong("id"));
		 }
		 return clist;
	}

}
