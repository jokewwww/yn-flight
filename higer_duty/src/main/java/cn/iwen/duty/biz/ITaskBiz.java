package cn.iwen.duty.biz;

import cn.iwen.frame.dao.IBaseDao;

import java.util.Date;
import java.util.List;

import cn.iwen.duty.entity.Task;

public interface ITaskBiz extends IBaseDao<Task> {

	//查询完成的任务列表
	List<Task> getTaskList(String aircode,String areaPort,Date kssj,Date jssj);
	
	//查询正在执行的任务列表
	//List<Task> getTaskingList(String aircode,String areaPort,Date kssj,Date jssj);
	
}

