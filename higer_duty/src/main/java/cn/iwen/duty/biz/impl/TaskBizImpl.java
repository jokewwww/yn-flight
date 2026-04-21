package cn.iwen.duty.biz.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.ITaskBiz;
import cn.iwen.duty.entity.Task;
import cn.iwen.frame.dao.BaseDao;
import cn.iwen.frame.dao.builder.SelectBuilder;

@Service
public class TaskBizImpl extends BaseDao<Task> implements ITaskBiz {

	@Override
	public List<Task> getTaskList(String aircode, String areaPort,Date kssj,Date jssj) {
		SelectBuilder builder = select();
		builder.addField("taskOpeStaffId");
		builder.addCond(and()
				.eq("taskAirportCode", aircode)
				.eq("taskAptareaCode", areaPort)
				.between("taskAccTime", kssj,jssj));
		return super.query(builder);
	}

}

