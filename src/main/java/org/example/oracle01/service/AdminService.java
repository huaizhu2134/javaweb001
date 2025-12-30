package org.example.oracle01.service;

import org.example.oracle01.model.Admin;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // 查询管理员列表
    Result<PageResult<Admin>> getAdminList(Map<String, Object> params);

    // 根据ID查询管理员
    Result<Admin> getAdminById(Long id);

    // 根据用户名查询管理员
    Result<Admin> getAdminByUsername(String username);

    // 新增管理员
    Result<String> addAdmin(Admin admin);

    // 更新管理员
    Result<String> updateAdmin(Admin admin);

    // 删除管理员
    Result<String> deleteAdmin(Long id);

    // 统计管理员数量
    Result<Integer> countAdmin(Map<String, Object> params);
}