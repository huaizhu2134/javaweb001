package org.example.oracle01.service.impl;

import org.example.oracle01.mapper.AdminMapper;
import org.example.oracle01.model.Admin;
import org.example.oracle01.service.AdminService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Result<PageResult<Admin>> getAdminList(Map<String, Object> params) {
        try {
            // 获取总记录数
            Integer page = params.get("page") != null ? (Integer) params.get("page") : 0;
            Integer size = params.get("size") != null ? (Integer) params.get("size") : 10;

            int total = adminMapper.countAdmin(params);
            List<Admin> adminList = adminMapper.selectAdminList(params);

            PageResult<Admin> pageResult = new PageResult<>((long) total, adminList, page/size + 1, size);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询管理员列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Admin> getAdminById(Long id) {
        try {
            Admin admin = adminMapper.selectAdminById(id);
            if (admin != null) {
                return Result.success(admin);
            } else {
                return Result.error("未找到ID为 " + id + " 的管理员");
            }
        } catch (Exception e) {
            return Result.error("查询管理员失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Admin> getAdminByUsername(String username) {
        try {
            Admin admin = adminMapper.selectAdminByUsername(username);
            if (admin != null) {
                return Result.success(admin);
            } else {
                return Result.error("未找到用户名为 " + username + " 的管理员");
            }
        } catch (Exception e) {
            return Result.error("查询管理员失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> addAdmin(Admin admin) {
        try {
            int result = adminMapper.insertAdmin(admin);
            if (result > 0) {
                return Result.success("新增管理员成功");
            } else {
                return Result.error("新增管理员失败");
            }
        } catch (Exception e) {
            return Result.error("新增管理员失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> updateAdmin(Admin admin) {
        try {
            int result = adminMapper.updateAdmin(admin);
            if (result > 0) {
                return Result.success("更新管理员成功");
            } else {
                return Result.error("更新管理员失败");
            }
        } catch (Exception e) {
            return Result.error("更新管理员失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> deleteAdmin(Long id) {
        try {
            int result = adminMapper.deleteAdmin(id);
            if (result > 0) {
                return Result.success("删除管理员成功");
            } else {
                return Result.error("删除管理员失败");
            }
        } catch (Exception e) {
            return Result.error("删除管理员失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> countAdmin(Map<String, Object> params) {
        try {
            int count = adminMapper.countAdmin(params);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计管理员数量失败：" + e.getMessage());
        }
    }
}