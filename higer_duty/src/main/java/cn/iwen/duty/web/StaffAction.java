package cn.iwen.duty.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.iwen.frame.*;
import cn.iwen.duty.entity.Staff;
import cn.iwen.duty.biz.IStaffBiz;

@Controller
@RequestMapping("/duty")
public final class StaffAction extends BaseAction{

	@Autowired
	private IStaffBiz iStaffBiz;

	@RequestMapping("/staff/list") 
	public ModelAndView json(PageBean<Staff> pb,Staff staff) {
		iStaffBiz.getPageList(pb,staff,null);
		return Json(pb);
	}

	@RequestMapping("/staff/save") 
	public ModelAndView save(MsgBean mbean,Staff staff) {
		mbean.setInfo("保存成功");
		iStaffBiz.save(staff);
		return Json(mbean);
	}

	@RequestMapping("/staff/delete") 
	public ModelAndView delete(MsgBean mbean,Staff staff) {
		mbean.setInfo("删除成功");
		iStaffBiz.delete(staff);
		return Json(mbean);
	}

}
