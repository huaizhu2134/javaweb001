package org.example.oracle01.controller;

import org.example.oracle01.util.Result;
import org.example.oracle01.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取财务统计信息
    @GetMapping("/summary")
    public Result<Map<String, Object>> getFinanceSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // 今日订单数
            String todayOrdersSql = "SELECT COUNT(*) FROM TB_ORDER WHERE TRUNC(CREATE_TIME) = TRUNC(SYSDATE)";
            Integer todayOrders = jdbcTemplate.queryForObject(todayOrdersSql, Integer.class);
            summary.put("todayOrders", todayOrders != null ? todayOrders : 0);
            
            // 今日收入
            String todayIncomeSql = "SELECT NVL(SUM(ORDER_AMOUNT), 0) FROM TB_ORDER WHERE TRUNC(CREATE_TIME) = TRUNC(SYSDATE) AND ORDER_STATUS = '已完成'";
            BigDecimal todayIncome = jdbcTemplate.queryForObject(todayIncomeSql, BigDecimal.class);
            summary.put("todayIncome", todayIncome != null ? todayIncome : BigDecimal.ZERO);
            
            // 平台总收入（平台抽成20%）
            String platformIncomeSql = "SELECT NVL(SUM(ORDER_AMOUNT * 0.2), 0) FROM TB_ORDER WHERE ORDER_STATUS = '已完成'";
            BigDecimal platformIncome = jdbcTemplate.queryForObject(platformIncomeSql, BigDecimal.class);
            summary.put("platformIncome", platformIncome != null ? platformIncome : BigDecimal.ZERO);
            
            // 陪玩总收入（陪玩分成80%）
            String staffIncomeSql = "SELECT NVL(SUM(ORDER_AMOUNT * 0.8), 0) FROM TB_ORDER WHERE ORDER_STATUS = '已完成'";
            BigDecimal staffIncome = jdbcTemplate.queryForObject(staffIncomeSql, BigDecimal.class);
            summary.put("staffIncome", staffIncome != null ? staffIncome : BigDecimal.ZERO);
            
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取财务统计失败：" + e.getMessage());
        }
    }

    // 获取提现列表
    @GetMapping("/withdraw/list")
    public Result<PageResult<Map<String, Object>>> getWithdrawList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "staffId", required = false) String staffId,
            @RequestParam(value = "status", required = false) String status) {
        try {
            // 由于没有专门的提现表，这里模拟从陪玩余额变动记录中查询
            // 实际项目中应该创建TB_WITHDRAW表来存储提现记录
            List<Map<String, Object>> withdrawList = new ArrayList<>();
            
            // 查询有余额的陪玩人员作为待提现数据
            String sql = "SELECT s.STAFF_ID, s.NICKNAME, s.BALANCE, s.CREATE_TIME " +
                    "FROM TB_STAFF s WHERE s.IS_DELETED = 'N' AND s.BALANCE > 0 " +
                    "ORDER BY s.CREATE_TIME DESC " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            
            List<Map<String, Object>> staffList = jdbcTemplate.queryForList(sql, (page - 1) * size, size);
            
            int id = 1;
            for (Map<String, Object> staff : staffList) {
                Map<String, Object> withdraw = new HashMap<>();
                withdraw.put("withdrawId", id++);
                withdraw.put("staffId", staff.get("STAFF_ID"));
                withdraw.put("staffName", staff.get("NICKNAME"));
                withdraw.put("amount", staff.get("BALANCE"));
                withdraw.put("status", "待审核");
                withdraw.put("applyTime", staff.get("CREATE_TIME"));
                withdraw.put("approveTime", null);
                withdraw.put("approver", null);
                withdrawList.add(withdraw);
            }
            
            String countSql = "SELECT COUNT(*) FROM TB_STAFF WHERE IS_DELETED = 'N' AND BALANCE > 0";
            Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);
            
            PageResult<Map<String, Object>> pageResult = new PageResult<>(
                    total != null ? total.longValue() : 0L, 
                    withdrawList, 
                    page, 
                    size
            );
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取提现列表失败：" + e.getMessage());
        }
    }

    // 审核提现 - 通过
    @PostMapping("/withdraw/approve")
    public Result<String> approveWithdraw(@RequestBody Map<String, Object> params) {
        try {
            Long staffId = Long.valueOf(params.get("staffId").toString());
            String status = params.get("status") != null ? params.get("status").toString() : "已通过";
            
            if ("已通过".equals(status)) {
                // 清空陪玩余额（表示已提现）
                String sql = "UPDATE TB_STAFF SET BALANCE = 0, UPDATE_TIME = SYSDATE WHERE STAFF_ID = ?";
                jdbcTemplate.update(sql, staffId);
                return Result.success("审核通过，提现成功");
            } else {
                return Result.success("已拒绝提现申请");
            }
        } catch (Exception e) {
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    // 获取收入明细列表
    @GetMapping("/income/list")
    public Result<PageResult<Map<String, Object>>> getIncomeList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "staffId", required = false) String staffId,
            @RequestParam(value = "orderNo", required = false) String orderNo) {
        try {
            // 从已完成订单中查询收入明细
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT o.ORDER_ID, o.ORDER_NO, o.STAFF_ID, s.NICKNAME AS STAFF_NAME, ");
            sql.append("o.ORDER_AMOUNT, o.ORDER_AMOUNT * 0.8 AS STAFF_INCOME, ");
            sql.append("o.ORDER_STATUS, o.CREATE_TIME ");
            sql.append("FROM TB_ORDER o ");
            sql.append("LEFT JOIN TB_STAFF s ON o.STAFF_ID = s.STAFF_ID ");
            sql.append("WHERE o.IS_DELETED = 'N' AND o.ORDER_STATUS = '已完成' ");
            
            List<Object> queryParams = new ArrayList<>();
            if (staffId != null && !staffId.isEmpty()) {
                sql.append("AND o.STAFF_ID = ? ");
                queryParams.add(Long.valueOf(staffId));
            }
            if (orderNo != null && !orderNo.isEmpty()) {
                sql.append("AND o.ORDER_NO LIKE ? ");
                queryParams.add("%" + orderNo + "%");
            }
            
            sql.append("ORDER BY o.CREATE_TIME DESC ");
            sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            queryParams.add((page - 1) * size);
            queryParams.add(size);
            
            List<Map<String, Object>> orderList = jdbcTemplate.queryForList(sql.toString(), queryParams.toArray());
            
            List<Map<String, Object>> incomeList = new ArrayList<>();
            int id = 1;
            BigDecimal runningBalance = new BigDecimal("10000"); // 模拟起始余额
            for (Map<String, Object> order : orderList) {
                Map<String, Object> income = new HashMap<>();
                income.put("incomeId", id++);
                income.put("staffId", order.get("STAFF_ID"));
                income.put("staffName", order.get("STAFF_NAME"));
                income.put("orderId", order.get("ORDER_NO"));
                income.put("incomeType", "订单收入");
                BigDecimal staffIncome = (BigDecimal) order.get("STAFF_INCOME");
                income.put("amount", staffIncome);
                runningBalance = runningBalance.add(staffIncome != null ? staffIncome : BigDecimal.ZERO);
                income.put("balanceAfter", runningBalance);
                income.put("createTime", order.get("CREATE_TIME"));
                income.put("remark", "订单完成收入");
                incomeList.add(income);
            }
            
            // 获取总数
            StringBuilder countSql = new StringBuilder();
            countSql.append("SELECT COUNT(*) FROM TB_ORDER o WHERE o.IS_DELETED = 'N' AND o.ORDER_STATUS = '已完成' ");
            List<Object> countParams = new ArrayList<>();
            if (staffId != null && !staffId.isEmpty()) {
                countSql.append("AND o.STAFF_ID = ? ");
                countParams.add(Long.valueOf(staffId));
            }
            if (orderNo != null && !orderNo.isEmpty()) {
                countSql.append("AND o.ORDER_NO LIKE ? ");
                countParams.add("%" + orderNo + "%");
            }
            
            Integer total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, countParams.toArray());
            
            PageResult<Map<String, Object>> pageResult = new PageResult<>(
                    total != null ? total.longValue() : 0L,
                    incomeList,
                    page,
                    size
            );
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取收入明细失败：" + e.getMessage());
        }
    }
}
