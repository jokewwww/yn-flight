package cn.iwen.duty.entity;

import javax.persistence.*;

//人班组关联表
@Table(name="t_staff_team")
@Entity
public class StaffTeam {
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "staff_team_id",nullable=false)
	private Integer staffTeamId;

	/**
	*组名称
	*/
	@Column(name = "team_name",nullable=false)
	private String teamName;

	/**
	*加油员id
	*/
	@Column(name = "staff_id")
	private String staffId;

	public Integer getStaffTeamId() {
		return staffTeamId;
	}

	public void setStaffTeamId(Integer staffTeamId) {
		this.staffTeamId = staffTeamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	
}
