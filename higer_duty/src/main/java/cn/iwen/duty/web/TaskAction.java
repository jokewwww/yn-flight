package cn.iwen.duty.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.iwen.frame.*;
import cn.iwen.duty.entity.Task;
import cn.iwen.duty.biz.ITaskBiz;

@Controller
@RequestMapping("/duty")
public final class TaskAction extends BaseAction{

	@Autowired
	private ITaskBiz iTaskBiz;

	@RequestMapping("/task/list") 
	public ModelAndView json(PageBean<Task> pb,Task task) {
		iTaskBiz.getPageList(pb,task,null);
		return Json(pb);
	}

	@RequestMapping("/task/save") 
	public ModelAndView save(MsgBean mbean,Task task) {
		mbean.setInfo("保存成功");
		iTaskBiz.save(task);
		return Json(mbean);
	}

	@RequestMapping("/task/delete") 
	public ModelAndView delete(MsgBean mbean,Task task) {
		mbean.setInfo("删除成功");
		iTaskBiz.delete(task);
		return Json(mbean);
	}

}
