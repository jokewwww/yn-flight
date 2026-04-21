package com.zh.bean.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务表
 * T_TASK
 */
@ApiModel(value = "任务表")
public class MyTask implements Serializable {
    /**
     * 任务ID（uuid）
     */
    @ApiModelProperty(value = "任务ID（uuid）")
    private String taskId;

    /**
     * 航班ID
     */
    private String taskFlightId;

    /**
     * 航班号
     */
    private String taskFlightNo;

    /**
     * 所属机场代码
     */
    private String taskAirportCode;

    /**
     * 所属机场区域代码
     */
    private String taskAptareaCode;

    /**
     * 加油员员工ID
     */
    private String taskOpeStaffId;

    /**
     * 加油员员工名称（业务字段）
     */
    private String taskOpeStaffName;


    /**
     * 任务内容（0：加油，1：抽油）
     */
    private Integer taskContent;

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     */
    private Integer taskStatus;

    /**
     * 任务派发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskAsgTime;

    /**
     * 任务接受时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskAccTime;

    /**
     * 加油开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskChagStaTime;

    /**
     * 加油到位时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskArriveTime;
    
	/**
	 * 打印油单完成时间
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date taskRcPrintTime;

	
    /**
     * 加油完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskChagEndTime;

    /**
     * 任务完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskDoneTime;

    /**
     * 加油单编号
     */
    private String taskFuelRecptNo;

    /**
     * 加油车编号
     */
    private String taskVehiNo;

    /**
     * 创建人员工ID
     */
    private String taskCreStaffId;
    
    /**
     * 创建人员工名称（业务字段）
     */
    private String taskCreStaffName;

    /**
     * 客户编号
     */
    private String customNum;

    /**
     * 飞机号
     */
    private String flgtRegn;

    /**
     * 信用评级
     */
    private String cilvl;

    /**
     * 信用状态：0-正常（默认）；1-提示（低风险）；2-警示（高风险）
     */
    private String citst;

    /**
     * 信用的描述信息
     */
    private String cirmk;

    public String getCilvl() {
        return cilvl;
    }

    public void setCilvl(String cilvl) {
        this.cilvl = cilvl;
    }

    public String getCitst() {
        return citst;
    }

    public void setCitst(String citst) {
        this.citst = citst;
    }

    public String getCirmk() {
        return cirmk;
    }

    public void setCirmk(String cirmk) {
        this.cirmk = cirmk;
    }

