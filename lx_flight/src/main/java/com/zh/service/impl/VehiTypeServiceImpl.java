package com.zh.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.StaffBind;
import com.zh.bean.login.VehiType;
import com.zh.bean.login.VehiTypeStaff;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.dao.mapper.my.VehiTypeMapper;
import com.zh.exception.CustomException;
import com.zh.service.VehiTypeService;
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
public class VehiTypeServiceImpl implements VehiTypeService {

    @Autowired
    private VehiTypeMapper vehiTypeMapper;
    @Autowired
    private StaffMapper staffMapper;

    @Override
    public List<VehiTypeStaff> getVehiNoByStaffId(String staffId) {
        MyStaff staffById = staffMapper.getStaffById(staffId);
        if (staffById == null) {
            throw new CustomException(ReturnMsg.getInstanceNGz("绑定车辆的人员不存在", null));
        }
        return vehiTypeMapper.getVehiNoByStaffId(staffId, staffById.getStaffAirportCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindVehiNoByStaffId(StaffBind staffBind) {
        vehiTypeMapper.deleteBindByStaffId(staffBind.getStaffId());
        int i = 0;
        if (CollectionUtil.isNotEmpty(staffBind.getIds())) {
            i = vehiTypeMapper.bindVehiNoByStaffId(staffBind);
        }
        return i;
    }

    @Override
    public List<VehiType> getVehiType(VehiType vehiType) {
        return vehiTypeMapper.getVehiType(vehiType);
    }

    @Override
    public VehiType addVehiType(VehiType vehiType) {
        VehiType one = vehiTypeMapper.getOneVehiType(vehiType);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        vehiTypeMapper.addVehiType(vehiType);
        return vehiType;
    }

    @Override
    public int updateVehiType(VehiType vehiType) {
        VehiType one = vehiTypeMapper.getOneVehiType(vehiType);
        if (ObjectUtil.isNotNull(one) && ObjectUtil.notEqual(one.getId(), vehiType.getId())) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
        }
        return vehiTypeMapper.updateVehiType(vehiType);
    }

    @Override
    public int deleteVehiType(VehiType vehiType) {
        return vehiTypeMapper.deleteVehiType(vehiType.getId());
    }

    @Override
    public List<VehiTypeStaff> getNoVehiNoByStaffId(String staffId) {
        return vehiTypeMapper.getNoVehiNoByStaffId(staffId);
    }
}
