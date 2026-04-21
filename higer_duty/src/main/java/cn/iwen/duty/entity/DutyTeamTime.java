package cn.iwen.duty.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_duty_team_time")
public class DutyTeamTime {
	
	/**
	*所属时间段
	*/
	@Id
	@Column(name = "time_id",nullable=false)
	private Integer timeId;

	/**
	*所在组
	*/
	@Id
	@Column(name = "team_id",nullable=false)
	private Integer teamId;

	
	private String timeIds;
	
	public Integer getTimeId() {
		return timeId;
	}

	public void setTimeId(Integer timeId) {
		this.timeId=timeId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId=teamId;
	}

	public String getTimeIds() {
		return timeIds;
	}

	public void setTimeIds(String timeIds) {
		this.timeIds = timeIds;
	}
	
}