    public String getFlgtRegn() {
        return flgtRegn;
    }

    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn;
    }

    public String getCustomNum() {
        return customNum;
    }

    public void setCustomNum(String customNum) {
        this.customNum = customNum;
    }

    /**
     * 创建人员工名称（业务字段）
     */
    public String getTaskCreStaffName() {
		return taskCreStaffName;
	}

    /**
     * 创建人员工名称（业务字段）
     */
	public void setTaskCreStaffName(String taskCreStaffName) {
		this.taskCreStaffName = taskCreStaffName;
	}
	
    /**
     * 任务星标（1：是，0：不是）默认0
     */
    private Integer taskStarmark;

    /**
     * 记录创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskRecCreTime;
    
    /**
     * 起飞油量
     */
    private Integer taskTakeoffFuel;

    /**
     * 轮挡油量
     */
    private Integer taskChockFuel;

    /**
     * 应加油量
     */
    private Integer taskTotalFuel;
    /**
     * 预加油量
     */
    private Integer flgtOtatFuel;
    /**
     * 仪表类型（KG，LB）
     */
    private String taskMeterType;

    /**
     * 中央邮箱油量
     */
    private Integer taskCentTank;

    /**
     * 左机翼油箱油量
     */
    private Integer taskLeftTank;

    /**
     * 右机翼油箱油量
     */
    private Integer taskRightTank;

    /**
     * 机组签名（JPG图片的base64编码）
     */
    private String taskCrewSign;
    
    /**
     * 业务字段（机位）
     */
    private String flgtPlacecode;
    
    /**
     * 业务字段（航空公司名）
     */
    private String flgtAlcname;

    /**
     * 业务字段（油单类型）
     */
    private Integer flrcType;

    /***
    * @Description: 老油单编号
    * @Param:
    * @return:
    * @Author: XiuHongXin
    * @Date: 2019/9/14
    */
    private String  oldFlrcNo;

    /*
      默认非保税 , (保税B 非保税 FB)
     */
    private String flrcBwtar;

    private Integer fuelPubType;


    private Integer flgtStatus;

    public Integer getFlgtStatus() {
        return flgtStatus;
    }

    public void setFlgtStatus(Integer flgtStatus) {
        this.flgtStatus = flgtStatus;
    }

    public Integer getFuelPubType() {
        return fuelPubType;
    }

    public void setFuelPubType(Integer fuelPubType) {
        this.fuelPubType = fuelPubType;
    }

    public String getFlrcBwtar() {
        return flrcBwtar;
    }

    public void setFlrcBwtar(String flrcBwtar) {
        this.flrcBwtar = flrcBwtar;
    }

    public String getOldFlrcNo() {
        return oldFlrcNo;
    }

    public void setOldFlrcNo(String oldFlrcNo) {
        this.oldFlrcNo = oldFlrcNo;
    }

    /**
     * 业务字段（油单类型）
     */
    public Integer getFlrcType() {
		return flrcType;
	}

    /**
     * 业务字段（油单类型）
     */
	public void setFlrcType(Integer flrcType) {
		this.flrcType = flrcType;
	}

	/**
     * 业务字段（机位）
     */
    public String getFlgtPlacecode() {
		return flgtPlacecode;
	}

    /**
     * 业务字段（机位）
     */
	public void setFlgtPlacecode(String flgtPlacecode) {
		this.flgtPlacecode = flgtPlacecode;
	}

	/**
     * T_TASK
     */
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID（uuid）
     * @return task_id 任务ID（uuid）
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 任务ID（uuid）
     * @param taskId 任务ID（uuid）
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * 航班ID
     * @return task_flight_id 航班ID
     */
    public String getTaskFlightId() {
        return taskFlightId;
    }

    /**
     * 航班ID
     * @param taskFlightId 航班ID
     */
    public void setTaskFlightId(String taskFlightId) {
        this.taskFlightId = taskFlightId == null ? null : taskFlightId.trim();
    }

    /**
     * 航班号
     * @return task_flight_no 航班号
     */
    public String getTaskFlightNo() {
        return taskFlightNo;
    }

    /**
     * 航班号
     * @param taskFlightNo 航班号
     */
    public void setTaskFlightNo(String taskFlightNo) {
        this.taskFlightNo = taskFlightNo == null ? null : taskFlightNo.trim();
    }

    /**
     * 所属机场代码
     * @return task_airport_code 所属机场代码
     */
    public String getTaskAirportCode() {
        return taskAirportCode;
    }

    /**
     * 所属机场代码
     * @param taskAirportCode 所属机场代码
     */
    public void setTaskAirportCode(String taskAirportCode) {
        this.taskAirportCode = taskAirportCode == null ? null : taskAirportCode.trim();
    }

    /**
     * 所属机场区域代码
     * @return task_aptarea_code 所属机场区域代码
     */
    public String getTaskAptareaCode() {
        return taskAptareaCode;
    }

    /**
     * 所属机场区域代码
     * @param taskAptareaCode 所属机场区域代码
     */
    public void setTaskAptareaCode(String taskAptareaCode) {
        this.taskAptareaCode = taskAptareaCode == null ? null : taskAptareaCode.trim();
    }

    /**
     * 加油员员工ID
     * @return task_ope_staff_id 加油员员工ID
     */
    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    /**
     * 加油员员工ID
     * @param taskOpeStaffId 加油员员工ID
     */
    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId == null ? null : taskOpeStaffId.trim();
    }

    /**
     * 任务内容（0：加油，1：抽油）
     * @return task_content 任务内容（0：加油，1：抽油）
     */
    public Integer getTaskContent() {
        return taskContent;
    }

    /**
     * 任务内容（0：加油，1：抽油）
     * @param taskContent 任务内容（0：加油，1：抽油）
     */
    public void setTaskContent(Integer taskContent) {
        this.taskContent = taskContent;
    }

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     * @return task_status 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     * @param taskStatus 任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留））
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 任务派发时间
     * @return task_asg_time 任务派发时间
     */
    public Date getTaskAsgTime() {
        return taskAsgTime;
    }

    /**
     * 任务派发时间
     * @param taskAsgTime 任务派发时间
     */
    public void setTaskAsgTime(Date taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    /**
     * 任务接受时间
     * @return task_acc_time 任务接受时间
     */
    public Date getTaskAccTime() {
        return taskAccTime;
    }

    /**
     * 任务接受时间
     * @param taskAccTime 任务接受时间
     */
    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    /**
     * 加油开始时间
     * @return task_chag_sta_time 加油开始时间
     */
    public Date getTaskChagStaTime() {
        return taskChagStaTime;
    }

    /**
     * 加油开始时间
     * @param taskChagStaTime 加油开始时间
     */
    public void setTaskChagStaTime(Date taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    /**
     * 加油完成时间
     * @return task_chag_end_time 加油完成时间
     */
    public Date getTaskChagEndTime() {
        return taskChagEndTime;
    }

    /**
     * 加油完成时间
     * @param taskChagEndTime 加油完成时间
     */
    public void setTaskChagEndTime(Date taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    /**
     * 任务完成时间
     * @return task_done_time 任务完成时间
     */
    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    /**
     * 任务完成时间
     * @param taskDoneTime 任务完成时间
     */
    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    /**
     * 加油单编号
     * @return task_fuel_recpt_no 加油单编号
     */
    public String getTaskFuelRecptNo() {
        return taskFuelRecptNo;
    }

    /**
     * 加油单编号
     * @param taskFuelRecptNo 加油单编号
     */
    public void setTaskFuelRecptNo(String taskFuelRecptNo) {
        this.taskFuelRecptNo = taskFuelRecptNo;
    }

    /**
     * 加油车编号
     * @return task_vehi_no 加油车编号
     */
    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    /**
     * 加油车编号
     * @param taskVehiNo 加油车编号
     */
    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo == null ? null : taskVehiNo.trim();
    }

    /**
     * 创建人员工ID
     * @return task_cre_staff_id 创建人员工ID
     */
    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    /**
     * 创建人员工ID
     * @param taskCreStaffId 创建人员工ID
     */
    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId == null ? null : taskCreStaffId.trim();
    }

    /**
     * 任务星标（1：是，0：不是）默认0
     * @return task_starmark 任务星标（1：是，0：不是）默认0
     */
    public Integer getTaskStarmark() {
        return taskStarmark;
    }

    /**
     * 任务星标（1：是，0：不是）默认0
     * @param taskStarmark 任务星标（1：是，0：不是）默认0
     */
    public void setTaskStarmark(Integer taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    /**
     * 记录创建时间
     * @return task_rec_cre_time 记录创建时间
     */
    public Date getTaskRecCreTime() {
        return taskRecCreTime;
    }

    /**
     * 记录创建时间
     * @param taskRecCreTime 记录创建时间
     */
    public void setTaskRecCreTime(Date taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
    }
    
    /**
     * 起飞油量
     * @return task_takeoff_fuel 起飞油量
     */
    public Integer getTaskTakeoffFuel() {
        return taskTakeoffFuel;
    }

    /**
     * 起飞油量
     * @param taskTakeoffFuel 起飞油量
     */
    public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
        this.taskTakeoffFuel = taskTakeoffFuel;
    }

    /**
     * 轮挡油量
     * @return task_chock_fuel 轮挡油量
     */
    public Integer getTaskChockFuel() {
        return taskChockFuel;
    }

    /**
     * 轮挡油量
     * @param taskChockFuel 轮挡油量
     */
    public void setTaskChockFuel(Integer taskChockFuel) {
        this.taskChockFuel = taskChockFuel;
    }

    /**
     * 应加油量
     * @return task_total_fuel 应加油量
     */
    public Integer getTaskTotalFuel() {
        return taskTotalFuel;
    }

    /**
     * 应加油量
     * @param taskTotalFuel 应加油量
     */
    public void setTaskTotalFuel(Integer taskTotalFuel) {
        this.taskTotalFuel = taskTotalFuel;
    }

    /**
     * 仪表类型（KG，LB）
     * @return task_meter_type 仪表类型（KG，LB）
     */
    public String getTaskMeterType() {
        return taskMeterType;
    }

    /**
     * 仪表类型（KG，LB）
     * @param taskMeterType 仪表类型（KG，LB）
     */
    public void setTaskMeterType(String taskMeterType) {
        this.taskMeterType = taskMeterType == null ? null : taskMeterType.trim();
    }

    /**
     * 中央邮箱油量
     * @return task_cent_tank 中央邮箱油量
     */
    public Integer getTaskCentTank() {
        return taskCentTank;
    }

    /**
     * 中央邮箱油量
     * @param taskCentTank 中央邮箱油量
     */
    public void setTaskCentTank(Integer taskCentTank) {
        this.taskCentTank = taskCentTank;
    }

    /**
     * 左机翼油箱油量
     * @return task_left_tank 左机翼油箱油量
     */
    public Integer getTaskLeftTank() {
        return taskLeftTank;
    }

    /**
     * 左机翼油箱油量
     * @param taskLeftTank 左机翼油箱油量
     */
    public void setTaskLeftTank(Integer taskLeftTank) {
        this.taskLeftTank = taskLeftTank;
    }

    /**
     * 右机翼油箱油量
     * @return task_right_tank 右机翼油箱油量
     */
    public Integer getTaskRightTank() {
        return taskRightTank;
    }

    /**
     * 右机翼油箱油量
     * @param taskRightTank 右机翼油箱油量
     */
    public void setTaskRightTank(Integer taskRightTank) {
        this.taskRightTank = taskRightTank;
    }

    /**
     * 机组签名（JPG图片的base64编码）
     * @return task_crew_sign 机组签名（JPG图片的base64编码）
     */
    public String getTaskCrewSign() {
        return taskCrewSign;
    }

    /**
     * 机组签名（JPG图片的base64编码）
     * @param taskCrewSign 机组签名（JPG图片的base64编码）
     */
    public void setTaskCrewSign(String taskCrewSign) {
        this.taskCrewSign = taskCrewSign == null ? null : taskCrewSign.trim();
    }
    
    /**
     * 加油到位时间
     * @return task_arrive_time 加油到位时间
     */
    public Date getTaskArriveTime() {
        return taskArriveTime;
    }
    /**
     * 加油到位时间
     * @param taskArriveTime 加油到位时间
     */
    public void setTaskArriveTime(Date taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }
    /**
     * 加油员员工名称（业务字段）
     */
    public String getTaskOpeStaffName() {
		return taskOpeStaffName;
	}

    /**
     * 加油员员工名称（业务字段）
     */
	public void setTaskOpeStaffName(String taskOpeStaffName) {
		this.taskOpeStaffName = taskOpeStaffName;
	}

	/**
	 * @return the flgtAlcname
	 */
	public String getFlgtAlcname() {
		return flgtAlcname;
	}

	/**
	 * @param flgtAlcname the flgtAlcname to set
	 */
	public void setFlgtAlcname(String flgtAlcname) {
		this.flgtAlcname = flgtAlcname;
	}
	/**
	 * 打印油单完成时间
	 */
	public Date getTaskRcPrintTime() {
		return taskRcPrintTime;
	}

	/**
	 * 打印油单完成时间
	 */
	public void setTaskRcPrintTime(Date taskRcPrintTime) {
		this.taskRcPrintTime = taskRcPrintTime;
	}

    public Integer getFlgtOtatFuel() {
        return flgtOtatFuel;
    }

    public void setFlgtOtatFuel(Integer flgtOtatFuel) {
        this.flgtOtatFuel = flgtOtatFuel;
    }
}