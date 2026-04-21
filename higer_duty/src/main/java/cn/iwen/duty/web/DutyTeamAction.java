package cn.iwen.duty.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.iwen.duty.biz.IDutyTeamBiz;
import cn.iwen.duty.biz.IDutyTeamTimeBiz;
import cn.iwen.duty.biz.IDutyTimeBiz;
import cn.iwen.duty.biz.IStaffBiz;
import cn.iwen.duty.entity.DutyTeam;
import cn.iwen.duty.entity.DutyTeamTime;
import cn.iwen.duty.entity.Staff;
import cn.iwen.frame.BaseAction;
import cn.iwen.frame.MsgBean;

@Controller
@RequestMapping("/duty")
public final class DutyTeamAction extends BaseAction{

	@Autowired
	private IDutyTeamBiz iDutyTeamBiz;

	@Autowired
	private IDutyTeamTimeBiz iDutyTeamTimeBiz;
	
	@Autowired
	private IStaffBiz iStaffBiz;
	

	@Autowired
	private IDutyTimeBiz iDutyTimeBiz;
	
	
	//清空重传标志
	@RequestMapping("/dutyteam/clearSend") 
	public ModelAndView clearSend(MsgBean mbean) {
		iDutyTeamBiz.cleanSendModel();
		return Json(mbean);
	}
	
	
	@RequestMapping("/dutyteam/dutyStaffs") 
	public ModelAndView dutyStaffs(MsgBean mbean) {
		mbean.setData(iDutyTeamBiz.getDutyStaff(null));
		return Json(mbean);
	}
	
	@RequestMapping("/dutyteam/testDuty") 
	public ModelAndView testDuty(MsgBean mbean,Integer mode) {
		if(mode == null) mode = 1;
		if(mode == 1) {
			iDutyTeamBiz.changeDuty(new String[] {"休班","白班","夜班"});
		}else {
			iDutyTeamBiz.changeDuty(new String[] {"白班","夜班","休班"});
		}
		return Json(mbean);
	}
		
	//查询班组列表
	@RequestMapping("/dutyteam/jsons") 
	public ModelAndView jsons(MsgBean mbean,DutyTeam dutyTeam) {
		List<DutyTeam> teamList = iDutyTeamBiz.getList(dutyTeam,null);
		for(DutyTeam team : teamList) {
			team.setTimeList(iDutyTimeBiz.getDutyTimeList(team.getTeamId()));
		}
		mbean.setData(teamList);
		return Json(mbean);
	}

	//查询班组列表
	@RequestMapping("/dutyteam/test") 
	public ModelAndView test(MsgBean mbean,String time) {
		mbean.setData(iDutyTeamBiz.getOffWork(time));
		return Json(mbean);
	}

	@RequestMapping("/dutyteam/save") 
	public ModelAndView save(MsgBean mbean,@RequestBody DutyTeam dutyTeam) {
		//mbean.setInfo("保存成功");
		if(StringUtils.isEmpty(dutyTeam.getTeamName())) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		iDutyTeamBiz.save(dutyTeam);
		dutyTeam = iDutyTeamBiz.get(dutyTeam);
		dutyTeam.setTimeList(iDutyTimeBiz.getDutyTimeList(dutyTeam.getTeamId()));
		mbean.setData(dutyTeam);
		return Json(mbean);
	}
	
	/*批量加入人员*/
	@RequestMapping("/dutyteam/staffIds") 
	public ModelAndView staffIds(MsgBean mbean,String teamName) {
		//mbean.setInfo("保存成功");
		Staff staff = new Staff();
		staff.setStaffGroupId(teamName);
		List<Staff> staffList = iStaffBiz.getList(staff);
		List<String> ids = new ArrayList<String>();
		for(Staff s : staffList) {
			ids.add(s.getSfvhStaffId());
		}
		mbean.setData(ids);
		return Json(mbean);
	}
	//班组添加
	@RequestMapping("/dutyteam/addStaffs") 
	public ModelAndView addStaffs(MsgBean mbean,Integer teamId,String staffIds) {
		mbean.setInfo("保存成功");
		DutyTeam dutyTeam = iDutyTeamBiz.get(teamId);
		iStaffBiz.batAddTeam(dutyTeam.getTeamName(),staffIds);
		return Json(mbean);
	}
	
