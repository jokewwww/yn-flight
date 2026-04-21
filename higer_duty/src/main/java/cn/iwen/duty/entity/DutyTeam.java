package cn.iwen.duty.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="t_duty_team")
public class DutyTeam {
	
	public final static String REDIS_TYPE = "70";
	
	public final static int AUTO_TEAM_YES = 1;
	
	public final static int AUTO_TEAM_NO = 2;
	
	private List<DutyTime> timeList = new ArrayList<>();
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id",nullable=false)
	private Integer teamId;

	/**
	*组名称
	*/
	@Column(name = "team_name",nullable=false)
	private String teamName;

	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*班组顺序
	*/
	@Column(name = "team_order")
	private Integer teamOrder;

	/**
	*自动排班
	*/
	@Column(name = "team_auto")
	private Integer teamAuto;
	
	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId=teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName=teamName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public List<DutyTime> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<DutyTime> timeList) {
		this.timeList = timeList;
	}

	public Integer getTeamOrder() {
		return teamOrder;
	}

	public void setTeamOrder(Integer teamOrder) {
		this.teamOrder = teamOrder;
	}

	public Integer getTeamAuto() {
		return teamAuto;
	}

	public void setTeamAuto(Integer teamAuto) {
		this.teamAuto = teamAuto;
	}


	
}
