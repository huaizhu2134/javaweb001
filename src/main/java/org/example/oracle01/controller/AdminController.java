package org.example.oracle01.controller;

import org.example.oracle01.model.Admin;
import org.example.oracle01.service.AdminService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 查询管理员列表
    @GetMapping("/list")
    public Result<PageResult<Admin>> getAdminList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) String status) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        params.put("username", username);
        params.put("status", status);
        
        return adminService.getAdminList(params);
    }

    // 根据ID查询管理员
    @GetMapping("/{id}")
    public Result<Admin> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }

    // 新增管理员
    @PostMapping
    public Result<String> addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    // 更新管理员
    @PutMapping
    public Result<String> updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin);
    }

    // 删除管理员
    @DeleteMapping("/{id}")
    public Result<String> deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }
}