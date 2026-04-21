package cn.iwen.weblog.web;

import cn.iwen.frame.dao.IBaseDao;

public interface ILogsBiz extends IBaseDao<LogsBean>{

	void importLog(String filePath);
}
