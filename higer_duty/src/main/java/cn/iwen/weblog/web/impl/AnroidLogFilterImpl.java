package cn.iwen.weblog.web.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.iwen.frame.BaseUtils;
import cn.iwen.weblog.web.ILogFilter;
import cn.iwen.weblog.web.LogsBean;

@Service
public class AnroidLogFilterImpl implements ILogFilter {

	protected static  Log log = LogFactory.getLog(AnroidLogFilterImpl.class);
	
	private final static String REGEX_HEADER = "^\\d{4}-\\d{2}-\\d{2}.+";
	
	@Override
	public boolean isHeader(String line) {
		if(line == null) return false;
		return line.matches(REGEX_HEADER);
	}

	private boolean doFilterHeader(LogsBean logsBean) {
		int ipos1 = -1,ipos2 = 0;;
		String msg = logsBean.getMsg();
		ipos1 = msg.indexOf(" ");
		ipos1 = msg.indexOf(" ",ipos1 + 1);
		String date = msg.substring(0, ipos1);
		logsBean.setCreateTime(BaseUtils.str2timems(date));
		
		ipos1++;
		ipos2 = msg.indexOf(",",ipos1);
		logsBean.setCodeName(msg.substring(ipos1 , ipos2));
		
		ipos1 = ipos2 + 2;
		ipos2 = msg.indexOf("]",ipos1);
		logsBean.setThreadName(msg.substring(ipos1, ipos2));
		
		logsBean.setMsg(msg.substring(ipos2 + 2));
		return false;
	}

	private boolean doFilter1(LogsBean logsBean) {
		String msg = logsBean.getMsg();
		String taskId = BaseUtils.regx(msg, "([A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{12})");
		if(taskId != null) logsBean.setText1(taskId);
		return false;
	}
	
	private boolean doFilter2(LogsBean logsBean) {
		String msg = logsBean.getMsg();
		String[] flrcNOs = BaseUtils.regMatches(msg, "\"(\\d{13})\"");
		if(flrcNOs.length > 0) logsBean.setText2(String.join(",",flrcNOs));
		return false;
	}
	
	@Override
	public boolean doFilter(LogsBean logsBean) {
		doFilterHeader(logsBean);
		doFilter1(logsBean);
		doFilter2(logsBean);
		if(null != logsBean.getText2())
			log.debug(JSON.toJSONString(logsBean,true));
		return false;
	}

}
