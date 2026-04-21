package com.zh.controller;

import com.zh.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试
 */
@RestController
@RequestMapping(value = "/api/user")
public class ApiUserController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/{abc}")
    public User view(@PathVariable int abc) {
        User user = new User();
        user.setAbc(abc + " ---- 1111111111111111122222222222222  abc");
        user.setDate2(new Date());
        return user;
    }

    @GetMapping(value = "/list")
    public List<User> returnList() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setAbc(" ---- 3333333333  abc");
//		user.setDate2(new Date());
        list.add(user);

        user = new User();
        user.setAbc(" ---- 4444444444  abc");
//		user.setDate2(new Date());
        list.add(user);

        return list;
    }

    @PostMapping(value = "/findObj")
    public User postObj(@RequestBody User user) {
        user.setAbc(user.getAbc() + " ---- 333333333  abc");
        return user;
    }

//	@GetMapping(value = "/placecodeByFlgtId")
//	public MyFlightTwo placecodeByFlgtId(String flgtId){
//		return flightInfoService.placecodeByFlgtId(flgtId);
//	}
}
