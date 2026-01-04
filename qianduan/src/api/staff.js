import request from '@/utils/request'

// 查询陪玩人员列表
export function getStaffList(params) {
  return request({
    url: '/api/staff/list',
    method: 'get',
    params
  })
}

// 查询陪玩人员详细
export function getStaff(id) {
  return request({
    url: '/api/staff/' + id,
    method: 'get'
  })
}

// 新增陪玩人员
export function addStaff(data) {
  return request({
    url: '/api/staff',
    method: 'post',
    data: data
  })
}

// 修改陪玩人员
export function updateStaff(data) {
  return request({
    url: '/api/staff',
    method: 'put',
    data: data
  })
}

// 删除陪玩人员
export function delStaff(id) {
  return request({
    url: '/api/staff/' + id,
    method: 'delete'
  })
}