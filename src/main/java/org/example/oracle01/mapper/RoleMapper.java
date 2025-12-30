package org.example.oracle01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.oracle01.model.Role;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {

    // 查询角色列表
    List<Role> selectRoleList(@Param("params") Map<String, Object> params);

    // 根据ID查询角色
    Role selectRoleById(@Param("id") Long id);

    // 新增角色
    int insertRole(Role role);

    // 更新角色
    int updateRole(Role role);

    // 删除角色（逻辑删除）
    int deleteRole(@Param("id") Long id);

    // 统计角色数量
    int countRole(@Param("params") Map<String, Object> params);
}