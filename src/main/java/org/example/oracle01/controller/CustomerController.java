package org.example.oracle01.controller;

import org.example.oracle01.model.Customer;
import org.example.oracle01.service.CustomerService;
import org.example.oracle01.util.Result;
import org.example.oracle01.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // 查询客户列表
    @GetMapping("/list")
    public Result<PageResult<Customer>> getCustomerList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) String status) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        params.put("username", username);
        params.put("status", status);
        
        return customerService.getCustomerList(params);
    }

    // 根据ID查询客户
    @GetMapping("/{id}")
    public Result<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // 新增客户
    @PostMapping
    public Result<String> addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    // 更新客户
    @PutMapping
    public Result<String> updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    // 删除客户
    @DeleteMapping("/{id}")
    public Result<String> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }

    // 客户充值
    @PostMapping("/recharge")
    public Result<String> rechargeBalance(@RequestBody Map<String, Object> params) {
        Long customerId = Long.valueOf(params.get("customerId").toString());
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        return customerService.rechargeBalance(customerId, amount);
    }
}