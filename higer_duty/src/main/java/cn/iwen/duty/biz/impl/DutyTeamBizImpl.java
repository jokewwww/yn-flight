package cn.iwen.duty.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.iwen.duty.biz.IDutyTeamBiz;
import cn.iwen.duty.biz.IDutyTeamTimeBiz;
import cn.iwen.duty.biz.IDutyTimeBiz;
import cn.iwen.duty.biz.IStaffBiz;
import cn.iwen.duty.biz.IStaffTeamBiz;
import cn.iwen.duty.entity.DutyTeam;
import cn.iwen.duty.entity.DutyTime;
import cn.iwen.duty.entity.Staff;
import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.BaseDao;
import cn.iwen.frame.redis.SendMsg2Redis;

@Service
public class DutyTeamBizImpl extends BaseDao<DutyTeam> implements IDutyTeamBiz {

	@Autowired
	private IStaffBiz iStaffBiz;
	
	@Autowired
	private IStaffTeamBiz iStaffTeamBiz;
	
	
	private List<Integer> prevTimeList = new ArrayList<>();
	
	 @Autowired
	 private StringRedisTemplate stringRedisTemplate;
	 
	@Autowired
	private IDutyTimeBiz iDutyTimeBiz;
	
	@Autowired
	private IDutyTeamTimeBiz iDutyTeamTimeBiz;
	
	//机场代码
	@Value("${staffAirportCode}")
	private String staffAirportCode;
	
	
	private int sendModel = 100;
	
	@Override
	public int save(DutyTeam obj) {
		int iret = super.save(obj);
		//若顺序号为空，则设置为主键
		if(obj.getTeamOrder() == null) {
			obj.setTeamOrder(obj.getTeamId());
			super.updateFields(obj, "teamOrder");
		}
		iDutyTeamTimeBiz.addTimes(obj);
		return iret;
	}

	/*
	 * 比较时间段是否一致
	 * */
	private boolean cmpTime(List<Integer> times1,List<Integer>  times2) {
		if(times1 == null || times2 == null) return false;
		if(times1.size() != times2.size()) return false;
		for(Integer num : times1) {
			if(!times2.contains(num)) return false;
		}
		return true;
	}
	//验证是否排第一
	private boolean checkFirst(String name) {
		DutyTeam dutyteam = new DutyTeam();
		dutyteam.setTeamAuto(DutyTeam.AUTO_TEAM_YES);
		List<DutyTeam> teamList = super.getList(dutyteam,"orderNum asc");//查询所有班组
		return name.equals(teamList.get(0).getTeamName());
	}
	/*
	 * 查询下班班组
	 * 返回结果:[上一个分组，当前下班班组，下一个班组]
	 * */
	public String[] getOffWork(String time) {
		String[] strs = new String[3];
		DutyTeam dutyteam = new DutyTeam();
		dutyteam.setTeamAuto(DutyTeam.AUTO_TEAM_YES);
		List<DutyTeam> teamList = super.getList(dutyteam,"orderNum asc");//查询所有班组
		//获取时间段
		int i = 0;
		for(DutyTeam team : teamList) {
			strs[i++] = team.getTeamName();
			if(i == 3) break;
		}
		DutyTime dutyTime = new DutyTime();
		dutyTime.setFlag(DutyTime.ONDUTY_FLAG);//上班
		DutyTime dutyTime1 = iDutyTimeBiz.get(dutyTime);
		if(time.equals(dutyTime1.getStartTimeStr())){
			strs[2] = null;
			return strs;
		}
		dutyTime.setFlag(DutyTime.OFFDUTY_FLAG);//下班
		dutyTime1 = iDutyTimeBiz.get(dutyTime);
		if(time.equals(dutyTime1.getEndTimeStr())){
			return strs;
		}
		strs[0] = null;
		return strs;
	}
	
	/*
	 * 查询下班班组
	 * 返回结果:[上一个分组，当前下班班组，下一个班组]
	 * */
	@Deprecated
	protected String[] getOffWork1(String time) {
		String[] strs = new String[3];
		DutyTeam dutyteam = new DutyTeam();
		dutyteam.setTeamAuto(DutyTeam.AUTO_TEAM_YES);
		List<DutyTeam> teamList = super.getList(dutyteam,"orderNum asc");//查询所有班组
		boolean flag = false;
		for(DutyTeam team : teamList) {
			List<DutyTime> timeList = iDutyTimeBiz.getDutyTimeList(team.getTeamId());
			if(flag) {
				strs[2] = team.getTeamName();
				break;
			}
			for(DutyTime dutytime : timeList) {
				if(flag) {
					flag = false;
					break;
				}
				if(time.equals(dutytime.getEndTimeStr())) {
					flag = true;
				}
			}
			if(flag) {
				strs[1] = team.getTeamName();
			}else {
				strs[0] = team.getTeamName();
			}
		}
		if(flag) {
			if(strs[0] == null) strs[0] = teamList.get(teamList.size() - 1).getTeamName();
			if(strs[2] == null) strs[2] = teamList.get(0).getTeamName();
		}
		return strs;
	}

	//查询值班人员列表
	public List<Staff> getDutyStaff(List<Integer> timeList) {
		List<Staff> staffList = new ArrayList<Staff>();
		if(timeList == null) {
			String time = BaseUtils.date2str(new Date(),"HH:mm");
			timeList = iDutyTimeBiz.matchTime(time);//最新时间段
		}
		if(timeList.isEmpty()) return staffList;
		Date[] dates = caleTaskDate();
		staffList.addAll(iStaffBiz.queryDutyStaff(timeList, dates[0],dates[1],staffAirportCode));
		staffList.addAll(getWordStaff());
		List<Staff> staffListNew = iStaffBiz.dutyStaff(staffList);
		return staffListNew;
	}

