package org.example.oracle01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.oracle01.model.Customer;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerMapper {

    // 查询客户列表
    List<Customer> selectCustomerList(@Param("params") Map<String, Object> params);

    // 根据ID查询客户
    Customer selectCustomerById(@Param("id") Long id);

    // 新增客户
    int insertCustomer(Customer customer);

    // 更新客户
    int updateCustomer(Customer customer);

    // 删除客户（逻辑删除）
    int deleteCustomer(@Param("id") Long id);

    // 统计客户数量
    int countCustomer(@Param("params") Map<String, Object> params);

    // 充值 - 更新余额
    int rechargeBalance(@Param("customerId") Long customerId, @Param("amount") java.math.BigDecimal amount);
}