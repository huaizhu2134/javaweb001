package org.example.oracle01.service;

import org.example.oracle01.model.Customer;
import org.example.oracle01.util.Result;
import org.example.oracle01.util.PageResult;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    // 查询客户列表
    Result<PageResult<Customer>> getCustomerList(Map<String, Object> params);

    // 根据ID查询客户
    Result<Customer> getCustomerById(Long id);

    // 新增客户
    Result<String> addCustomer(Customer customer);

    // 更新客户
    Result<String> updateCustomer(Customer customer);

    // 删除客户
    Result<String> deleteCustomer(Long id);

    // 统计客户数量
    Result<Integer> countCustomer(Map<String, Object> params);

    // 客户充值
    Result<String> rechargeBalance(Long customerId, java.math.BigDecimal amount);
}