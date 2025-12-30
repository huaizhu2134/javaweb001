package org.example.oracle01.controller;

import org.example.oracle01.model.Staff;
import org.example.oracle01.service.StaffService;
import org.example.oracle01.util.Result;
import org.example.oracle01.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // 查询陪玩人员列表
    @GetMapping("/list")
    public Result<PageResult<Staff>> getStaffList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "staffName", required = false) String staffName,
            @RequestParam(value = "status", required = false) String status) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        params.put("staffName", staffName);
        params.put("status", status);
        
        return staffService.getStaffList(params);
    }

    // 根据ID查询陪玩人员
    @GetMapping("/{id}")
    public Result<Staff> getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id);
    }

    // 新增陪玩人员
    @PostMapping
    public Result<String> addStaff(@RequestBody Staff staff) {
        return staffService.addStaff(staff);
    }

    // 更新陪玩人员
    @PutMapping
    public Result<String> updateStaff(@RequestBody Staff staff) {
        return staffService.updateStaff(staff);
    }

    // 删除陪玩人员
    @DeleteMapping("/{id}")
    public Result<String> deleteStaff(@PathVariable Long id) {
        return staffService.deleteStaff(id);
    }
}