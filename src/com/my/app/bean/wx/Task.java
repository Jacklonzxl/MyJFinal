package com.my.app.bean.wx;

import java.util.List;

public class Task {
	
	private String tasktype;
	private String taskid;
	private List<Data> Data;
	public String getTasktype() {
		return tasktype;
	}
	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public List<Data> getData() {
		return Data;
	}
	public void setData(List<Data> data) {
		Data = data;
	}
 

}
