package com.zh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zh.bean.auto.TRoleUser;
import com.zh.dao.mapper.TRoleUserMapper;
import com.zh.dao.mapper.my.MyTRoleUserMapper;
import com.zh.service.TRoleUserService;

@Service
public class TRoleUserServiceImpl implements TRoleUserService {

	@Autowired
	private TRoleUserMapper mapper;

	@Autowired
	private MyTRoleUserMapper myMapper;

	@Override
	public List<TRoleUser> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public List<TRoleUser> selectAllManager() {
		return myMapper.selectAllManager();
	}
}
