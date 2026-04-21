package com.zh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zh.bean.auto.TPermission;
import com.zh.dao.mapper.TPermissionMapper;
import com.zh.service.TPermissionService;

@Service
public class TPermissionServiceImpl implements TPermissionService {

	@Autowired
	private TPermissionMapper mapper;

	@Override
	public List<TPermission> selectAll() {
		return mapper.selectAll();
	}
}
