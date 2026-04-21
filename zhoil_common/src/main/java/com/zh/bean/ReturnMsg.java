package com.zh.bean;

import java.util.Map;

import com.zh.constant.Constant;
import org.springframework.http.HttpStatus;

/**
 * 统一返回值
 */
public class ReturnMsg<T> {

	public ReturnMsg(String code, String errInfo, T data) {
		super();
		this.code = code;
		this.errInfo = errInfo;
		this.data = data;
		this.errorCode = HttpStatus.BAD_REQUEST.value();
	}
	public ReturnMsg(String code, String errInfo, T data,Integer errorCode) {
		super();
		this.code = code;
		this.errInfo = errInfo;
		this.data = data;
		this.errorCode = errorCode;
	}

	/**
	 * code 0:OK; 2:NG (1:warn，这个暂时不用)
	 */
	private String code;

	/**
	 * 错误信息
	 */
	private String errInfo;

	/**
	 * 数据
	 */
	private T data;

	private Integer errorCode;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	public static ReturnMsg getInstance(String code, String errInfo, Object data) {
		return new ReturnMsg(code, errInfo, data);
	}

	public static ReturnMsg<Map<String, String>> getInstanceOK() {
		return new ReturnMsg<Map<String, String>>(Constant.CODE_OK, null, null);
	}

	public static <Z> ReturnMsg<Z> getInstanceOKz(Z z) {
		return new ReturnMsg<Z>(Constant.CODE_OK, null, z);
	}

	public static ReturnMsg<Map<String, String>> getInstanceNG() {
		return new ReturnMsg<Map<String, String>>(Constant.CODE_ERR, null, null);
	}

	public static <Z> ReturnMsg<Z> getInstanceNGz(Z z) {
		return new ReturnMsg<Z>(Constant.CODE_ERR, null, z);
	}

	public static <Z> ReturnMsg<Z> getInstanceNGz(String errInfo, Z z) {
		return new ReturnMsg<Z>(Constant.CODE_ERR, errInfo, z);
	}

	public static <Z> ReturnMsg<Z> getInstanceNGz(String errInfo, Z z,Integer errorCode) {
		return new ReturnMsg<Z>(Constant.CODE_ERR, errInfo, z,errorCode);
	}
	public static <Z> ReturnMsg<Z> getInstanceNGz(String errCode,String errInfo, Z z,Integer errorCode) {
		return new ReturnMsg<Z>(errCode, errInfo, z,errorCode);
	}

	public static ReturnMsg<Map<String, String>> getInstanceWarn() {
		return new ReturnMsg<Map<String, String>>(Constant.CODE_ERR, null, null);
	}

}