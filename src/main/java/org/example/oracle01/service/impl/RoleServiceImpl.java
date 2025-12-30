package org.example.oracle01.service.impl;

import org.example.oracle01.mapper.RoleMapper;
import org.example.oracle01.model.Role;
import org.example.oracle01.service.RoleService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Result<PageResult<Role>> getRoleList(Map<String, Object> params) {
        try {
            // 获取总记录数
            Integer page = params.get("page") != null ? (Integer) params.get("page") : 0;
            Integer size = params.get("size") != null ? (Integer) params.get("size") : 10;

            int total = roleMapper.countRole(params);
            List<Role> roleList = roleMapper.selectRoleList(params);

            PageResult<Role> pageResult = new PageResult<>((long) total, roleList, page/size + 1, size);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询角色列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Role> getRoleById(Long id) {
        try {
            Role role = roleMapper.selectRoleById(id);
            if (role != null) {
                return Result.success(role);
            } else {
                return Result.error("未找到ID为 " + id + " 的角色");
            }
        } catch (Exception e) {
            return Result.error("查询角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> addRole(Role role) {
        try {
            int result = roleMapper.insertRole(role);
            if (result > 0) {
                return Result.success("新增角色成功");
            } else {
                return Result.error("新增角色失败");
            }
        } catch (Exception e) {
            return Result.error("新增角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> updateRole(Role role) {
        try {
            int result = roleMapper.updateRole(role);
            if (result > 0) {
                return Result.success("更新角色成功");
            } else {
                return Result.error("更新角色失败");
            }
        } catch (Exception e) {
            return Result.error("更新角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> deleteRole(Long id) {
        try {
            int result = roleMapper.deleteRole(id);
            if (result > 0) {
                return Result.success("删除角色成功");
            } else {
                return Result.error("删除角色失败");
            }
        } catch (Exception e) {
            return Result.error("删除角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> countRole(Map<String, Object> params) {
        try {
            int count = roleMapper.countRole(params);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计角色数量失败：" + e.getMessage());
        }
    }
}