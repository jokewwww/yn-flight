package cn.iwen.frame.dao.tools.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.iwen.frame.BaseAction;
import cn.iwen.frame.MsgBean;
import cn.iwen.frame.dao.tools.bean.ConvertInfo;
import cn.iwen.frame.dao.tools.bean.TestCase;
import cn.iwen.frame.dao.tools.biz.IAutoToolsBiz;

@Controller
@RequestMapping("/")
public class ToolsAction extends BaseAction{

	@Autowired
	private IAutoToolsBiz toolsbiz;
	
	@TestCase("[{packName:'cn.iwen.duty',tableName:'department',savePath:'/home/wen/kvm/orm'}]")
	@RequestMapping(value="/orm/tools/create",method = RequestMethod.POST)
	public ModelAndView select(MsgBean mbean,@RequestBody String json) {
		log.debug("create:" + json);
		List<ConvertInfo> infolist = JSON.parseArray(json, ConvertInfo.class);
		//mbean.setData(toolsbiz.getTables());
		toolsbiz.convert(infolist);
		return Json(mbean);
	}
	
	@TestCase("{a:12,b:34}")
	@RequestMapping(value="testAction",method = RequestMethod.GET)
	public ModelAndView weblog(ModelAndView mav,MsgBean mbean,String path) {
		JSONObject pktJson = toolsbiz.getWebMethods();
		mav.setViewName("jsp/TestAction");
		mav.addObject("pktJson", pktJson);
		return mav;
	}
	
}
