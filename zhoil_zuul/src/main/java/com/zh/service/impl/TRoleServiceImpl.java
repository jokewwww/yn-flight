package com.zh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zh.bean.auto.TRole;
import com.zh.dao.mapper.TRoleMapper;
import com.zh.service.TRoleService;

@Service
public class TRoleServiceImpl implements TRoleService {

	@Autowired
	private TRoleMapper mapper;

	@Override
	public List<TRole> selectAll() {
		return mapper.selectAll();
	}
}
