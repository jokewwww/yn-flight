package cn.iwen.duty.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.IDutyTimeBiz;
import cn.iwen.duty.entity.DutyTime;
import cn.iwen.frame.dao.BaseDao;

@Service
public class DutyTimeBizImpl extends BaseDao<DutyTime> implements IDutyTimeBiz {

	@Override
	public List<Integer> matchTime(String time) {
		List<Integer> idList = new ArrayList<Integer>();
		List<DutyTime> timeList = super.getList(null);
		short itime = DutyTime.fmtTime(time);
		if(itime < 0) return idList;
		for(DutyTime dtime : timeList) {
			if(dtime.check(itime))
				idList.add(dtime.getTimeId());
		}
		return idList;
	}

	@Override
	public List<DutyTime> getDutyTimeList(Integer teamId) {
		String sql = "select t1.* from t_duty_time t1,t_duty_team_time t2 "
				+ " where t1.time_id = t2.time_id and t2.team_id = :teamId order by t1.time_id asc";
		Map<String,Object> params = new HashMap<>();
		params.put("teamId", teamId);
		return super.query(sql,params);
	}

}

