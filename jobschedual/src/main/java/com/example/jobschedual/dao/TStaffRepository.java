package com.example.jobschedual.dao;

import com.example.jobschedual.entity.TStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TStaffRepository extends JpaRepository<TStaff, Serializable>, JpaSpecificationExecutor<TStaff> {

    TStaff findByStaffId(String staffId);
    @Query("select a from TStaff a where a.staffGroupId =:staffGroupId and a.staffType < 4 order by staffLevel")
    List<TStaff> findByStaffGroupIds(@Param("staffGroupId") String staffGroupId);

    List<TStaff> findByStaffGroupId(String staffGroupId);

    List<TStaff> findByStaffGroupIdAndStaffType(String staffGroupId,String staffType);

    @Query(value = "SELECT t.* FROM `T_STAFF` t LEFT JOIN `T_SCHEDULING` tt ON t.staff_id=tt.staff_id where tt.group_id=:groupId  ORDER BY tt.group_id ASC, tt.`id` ASC",nativeQuery = true)
    List<TStaff> findAllByTScheduling(@Param("groupId") String groupId);
}
