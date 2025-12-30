import request from '@/utils/request'

// 查询客户列表
export function getCustomerList(params) {
  return request({
    url: '/api/customer/list',
    method: 'get',
    params
  })
}

// 查询客户详细
export function getCustomer(id) {
  return request({
    url: '/api/customer/' + id,
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/api/customer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: '/api/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(id) {
  return request({
    url: '/api/customer/' + id,
    method: 'delete'
  })
}

// 客户充值
export function rechargeBalance(data) {
  return request({
    url: '/api/customer/recharge',
    method: 'post',
    data: data
  })
}