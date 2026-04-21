package com.example.contrast_entity.util;

/**
 * 定数
 */
public class Constant {

	/**
	 * code, 2:NG
	 */
	public final static String CODE_ERR = "2";

	/**
	 * code, 0:OK
	 */
	public final static String CODE_OK = "0";

	/**
	 * code, 1:warn
	 */
	public final static String CODE_WARN = "1";

	/**
	 * code, 9:CODE_EMPTY_FILE
	 */
	public final static String CODE_EMPTY_FILE = "9";

	/**
	 * code, 9:timeout
	 */
	public final static String TIMEOUT = "91";

	/**
	 * http://
	 */
	public final static String HTTP = "http://";

	/**
	 * 日期格式
	 */
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	
	/**
	 * 日期格式
	 */
	public final static String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 	0:人员 
	 */
	public final static String STAFF = "0";
	
	/**
	 *  1：航班任务
	 */
	public final static String TASKFLIGHT = "1";
	
	/**
	 * 2：航班
	 */
	public final static String FLIGHT = "2";
	
	/**
	 * 104 加油员或调度员自己下线（告知过期，WS需要断掉）
	 */
	public final static String PAD_OR_PC_OFFLINE = "104";

	
	/**
	 * 本场航班
	 */
	public final static String FLGT_DGAME = "Y";
	
	/**
	 * 非本场航班
	 */
	public final static String NO_FLGT_DGAME = "N";
	
	/**
	 * D：出港
	 */
	public final static String CLEAR_A_PORT = "D";
	
	/**
	 * A：进港
	 */
	public final static String CLEAR_A_ENTER = "A";
	
	/**
	 * 登录判断：加油员
	 */
	public final static String CHEER_STAFF = "3";
	
	/**
	 * 登录判断：调度员
	 */
	public final static String DISPATCH_STAFF = "2";
	/**
	 * 登录判断：管理员	
	 */
	public final static String MANAGER_STAFF = "1";
	
	/**
	 * 登录时存进radis中的key
	 */
	public final static String LOGIN_KEY = "login:";
	
	public final static String TOKENS = "tokens:";
	
	/**
	 * 登录错误时存进map集合中的key
	 */
	public final static String LOGIN_ERROR_KEY = "error";
	
	/**
	 * 登录时判断是不是加油员需要存进map集合的值
	 */
	public final static String NO_CHEER_STAFF_VALUE = "您不是加油员！";
	
	/**
	 * 登录时判断是不是调度员需要存进map集合的值
	 */
	public final static String NO_DISPATCH_STAFF_VALUE = "您不是调度员！";
	
	/**
	 * 登录时判断账号或密码错误需要存进map集合的值
	 */
	public final static String NAME_PWD_ERROR_VALUE = "账号或密码错误！";
	
	/**
	 * PAD登录时判断账号未登出
	 */
	public final static String NOT_LOGOFF_ERROR_VALUE = "该账户没有退出登录，请注销后再登录！";
	
	/**
	 * 在线
	 */
	public final static String ON_LINE = "在线";
	
	/**
	 * 离线
	 */
	public final static String OFF_LINE = "离线";
	
	/**
	 * PAD人员登录（发给PC）
	 */
	public final static String PAD_STAFF_LOGIN = "0";
	
	/**
	 * PAD人员登出（发给PC）
	 */
	public final static String PAD_STAFF_REMOVE = "1";

	/**
	 * 1 加油员下线（原来是100，改成1了）
	 */
//	public final static String PAD_STAFF_OFFLINE = "100";
	public final static String PAD_STAFF_OFFLINE = PAD_STAFF_REMOVE;

	/**
	 * 0 加油员上线（原来是102，改成0了）
	 */
//	public final static String PAD_STAFF_ONLINE = "101";
	public final static String PAD_STAFF_ONLINE = PAD_STAFF_LOGIN;

	
	/**
	 * 102	心跳检测
	 */
	public final static String PAD_HEART_BEAT = "102";

	/**
	 * 103 断网
	 */
	public final static String PAD_BROKEN_NETWORK = "103";
	
	
	/**
	 * PAD人车绑定（发给PC）
	 */
	public final static String PAD_STAFF_VEHI = "2";
	
	/**
	 * PC任务下发（发给PC）
	 */
	public final static String PC_TASK_ISSUE = "3";
	
	/**
	 * PAD任务状态变更（发给PC）
	 */
	public final static String PAD_TASK_STATE_UPDATE = "5";
	
	/**
	 * PC任务取消（发给PC）
	 */
	public final static String PC_TASK_CANCEL = "6";
	
	/**
	 * PC航班新建（发给PC）
	 */
	public final static String PC_FLIGHT_ADD = "8";
	
	/**
	 * PC航班修改（发给PC）
	 */
	public final static String PC_FLIGHT_UPDATE = "11";
	
	/**
	 * PAD航班新建（发给PC）
	 */
	public final static String PAD_FLIGHT_ADD = "8";
	
	/**
	 * PAD任务申领（发给PC）
	 */
	public final static String PAD_TASK_APPLY = "14";
	
	/**
	 * PAD任务下发（发给PAD）
	 */
	public final static String PAD_TASK_ISSUE = "20";
	
	/**
	 * PAD任务取消（发给PAD）
	 */
	public final static String PAD_TASK_CANCEL = "21";
	
	/**
	 * 油料录入（发给地图）
	 */
	public final static String OIL_ADD = "30";
	/**
	 * 航班改变日志（发给前端）
	 */
	public final static String FLIGHT_CHANGE_LOG = "19";
}
