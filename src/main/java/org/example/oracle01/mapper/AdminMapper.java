package org.example.oracle01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.oracle01.model.Admin;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    // 查询管理员列表
    List<Admin> selectAdminList(@Param("params") Map<String, Object> params);

    // 根据ID查询管理员
    Admin selectAdminById(@Param("id") Long id);

    // 新增管理员
    int insertAdmin(Admin admin);

    // 更新管理员
    int updateAdmin(Admin admin);

    // 删除管理员（逻辑删除）
    int deleteAdmin(@Param("id") Long id);

    // 统计管理员数量
    int countAdmin(@Param("params") Map<String, Object> params);
    
    // 根据用户名查询管理员
    Admin selectAdminByUsername(@Param("username") String username);
}