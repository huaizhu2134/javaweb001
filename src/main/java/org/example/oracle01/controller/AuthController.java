package org.example.oracle01.controller;

import org.example.oracle01.model.Admin;
import org.example.oracle01.service.AdminService;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    // 简单的登录接口
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }

        // 查询管理员信息
        Result<Admin> result = adminService.getAdminByUsername(username);
        if (result.getCode() == 200 && result.getData() != null) {
            Admin admin = result.getData();
            if ("N".equals(admin.getIsDeleted()) && password.equals(admin.getPassword())) {
                // 登录成功，返回用户信息和token
                Map<String, Object> response = new HashMap<>();
                response.put("adminId", admin.getAdminId());
                response.put("username", admin.getUsername());
                response.put("nickname", admin.getNickname());
                response.put("roleId", admin.getRoleId());
                response.put("token", "fake-token-" + System.currentTimeMillis()); // 实际项目中应该使用JWT等安全方式生成token
                
                return Result.success("登录成功", response);
            } else {
                return Result.error("用户名或密码错误");
            }
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    // 简单的登出接口
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        // 实际项目中应该将token加入黑名单
        return Result.success("登出成功");
    }
}