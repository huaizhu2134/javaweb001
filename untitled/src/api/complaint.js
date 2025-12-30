import request from '@/utils/request'

// 查询投诉列表
export function getComplaintList(params) {
  return request({
    url: '/api/complaint/list',
    method: 'get',
    params
  })
}

// 查询投诉详细
export function getComplaint(id) {
  return request({
    url: '/api/complaint/' + id,
    method: 'get'
  })
}

// 新增投诉
export function addComplaint(data) {
  return request({
    url: '/api/complaint',
    method: 'post',
    data: data
  })
}

// 修改投诉
export function updateComplaint(data) {
  return request({
    url: '/api/complaint',
    method: 'put',
    data: data
  })
}

// 删除投诉
export function delComplaint(id) {
  return request({
    url: '/api/complaint/' + id,
    method: 'delete'
  })
}

// 处理投诉
export function handleComplaint(data) {
  return request({
    url: '/api/complaint/handle',
    method: 'post',
    data: data
  })
}