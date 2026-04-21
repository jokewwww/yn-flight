package com.zh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zh.annotation.SystemLog;
import com.zh.bean.MyPage;
import com.zh.bean.OutMessage;
import com.zh.bean.ReturnMsg;
import com.zh.bean.User;
import com.zh.bean.auto.TestUser;
import com.zh.component.RedisComponent;
import com.zh.constant.Constant;
import com.zh.prop.Prop;
import com.zh.service.TestUserService;
import com.zh.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

//import com.zh.annotation.SystemLog;

/**
 * 测试用
 *
 * @author 徐陆
 */
@EnableScheduling
@RestController
@RequestMapping(value = "/index")
public class IndexController extends BaseController {

    private final static Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    Prop prop;

    @Autowired
    TestUserService testUserService;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisComponent redisCnt;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//	@Scheduled(fixedRate = 5000)
	/*public void testDingYue() {
		OutMessage<String> outMessage = new OutMessage<String>();
		outMessage.setTo("111"); // 要发送给谁（id），这个是必须的。
		String str = "任务来了。。请及时处理。" + new Date().toLocaleString();
		System.out.println(str);
		outMessage.setContent(str); // 其它字段根据业务需要自定义。
//		outMessage.setContent22("任务来了。。请及时处理。"); // 其它字段根据业务需要自定义。
//		outMessage.setContent33("任务来了。。请及时处理。"); // 其它字段根据业务需要自定义。
		stringRedisTemplate.convertAndSend("chat", JsonHelper.object2str(outMessage).getData());
	}*/

    //	@Scheduled(fixedRate = 2000)
    public void ttt() {
        OutMessage<String> outMessage = new OutMessage<String>();
        outMessage.setTo("test,aaa");
        outMessage.setContent("任务来了。。请及时处理。" + new Date().toLocaleString());
        stringRedisTemplate.convertAndSend("chat", JsonHelper.object2str(outMessage).getData());
        System.out.println("====================" + JsonHelper.object2str(outMessage).getData());
        outMessage.setTo("admin");
        outMessage.setContent("任务来了。。请及时处理。" + new Date().toLocaleString());
        stringRedisTemplate.convertAndSend("chat", JsonHelper.object2str(outMessage).getData());
        System.out.println("====================" + JsonHelper.object2str(outMessage).getData());
    }

    @GetMapping(value = "/test/{abc}")
    public User get(@PathVariable(value = "abc") int abc) {
        return restTemplate.getForObject(Constant.HTTP + prop.getFlightIp() + ":" + prop.getFlightPort() + "/flight/api/user/{abc}", User.class, abc);
    }

    @GetMapping(value = "/test/list")
    public List<User> getList() {
        ResponseEntity<List<User>> rateResponse =
                restTemplate.exchange(Constant.HTTP + prop.getFlightIp() + ":" + prop.getFlightPort() + "/flight/api/user/list",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                        });
        List<User> list = rateResponse.getBody();
        System.out.println(list.get(0).getAbc());
        return list;
    }

    @GetMapping(value = "/findObj/{abc}")
    public User findObj(@PathVariable(value = "abc") int abc) {
        User user = new User();
        user.setAbc(abc + "啦啦啦啦啦啦");

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> formEntity = new HttpEntity<String>(JsonHelper.object2str(user).getData(), headers);
        return restTemplate.postForObject(Constant.HTTP + prop.getFlightIp() + ":" + prop.getFlightPort() + "/api/user/findObj", formEntity, User.class);
    }

//	@GetMapping(value = "/task/{abc}")
//	public User task(@PathVariable(value = "abc") int abc) {
//		return restTemplate.getForObject(Constant.HTTP + prop.getTaskIp() + ":" + prop.getTaskPort() + "/api/user/findObj", User.class, abc);
//	}


    @RequestMapping
    public String index(HttpSession httpSession) {
        httpSession.setAttribute("user", "userName...");
        return "hello world ==== " + ",,,,,sessionID=" + httpSession.getId();
    }

//	@PostMapping(value = "/getPage")
//	public ReturnMsg<User> getPage(@RequestBody @Valid User user, @RequestBody @Valid MyPage myPage) {
//		// 第一个参数：第几页
//		// 第二个参数：每页显示多少条。
//		PageHelper.startPage(myPage.getPage(), myPage.getPageSize(), "name ASC, create_time DESC");
//		
////		System.out.println(user.getId());
////		System.out.println(user.getName());
//		
//		user.setAbc(user.getAbc() + " =========== ");
////
//		// 查询

    /// /		List<TestUser> selectPageTest = testUserService.selectPageTest();
    /// /		PageInfo<TestUser> page = new PageInfo<>(selectPageTest);
    /// ///		ReturnMsg<PageInfo<TestUser>> msg = new ReturnMsg<>(Constant.CODE_OK, null, page);
    /// /		ReturnMsg<PageInfo<TestUser>> msg = ReturnMsg.getInstanceOKz(page);
//		ReturnMsg<User> msg = ReturnMsg.getInstanceOKz(user);
//		
//		return msg;
//	}

//	@RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @PostMapping(value = "/getPage")
//	public ReturnMsg<PageInfo<TestUser>> getPage(@RequestBody @Validated User user, @RequestBody @Validated MyPage myPage) {
    public ReturnMsg<PageInfo<TestUser>> getPage(@RequestBody @Valid User user, @RequestBody @Valid MyPage myPage) {
        // 第一个参数：第几页
        // 第二个参数：每页显示多少条。
//		PageHelper.startPage(2, 3, "name ASC, create_time DESC");
        PageHelper.startPage(myPage.getPage(), myPage.getPageSize(), "name ASC, create_time DESC");

//		System.out.println(user.getId());
//		System.out.println(user.getName());
//		
        // 查询
        List<TestUser> selectPageTest = testUserService.selectPageTest();
        PageInfo<TestUser> page = new PageInfo<>(selectPageTest);
//		ReturnMsg<PageInfo<TestUser>> msg = new ReturnMsg<>(Constant.CODE_OK, null, page);
        ReturnMsg<PageInfo<TestUser>> msg = ReturnMsg.getInstanceOKz(page);

        return msg;
    }


    @GetMapping(value = "/{abc}")
    @SystemLog(description = "测试有。。。。。。111")
    public User view(@PathVariable int abc) {
        User user = new User();
        user.setAbc(abc + " ---- 333333333333333333333  abc");
        user.setDate2(new Date());
        return user;
    }

    @PostMapping(value = "/testPost")
    @SystemLog(description = "测试有。。。。。。222")
    public User view222() {
        User user = new User();
        user.setAbc(" ---- 444444444444444444444  abc");
        user.setDate2(new Date());
        log.debug("test debug ============================================================ debug");
        return user;
    }
}
