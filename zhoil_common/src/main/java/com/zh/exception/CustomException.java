package com.zh.exception;

import com.zh.bean.ReturnMsg;

/**
 * 自定义异常
 */
public class CustomException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	/**
	 * 统一返回值
	 */
	private ReturnMsg returnMsg;

	public CustomException() {
	}

	public CustomException(ReturnMsg returnMsg) {
		super(returnMsg.getErrInfo());
		this.setReturnMsg(returnMsg);
	}

	public ReturnMsg getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(ReturnMsg returnMsg) {
		this.returnMsg = returnMsg;
	}

}
