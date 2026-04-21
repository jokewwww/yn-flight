package com.zh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zh.bean.auto.TRolePermission;
import com.zh.dao.mapper.TRolePermissionMapper;
import com.zh.service.TRolePermissionService;

@Service
public class TRolePermissionServiceImpl implements TRolePermissionService {

	@Autowired
	private TRolePermissionMapper mapper;

	@Override
	public List<TRolePermission> selectAll() {
		return mapper.selectAll();
	}
}
