package cn.iwen.duty.biz;

import cn.iwen.duty.entity.StaffTeam;
import cn.iwen.frame.dao.IBaseDao;

public interface IStaffTeamBiz extends IBaseDao<StaffTeam> {

	//人员到班组
	void staff2team(String srcName,String dstName);
	
	//人员到人员
	void staff2staff(String srcName,String dstName);
		
	
	//班组到人员
	void team2staff(String srcName,String dstName);
		
}

