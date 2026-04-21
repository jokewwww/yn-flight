package com.zh.constant;

import com.google.common.collect.Lists;
import java.util.Map;
import java.util.stream.Collectors;

public class StatusConstant {

	/**
	 * 状态为0
	 */
	public final static String STATUS_ZERO = "未下发";
	/**
	 * 状态为1
	 */
	public final static String STATUS_ONE = "待接受";
	/**
	 * 状态2
	 */
	public final static String STATUS_TWO = "申请待批";
	/**
	 * 状态为3
	 */
	public final static String STATUS_THREE = "已接受";
	/**
	 * 状态为4
	 */
	public final static String STATUS_FOUR = "到位";
	/**
	 * 状态为5
	 */
	public final static String STATUS_FIVE = "加油完成";
	/**
	 * 状态为6
	 */
	public final static String STATUS_SIX = "油单待审核";
	/**
	 * 状态为7
	 */
	public final static String STATUS_SEVEN = "任务完成";
	/**
	 * 状态为8
	 */
	public final static String STATUS_EIGHT = "拒绝";
	/**
	 * 状态为取消
	 */
	public final static String  STATUS_CANCLE="任务取消";
	/**
	 * 状态为更新失败
	 */
	public  final static String UPDATE_FALSE ="更新失败";
	/**
	 * 状态为添加失败
	 */
	public  final static String INSERT_FALSE ="添加失败";

    /**
     * 状态为添加失败
     */
    public  final static String TYPE_REPEAT ="类型重复";

    /**
     * 状态为添加失败
     */
    public  final static String CODE_REPEAT ="编号重复";
	/**
	 * 0
	 */
	public  final static String ZERO ="0";
	/**
	 * 1
	 */
	public  final static String ONE ="1";
	/**
	 * 2
	 */
	public  final static String TWO ="2";
	/**
	 * 3
	 */
	public  final static String THREE ="3";
	/**
	 * 4
	 */
	public  final static String FOUR ="4";
	/**
	 * 5
	 */
	public  final static String FIVE ="5";
	/**
	 * 6
	 */
	public  final static String SIX ="6";
	/**
	 * 7
	 */
	public  final static String SERVEN ="7";
	/**
	 * 8
	 */
	public  final static String EIGHT ="8";
	/**
	 * 实际
	 */
	public final static String CONS_SJ = "实际";
	
	/**
	 * 预计
	 */
	public final static String CONS_YJ = "预计";
	/**
	 * 计划
	 */
	public final static String CONS_JH = "计划";

	public  enum  FlrcBwtarEnum{
		B("B","保税"),
		FB("FB","非保税"),
		;

		private String ftypType;
		private String ftypTypeName;

		FlrcBwtarEnum(String ftypType, String ftypTypeName) {
			this.ftypType = ftypType;
			this.ftypTypeName = ftypTypeName;
		}

		public String getFtypType() {
			return ftypType;
		}

		public String getFtypTypeName() {
			return ftypTypeName;
		}

		public static String getValueByType(Object type){
			FlrcBwtarEnum[] values = FlrcBwtarEnum.values();
			Map<String, String> collect = Lists.newArrayList(values).stream().collect(Collectors.toMap(FlrcBwtarEnum::getFtypType, FlrcBwtarEnum::getFtypTypeName));
			return collect.getOrDefault(type,"");
		}
	}

	/**
	 * 航班性质
	 * 航班性质（PAX 客机，CGO 货机，GEN 通用，SPE 特殊）
	 */
	public  enum  FlgtNatureEnum{
		PAX("PAX","客机"),
		CGO("CGO","货机"),
		GEN("GEN","通用"),
		SPE("SPE","特殊"),
		;

		private String ftypType;
		private String ftypTypeName;

		FlgtNatureEnum(String ftypType, String ftypTypeName) {
			this.ftypType = ftypType;
			this.ftypTypeName = ftypTypeName;
		}

		public String getFtypType() {
			return ftypType;
		}

		public String getFtypTypeName() {
			return ftypTypeName;
		}

		public static String getValueByType(Object type){
			FlgtNatureEnum[] values = FlgtNatureEnum.values();
			Map<String, String> collect = Lists.newArrayList(values).stream().collect(Collectors.toMap(FlgtNatureEnum::getFtypType, FlgtNatureEnum::getFtypTypeName));
			return collect.getOrDefault(type,"");
		}
	}

}
