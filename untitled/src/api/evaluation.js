import request from '@/utils/request'

// 查询评价列表
export function getEvaluationList(params) {
  return request({
    url: '/api/evaluation/list',
    method: 'get',
    params
  })
}

// 查询评价详细
export function getEvaluation(id) {
  return request({
    url: '/api/evaluation/' + id,
    method: 'get'
  })
}

// 新增评价
export function addEvaluation(data) {
  return request({
    url: '/api/evaluation',
    method: 'post',
    data: data
  })
}

// 修改评价
export function updateEvaluation(data) {
  return request({
    url: '/api/evaluation',
    method: 'put',
    data: data
  })
}

// 删除评价
export function delEvaluation(id) {
  return request({
    url: '/api/evaluation/' + id,
    method: 'delete'
  })
}