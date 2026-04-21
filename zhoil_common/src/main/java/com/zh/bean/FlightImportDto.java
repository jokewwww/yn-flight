package com.zh.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("pad航班导入excel 实体类")
public class FlightImportDto {
	@ApiModelProperty("数据行号")
	private int rowNum;

	@ApiModelProperty("航班号")
	private String flgtFlno;

	@ApiModelProperty("飞机号")
	private String flgtReg;

	@ApiModelProperty("航班区域属性")
	private String flgtFlti;

	@ApiModelProperty("目的地")
	private String airportNames;

	@ApiModelProperty("计划起飞时间")
	private String dstot;

	@ApiModelProperty("预计起飞时间")
	private String detot;

	@ApiModelProperty("停机位")
	private String placeCode;

	@ApiModelProperty("航班属性")
	private String flgtNature;

	private boolean isOk;
}