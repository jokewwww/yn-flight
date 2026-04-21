package cn.iwen.duty.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.iwen.duty.biz.IDutyTimeBiz;
import cn.iwen.duty.entity.DutyTime;
import cn.iwen.frame.BaseAction;
import cn.iwen.frame.MsgBean;

@Controller
@RequestMapping("/duty")
public final class DutyTimeAction extends BaseAction{

	@Autowired
	private IDutyTimeBiz iDutyTimeBiz;

	@RequestMapping("/dutytime/list") 
	public ModelAndView json(MsgBean mbean,DutyTime dutyTime) {
		mbean.setData(iDutyTimeBiz.getList(dutyTime,null));
		return Json(mbean);
	}
	
	@RequestMapping("/dutytime/save") 
	public ModelAndView save(MsgBean mbean,@RequestBody JSONObject json) {
		//mbean.setInfo("保存成功");
		Integer timeId = json.getInteger("timeId");
		String startTime = json.getString("startTime");
		String endTime = json.getString("endTime");
		short time1 = DutyTime.fmtTime(startTime);
		short time2 = DutyTime.fmtTime(endTime);
		if(time1 < 0 || time2 < 0) {
			mbean.setRetInfo(-1, "格式不正常");
			return Json(mbean);
		}
		DutyTime dutyTime = new DutyTime();
		dutyTime.setStartTime(time1);
		dutyTime.setEndTime(time2);
		dutyTime.setTimeId(timeId);
		dutyTime.setRemark(json.getString("remark"));
		iDutyTimeBiz.save(dutyTime);
		mbean.setData(iDutyTimeBiz.get(dutyTime));
		return Json(mbean);
	}

	@RequestMapping("/dutytime/delete") 
	public ModelAndView delete(MsgBean mbean,@RequestBody DutyTime dutyTime) {
		//mbean.setInfo("删除成功");
		iDutyTimeBiz.delete(dutyTime);
		mbean.setData(dutyTime);
		return Json(mbean);
	}

}