	@RequestMapping("/dutyteam/addStaff") 
	public ModelAndView addStaff(MsgBean mbean,@RequestBody JSONObject json) {
		//mbean.setInfo("添加成功");
		String teamName = json.getString("teamName");
		String staffId = json.getString("staffId");
		if(StringUtils.isEmpty(teamName) || StringUtils.isEmpty(staffId)) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		DutyTeam dutyTeam = new DutyTeam();
		dutyTeam.setTeamName(teamName);
		dutyTeam = iDutyTeamBiz.get(dutyTeam);
		if(dutyTeam == null) {
			mbean.setRetInfo(-1, "班组不存在");
			return Json(mbean);
		}
		Staff staff = new Staff();
		staff.setStaffGroupId(teamName);
		staff.setSfvhStaffId(staffId);
		int iret = iStaffBiz.update(staff);
		if(iret != 1) {
			mbean.setRetInfo(-1, "添加失败");
		}
		return Json(mbean);
	}

	@RequestMapping("/dutyteam/removeStaff") 
	public ModelAndView removeStaff(MsgBean mbean,@RequestBody JSONObject json) {
		mbean.setInfo("移除成功");
		String teamName = json.getString("teamName");
		String staffId = json.getString("staffId");
		if(StringUtils.isEmpty(teamName) || StringUtils.isEmpty(staffId)) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		DutyTeam dutyTeam = new DutyTeam();
		dutyTeam.setTeamName(teamName);
		dutyTeam = iDutyTeamBiz.get(dutyTeam);
		if(dutyTeam == null) {
			mbean.setRetInfo(-1, "班组不存在");
			return Json(mbean);
		}
		Staff staff = new Staff();
		staff.setSfvhStaffId(staffId);
		//set null
		int iret = iStaffBiz.updateFields(staff,"staffGroupId");
		if(iret != 1) {
			mbean.setRetInfo(-1, "移除失败");
		}
		return Json(mbean);
	}
	
	@RequestMapping("/dutyteam/delete") 
	public ModelAndView delete(MsgBean mbean,@RequestBody DutyTeam dutyTeam) {
		//mbean.setInfo("删除成功");
		iDutyTeamBiz.delete(dutyTeam);
		mbean.setData(dutyTeam);
		return Json(mbean);
	}

	//班组添加时间段
	@RequestMapping("/dutyteam/times") 
	public ModelAndView times(MsgBean mbean,@RequestBody DutyTeam dutyTeam) {
		//mbean.setInfo("删除成功");
		dutyTeam = iDutyTeamBiz.get(dutyTeam);
		if(dutyTeam == null) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		DutyTeamTime teamTime = new DutyTeamTime();
		teamTime.setTeamId(dutyTeam.getTeamId());
		mbean.setData(iDutyTimeBiz.getDutyTimeList(dutyTeam.getTeamId()));
		return Json(mbean);
	}
		
	//批量班组添加时间段
	@RequestMapping("/dutyteam/addTimes") 
	public ModelAndView addTimes(MsgBean mbean,@RequestBody DutyTeamTime teamTime) {
		//mbean.setInfo("删除成功");
		if(teamTime.getTeamId() == null || teamTime.getTimeIds() == null) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		iDutyTeamTimeBiz.addTimes(teamTime);
		return Json(mbean);
	}
	
	//班组添加时间段
	@RequestMapping("/dutyteam/addTime") 
	public ModelAndView addTime(MsgBean mbean,@RequestBody DutyTeamTime teamTime) {
		//mbean.setInfo("删除成功");
		if(teamTime.getTeamId() == null || teamTime.getTimeId() == null) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		iDutyTeamTimeBiz.add(teamTime);
		return Json(mbean);
	}
	
	//班组删除时间段
	@RequestMapping("/dutyteam/deleteTime") 
	public ModelAndView removeTime(MsgBean mbean,@RequestBody DutyTeamTime teamTime) {
		//mbean.setInfo("删除成功");
		if(teamTime.getTeamId() == null || teamTime.getTimeId() == null) {
			mbean.setRetInfo(-1, "参数不正确");
			return Json(mbean);
		}
		iDutyTeamTimeBiz.delete(teamTime);
		return Json(mbean);
	}
	
}
