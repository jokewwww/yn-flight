package cn.iwen.duty.biz.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.IStaffBiz;
import cn.iwen.duty.biz.ITaskBiz;
import cn.iwen.duty.entity.Staff;
import cn.iwen.duty.entity.Task;
import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.BaseDao;

@Service
public class StaffBizImpl extends BaseDao<Staff> implements IStaffBiz {

	/**
	 * 登录时存进radis中的key
	 */
	public final static String LOGIN_KEY = "login:";
	
	 @Autowired
	 private StringRedisTemplate stringRedisTemplate;
	 
	@Autowired
	private ITaskBiz iTaskBiz;
	
	@Override
	public void batAddTeam(String teamName, String staffIds) {
		Staff staff = new Staff();
		//清空
		super.updateFields(staff, "staffGroupId", and().eq("staffGroupId", teamName));
		staff.setStaffGroupId(teamName);
		for(String staffId : staffIds.split(",")) {
			staff.setSfvhStaffId(staffId);
			super.updateFields(staff, "staffGroupId");
		}
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
	
	public List<Staff> queryDutyStaff(List<Integer> timeList,Date kssj,Date jssj,String airportCode){
		//查询正进行任务
		String taskingSql = ",(select concat(tt1.task_flight_no,',',tt2.flgt_placecode,',',tt1.task_status,',',tt1.task_id)"
				+ " from T_TASK tt1,T_FLIGHT tt2   " + 
				"where tt1.task_flight_id = tt2.flgt_id and tt1.task_status in (1,2,3,4,5,6)  " +
				"	and tt1.task_acc_time between :kssj and :jssj   " + 
				" 	and tt1.task_ope_staff_id = t1.staff_id   " + 
				" 	order by tt1.task_asg_time desc limit 1) as flgtFlno ";
		
		String vehSql = ",(select tt1.vehi_plate_no  from T_VEHI tt1,T_STAFF_VEHI tt2   " + 
				"where tt1.vehi_no = tt2.sfvh_vehi_no "
				+ " and tt2.sfvh_staff_id = t1.staff_id limit 1) as vehiPlateNo ";
		
		String sql = "select DISTINCT t1.staff_id as sfvhStaffId,t1.staff_name,t1.staff_group_id   " + 
					",t1.staff_airport_code,t1.staff_aptarea_code,t1.staff_level,t1.staff_gender" +
	
		//在线任务
				taskingSql +
				vehSql +
				"from T_STAFF t1 ,t_duty_team t2,t_duty_team_time t3  " +
				"where t1.staff_group_id = t2.team_name and t2.team_id = t3.team_id   " + 
				"and t1.staff_airport_code=:airportCode and t3.time_id in (:timeList) order by t1.staff_level asc ";

		System.err.println("sql : " + sql);

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("timeList", timeList);
		params.put("kssj", kssj);
		params.put("jssj", jssj);
		params.put("airportCode", airportCode);
		return super.query(sql, params);
	}




	public List<Staff> queryWordStaff(String airportCode){
		String sql = "SELECT DISTINCT\n" +
				"\tb.staff_id AS sfvhStaffId,\n" +
				"\tb.staff_name,\n" +
				"\tb.staff_group_id,\n" +
				"\tb.staff_airport_code,\n" +
				"\tb.staff_aptarea_code,\n" +
				"\tb.staff_level,\n" +
				"\ta.task_status,\n" +
				"\tb.staff_gender\n" +
				"\tfrom T_STAFF b  left join T_TASK a on a.task_ope_staff_id = b.staff_id\n" +
				"\t\n" +
				"\twhere a.task_status BETWEEN 1 and 6 and b.staff_airport_code = :airportCode";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("airportCode", airportCode);
		return super.query(sql, params);
	}
	/*
	 * 航班号
	 * 机位号
	 * 连架次 redis
	 * 总架次
	 * 在线 redis
	 * 姓名
	 * --车辆
	 * 休息时间 redis
	 * */
	@Override
	public List<Staff> dutyStaff(List<Staff> staffList) {
		if(staffList.isEmpty()) return staffList;
		Date[] dates = caleTaskDate();
		Date kssj = dates[0],jssj = dates[1];
		log.debug("值班人数:" + staffList.size());
		List<Task> taskList = null; 
		//计算任务数
		for(Staff staff : staffList) {
			if(taskList == null) {
				//查询任务总数
				taskList = iTaskBiz.getTaskList(staff.getStaffAirportCode()
							,staff.getStaffAptareaCode(),kssj,jssj);
				log.debug("今天总任务数:" + taskList.size());
			}
			String flno = staff.getFlgtFlno();
			log.debug("加油员id:" + staff.getSfvhStaffId() + "," + flno);
			//查询是否在线
			if(null == flno) {
				String staffStatus = readKeyValue(LOGIN_KEY + staff.getSfvhStaffId());
				log.debug("人员在线:" + staffStatus);
	            if (null != staffStatus) {
	                // 如果取到说明人员登录
	              staff.setSfvhStaffStatus(1);
	            } else {
	                // 如果取不到说明人员未登录
	              staff.setSfvhStaffStatus(0);
	            }
			}else {
				String[] strs = flno.split(",");
				if(strs.length == 4) {
					staff.setFlgtFlno(strs[0]);
					staff.setFlgtPlacecode(strs[1]);
					staff.setSfvhStaffStatus(2);
					if(StringUtils.isNotEmpty(strs[3])){
						staff.setTaskStatus(BaseUtils.parseInt(strs[2], 0));
					}
					staff.setTaskId(strs[3]);
				}
			}
			String areaCode = staff.getStaffAptareaCode();
			String str = null;
			if(StringUtils.isEmpty(areaCode)) areaCode = "";
			//查询连续休息时间
			Object freetime = readKeyValue(LOGIN_KEY + staff.getSfvhStaffId() + ":" + staff.getStaffAirportCode() 
					+ ":" + areaCode + ":" + "freetime");
			str = freetime == null?"":freetime.toString();
           staff.setStaffDateFree(str.replaceAll("\"", ""));
           log.debug("查询连续休息时间:" + freetime);
			//连续工作时间
			Object dateIng =  readKeyValue(LOGIN_KEY + staff.getSfvhStaffId() + ":" + staff.getStaffAirportCode()
					+ ":" + areaCode + ":" + "staffDateIng");
			str = dateIng == null?"":dateIng.toString();
			staff.setStaffDateIng(str.replaceAll("\"", ""));
			log.debug("连续工作时间:" + dateIng);
			//查询连续架次
			Object taskCount = readKeyValue(LOGIN_KEY + staff.getSfvhStaffId() + ":" + staff.getStaffAirportCode() 
					+ ":" + areaCode + ":" + "taskCount");
			staff.setStaffingTaskCount(taskCount==null?null:Integer.parseInt(taskCount.toString()));
			log.debug("查询连续架次:" + taskCount);
			//计算加油员任务总数
			String staffId = staff.getSfvhStaffId();
			for(Task task : taskList) {
				if(staffId.equals(task.getTaskOpeStaffId()))
					staff.addTaskCount();
			}
			log.debug("加油员任务总数:" + staff.getStaffEndTaskCount());
		}
		ArrayList<Staff> staff = removeDuplicateUser(staffList);
		return staff;
	}
	static ArrayList<Staff> removeDuplicateUser(List<Staff> users) {
		return users.stream().collect(
				Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
						Comparator.comparing(s -> s.getSfvhStaffId()))), ArrayList::new));
	}


	private String readKeyValue(String key) {
		try {
			log.debug(key);
			return stringRedisTemplate.opsForValue().get(key);
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	@Override
	public String getStaffIds(String type) {
		List<String> ids = new ArrayList<String>();
		Staff staff = new Staff();
		staff.setStaffType(type);
		List<Staff> tostaffList = getList(staff);
		for(Staff s : tostaffList) ids.add(s.getSfvhStaffId());
		return String.join(",", ids);
	}

	//加油员调班
	@Override
	public void changeStaffDuty(String srcDuty, String dstDuty) {
		Staff staff = new Staff();
		staff.setStaffGroupId(dstDuty);
		super.updateFields(staff, "staffGroupId", and().eq("staffGroupId", srcDuty));
	}
	
}

