package cn.iwen.duty.entity;

import javax.persistence.*;
import cn.iwen.frame.BaseUtils;
@Entity
@Table(name="t_duty_time")
public class DutyTime {
	
	private static final int ONEDAY_TIME = 60 * 24;
	public static final int ONDUTY_FLAG = 1;
	public static final int OFFDUTY_FLAG = 2;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "time_id",nullable=false)
	private Integer timeId;

	//验证日期格式
	public static short fmtTime(String time) {
		if(time == null || time.length() != 5) return -1;
		if(!time.matches("\\d{2}:\\d{2}")) return -1;
		String[] strs = time.split(":");
		int h = BaseUtils.parseInt(strs[0], -1);
		int m = BaseUtils.parseInt(strs[1], -1);
		if(h >= 24 || m >= 60) return -1;
		return (short)(h * 60 + m);
	}
	
	public boolean check(short time) {
		int iend = (startTime > endTime)?ONEDAY_TIME + endTime:endTime; 
		if(time >= startTime && time <= iend) return true;
		if(time < startTime && time + ONEDAY_TIME <= iend) return true;
		return false;
	}
	
	/**
	*开始时间
	*/
	@Column(name = "start_time",nullable=false)
	private Short startTime;

	/**
	*结束时间
	*/
	@Column(name = "end_time",nullable=false)
	private Short endTime;

	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;


	/**
	*排班标志
	*/
	@Column(name = "flag")
	private Integer flag;

	
	public Integer getTimeId() {
		return timeId;
	}

	public void setTimeId(Integer timeId) {
		this.timeId=timeId;
	}

	public Short getStartTime() {
		return startTime;
	}

	public String getStartTimeStr() {
		return String.format("%02d:%02d", startTime / 60,startTime % 60);
	}
	
	public void setStartTime(Short startTime) {
		this.startTime=startTime;
	}

	public Short getEndTime() {
		return endTime;
	}

	public String getEndTimeStr() {
		return String.format("%02d:%02d", endTime / 60,endTime % 60);
	}
	
	public void setEndTime(Short endTime) {
		this.endTime=endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	
}
