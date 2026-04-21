package cn.iwen.duty.entity;

import javax.persistence.*;

@Entity
@Table(name="t_sys_code")
public class SysCode {
	
	//字典类型
	@Column(name = "code_type_name",insertable = false)
	@ManyToMany(targetEntity = SysCode.class,mappedBy = "codeName;codeType=1;codeValue=codeType")
	private String codeTypeName;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code_id",nullable=false)
	private Integer codeId;

	/**
	*字典名称
	*/
	@Column(name = "code_name",nullable=false)
	private String codeName;

	/**
	*字典类别
	*/
	@Column(name = "code_type",nullable=false)
	private String codeType;

	/**
	*字段值
	*/
	@Column(name = "code_value",nullable=false)
	private String codeValue;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc1")
	private String codeDesc1;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc2")
	private String codeDesc2;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc3")
	private String codeDesc3;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc4")
	private String codeDesc4;

	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;


	
	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId=codeId;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName=codeName;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType=codeType;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue=codeValue;
	}

	public String getCodeDesc1() {
		return codeDesc1;
	}

	public void setCodeDesc1(String codeDesc1) {
		this.codeDesc1=codeDesc1;
	}

	public String getCodeDesc2() {
		return codeDesc2;
	}

	public void setCodeDesc2(String codeDesc2) {
		this.codeDesc2=codeDesc2;
	}

	public String getCodeDesc3() {
		return codeDesc3;
	}

	public void setCodeDesc3(String codeDesc3) {
		this.codeDesc3=codeDesc3;
	}

	public String getCodeDesc4() {
		return codeDesc4;
	}

	public void setCodeDesc4(String codeDesc4) {
		this.codeDesc4=codeDesc4;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public String getCodeTypeName() {
		return codeTypeName;
	}

	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}
	
}
