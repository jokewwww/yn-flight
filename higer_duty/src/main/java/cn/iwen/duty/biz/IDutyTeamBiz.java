package cn.iwen.duty.biz;

import cn.iwen.frame.dao.IBaseDao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.iwen.duty.entity.DutyTeam;
import cn.iwen.duty.entity.Staff;

@Transactional
public interface IDutyTeamBiz extends IBaseDao<DutyTeam> {

	/*
	 * 定时任务
	 * 查询排班人员
	 * */
	void schedulerTask();
	
	void cleanSendModel();
	
	List<Staff> getDutyStaff(List<Integer> timeList);
	
	
	void changeDuty(String[] offWorks);

	String[] getOffWork(String time);
}

