package cn.iwen.duty.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.IStaffBiz;
import cn.iwen.duty.biz.IStaffTeamBiz;
import cn.iwen.duty.entity.Staff;
import cn.iwen.duty.entity.StaffTeam;
import cn.iwen.frame.dao.BaseDao;

@Service
public class StaffTeamBizImpl extends BaseDao<StaffTeam> implements IStaffTeamBiz {

	@Autowired
	private IStaffBiz iStaffBiz;
	
	@Override
	public void staff2team(String srcName, String dstName) {
		Staff staff = new Staff();
		staff.setStaffGroupId(srcName);
		List<Staff> staffList = iStaffBiz.getList(staff);
		StaffTeam st = new StaffTeam();
		st.setTeamName(dstName);
		for(Staff s : staffList) {
			st.setStaffId(s.getSfvhStaffId());
			super.add(st);
		}
	}

	@Override
	public void team2staff(String srcName, String dstName) {
		StaffTeam st = new StaffTeam();
		st.setTeamName(srcName);
		List<StaffTeam> stList = super.getList(st);
		Staff staff = new Staff();
		staff.setStaffGroupId(dstName);
		int iret = -1;
		for(StaffTeam e : stList) {
			staff.setSfvhStaffId(e.getStaffId());
			iret = iStaffBiz.updateFields(staff,"staffGroupId");
			log.debug("更新结果:" + iret);
		}
		iret = super.delete(st);
		log.debug("删除数:" + iret);
	}

	//人员到人员
	@Override
	public void staff2staff(String srcName, String dstName) {
		Staff staff = new Staff();
		staff.setStaffGroupId(dstName);
		int iret = iStaffBiz.update(staff, And(Staff.class).eq("staffGroupId", srcName));
		log.debug("设置人数:" + iret);
	}

}


