import request from '@/utils/request'

// 获取财务统计信息
export function getFinanceSummary(params) {
  return request({
    url: '/api/finance/summary',
    method: 'get',
    params
  })
}

// 提现申请
export function applyWithdraw(data) {
  return request({
    url: '/api/finance/withdraw',
    method: 'post',
    data: data
  })
}

// 审核提现
export function approveWithdraw(data) {
  return request({
    url: '/api/finance/withdraw/approve',
    method: 'put',
    data: data
  })
}

// 获取提现列表
export function getWithdrawList(params) {
  return request({
    url: '/api/finance/withdraw/list',
    method: 'get',
    params
  })
}

// 获取收入明细
export function getIncomeList(params) {
  return request({
    url: '/api/finance/income/list',
    method: 'get',
    params
  })
}