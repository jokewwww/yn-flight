package cn.iwen.duty.biz;

import cn.iwen.frame.dao.IBaseDao;

import java.util.List;

import cn.iwen.duty.entity.DutyTime;

public interface IDutyTimeBiz extends IBaseDao<DutyTime> {

	//获取匹配的时间段
	List<Integer> matchTime(String time);
	
	List<DutyTime> getDutyTimeList(Integer teamId);
}

