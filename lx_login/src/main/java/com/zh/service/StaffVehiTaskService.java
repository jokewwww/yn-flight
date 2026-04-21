package com.zh.service;

import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.MyVehi;

import java.util.List;

public interface StaffVehiTaskService {


    List<MyStaffVehiTask> findStaffvehi(MyStaff stask);

    List<MyVehi> findListVehi(MyStaff staff);

    List<MyStaff> findstafflist(MyStaff staff);

    Integer insertstaff(MyStaff staff);

    void updatestaff(MyStaff staff);

    void deletestaff(MyStaff staff);

    Integer updatevehi(MyVehi vehi);

    void deletevehi(MyVehi vehi);

    MyStaff staffonefind(MyStaff staff);

    MyVehi vehionefind(MyVehi vehi);

    int selectstaffname(MyStaff staff);

}
