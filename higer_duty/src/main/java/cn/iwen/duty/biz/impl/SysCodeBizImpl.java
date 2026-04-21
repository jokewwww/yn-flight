package cn.iwen.duty.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.iwen.duty.biz.ISysCodeBiz;
import cn.iwen.duty.entity.SysCode;
import cn.iwen.frame.dao.BaseDao;
import cn.iwen.frame.dao.expr.Exprs;


@Service
public class SysCodeBizImpl extends BaseDao<SysCode> implements ISysCodeBiz {

	@Override
	public List<SysCode> getCodeList(String qstr) {
		Exprs expr = super.or();
		if(StringUtils.isNotEmpty(qstr)) {
			expr.like("codeName", "%" + qstr + "%")
				.eq("codeValue", qstr);
		}
		return super.query(super.select().addCond(expr).asc(0, "codeId"));
	}

	//@Override
	public List<SysCode> getCodeBox1(String codeType) {
		String sql = "select t.code_name,t.code_value,t.code_desc1,t.code_name as 'code.codeValue', t.code_value as 'a.b.d',t.code_id as 'a.z' from sys_code t limit 3";
		Map<String,Object> params = new HashMap<String,Object>();
		return super.query(sql, params);
	}

	@Override
	public List<SysCode> getCodeBox(String codeType) {
		SysCode code = new SysCode();
		code.setCodeType(codeType);
		return getList("codeName,codeValue",and().eq("codeType", codeType),"codeValue asc");
	}

	@Override
	public SysCode getSysCodeList(String codeType, String codeVue) {
		SysCode code = new SysCode();
		code.setCodeType(codeType);
		code.setCodeValue(codeVue);
		return super.get(code);
	}
	
}

