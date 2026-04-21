package cn.iwen.duty.biz;

import cn.iwen.frame.dao.IBaseDao;
import cn.iwen.duty.entity.DutyTeam;
import cn.iwen.duty.entity.DutyTeamTime;

public interface IDutyTeamTimeBiz extends IBaseDao<DutyTeamTime> {

	void addTimes(DutyTeamTime teamTime);
	
	void addTimes(DutyTeam dutyteam);
	
}

