package com.ncse.zhhygis.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ncse.zhhygis.entity.HeightAlarmInfos;
import com.ncse.zhhygis.service.HeightAlarmInfosService;
import com.ncse.zhhygis.utils.ServerResponse;
import com.zh.bean.login.MyStaff;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:  [HeightAlarmInfosController]
 * Description:  [超高报警]
 * Date:  2018/12/20 15 17
 *
 * @author Xugn
 * @version 1.0.0
 */
@RestController
/**自动返回的是json格式数据***/
@RequestMapping("/heightAlarmInfos")
@CrossOrigin(allowCredentials = "true")
public class HeightAlarmInfosController {

    private final Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private HeightAlarmInfosService heightAlarmInfosService;

    /***
     * MethodName:  [selectByParms]
     * Description:  [根据条件查询超高报警信息]
     * @param: [ carnum 车牌号, createtimeb 开始时间, createtimee 结束时间, isalarm 是否在线 , driver 司机姓名,regname 区域名称]
     * @return: com.ncse.zhhygis.utils.ServerResponse
     */
    @RequestMapping("selectByParms")
    public ServerResponse selectByParms(MyStaff staff, @RequestParam Map parmsMap) {
        Map map = new HashMap();
        try {
            log.info("selectByParms查询条件：" + parmsMap);
            if (parmsMap.get("pageNum") != null && parmsMap.get("pageSize") != null) {
                //进行分页
                PageHelper.startPage(Integer.parseInt((String) parmsMap.get("pageNum")), Integer.parseInt((String) parmsMap.get("pageSize")));
                map.put("pageSize", parmsMap.get("pageSize"));
                map.put("pageNum", parmsMap.get("pageNum"));
            }
            List<HeightAlarmInfos> heightAlarmInfos = heightAlarmInfosService.selectByParms(parmsMap);
            PageInfo<HeightAlarmInfos> info = new PageInfo<>(heightAlarmInfos);
            map.put("count", info.getTotal());//获取数据总条数
            map.put("data", heightAlarmInfos);
            map.put("code", "0");
        } catch (Exception e) {
            map.put("code", "1");
            log.error(e);
        }
        return ServerResponse.createBySuccess(map);
    }
}
