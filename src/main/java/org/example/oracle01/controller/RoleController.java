package org.example.oracle01.controller;

import org.example.oracle01.model.Role;
import org.example.oracle01.service.RoleService;
import org.example.oracle01.util.PageResult;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 查询角色列表
    @GetMapping("/list")
    public Result<PageResult<Role>> getRoleList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "roleName", required = false) String roleName) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        params.put("roleName", roleName);
        
        return roleService.getRoleList(params);
    }

    // 根据ID查询角色
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    // 新增角色
    @PostMapping
    public Result<String> addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    // 更新角色
    @PutMapping
    public Result<String> updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    // 删除角色
    @DeleteMapping("/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}