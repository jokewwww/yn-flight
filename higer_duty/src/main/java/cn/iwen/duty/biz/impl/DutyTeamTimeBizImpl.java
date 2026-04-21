package cn.iwen.duty.biz.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.IDutyTeamTimeBiz;
import cn.iwen.duty.entity.DutyTeam;
import cn.iwen.duty.entity.DutyTeamTime;
import cn.iwen.duty.entity.DutyTime;
import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.BaseDao;

@Service
public class DutyTeamTimeBizImpl extends BaseDao<DutyTeamTime> implements IDutyTeamTimeBiz {

	@Override
	public int delete(DutyTeamTime obj) {
		return super.delete(and().eq("teamId", obj.getTeamId())
				.eq("timeId", obj.getTimeId()));
	}

	@Override
	public void addTimes(DutyTeamTime teamTime) {
		super.delete(and().eq("teamId", teamTime.getTeamId()));
		List<Integer> timeIds = BaseUtils.str2IntList(teamTime.getTimeIds());
		for(Integer timeId : timeIds) {
			teamTime.setTimeId(timeId);
			super.add(teamTime);
		}
	}

	@Override
	public void addTimes(DutyTeam dutyteam) {
		super.delete(and().eq("teamId", dutyteam.getTeamId()));
		if(dutyteam.getTimeList().isEmpty()) return;
		DutyTeamTime teamTime = new DutyTeamTime();
		teamTime.setTeamId(dutyteam.getTeamId());
		for(DutyTime dutytime : dutyteam.getTimeList()) {
			teamTime.setTimeId(dutytime.getTimeId());
			super.add(teamTime);
		}
	}

}

