package com.zh.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.zh.annotation.MsgList;
import com.zh.bean.ReturnMsg;
import com.zh.constant.Constant;
import com.zh.exception.CustomException;
import com.zh.util.JsonHelper;

/**
 * 控制层基类
 */
public class BaseController {

	private static final Logger logger = LogManager.getLogger(BaseController.class);

	/**
	 * HttpMessageNotReadableException异常
	 * 
	 * @param ex
	 *            异常
	 * @param msg
	 *            统一返回值
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	Object handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

		logger.error(ex.getMessage(), ex);

		ReturnMsg msg = ReturnMsg.getInstance(Constant.CODE_ERR, ex.getMessage(), null);

		if (ex.getRootCause() instanceof CustomException) {
			doReturnMsg(ex, msg);
		} else if (ex.getRootCause() instanceof InvalidFormatException) {
			msg.setErrInfo("输入错误！");
			exceptionHandler(ex, msg);
		}
		return msg;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		logger.error(ex.getMessage(), ex);

		MsgList msgList = null;
		Map<String, Object> errMap = new HashMap<String, Object>();
		ReturnMsg<Map<String, Object>> msg = new ReturnMsg<>(Constant.CODE_ERR, "输入错误！", errMap);
		BindingResult result = ex.getBindingResult();

		if (result.hasErrors()) {
			Field declaredField = null;
			Object key = null;
			Object errMsg = null;
			String errInfo = null;

			List<ObjectError> errors = result.getAllErrors();
			for (ObjectError error : errors) {
				try {
					declaredField = FieldError.class.getDeclaredField("field");
					declaredField.setAccessible(true);
					key = declaredField.get(error);

					Field target = BeanPropertyBindingResult.class.getDeclaredField("target");
					target.setAccessible(true);
					Object object = target.get(result);
					Field targetField = object.getClass().getDeclaredField(key.toString());
					String[] msgs = targetField.getAnnotation(MsgList.class).msgs();

					declaredField = DefaultMessageSourceResolvable.class.getDeclaredField("defaultMessage");
					declaredField.setAccessible(true);
					errMsg = declaredField.get(error);

					msgList = targetField.getAnnotation(MsgList.class);
					if (msgList != null) {
						msgs = msgList.msgs();
						if ((msgs != null || msgs.length > 0)) {
							errInfo = "【" + msgs[0] + "】：" + errMsg;
						}
					}

					errMap.put(key.toString(), errInfo);

					logger.error(JsonHelper.object2str(msg));

				} catch (Exception e) {
					msg.setErrInfo("请联系管理员！");
					logger.error("请联系管理员！", e);
				}
			}
			// throw new CustomException(msg);
		}
		return msg;
	}

	/**
	 * InvalidFormatException異常処理
	 * 
	 * @param ex
	 *            异常
	 * @param msg
	 *            统一返回值
	 */
	private void exceptionHandler(HttpMessageNotReadableException ex, ReturnMsg msg) {
		try {
			Field targetField = null;
			JsonMappingException.Reference mapping = null;
			MsgList msgList = null;
			String[] msgs = null;
			String errInfo = null;

			Map<String, Object> errMap = new HashMap<String, Object>();
			Field targetType = InvalidFormatException.class.getDeclaredField("_targetType");
			targetType.setAccessible(true);
			Object object = targetType.get(ex.getCause());

			if (object == java.lang.Integer.class || object == java.lang.Double.class || object == java.lang.Long.class
					|| object == java.lang.Float.class || object == java.lang.Byte.class
					|| object == java.lang.Short.class) {

				errInfo = "请输入数字！";
			} else if (object == java.lang.Boolean.class) {
				errInfo = "请输true或false！";
			} else {
				errInfo = "请联系管理员！";
			}

			Field declaredField = JsonMappingException.class.getDeclaredField("_path");
			declaredField.setAccessible(true);
			LinkedList list = (LinkedList) declaredField.get(ex.getCause());

			declaredField = Reference.class.getDeclaredField("_fieldName");
			declaredField.setAccessible(true);
			String key = (String) declaredField.get(list.get(0));

			mapping = (JsonMappingException.Reference) list.get(0);
			targetField = mapping.getFrom().getClass().getDeclaredField(key);
			msgList = targetField.getAnnotation(MsgList.class);
			if (msgList != null) {
				msgs = msgList.msgs();
				if ((msgs != null || msgs.length > 0)) {
					// errInfo = MessagesFormatUtil.format(errInfo, msgs);
					errInfo = "【" + msgs[0] + "】：" + errInfo;
				}
			}

			errMap.put(key, errInfo);

			msg.setData(errMap);

			logger.error(errInfo, ex);
		} catch (Exception e) {
			logger.error("请联系管理员！", e);
		}
	}

	/**
	 * CustomException异常处理
	 * 
	 * @param ex
	 *            异常
	 * @param msg
	 *            统一返回值
	 */
	private void doReturnMsg(HttpMessageNotReadableException ex, ReturnMsg msg) {
		try {
			msg.setErrInfo(ex.getRootCause().getMessage());
			Field declaredField = ex.getCause().getCause().getClass().getDeclaredField("returnMsg");
			declaredField.setAccessible(true);
			ReturnMsg retMsg = (ReturnMsg) declaredField.get(ex.getCause().getCause());
			msg.setData(retMsg.getData());

			logger.error(JsonHelper.object2str(msg));
		} catch (Exception e) {
			msg.setErrInfo("请联系管理员！");
			logger.error("请联系管理员！", e);
		}
	}
}