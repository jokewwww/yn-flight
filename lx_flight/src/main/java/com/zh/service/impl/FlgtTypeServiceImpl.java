package com.zh.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.*;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.FlgtTypeMapper;
import com.zh.exception.CustomException;
import com.zh.service.FlgtTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class FlgtTypeServiceImpl implements FlgtTypeService {

    @Autowired
    private FlgtTypeMapper flgtTypeMapper;

    @Override
    public List<FlgtTypeStaff> getFlgtTypeByStaffId(String staffId) {
        return flgtTypeMapper.getFlgtTypeByStaffId(staffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindFlgtTypeByStaffId(StaffBind staffBind) {
        int deleteBind = flgtTypeMapper.deleteBindByStaffId(staffBind.getStaffId());
        int i = 0;
        if (CollectionUtil.isNotEmpty(staffBind.getIds())) {
            i = flgtTypeMapper.bindFlgtTypeByStaffId(staffBind);
        }
        return i;
    }

    @Override
    public List<FlgtType> getFlgtType(FlgtType flgtType) {
        return flgtTypeMapper.getFlgtType(flgtType);
    }

    @Override
    public FlgtType addFlgtType(FlgtType flgtType) {
        FlgtType one = flgtTypeMapper.getOne(flgtType);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        Integer id = flgtTypeMapper.addFlgtType(flgtType);
        return flgtType;
    }

    @Override
    public int updateFlgtType(FlgtType flgtType) {
        FlgtType one = flgtTypeMapper.getOne(flgtType);
        if (ObjectUtil.isNotNull(one) && ObjectUtil.notEqual(one.getId(), flgtType.getId())) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        return flgtTypeMapper.updateFlgtType(flgtType);
    }

    @Override
    public int deleteFlgtType(FlgtType flgtType) {
        return flgtTypeMapper.deleteFlgtType(flgtType.getId());
    }

    @Override
    public List<FlgtTypeStaff> getNoFlgtTypeByStaffId(String staffId) {
        return flgtTypeMapper.getNoFlgtTypeByStaffId(staffId);
    }

    @Override
    public List<FlgtTypeVehi> getFlgtTypeByVehi(String flgtTypeId) {
        return flgtTypeMapper.getFlgtTypeByVehi(flgtTypeId);
    }

    @Override
    public int bindFlgtTypeBySVehi(VehiBind vehiBind) {
        int deleteBind = flgtTypeMapper.deleteBindByVehi(vehiBind.getFlgtTypeId());
        int i = 0;
        if (CollectionUtil.isNotEmpty(vehiBind.getIds())) {
            i = flgtTypeMapper.bindFlgtTypeBySVehi(vehiBind);
        }

        return i;
    }

    @Override
    public int getNoFlgtNoByStaffId(String staffId, String type) {
        List<FlgtTypeStaff> noFlgtByStaffId = flgtTypeMapper.getNoFlgtByStaffId(staffId, type);
        if (CollectionUtil.isEmpty(noFlgtByStaffId)) {
            MyStaffVehi sfvhVehiNo = flgtTypeMapper.getSfvhVehiNo(staffId);
            if (ObjectUtil.isNotNull(sfvhVehiNo)) {
                List<FlgtTypeVehi> noFlgtByVehiNo = flgtTypeMapper.getNoFlgtByVehiNo(sfvhVehiNo.getSfvhVehiNo(), type);
                if (CollectionUtil.isEmpty(noFlgtByVehiNo)) {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
        return 2;
    }
}
