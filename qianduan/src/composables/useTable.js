import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 通用表格管理组合式函数
 * @param {Function} fetchApi - 获取数据的API函数
 * @param {Object} options - 配置选项
 */
export function useTable(fetchApi, options = {}) {
  const {
    defaultPageSize = 10,
    immediate = true,
    formatParams = null,
    onSuccess = null,
    onError = null
  } = options

  // 加载状态
  const loading = ref(false)
  
  // 数据列表
  const dataList = ref([])
  
  // 分页信息
  const pagination = reactive({
    current: 1,
    size: defaultPageSize,
    total: 0
  })
  
  // 搜索参数（由调用方传入）
  let searchParams = reactive({})

  /**
   * 设置搜索参数引用
   */
  const setSearchParams = (params) => {
    searchParams = params
  }

  /**
   * 获取数据
   */
  const fetchData = async () => {
    loading.value = true
    try {
      let params = {
        ...searchParams,
        page: pagination.current,
        size: pagination.size
      }
      
      // 自定义参数格式化
      if (formatParams) {
        params = formatParams(params)
      }
      
      const response = await fetchApi(params)
      
      if (response.code === 200) {
        dataList.value = response.data?.records || []
        pagination.total = response.data?.total || 0
        onSuccess?.(response.data)
      } else {
        console.error('获取数据失败:', response.message)
        ElMessage.error(response.message || '获取数据失败')
      }
    } catch (error) {
      console.error('获取数据失败:', error)
      ElMessage.error('获取数据失败')
      onError?.(error)
    } finally {
      loading.value = false
    }
  }

  /**
   * 搜索（重置到第一页）
   */
  const handleSearch = () => {
    pagination.current = 1
    fetchData()
  }

  /**
   * 刷新（保持当前页）
   */
  const refresh = () => {
    fetchData()
  }

  /**
   * 分页大小变化
   */
  const handleSizeChange = (val) => {
    pagination.size = val
    fetchData()
  }

  /**
   * 当前页变化
   */
  const handleCurrentChange = (val) => {
    pagination.current = val
    fetchData()
  }

  return {
    loading,
    dataList,
    pagination,
    setSearchParams,
    fetchData,
    handleSearch,
    refresh,
    handleSizeChange,
    handleCurrentChange
  }
}

/**
 * 防抖函数
 * @param {Function} fn - 要防抖的函数
 * @param {number} delay - 延迟时间（毫秒）
 */
export function debounce(fn, delay = 300) {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

/**
 * 状态标签类型映射
 */
export const statusTypeMap = {
  // 通用状态
  '正常': 'success',
  '冻结': 'danger',
  '注销': 'info',
  
  // 陪玩状态
  '空闲': 'success',
  '忙碌': 'warning',
  '离线': 'info',
  '封禁': 'danger',
  
  // 订单状态
  '待支付': 'warning',
  '已支付': 'primary',
  '服务中': 'info',
  '已完成': 'success',
  '已取消': 'danger',
  '退款中': 'warning',
  '已退款': 'danger',
  
  // 投诉状态
  '待处理': 'warning',
  '处理中': 'info',
  '已解决': 'success',
  '已关闭': 'danger',
  
  // 提现状态
  '待审核': 'warning',
  '已通过': 'success',
  '已拒绝': 'danger',
  
  // 会员等级
  '普通会员': 'info',
  'VIP会员': 'warning',
  'SVIP会员': 'danger',
  
  // 技能等级
  '青铜': 'info',
  '白银': 'info',
  '黄金': 'warning',
  '铂金': 'primary',
  '钻石': 'primary',
  '王者': 'danger'
}

/**
 * 获取状态标签类型
 * @param {string} status - 状态值
 * @returns {string} - Element Plus tag type
 */
export function getStatusType(status) {
  return statusTypeMap[status] || 'info'
}
