package org.example.oracle01.service;

import org.example.oracle01.model.Staff;
import org.example.oracle01.util.Result;
import org.example.oracle01.util.PageResult;

import java.util.List;
import java.util.Map;

public interface StaffService {

    // 查询陪玩人员列表
    Result<PageResult<Staff>> getStaffList(Map<String, Object> params);

    // 根据ID查询陪玩人员
    Result<Staff> getStaffById(Long id);

    // 新增陪玩人员
    Result<String> addStaff(Staff staff);

    // 更新陪玩人员
    Result<String> updateStaff(Staff staff);

    // 删除陪玩人员
    Result<String> deleteStaff(Long id);

    // 统计陪玩人员数量
    Result<Integer> countStaff(Map<String, Object> params);
}