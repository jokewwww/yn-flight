package cn.iwen.weblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.iwen.frame.BaseAction;
import cn.iwen.frame.MsgBean;

@Controller
@RequestMapping("/weblog")
public class LogsAction extends BaseAction{

	@Autowired
	private ILogsBiz logsbiz;
	
	@RequestMapping("/import")
	public ModelAndView weblog(MsgBean mbean,String filePath) {
		log.debug("log file:" + filePath);
		logsbiz.importLog(filePath);
		return Json(mbean);
	}
	
}
