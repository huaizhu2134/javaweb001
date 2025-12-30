package org.example.oracle01.service;

import org.example.oracle01.model.Role;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;

import java.util.List;
import java.util.Map;

public interface RoleService {

    // 查询角色列表
    Result<PageResult<Role>> getRoleList(Map<String, Object> params);

    // 根据ID查询角色
    Result<Role> getRoleById(Long id);

    // 新增角色
    Result<String> addRole(Role role);

    // 更新角色
    Result<String> updateRole(Role role);

    // 删除角色
    Result<String> deleteRole(Long id);

    // 统计角色数量
    Result<Integer> countRole(Map<String, Object> params);
}