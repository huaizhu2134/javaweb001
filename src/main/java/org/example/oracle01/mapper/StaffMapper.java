package org.example.oracle01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.oracle01.model.Staff;

import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper {

    // 查询陪玩人员列表
    List<Staff> selectStaffList(@Param("params") Map<String, Object> params);

    // 根据ID查询陪玩人员
    Staff selectStaffById(@Param("id") Long id);

    // 新增陪玩人员
    int insertStaff(Staff staff);

    // 更新陪玩人员
    int updateStaff(Staff staff);

    // 删除陪玩人员（逻辑删除）
    int deleteStaff(@Param("id") Long id);

    // 统计陪玩人员数量
    int countStaff(@Param("params") Map<String, Object> params);
}