	public List<Staff> getWordStaff(){
		List<Staff>  staffList = iStaffBiz.queryWordStaff(staffAirportCode);
		List<Staff> staffListNew = iStaffBiz.dutyStaff(staffList);
		return staffListNew;
	}



	//计算任务查询时间
	private Date[] caleTaskDate() {
		Date now = new Date();
		String nowStr = BaseUtils.date2str(now, BaseUtils.DATE);
		long time = 60 * 1000 * 60 * 4;// 60秒
		Date beforeDate = new Date(now.getTime() - time);// 4小时前的时间
		String beforeDatetr = BaseUtils.date2str(beforeDate, BaseUtils.DATE);
		if (!nowStr.equals(beforeDatetr)) {
		    nowStr = beforeDatetr;
		}
		Date date = BaseUtils.str2date(nowStr, BaseUtils.DATE);
		Date[] dates = new Date[2];
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.HOUR, 4);
		Date kssj = cale.getTime();
		cale.add(Calendar.HOUR, 24);
		Date jssj = cale.getTime(); 
		dates[0] = kssj;
		dates[1] = jssj;
		return dates;
	}
		
	/*
	 * 换班逻辑
	 * 白正 -> 夜缓 -> 夜正 -> 休缓 -> 休正 -> 白缓
	 * */
	@Deprecated
	public void changeDuty1(String[] offWorks) {
		if(offWorks[1] == null) return;
		log.debug("新算法,下班了:" + String.join(",", offWorks));
		if(checkFirst(offWorks[1])) {//休息 白班 夜班
			log.debug("白正 -> 夜缓");
			iStaffTeamBiz.staff2team(offWorks[1],offWorks[2]);//白正 -> 夜缓
			log.debug("休正 -> 白正");
			iStaffTeamBiz.staff2staff(offWorks[0], offWorks[1]);//休正 -> 白正
		}else {//白班 夜班 休息
			log.debug("夜正 -> 休正");
			iStaffTeamBiz.staff2staff(offWorks[1], offWorks[2]);//夜正 -> 休正
			log.debug("夜缓 -> 夜正");
			iStaffTeamBiz.team2staff(offWorks[1],offWorks[1]);//夜缓 -> 夜正
		}
	}
	
	/*
	 * 换班逻辑
	 * */
	public void changeDuty(String[] offWorks) {
		if(offWorks[0] == null) return;
		if(offWorks[2] == null) {//白班 早夜班
			log.debug("早夜班上班：缓存当前早夜班，白班--》早夜班");
			iStaffTeamBiz.staff2team(offWorks[1],offWorks[1]);//缓存
			iStaffTeamBiz.staff2staff(offWorks[0],offWorks[1]);//白班 -》 早夜班
		}else {//白班 早夜班 休息
			log.debug("早夜班下班：正休息->白班，缓存的早夜班-->休息");
			iStaffTeamBiz.staff2staff(offWorks[2],offWorks[0]);//休息-》白班
			iStaffTeamBiz.team2staff(offWorks[1],offWorks[2]);//缓存的早夜班-》休息
		}
	}
	
	@Override
	@Scheduled(cron = "0 0/1 * * * ?")
	public void schedulerTask() {
		String time = BaseUtils.date2str(new Date(),"HH:mm");
		log.debug("开始排班任务 Time:" + time);
		changeDuty(getOffWork(time));//值班表换班
		List<Integer> timeList = iDutyTimeBiz.matchTime(time);//大小班组交接问题
		log.debug("old:" + JSON.toJSONString(prevTimeList) + ",new:" + JSON.toJSONString(timeList));
		System.out.println(1);
		if(!cmpTime(prevTimeList, timeList)) {//时间段变化
			List<Staff> staffList = getDutyStaff(timeList);

			prevTimeList = timeList;
			log.debug("时间段变化，更新值班人员:" + staffList.size());
			//List<Staff> wordStaff1 = getWordStaff();
			//staffList.addAll(wordStaff1);
			//StaffBizImpl.removeDuplicateUser(staffList);
			//log.debug("时间段变化，更新值班人员:增加正在工作中的人员" + JSON.toJSONString(wordStaff1));
			//查询所有调度员
			String toIds = iStaffBiz.getStaffIds("2");
			SendMsg2Redis.testDingYue(stringRedisTemplate,toIds,"",DutyTeam.REDIS_TYPE, staffList);
			sendModel = 0;
			sendMsg(toIds,staffList);
		}
		log.debug("结束排班");
	}
	
	//取消重传
	@Override
	public void cleanSendModel() {
		sendModel = 10;
	}
	
	//重复发送人员值班变更
	private void sendMsg(final String toIds,final List<Staff> staffList) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				log.warn("重复线程开始");
				try {
					while(sendModel++ < 2) {
						Thread.sleep(60 * 1000);
						log.debug("值班人员重发:" + staffList.size() + "," + sendModel);
						SendMsg2Redis.testDingYue(stringRedisTemplate,toIds,"",DutyTeam.REDIS_TYPE, staffList);
					}
				}catch(Exception ex) {}
				log.warn("重复线程结束");
			}
		}).start();
	}

	public String getStaffAirportCode() {
		return staffAirportCode;
	}

	public void setStaffAirportCode(String staffAirportCode) {
		this.staffAirportCode = staffAirportCode;
	}
	
}

