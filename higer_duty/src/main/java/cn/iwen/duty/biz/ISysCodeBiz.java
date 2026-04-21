package cn.iwen.duty.biz;

import java.util.List;

import cn.iwen.duty.entity.SysCode;
import cn.iwen.frame.dao.IBaseDao;


public interface ISysCodeBiz extends IBaseDao<SysCode> {

	List<SysCode> getCodeList(String qstr);
	
	List<SysCode> getCodeBox(String codeType);
	
	SysCode getSysCodeList(String codeType,String codeVue);
	
}

