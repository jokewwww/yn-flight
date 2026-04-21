package com.zh.exception;

import com.zh.bean.ReturnMsg;
import com.zh.constant.Constant;
import com.zh.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

/**
 * 異常処理
 */
@ControllerAdvice
public class ErrorExceptionHandler {

	private static final Logger logger = LogManager.getLogger(ErrorExceptionHandler.class);

	private static final String PATTERN="yyyy-MM-dd HH:mm:ss";
	private static final String PATTERN2="yyyy-MM-dd HH:mm";

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
	SimpleDateFormat simpleDateFormat11 = new SimpleDateFormat(PATTERN2);

	/**
	 * Exception处理
	 * 
	 *            异常
	 * @return 统一返回值
	 */
	@ExceptionHandler({ Throwable.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ReturnMsg processException(Throwable ex , HttpServletResponse response) {

		ReturnMsg msg = ReturnMsg.getInstance(Constant.CODE_ERR, "请联系管理员！", null);
		try {
			Field returnMsg = CustomException.class.getDeclaredField("returnMsg");
			returnMsg.setAccessible(true);
			/*if (ex instanceof CustomException) {
				ReturnMsg retMsg = (ReturnMsg) returnMsg.get(ex);
				msg.setData(retMsg.getData());
				msg.setErrInfo(retMsg.getErrInfo());
				msg.setCode(retMsg.getCode());
				logger.error(JsonHelper.object2str(msg));
			}*/
			if(ex instanceof  IllegalArgumentException){
				msg=ReturnMsg.getInstanceNGz(ex.getMessage(),null);
				logger.error(JsonHelper.object2str(msg));
			}
			if(ex instanceof CustomException){
				ReturnMsg retMsg = (ReturnMsg) returnMsg.get(ex);
				msg.setData(retMsg.getData());
				msg.setErrInfo(retMsg.getErrInfo());
				msg.setCode(retMsg.getCode());
				if(null != retMsg.getErrorCode() && 0 != retMsg.getErrorCode()){
					response.setStatus(retMsg.getErrorCode());
				}
				msg.setErrorCode(retMsg.getErrorCode());
				logger.error(JsonHelper.object2str(msg));
			}


		} catch (Exception e) {
			e.printStackTrace();
            saveErrorLog(e);
			logger.error("请联系管理员！", e);
		}
		try {
			logger.error(JsonHelper.object2str(msg), ex);
		} catch (Exception e) {
            saveErrorLog(e);
			logger.error("请联系管理员！", e);
		}

		return msg;
	}

    private void saveErrorLog(Exception e) {
        ApiLogContextHolder.setException(e.getMessage());
    }

}
