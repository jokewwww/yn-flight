package cn.iwen.duty.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.iwen.duty.biz.IDutyTeamTimeBiz;
import cn.iwen.duty.entity.DutyTeamTime;
import cn.iwen.frame.BaseAction;
import cn.iwen.frame.MsgBean;
import cn.iwen.frame.PageBean;

@Controller
@RequestMapping("/duty")
public final class DutyTeamTimeAction extends BaseAction{

	@Autowired
	private IDutyTeamTimeBiz iDutyTeamTimeBiz;

	@RequestMapping("/dutyteamtime/list") 
	public ModelAndView json(PageBean<DutyTeamTime> pb,DutyTeamTime dutyTeamTime) {
		iDutyTeamTimeBiz.getPageList(pb,dutyTeamTime,null);
		return Json(pb);
	}

	@RequestMapping("/dutyteamtime/save") 
	public ModelAndView save(MsgBean mbean,DutyTeamTime dutyTeamTime) {
		mbean.setInfo("保存成功");
		iDutyTeamTimeBiz.save(dutyTeamTime);
		return Json(mbean);
	}

	@RequestMapping("/dutyteamtime/delete") 
	public ModelAndView delete(MsgBean mbean,DutyTeamTime dutyTeamTime) {
		mbean.setInfo("删除成功");
		iDutyTeamTimeBiz.delete(dutyTeamTime);
		return Json(mbean);
	}

}
