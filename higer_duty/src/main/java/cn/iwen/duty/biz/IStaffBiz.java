package cn.iwen.duty.biz;

import cn.iwen.frame.dao.IBaseDao;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.iwen.duty.entity.Staff;

public interface IStaffBiz extends IBaseDao<Staff> {

	
	@Transactional
	void batAddTeam(String teamName,String staffIds);
	
	List<Staff> queryDutyStaff(List<Integer> timeList,Date kssj,Date jssj,String airportCode);
	
	List<Staff> dutyStaff(List<Staff> staffList);
	 
	//查询所有调度员id
	String getStaffIds(String type);
	
	//人员调班
	void changeStaffDuty(String srcDuty,String dstDuty);

	List<Staff> queryWordStaff(String airportCode);
}

