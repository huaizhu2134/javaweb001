# 游戏陪玩管理系统 - AI提示词大全

## 使用说明

本文档包含15个详细的AI提示词，覆盖数据库设计、前端开发、后端开发三大模块。每个提示词都经过优化，可以直接复制到ChatGPT、Claude、Kimi等AI工具中使用。

---

## 📊 数据库设计提示词（5个）

### 提示词1：PDB和表空间创建

**适用范围**：Oracle数据库初始化

```
请生成Oracle 19c数据库的PDB创建和表空间创建SQL语句，要求如下：

1. 创建一个名为"pdb_playmate"的可插拔数据库
   - 管理员用户：admin，密码：admin123
   - 默认表空间：users
   - 数据文件路径：/u01/app/oracle/oradata/orcl/pdb_playmate/
   - 初始大小：500M，自动扩展100M，最大10G
   - 字符集：AL32UTF8

2. 创建以下表空间：
   - TBS_PLAYMATE_STAFF：陪玩人员表空间，初始500M，自动扩展100M，最大5G
   - TBS_PLAYMATE_CUSTOMER：客户表空间，初始500M，自动扩展100M，最大5G
   - TBS_PLAYMATE_ORDER：订单表空间，初始1G，自动扩展200M，最大10G
   - TBS_PLAYMATE_FINANCE：财务表空间，初始500M，自动扩展100M，最大5G
   - TBS_PLAYMATE_SYSTEM：系统表空间，初始300M，自动扩展50M，最大3G
   - TBS_PLAYMATE_TEMP：临时表空间，初始200M，自动扩展50M，最大2G
   - TBS_PLAYMATE_UNDO：撤销表空间，初始300M，自动扩展50M，最大3G

3. 每个表空间指定独立的数据文件，文件名格式：tbs_[name]01.dbf

4. 提供完整的SQL脚本，包括：
   - 创建PDB
   - 打开PDB
   - 设置PDB自动启动
   - 创建所有表空间
   - 验证创建结果

请在每个SQL语句后添加注释说明其作用。
```

**预期输出**：完整的PDB和表空间创建SQL脚本

**使用建议**：在Oracle容器数据库（CDB）中执行PDB创建，然后在PDB中执行表空间创建

---

### 提示词2：核心数据表设计

**适用范围**：数据库表结构设计

```
请为游戏陪玩后台管理系统设计Oracle数据库表结构，包含以下9张核心表：

1. TB_STAFF（陪玩人员表）
   字段：staff_id(主键), staff_name, real_name, gender(M/F), age(18-60), 
         phone(唯一), email, avatar_url, skill_level, service_type, 
         unit_price(≥0), status(空闲/忙碌/离线/封禁), total_orders, 
         total_income, avg_score, cert_status, create_time, update_time, 
         last_login_time, is_deleted(Y/N)
   约束：主键、唯一约束(phone)、检查约束(age, unit_price, status)
   索引：staff_name, status, skill_level, create_time

2. TB_CUSTOMER（客户表）
   字段：customer_id(主键), username(唯一), password, nickname, 
         phone(唯一), email, gender, age, avatar_url, member_level, 
         total_consume, order_count, balance, status(正常/冻结/注销), 
         create_time, update_time, last_login_time, is_deleted
   约束：主键、唯一约束(username, phone)、检查约束(status)

3. TB_ORDER（订单表）- 按季度分区
   字段：order_id(主键), order_no(唯一), customer_id(外键), 
         staff_id(外键), game_type, service_hours(>0), unit_price(≥0), 
         total_amount(≥0), platform_commission, staff_income, 
         order_status(待支付/已支付/服务中/已完成/已取消/退款中/已退款), 
         pay_time, start_time, end_time, customer_comment, staff_comment, 
         create_time, update_time, is_deleted
   约束：主键、外键(customer_id, staff_id)、检查约束
   分区：按create_time按季度范围分区(P2025_Q1, P2025_Q2...)

4. TB_EVALUATION（评价表）
   字段：eval_id(主键), order_id(外键), customer_id(外键), 
         staff_id(外键), score(1-5), content, tags, is_anonymous(Y/N), 
         staff_reply, reply_time, create_time, is_deleted

5. TB_COMPLAINT（投诉表）
   字段：complaint_id(主键), order_id(外键), customer_id(外键), 
         staff_id(外键), complaint_type(服务态度/服务质量/收费问题/其他), 
         complaint_content, evidence_url, 
         complaint_status(待处理/处理中/已解决/已关闭), 
         handler_id, handler_comment, handle_time, create_time, is_deleted

6. TB_ADMIN（管理员表）
   字段：admin_id(主键), username(唯一), password, nickname, 
         phone, email, role_id(外键), status(正常/冻结), 
         last_login_time, create_time, update_time, is_deleted

7. TB_ROLE（角色表）
   字段：role_id(主键), role_name, role_desc, permissions, 
         create_time, update_time, is_deleted

8. TB_DICTIONARY（数据字典表）
   字段：dict_id(主键), dict_type, dict_code, dict_value, 
         dict_desc, sort_order, create_time, is_deleted

9. TB_OPERATION_LOG（操作日志表）
   字段：log_id(主键), admin_id, admin_name, operation_type, 
         operation_desc, request_url, request_method, request_ip, 
         request_params, response_result, execute_time, create_time, is_deleted

要求：
- 每张表都要有主键约束
- 设置必要的外键约束（标注引用关系）
- 设置唯一约束（phone, username, order_no等）
- 设置检查约束（age范围、price≥0、status值范围）
- 为高频查询字段创建索引
- 使用序列作为主键自增方式
- 每张表都有create_time, update_time, is_deleted字段
- 订单表按季度进行范围分区
- 所有字段使用驼峰命名转下划线命名

请提供完整的CREATE TABLE语句，包括所有约束和索引，并添加中文注释说明。
```

**预期输出**：9张表的完整CREATE TABLE语句

**使用建议**：逐个表创建，先创建主表（TB_STAFF, TB_CUSTOMER），再创建子表

---

### 提示词3：存储过程和函数

**适用范围**：数据库业务逻辑实现

```
请为游戏陪玩系统生成Oracle存储过程和函数，要求如下：

存储过程1：PROC_CREATE_ORDER（创建订单）
输入参数：
  p_customer_id IN NUMBER - 客户ID
  p_staff_id IN NUMBER - 陪玩ID
  p_game_type IN VARCHAR2 - 游戏类型
  p_service_hours IN NUMBER - 服务时长
  p_unit_price IN NUMBER - 单价
输出参数：
  p_order_no OUT VARCHAR2 - 订单号
  p_result OUT VARCHAR2 - 结果：SUCCESS/FAIL
  p_message OUT VARCHAR2 - 消息
功能流程：
  1. 检查陪玩人员状态是否为"空闲"
  2. 检查客户账户状态是否为"正常"
  3. 计算总金额：服务时长 * 单价
  4. 计算平台抽成（默认20%）和陪玩收入
  5. 生成订单号格式：PM + 年月日 + 10位序列号
  6. 插入订单数据到TB_ORDER
  7. 更新陪玩人员状态为"忙碌"
  8. 提交事务
异常处理：
  - 如果陪玩人员状态不是空闲，返回FAIL和具体状态
  - 如果客户状态异常，返回FAIL和具体状态
  - 任何异常回滚事务，返回FAIL和错误信息

存储过程2：PROC_COMPLETE_ORDER（完成订单）
输入参数：
  p_order_id IN NUMBER - 订单ID
输出参数：
  p_result OUT VARCHAR2 - 结果：SUCCESS/FAIL
  p_message OUT VARCHAR2 - 消息
功能流程：
  1. 查询订单状态，检查是否为"服务中"
  2. 更新订单状态为"已完成"，设置结束时间
  3. 更新陪玩人员状态为"空闲"
  4. 增加陪玩人员总收入和订单数
  5. 增加客户订单数
  6. 提交事务
异常处理：
  - 如果订单状态不是"服务中"，返回FAIL
  - 任何异常回滚事务

存储过程3：PROC_WITHDRAW_APPLICATION（提现申请）
输入参数：
  p_staff_id IN NUMBER - 陪玩ID
  p_amount IN NUMBER - 提现金额
输出参数：
  p_result OUT VARCHAR2 - 结果：SUCCESS/FAIL
  p_message OUT VARCHAR2 - 消息
功能流程：
  1. 查询陪玩人员可用余额（总收入 - 已提现金额）
  2. 检查余额是否足够
  3. 检查提现金额是否低于最低门槛（100元）
  4. 插入提现记录到TB_WITHDRAW表
  5. 提交事务
异常处理：
  - 余额不足返回FAIL和当前余额
  - 低于最低门槛返回FAIL

函数1：FUNC_CALC_STAFF_INCOME（计算陪玩人员收入）
输入参数：
  p_staff_id IN NUMBER - 陪玩ID
  p_start_date IN DATE DEFAULT NULL - 开始时间（可选）
  p_end_date IN DATE DEFAULT NULL - 结束时间（可选）
返回值：
  NUMBER - 总收入金额
功能：
  - 如果时间参数为空，查询总收入
  - 如果时间参数不为空，查询指定时间范围的收入
  - 只统计已完成订单的收入

函数2：FUNC_GET_STAFF_AVG_SCORE（获取陪玩人员平均评分）
输入参数：
  p_staff_id IN NUMBER - 陪玩ID
返回值：
  NUMBER - 平均评分（保留2位小数）
功能：
  - 计算陪玩人员的平均评分
  - 如果没有评价记录，返回5.00

函数3：FUNC_CHECK_CUSTOMER_BALANCE（检查客户余额）
输入参数：
  p_customer_id IN NUMBER - 客户ID
返回值：
  NUMBER - 当前余额
功能：
  - 查询客户的账户余额
  - 如果客户不存在，返回0

请提供完整的PL/SQL代码，包括声明部分、执行部分、异常处理部分，并添加详细注释。
```

**预期输出**：3个存储过程和3个函数的完整PL/SQL代码

**使用建议**：逐个创建，先测试简单的函数，再测试复杂的存储过程

---

### 提示词4：触发器和视图

**适用范围**：数据库自动维护和查询优化

```
请为游戏陪玩系统生成Oracle触发器和视图，要求如下：

触发器1：TRIG_STAFF_UPDATE_TIME
触发时机：BEFORE UPDATE ON TB_STAFF
类型：行级触发器
功能：
  - 自动更新UPDATE_TIME字段为当前时间SYSDATE
  - 对每次更新操作都生效

触发器2：TRIG_ORDER_UPDATE_TIME
触发时机：BEFORE UPDATE ON TB_ORDER
类型：行级触发器
功能：
  - 自动更新UPDATE_TIME字段为当前时间SYSDATE
  - 对每次更新操作都生效

触发器3：TRIG_AUTO_EVALUATION
触发时机：AFTER UPDATE OF ORDER_STATUS ON TB_ORDER
类型：行级触发器
条件：当ORDER_STATUS从"服务中"变为"已完成"
功能：
  1. 检查该订单是否已有评价记录
  2. 如果没有评价，自动插入一条好评：
     - EVAL_ID：使用seq_eval_id.nextval
     - ORDER_ID：新完成的订单ID
     - CUSTOMER_ID, STAFF_ID：从订单表中获取
     - SCORE：5分
     - CONTENT：'系统自动好评：服务满意'
     - CREATE_TIME：当前时间

触发器4：TRIG_EVALUATION_UPDATE_SCORE
触发时机：AFTER INSERT OR UPDATE OF SCORE ON TB_EVALUATION
类型：行级触发器
功能：
  1. 重新计算该陪玩人员的平均评分
  2. 更新TB_STAFF表的AVG_SCORE字段
  3. 同时更新UPDATE_TIME字段

视图1：VIEW_STAFF_PERFORMANCE（陪玩人员业绩视图）
字段：
  - s.STAFF_ID, s.STAFF_NAME, s.REAL_NAME, s.SKILL_LEVEL
  - s.UNIT_PRICE, s.AVG_SCORE, s.STATUS, s.CREATE_TIME
  - COUNT(o.ORDER_ID) AS TOTAL_ORDERS（总订单数）
  - NVL(SUM(o.TOTAL_AMOUNT), 0) AS TOTAL_SALES（总销售额）
  - NVL(SUM(o.STAFF_INCOME), 0) AS TOTAL_INCOME（总收入）
  - NVL(AVG(o.TOTAL_AMOUNT), 0) AS AVG_ORDER_AMOUNT（平均订单金额）
关联：TB_STAFF s LEFT JOIN TB_ORDER o
条件：o.ORDER_STATUS = '已完成' AND o.IS_DELETED = 'N'
分组：按陪玩人员分组

视图2：VIEW_CUSTOMER_CONSUMPTION（客户消费统计视图）
字段：
  - c.CUSTOMER_ID, c.USERNAME, c.NICKNAME, c.PHONE, c.MEMBER_LEVEL
  - c.BALANCE, c.STATUS, c.CREATE_TIME AS REGISTER_TIME
  - COUNT(o.ORDER_ID) AS ORDER_COUNT（订单数）
  - NVL(SUM(o.TOTAL_AMOUNT), 0) AS TOTAL_CONSUME（总消费）
  - NVL(AVG(o.TOTAL_AMOUNT), 0) AS AVG_CONSUME（平均消费）
  - MAX(o.CREATE_TIME) AS LAST_ORDER_TIME（最后下单时间）
关联：TB_CUSTOMER c LEFT JOIN TB_ORDER o
条件：o.ORDER_STATUS IN ('已支付', '服务中', '已完成') AND o.IS_DELETED = 'N'
分组：按客户分组

视图3：VIEW_ORDER_DETAIL（订单详情视图）
字段：
  - o.所有字段
  - c.USERNAME AS CUSTOMER_NAME, c.PHONE AS CUSTOMER_PHONE
  - s.STAFF_NAME, s.PHONE AS STAFF_PHONE
  - e.SCORE, e.CONTENT AS EVAL_CONTENT
  - comp.COMPLAINT_ID, comp.COMPLAINT_STATUS
关联：
  TB_ORDER o
  LEFT JOIN TB_CUSTOMER c ON o.CUSTOMER_ID = c.CUSTOMER_ID
  LEFT JOIN TB_STAFF s ON o.STAFF_ID = s.STAFF_ID
  LEFT JOIN TB_EVALUATION e ON o.ORDER_ID = e.ORDER_ID AND e.IS_DELETED = 'N'
  LEFT JOIN TB_COMPLAINT comp ON o.ORDER_ID = comp.ORDER_ID AND comp.IS_DELETED = 'N'
条件：o.IS_DELETED = 'N'

视图4：VIEW_FINANCE_SUMMARY（财务汇总视图）
字段：
  - TRUNC(CREATE_TIME) AS STAT_DATE（统计日期）
  - COUNT(*) AS ORDER_COUNT（订单数）
  - NVL(SUM(TOTAL_AMOUNT), 0) AS TOTAL_SALES（总销售额）
  - NVL(SUM(PLATFORM_COMMISSION), 0) AS PLATFORM_INCOME（平台收入）
  - NVL(SUM(STAFF_INCOME), 0) AS STAFF_INCOME（陪玩收入）
  - COUNT(CASE WHEN ORDER_STATUS = '已完成' THEN 1 END) AS COMPLETED_COUNT
  - COUNT(CASE WHEN ORDER_STATUS = '已退款' THEN 1 END) AS REFUND_COUNT
  - NVL(SUM(CASE WHEN ORDER_STATUS = '已退款' THEN TOTAL_AMOUNT END), 0) AS REFUND_AMOUNT
分组：按日期分组 TRUNC(CREATE_TIME)
排序：按日期倒序
条件：IS_DELETED = 'N'

请提供完整的CREATE TRIGGER和CREATE VIEW语句，并添加注释说明。
```

**预期输出**：4个触发器和4个视图的完整SQL代码

**使用建议**：触发器创建后要测试，确保不会引起递归触发

---

### 提示词5：权限管理和闪回技术

**适用范围**：数据库安全和数据恢复

```
请为游戏陪玩系统生成Oracle权限管理和闪回技术SQL，要求如下：

第一部分：权限管理

1. 创建角色（5个）

角色1：ROLE_SUPER_ADMIN（超级管理员）
权限：
  - 所有表的SELECT, INSERT, UPDATE, DELETE权限
  - 所有视图的SELECT权限
  - 所有存储过程的EXECUTE权限
  - 所有序列的SELECT权限
  - 系统权限：CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE等

角色2：ROLE_OPERATOR（运营管理员）
权限：
  - TB_STAFF: SELECT, INSERT, UPDATE
  - TB_CUSTOMER: SELECT, UPDATE
  - TB_ORDER: SELECT, UPDATE
  - TB_EVALUATION: SELECT, UPDATE, DELETE
  - TB_DICTIONARY: SELECT, INSERT, UPDATE
  - 视图：VIEW_STAFF_PERFORMANCE, VIEW_ORDER_DETAIL, VIEW_CUSTOMER_CONSUMPTION
  - 存储过程：PROC_CREATE_ORDER, PROC_COMPLETE_ORDER

角色3：ROLE_CUSTOMER_SERVICE（客服人员）
权限：
  - TB_CUSTOMER: SELECT
  - TB_ORDER: SELECT
  - TB_EVALUATION: SELECT, UPDATE
  - TB_COMPLAINT: SELECT, INSERT, UPDATE
  - TB_STAFF: SELECT
  - 视图：VIEW_ORDER_DETAIL

角色4：ROLE_FINANCE（财务人员）
权限：
  - TB_ORDER: SELECT
  - TB_STAFF: SELECT
  - TB_CUSTOMER: SELECT
  - TB_WITHDRAW: SELECT, INSERT, UPDATE（假设存在提现表）
  - 视图：VIEW_FINANCE_SUMMARY, VIEW_STAFF_PERFORMANCE
  - 存储过程：PROC_WITHDRAW_APPLICATION

角色5：ROLE_VIEWER（查看员）
权限：
  - 所有表的SELECT权限
  - 所有视图的SELECT权限
  - 只有查询权限，无修改权限

2. 创建用户并分配角色（5个用户）

用户1：USER_SUPER_ADMIN（超级管理员）
CREATE USER USER_SUPER_ADMIN IDENTIFIED BY SuperAdmin123
DEFAULT TABLESPACE TBS_PLAYMATE_SYSTEM
TEMPORARY TABLESPACE TBS_PLAYMATE_TEMP;
GRANT ROLE_SUPER_ADMIN TO USER_SUPER_ADMIN;
GRANT CREATE SESSION TO USER_SUPER_ADMIN;

用户2：USER_OPERATOR（运营人员）
CREATE USER USER_OPERATOR IDENTIFIED BY Operator123
DEFAULT TABLESPACE TBS_PLAYMATE_SYSTEM;
GRANT ROLE_OPERATOR TO USER_OPERATOR;
GRANT CREATE SESSION TO USER_OPERATOR;

用户3：USER_CUSTOMER_SERVICE（客服人员）
CREATE USER USER_CUSTOMER_SERVICE IDENTIFIED BY CustomerService123
DEFAULT TABLESPACE TBS_PLAYMATE_SYSTEM;
GRANT ROLE_CUSTOMER_SERVICE TO USER_CUSTOMER_SERVICE;
GRANT CREATE SESSION TO USER_CUSTOMER_SERVICE;

用户4：USER_FINANCE（财务人员）
CREATE USER USER_FINANCE IDENTIFIED BY Finance123
DEFAULT TABLESPACE TBS_PLAYMATE_SYSTEM;
GRANT ROLE_FINANCE TO USER_FINANCE;
GRANT CREATE SESSION TO USER_FINANCE;

用户5：USER_VIEWER（查看员）
CREATE USER USER_VIEWER IDENTIFIED BY Viewer123
DEFAULT TABLESPACE TBS_PLAYMATE_SYSTEM;
GRANT ROLE_VIEWER TO USER_VIEWER;
GRANT CREATE SESSION TO USER_VIEWER;

第二部分：闪回技术演示

场景1：误删除数据恢复
-- 1. 模拟误删除
DELETE FROM TB_STAFF WHERE STAFF_ID = 10001;
COMMIT;

-- 2. 验证数据已删除
SELECT * FROM TB_STAFF WHERE STAFF_ID = 10001;

-- 3. 使用闪回查询查看删除前的数据
SELECT * FROM TB_STAFF AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '10' MINUTE)
WHERE STAFF_ID = 10001;

-- 4. 闪回表恢复（恢复到5分钟前）
FLASHBACK TABLE TB_STAFF TO TIMESTAMP (SYSTIMESTAMP - INTERVAL '5' MINUTE);

-- 5. 如果表结构被修改，需要先启用行移动
ALTER TABLE TB_STAFF ENABLE ROW MOVEMENT;
FLASHBACK TABLE TB_STAFF TO TIMESTAMP (SYSTIMESTAMP - INTERVAL '5' MINUTE);
ALTER TABLE TB_STAFF DISABLE ROW MOVEMENT;

-- 6. 验证恢复结果
SELECT * FROM TB_STAFF WHERE STAFF_ID = 10001;

场景2：误删表恢复
-- 1. 模拟误删表
DROP TABLE TB_DICTIONARY;

-- 2. 查看回收站中的对象
SELECT * FROM RECYCLEBIN WHERE ORIGINAL_NAME = 'TB_DICTIONARY';

-- 3. 闪回删除
FLASHBACK TABLE TB_DICTIONARY TO BEFORE DROP;

-- 4. 如果需要重命名恢复
FLASHBACK TABLE TB_DICTIONARY TO BEFORE DROP RENAME TO TB_DICTIONARY_RESTORED;

-- 5. 验证恢复结果
SELECT * FROM TB_DICTIONARY;

场景3：误更新数据恢复
-- 1. 模拟误更新
UPDATE TB_ORDER SET ORDER_STATUS = '已退款' WHERE ORDER_ID = 3000000001;
COMMIT;

-- 2. 验证数据已更新
SELECT ORDER_STATUS FROM TB_ORDER WHERE ORDER_ID = 3000000001;

-- 3. 使用闪回查询查看更新前的数据
SELECT ORDER_STATUS FROM TB_ORDER AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '5' MINUTE)
WHERE ORDER_ID = 3000000001;

-- 4. 使用闪回查询恢复数据（方法1：子查询更新）
UPDATE TB_ORDER o
SET ORDER_STATUS = (
    SELECT ORDER_STATUS FROM TB_ORDER AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '5' MINUTE)
    WHERE ORDER_ID = o.ORDER_ID
)
WHERE ORDER_ID = 3000000001;

-- 5. 或者使用闪回表恢复（方法2：整表恢复）
FLASHBACK TABLE TB_ORDER TO TIMESTAMP (SYSTIMESTAMP - INTERVAL '5' MINUTE);

-- 6. 验证恢复结果
SELECT ORDER_STATUS FROM TB_ORDER WHERE ORDER_ID = 3000000001;

请提供完整的SQL脚本，并为每个步骤添加详细的中文注释说明。
```

**预期输出**：权限管理SQL + 3种闪回技术演示脚本

**使用建议**：先在测试环境演练闪回操作，熟悉后再在生产环境使用

---

## 🎨 前端开发提示词（5个）

### 提示词6：陪玩人员管理页面

**适用范围**：Vue.js前端页面开发

```
请生成一个基于Vue.js 3 + Element Plus的陪玩人员管理页面，要求如下：

页面布局：
1. 搜索栏（顶部）
   - 陪玩昵称：el-input输入框，placeholder="请输入陪玩昵称"
   - 技能等级：el-select下拉框，选项：全部、青铜、白银、黄金、铂金、钻石、王者
   - 状态：el-select下拉框，选项：全部、空闲、忙碌、离线、封禁
   - 查询按钮：el-button type="primary"，点击触发handleSearch
   - 重置按钮：el-button，点击触发handleReset

2. 操作栏（搜索栏下方）
   - 新增陪玩按钮：el-button type="primary" @click="handleAdd"
   - 导出数据按钮：el-button type="success" @click="handleExport"

3. 数据表格（主体）
   - el-table，border属性，v-loading="loading"
   - 列定义：
     * type="index"（序号列，width=50）
     * prop="staffId" label="ID" width=80
     * prop="staffName" label="陪玩昵称" width=120
     * prop="realName" label="真实姓名" width=100
     * prop="gender" label="性别" width=60，格式化：M=男，F=女
     * prop="age" label="年龄" width=60
     * prop="phone" label="手机号" width=120
     * prop="skillLevel" label="技能等级" width=80
     * prop="serviceType" label="服务类型" width=150
     * prop="unitPrice" label="单价(元/小时)" width=100
     * prop="totalOrders" label="订单数" width=80
     * prop="totalIncome" label="总收入(元)" width=100
     * prop="avgScore" label="评分" width=80，使用el-rate组件显示
     * prop="status" label="状态" width=80，使用el-tag显示，不同状态不同颜色
     * prop="createTime" label="创建时间" width=160
     * 操作列：width=200，fixed="right"
       - 编辑按钮：el-button size="mini" @click="handleEdit(scope.row)"
       - 删除按钮：el-button size="mini" type="danger" @click="handleDelete(scope.row)"
       - 更多下拉菜单：el-dropdown
         * 详情、订单记录、收入明细

4. 分页（底部）
   - el-pagination
   - 属性：:current-page="page.current"，:page-size="page.size"，:total="page.total"
   - 事件：@size-change="handleSizeChange"，@current-change="handleCurrentChange"
   - layout="total, sizes, prev, pager, next, jumper"

5. 新增/编辑对话框
   - el-dialog，width="600px"
   - el-form，label-width="100px"
   - 表单项：
     * 陪玩昵称：el-input，验证required
     * 真实姓名：el-input
     * 性别：el-radio-group，选项M/F
     * 年龄：el-input-number，min=18, max=60
     * 手机号：el-input，验证required和手机号格式
     * 邮箱：el-input，验证邮箱格式
     * 技能等级：el-select，选项：青铜到王者
     * 服务类型：el-checkbox-group，选项：英雄联盟、王者荣耀等
     * 单价：el-input-number，min=0, precision=2
     * 状态：el-select，选项：空闲/忙碌/离线/封禁
   - 对话框底部：取消和确定按钮

数据结构：
data() {
  return {
    loading: false,
    searchForm: { staffName: '', skillLevel: '', status: '' },
    staffList: [],
    page: { current: 1, size: 10, total: 0 },
    dialogVisible: false,
    dialogTitle: '',
    staffForm: { ... },
    staffRules: { ... }
  }
}

方法：
methods: {
  fetchData() // 获取数据列表
  handleSearch() // 搜索
  handleReset() // 重置
  handleAdd() // 新增
  handleEdit(row) // 编辑
  handleDelete(row) // 删除
  handleSubmit() // 提交表单
  handleSizeChange(val) // 分页大小变化
  handleCurrentChange(val) // 当前页变化
  getStatusType(status) // 获取状态标签类型
  handleCommand(command) // 处理下拉菜单命令
}

样式：
- 使用scoped样式
- .staff-container { padding: 20px; }
- .search-card, .operation-card, .table-card { margin-bottom: 20px; }
- 响应式设计

API接口：
- getStaffList(params) // 获取列表
- addStaff(data) // 新增
- updateStaff(data) // 更新
- deleteStaff(id) // 删除

请提供完整的Vue单文件组件代码，包括template、script、style三部分。
```

**预期输出**：完整的陪玩人员管理Vue页面

**使用建议**：先创建API接口文件，再创建页面组件

---

### 提示词7：订单管理页面

**适用范围**：核心业务页面开发

```
请生成一个基于Vue.js 3 + Element Plus的订单管理页面，要求如下：

页面布局：
1. 搜索栏
   - 订单号、客户名称、陪玩昵称：el-input
   - 游戏类型、订单状态：el-select
   - 创建时间：el-date-picker type="daterange"
   - 查询和重置按钮

2. 操作栏
   - 创建订单按钮：el-button type="primary"
   - 导出数据按钮：el-button type="success"
   - 批量取消按钮：el-button type="warning"

3. 统计卡片（4个）
   - 今日订单数、今日收入、待处理订单、已完成订单
   - 使用el-card布局，不同颜色图标
   - 大数字显示，居中对齐

4. 数据表格
   - el-table，支持多选 @selection-change
   - 列：多选框、序号、订单号、客户名称、陪玩昵称、游戏类型、
        服务时长、单价、总金额、订单状态、创建时间、支付时间、操作
   - 状态列：el-tag，不同状态不同颜色
   - 操作列：
     * 详情按钮
     * 确认支付（状态=待支付）
     * 开始服务（状态=已支付）
     * 完成服务（状态=服务中）
     * 取消（状态=待支付/已支付）
   - 分页功能

5. 创建订单对话框
   - el-dialog title="创建订单"
   - el-form
   - 表单项：
     * 选择客户：el-select filterable，加载客户列表
     * 选择陪玩：el-select filterable，只显示空闲陪玩
     * 游戏类型：el-select
     * 服务时长：el-input-number
   - 动态显示单价和总金额
   - 监听staffId变化，更新unitPrice

6. 样式
   - 统计卡片使用不同背景色
   - 状态标签颜色：
     * 待支付=warning（橙色）
     * 已支付=primary（蓝色）
     * 服务中=info（灰色）
     * 已完成=success（绿色）
     * 已取消/已退款=danger（红色）

数据结构：
statistics: {
  todayOrders: 0,
  todayIncome: 0,
  pendingOrders: 0,
  completedOrders: 0
}

特殊功能：
- 监听陪玩选择变化，自动更新单价
- 实时计算总金额：serviceHours * unitPrice
- 批量操作：获取选中的订单ID列表

API接口：
- getOrderList(params)
- createOrder(data)
- updateOrderStatus(orderId, status)
- getOrderStatistics()
- getCustomerList(params)
- getStaffList(params)

请提供完整的Vue单文件组件代码，注意状态判断逻辑和样式设计。
```

**预期输出**：完整的订单管理Vue页面

**使用建议**：订单状态判断逻辑要清晰，避免状态错乱

---

### 提示词8：客户管理页面

**适用范围**：客户管理模块

```
请生成一个基于Vue.js 3 + Element Plus的客户管理页面，要求如下：

页面布局：
1. 搜索栏
   - 用户名、手机号：el-input
   - 会员等级、状态：el-select
   - 注册时间：el-date-picker type="daterange"

2. 操作栏
   - 新增客户按钮
   - 批量操作下拉菜单（冻结、解冻、删除）

3. 数据表格
   - el-table，支持多选
   - 列：多选框、序号、ID、用户名、昵称、手机号、邮箱、性别、年龄、
        会员等级、总消费、订单数、余额、状态、注册时间、最后登录时间、操作
   - 会员等级列：不同等级显示不同颜色标签
   - 状态列：el-tag显示
   - 操作列：编辑、删除、详情、充值、消费记录

4. 新增/编辑对话框
   - el-form
   - 表单项：用户名、密码（新增时显示）、昵称、手机号、邮箱、
            性别、年龄、会员等级、余额、状态
   - 表单验证规则

5. 充值对话框
   - el-dialog
   - 充值金额：el-input-number
   - 备注：el-input type="textarea"

6. 消费记录对话框
   - el-dialog，大尺寸
   - el-table显示订单列表
   - 分页功能

特殊功能：
- 批量操作：获取选中的客户ID列表
- 充值功能：更新客户余额
- 会员等级颜色：普通=info，VIP=warning，SVIP=success

API接口：
- getCustomerList(params)
- addCustomer(data)
- updateCustomer(data)
- deleteCustomer(id)
- rechargeCustomer(customerId, amount)
- getCustomerOrders(customerId, params)

样式：
- 会员等级使用渐变色彩
- 余额使用绿色加粗显示
- 冻结状态使用红色标签

请提供完整的Vue单文件组件代码。
```

**预期输出**：完整的客户管理Vue页面

**使用建议**：客户管理涉及财务操作（充值），要做好权限控制

---

### 提示词9：评价与投诉管理页面

**适用范围**：售后管理模块

```
请生成一个基于Vue.js 3 + Element Plus的评价与投诉管理页面，要求如下：

页面布局：使用el-tabs标签页

tab1：评价管理
1. 搜索栏
   - 订单号、陪玩昵称、客户名称：el-input
   - 评分：el-select（1-5星）
   - 评价时间：el-date-picker

2. 数据表格
   - 列：序号、订单号、陪玩昵称、客户名称、评分、评价内容、
        标签、是否匿名、回复状态、评价时间、操作
   - 评分列：el-rate组件，只读，显示分数
   - 操作列：查看详情、回复

3. 回复对话框
   - 评价信息（只读）
   - 回复内容：el-input type="textarea"
   - 提交回复按钮

tab2：投诉管理
1. 搜索栏
   - 订单号：el-input
   - 投诉类型：el-select（服务态度/服务质量/收费问题/其他）
   - 投诉状态：el-select（待处理/处理中/已解决/已关闭）
   - 投诉时间：el-date-picker

2. 数据表格
   - 列：序号、订单号、客户名称、陪玩昵称、投诉类型、投诉内容、
        投诉状态、处理人、处理时间、操作
   - 投诉状态列：不同颜色标签
   - 操作列：查看详情、处理

3. 处理对话框
   - 投诉信息（只读）
   - 处理结果：el-input type="textarea"
   - 处理状态：el-select
   - 提交处理按钮

数据结构：
activeTab: 'evaluation' // 当前激活的标签页
evaluationList: [] // 评价列表
complaintList: [] // 投诉列表

API接口：
// 评价相关
tab1（评价管理）：
- getEvaluationList(params)
- replyEvaluation(evalId, replyContent)

// 投诉相关
tab2（投诉管理）：
- getComplaintList(params)
- handleComplaint(complaintId, result, status)

样式：
- 标签页使用卡片样式
- 评分使用金色星级
- 投诉状态颜色：待处理=warning，处理中=info，已解决=success，已关闭=danger

请提供完整的Vue单文件组件代码，包括标签页切换逻辑。
```

**预期输出**：评价与投诉管理页面（含标签页）

**使用建议**：两个功能在一个页面，注意数据隔离和状态管理

---

### 提示词10：系统管理页面（角色权限）

**适用范围**：系统管理模块

```
请生成一个基于Vue.js 3 + Element Plus的系统管理页面，包含角色管理和管理员管理，要求如下：

页面布局：使用el-tabs标签页

tab1：角色管理
1. 操作栏
   - 新增角色按钮

2. 数据表格
   - 列：序号、角色ID、角色名称、角色描述、权限列表、创建时间、操作
   - 操作列：编辑、删除、分配权限

3. 新增/编辑对话框
   - 角色名称、角色描述：el-input
   - 权限配置：el-tree（树形结构，多选）

4. 分配权限对话框
   - el-tree显示所有权限
   - 支持全选/全不选
   - 保存权限配置

tab2：管理员管理
1. 操作栏
   - 新增管理员按钮

2. 数据表格
   - 列：序号、管理员ID、用户名、昵称、手机号、邮箱、角色、状态、
        最后登录时间、创建时间、操作
   - 状态列：el-tag显示
   - 操作列：编辑、删除、重置密码

3. 新增/编辑对话框
   - 用户名、密码（新增时必填）、昵称、手机号、邮箱：el-input
   - 角色：el-select
   - 状态：el-switch

数据结构：
roleList: []
adminList: []
permissionTree: [] // 权限树

API接口：
// 角色相关
tab1：
- getRoleList()
- addRole(data)
- updateRole(data)
- deleteRole(id)
- getPermissionTree()
- updateRolePermissions(roleId, permissions)

// 管理员相关
tab2：
- getAdminList()
- addAdmin(data)
- updateAdmin(data)
- deleteAdmin(id)
- resetPassword(adminId)

权限树结构：
{
  id: 'staff',
  label: '陪玩管理',
  children: [
    { id: 'staff:view', label: '查看' },
    { id: 'staff:add', label: '新增' },
    { id: 'staff:edit', label: '编辑' },
    { id: 'staff:delete', label: '删除' }
  ]
}

特殊功能：
- 权限树递归渲染
- 角色删除前检查是否有管理员使用
- 管理员重置密码功能

请提供完整的Vue单文件组件代码。
```

**预期输出**：系统管理页面（角色+管理员）

**使用建议**：权限管理是核心功能，要做好数据验证和安全控制

---

## 💻 后端开发提示词（5个）

### 提示词11：实体类和Mapper接口

**适用范围**：后端基础代码生成

```
请为游戏陪玩系统生成SpringBoot实体类和MyBatis Mapper接口，要求如下：

实体类1：Staff（陪玩人员）
包名：com.playmate.admin.model.entity
注解：
  - @Data（Lombok）
  - @EqualsAndHashCode(callSuper = false)
  - @TableName("TB_STAFF")
  - @ApiModel(value="Staff对象", description="陪玩人员表")
字段（使用@TableField注解）：
  private Long staffId; // @TableId(value = "STAFF_ID", type = IdType.ASSIGN_ID)
  private String staffName; // @TableField("STAFF_NAME")
  private String realName;
  private String gender;
  private Integer age;
  private String phone;
  private String email;
  private String avatarUrl;
  private String skillLevel;
  private String serviceType;
  private BigDecimal unitPrice;
  private String status;
  private Integer totalOrders;
  private BigDecimal totalIncome;
  private BigDecimal avgScore;
  private String certStatus;
  private Date createTime;
  private Date updateTime;
  private Date lastLoginTime;
  private String isDeleted;

实体类2：Customer（客户）
字段：
  private Long customerId; // @TableId
  private String username;
  private String password;
  private String nickname;
  private String phone;
  private String email;
  private String gender;
  private Integer age;
  private String avatarUrl;
  private String memberLevel;
  private BigDecimal totalConsume;
  private Integer orderCount;
  private BigDecimal balance;
  private String status;
  private Date createTime;
  private Date updateTime;
  private Date lastLoginTime;
  private String isDeleted;

实体类3：Order（订单）
字段：
  private Long orderId;
  private String orderNo;
  private Long customerId;
  private Long staffId;
  private String gameType;
  private Integer serviceHours;
  private BigDecimal unitPrice;
  private BigDecimal totalAmount;
  private BigDecimal platformCommission;
  private BigDecimal staffIncome;
  private String orderStatus;
  private Date payTime;
  private Date startTime;
  private Date endTime;
  private String customerComment;
  private String staffComment;
  private Date createTime;
  private Date updateTime;
  private String isDeleted;

Mapper接口1：StaffMapper
包名：com.playmate.admin.mapper
注解：@Mapper（MyBatis-Plus）
方法：
  - IPage<Staff> selectPage(Page<Staff> page, @Param("query") Map<String, Object> query);
  - int insertStaff(Staff staff);
  - int updateStaff(Staff staff);
  - int deleteStaffById(Long staffId);
  - Staff selectStaffById(Long staffId);
  - List<Staff> selectStaffList(@Param("query") Map<String, Object> query);
  - int countStaff(@Param("query") Map<String, Object> query);

Mapper接口2：CustomerMapper
方法类似StaffMapper

Mapper接口3：OrderMapper
方法：
  - IPage<Order> selectPage(Page<Order> page, @Param("query") Map<String, Object> query);
  - int insertOrder(Order order);
  - int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);
  - Order selectOrderById(Long orderId);
  - Map<String, Object> selectOrderStatistics();
  - int updateOrder(Order order);

XML映射文件（示例）：
StaffMapper.xml
  - <resultMap id="BaseResultMap" type="com.playmate.admin.model.entity.Staff">
  - <sql id="Base_Column_List">STAFF_ID, STAFF_NAME, ...</sql>
  - <select id="selectPage" resultMap="BaseResultMap">
      SELECT <include refid="Base_Column_List"/> FROM TB_STAFF
      <where>
        <if test="query.staffName != null and query.staffName != ''">
          AND STAFF_NAME LIKE '%' || #{query.staffName} || '%'
        </if>
        <if test="query.status != null and query.status != ''">
          AND STATUS = #{query.status}
        </if>
      </where>
    </select>

要求：
- 使用MyBatis-Plus框架
- 支持分页查询（IPage, Page）
- 使用@TableName, @TableField, @TableId注解
- 使用Lombok @Data注解
- 日期类型使用java.util.Date
- 金额类型使用java.math.BigDecimal
- 每个字段都要有Javadoc注释

请提供完整的Java实体类、Mapper接口和XML映射文件代码。
```

**预期输出**：实体类和Mapper接口的完整Java代码

**使用建议**：先生成实体类，再生成Mapper接口

---

### 提示词12：订单服务层

**适用范围**：后端业务逻辑实现

```
请为游戏陪玩系统生成SpringBoot订单服务层代码，要求如下：

接口：OrderService
包名：com.playmate.admin.service
方法：

1. String createOrder(OrderCreateDTO orderDTO)
   - 功能：创建订单
   - 步骤：
     * 验证客户状态（查询TB_CUSTOMER）
     * 验证陪玩人员状态（查询TB_STAFF，必须为"空闲"）
     * 计算总金额 = serviceHours * unitPrice
     * 计算平台抽成（默认20%）和陪玩收入
     * 生成订单号：PM + yyyyMMdd + 10位序列号
     * 插入订单记录
     * 更新陪玩人员状态为"忙碌"
     * 返回订单号
   - 事务：@Transactional
   - 异常：状态异常、余额不足、系统错误

2. PageResult<OrderVO> getOrderList(OrderQueryDTO queryDTO)
   - 功能：分页查询订单列表
   - 参数：queryDTO包含分页参数、查询条件
   - 返回：PageResult封装分页结果
   - 查询条件：
     * orderNo：订单号模糊查询
     * customerName：客户名称
     * staffName：陪玩昵称
     * gameType：游戏类型
     * orderStatus：订单状态
     * createTimeRange：创建时间范围
   - 关联查询：TB_ORDER、TB_CUSTOMER、TB_STAFF
   - 使用PageHelper或MyBatis-Plus分页

3. OrderVO getOrderById(Long orderId)
   - 功能：根据ID查询订单详情
   - 关联查询客户和陪玩信息
   - 返回OrderVO（包含客户名称、陪玩昵称）

4. void updateOrderStatus(Long orderId, String status)
   - 功能：更新订单状态
   - 验证订单是否存在
   - 根据新状态执行相应逻辑
   - 记录操作日志

5. void completeOrder(Long orderId)
   - 功能：完成订单
   - 验证订单状态是否为"服务中"
   - 更新订单状态为"已完成"，设置结束时间
   - 更新陪玩人员状态为"空闲"
   - 增加陪玩人员总收入和订单数
   - 增加客户订单数
   - 事务管理

6. void cancelOrder(Long orderId)
   - 功能：取消订单
   - 验证订单状态（只能是待支付或已支付）
   - 更新订单状态为"已取消"
   - 恢复陪玩人员状态为"空闲"
   - 如果是已支付，需要退款处理

7. Map<String, Object> getOrderStatistics()
   - 功能：获取订单统计信息
   - 统计项：
     * todayOrders：今日订单数
     * todayIncome：今日收入
     * pendingOrders：待处理订单数（待支付+已支付）
     * completedOrders：已完成订单数
   - 使用SQL聚合函数统计

实现类：OrderServiceImpl
包名：com.playmate.admin.service.impl
注解：
  - @Service
  - @Transactional（类级别）
依赖注入：
  - @Autowired OrderMapper orderMapper
  - @Autowired StaffMapper staffMapper
  - @Autowired CustomerMapper customerMapper
  - @Autowired IdWorker idWorker（雪花ID生成器）

DTO类：
1. OrderCreateDTO
   - customerId, staffId, gameType, serviceHours, unitPrice
   - 使用Lombok @Data

2. OrderQueryDTO
   - page, size, orderNo, customerName, staffName, gameType, orderStatus, createTimeRange
   - 使用Lombok @Data

3. OrderVO（视图对象）
   - Order所有字段 + customerName, staffName
   - 使用Lombok @Data

异常处理：
  - 自定义异常类：BusinessException
  - try-catch捕获，抛出BusinessException
  - 全局异常处理器：GlobalExceptionHandler

日志记录：
  - 使用SLF4J Logger
  - 记录关键操作和异常

请提供完整的Java代码，包括接口、实现类、DTO类，并添加详细注释。
```

**预期输出**：OrderService接口、实现类、DTO类的完整Java代码

**使用建议**：服务层是业务核心，要仔细测试每个方法

---

### 提示词13：统一返回结果和分页工具

**适用范围**：后端通用工具类

```
请为SpringBoot项目生成统一返回结果类和分页工具类，要求如下：

类1：Result<T>（统一返回结果）
包名：com.playmate.admin.util
注解：无
泛型：T（数据类型）
字段：
  private Integer code; // 状态码，200=成功，500=失败
  private String message; // 消息
  private T data; // 数据
  private Long timestamp; // 时间戳

构造方法：
  public Result() { this.timestamp = System.currentTimeMillis(); }
  public Result(Integer code, String message, T data) {
    this.code = code; this.message = message; this.data = data;
    this.timestamp = System.currentTimeMillis();
  }

静态方法：
  public static <T> Result<T> success() {
    return new Result<>(200, "操作成功", null);
  }
  public static <T> Result<T> success(T data) {
    return new Result<>(200, "操作成功", data);
  }
  public static <T> Result<T> success(String message, T data) {
    return new Result<>(200, message, data);
  }
  public static <T> Result<T> error(String message) {
    return new Result<>(500, message, null);
  }
  public static <T> Result<T> error(Integer code, String message) {
    return new Result<>(code, message, null);
  }

getter/setter方法：使用Lombok @Data简化

类2：PageResult<T>（分页结果）
包名：com.playmate.admin.util
字段：
  private Long total; // 总记录数
  private List<T> records; // 数据列表
  private Long size; // 每页大小
  private Long current; // 当前页
构造方法：
  public PageResult() {}
  public PageResult(Long total, List<T> records) {
    this.total = total; this.records = records;
  }
  public PageResult(Long total, List<T> records, Long size, Long current) {
    this.total = total; this.records = records;
    this.size = size; this.current = current;
  }

类3：PageQuery（分页查询参数）
包名：com.playmate.admin.model.query
字段：
  private Integer page = 1; // 当前页，默认1
  private Integer size = 10; // 每页大小，默认10
方法：
  public Integer getOffset() {
    return (page - 1) * size;
  }

类4：BusinessException（业务异常）
包名：com.playmate.admin.exception
注解：无
继承：RuntimeException
字段：
  private Integer code; // 错误码
  private String message; // 错误消息
构造方法：
  public BusinessException(String message) {
    super(message); this.code = 500; this.message = message;
  }
  public BusinessException(Integer code, String message) {
    super(message); this.code = code; this.message = message;
  }

类5：GlobalExceptionHandler（全局异常处理器）
包名：com.playmate.admin.config
注解：
  - @ControllerAdvice
  - @Slf4j
方法：
  @ExceptionHandler(BusinessException.class)
  public Result handleBusinessException(BusinessException e) {
    log.error("业务异常：", e);
    return Result.error(e.getCode(), e.getMessage());
  }
  @ExceptionHandler(Exception.class)
  public Result handleException(Exception e) {
    log.error("系统异常：", e);
    return Result.error("系统繁忙，请稍后重试");
  }

类6：CorsConfig（跨域配置）
包名：com.playmate.admin.config
注解：@Configuration
方法：
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

请提供完整的Java代码，并添加详细的注释说明每个类的作用。
```

**预期输出**：6个通用工具类和配置类的完整Java代码

**使用建议**：这些类是所有模块的基础，优先创建

---

### 提示词14：Controller控制器

**适用范围**：后端API接口开发

```
请为游戏陪玩系统生成SpringBoot Controller层代码，要求如下：

控制器1：StaffController（陪玩人员管理）
包名：com.playmate.admin.controller
注解：
  - @RestController
  - @RequestMapping("/staff")
  - @CrossOrigin
  - @Slf4j
依赖注入：
  - @Autowired StaffService staffService

方法1：分页查询
  @GetMapping("/list")
  public Result<PageResult<StaffVO>> getStaffList(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String staffName,
      @RequestParam(required = false) String skillLevel,
      @RequestParam(required = false) String status) {
    Map<String, Object> query = new HashMap<>();
    query.put("staffName", staffName);
    query.put("skillLevel", skillLevel);
    query.put("status", status);
    PageResult<StaffVO> result = staffService.getStaffList(page, size, query);
    return Result.success(result);
  }

方法2：查询详情
  @GetMapping("/detail/{id}")
  public Result<Staff> getStaffById(@PathVariable Long id) {
    Staff staff = staffService.getStaffById(id);
    return Result.success(staff);
  }

方法3：新增
  @PostMapping("/add")
  public Result<Void> addStaff(@RequestBody @Valid StaffDTO staffDTO) {
    staffService.addStaff(staffDTO);
    return Result.success("新增成功");
  }

方法4：更新
  @PutMapping("/update")
  public Result<Void> updateStaff(@RequestBody @Valid StaffDTO staffDTO) {
    staffService.updateStaff(staffDTO);
    return Result.success("更新成功");
  }

方法5：删除
  @DeleteMapping("/delete/{id}")
  public Result<Void> deleteStaff(@PathVariable Long id) {
    staffService.deleteStaff(id);
    return Result.success("删除成功");
  }

方法6：统计信息
  @GetMapping("/statistics")
  public Result<Map<String, Object>> getStaffStatistics() {
    Map<String, Object> statistics = staffService.getStatistics();
    return Result.success(statistics);
  }

控制器2：CustomerController（客户管理）
类似StaffController，额外方法：

方法7：充值
  @PostMapping("/recharge")
  public Result<Void> recharge(@RequestParam Long customerId,
                               @RequestParam BigDecimal amount) {
    customerService.recharge(customerId, amount);
    return Result.success("充值成功");
  }

控制器3：OrderController（订单管理）
包名：com.playmate.admin.controller
注解：同上

方法1：创建订单
  @PostMapping("/create")
  public Result<String> createOrder(@RequestBody @Valid OrderCreateDTO orderDTO) {
    String orderNo = orderService.createOrder(orderDTO);
    return Result.success("订单创建成功", orderNo);
  }

方法2：分页查询
  @GetMapping("/list")
  public Result<PageResult<OrderVO>> getOrderList(OrderQueryDTO queryDTO) {
    PageResult<OrderVO> result = orderService.getOrderList(queryDTO);
    return Result.success(result);
  }

方法3：查询详情
  @GetMapping("/detail/{id}")
  public Result<OrderVO> getOrderById(@PathVariable Long id) {
    OrderVO order = orderService.getOrderById(id);
    return Result.success(order);
  }

方法4：更新状态
  @PutMapping("/status/{id}")
  public Result<Void> updateOrderStatus(@PathVariable Long id,
                                        @RequestParam String status) {
    orderService.updateOrderStatus(id, status);
    return Result.success("状态更新成功");
  }

方法5：完成订单
  @PutMapping("/complete/{id}")
  public Result<Void> completeOrder(@PathVariable Long id) {
    orderService.completeOrder(id);
    return Result.success("订单完成成功");
  }

方法6：取消订单
  @PutMapping("/cancel/{id}")
  public Result<Void> cancelOrder(@PathVariable Long id) {
    orderService.cancelOrder(id);
    return Result.success("订单取消成功");
  }

方法7：批量取消
  @PutMapping("/batch/cancel")
  public Result<Void> batchCancelOrder(@RequestBody List<Long> orderIds) {
    orderService.batchCancelOrder(orderIds);
    return Result.success("批量取消成功");
  }

方法8：统计信息
  @GetMapping("/statistics")
  public Result<Map<String, Object>> getOrderStatistics() {
    Map<String, Object> statistics = orderService.getOrderStatistics();
    return Result.success(statistics);
  }

控制器4：EvaluationController（评价管理）
包名：com.playmate.admin.controller
路径：@RequestMapping("/evaluation")

方法1：查询评价列表
  @GetMapping("/list")
  public Result<PageResult<EvaluationVO>> getEvaluationList(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String orderNo,
      @RequestParam(required = false) String staffName,
      @RequestParam(required = false) Integer score) {
    // 实现代码
  }

方法2：回复评价
  @PostMapping("/reply")
  public Result<Void> replyEvaluation(@RequestParam Long evalId,
                                      @RequestParam String replyContent) {
    evaluationService.replyEvaluation(evalId, replyContent);
    return Result.success("回复成功");
  }

控制器5：ComplaintController（投诉管理）
类似评价管理控制器

方法1：查询投诉列表
方法2：处理投诉
  @PostMapping("/handle")
  public Result<Void> handleComplaint(@RequestParam Long complaintId,
                                      @RequestParam String result,
                                      @RequestParam String status) {
    complaintService.handleComplaint(complaintId, result, status);
    return Result.success("处理成功");
  }

控制器6：FinanceController（财务管理）
路径：@RequestMapping("/finance")

方法1：获取财务统计
  @GetMapping("/summary")
  public Result<Map<String, Object>> getFinanceSummary(
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate) {
    Map<String, Object> summary = financeService.getSummary(startDate, endDate);
    return Result.success(summary);
  }

方法2：提现申请
  @PostMapping("/withdraw")
  public Result<Void> withdraw(@RequestParam Long staffId,
                               @RequestParam BigDecimal amount) {
    financeService.withdraw(staffId, amount);
    return Result.success("提现申请提交成功");
  }

方法3：提现审核
  @PutMapping("/withdraw/approve")
  public Result<Void> approveWithdraw(@RequestParam Long withdrawId,
                                      @RequestParam Boolean approved) {
    financeService.approveWithdraw(withdrawId, approved);
    return Result.success(approved ? "审核通过" : "已拒绝");
  }

通用特性：
- 使用@Valid注解进行参数验证
- 使用@Slf4j记录日志
- 使用Result统一返回结果
- 使用@CrossOrigin解决跨域
- 每个方法都要有Javadoc注释

请提供完整的Controller类代码，并添加详细的注释说明每个接口的作用。
```

**预期输出**：6个Controller类的完整Java代码

**使用建议**：Controller层只负责参数接收和返回，业务逻辑放在Service层

---

### 提示词15：配置文件和启动类

**适用范围**：SpringBoot项目初始化

```
请为SpringBoot项目生成配置文件和启动类，要求如下：

文件1：application.yml（主配置文件）
路径：src/main/resources/application.yml
内容：
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  profiles:
    active: dev
  application:
    name: playmate-admin

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: assign_id
      logic-delete-field: isDeleted
      logic-delete-value: Y
      logic-not-delete-value: N
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.playmate.admin.model.entity

logging:
  level:
    com.playmate.admin.mapper: debug

文件2：application-dev.yml（开发环境配置）
路径：src/main/resources/application-dev.yml
内容：
server:
  port: 8080

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: oracle.jdbc.OracleDriver
    url: jdbc:oracle:thin:@localhost:1521:pdb_playmate
    username: playmate_admin
    password: playmate123
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      validation-query: SELECT 1 FROM DUAL
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
      filters: stat,slf4j
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000

文件3：pom.xml（Maven配置文件）
路径：项目根目录/pom.xml
内容：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.12</version>
    <relativePath/>
  </parent>
  
  <groupId>com.playmate</groupId>
  <artifactId>playmate-admin</artifactId>
  <version>1.0.0</version>
  <name>playmate-admin</name>
  <description>游戏陪玩后台管理系统</description>
  
  <properties>
    <java.version>1.8</java.version>
    <mybatis-plus.version>3.5.3.1</mybatis-plus.version>
    <druid.version>1.2.16</druid.version>
    <oracle.version>19.3.0.0</oracle.version>
  </properties>
  
  <dependencies>
    <!-- SpringBoot Starter -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis-Plus -->
    <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-boot-starter</artifactId>
      <version>${mybatis-plus.version}</version>
    </dependency>
    
    <!-- Druid连接池 -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid-spring-boot-starter</artifactId>
      <version>${druid.version}</version>
    </dependency>
    
    <!-- Oracle驱动 -->
    <dependency>
      <groupId>com.oracle.database.jdbc</groupId>
      <artifactId>ojdbc8</artifactId>
      <version>${oracle.version}</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>
    
    <!-- 测试 -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
  
  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>

类4：PlaymateAdminApplication（启动类）
包名：com.playmate.admin
注解：
  - @SpringBootApplication
  - @MapperScan("com.playmate.admin.mapper")
代码：
@SpringBootApplication
@MapperScan("com.playmate.admin.mapper")
public class PlaymateAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(PlaymateAdminApplication.class, args);
    System.out.println("========================================");
    System.out.println("  游戏陪玩后台管理系统启动成功！");
    System.out.println("  访问地址：http://localhost:8080/api");
    System.out.println("========================================");
  }
}

类5：MyBatisPlusConfig（MyBatis-Plus配置）
包名：com.playmate.admin.config
注解：@Configuration
代码：
@Configuration
public class MyBatisPlusConfig {
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // 分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
    return interceptor;
  }
}

请提供完整的配置文件和Java代码，并添加注释说明每个配置的作用。
```

**预期输出**：application.yml、application-dev.yml、pom.xml、启动类、配置类的完整代码

**使用建议**：这是项目的基础配置，创建项目时首先配置好

---

## 🎯 提示词使用技巧

### 技巧1：分步生成，逐步完善
不要一次性生成所有代码，而是按照模块逐步生成：
1. 先生成数据库表结构
2. 测试通过后生成存储过程
3. 再生成触发器和视图
4. 最后生成前后端代码

### 技巧2：明确需求，精准描述
在提示词中明确说明：
- 使用什么技术栈（Vue.js 3 + Element Plus）
- 需要什么功能（增删改查、分页、搜索）
- 有什么特殊要求（权限控制、数据验证）

### 技巧3：提供示例，引导输出
在提示词中提供数据结构示例、方法签名示例，让AI更清楚你的需求

### 技巧4：迭代优化，持续改进
如果AI生成的代码不完全符合需求，可以继续提问：
- "请优化上面的代码，添加XX功能"
- "请修复上面的代码中的XX问题"
- "请为上面的代码添加注释"

### 技巧5：验证测试，确保正确
AI生成的代码需要人工验证：
- SQL代码在数据库中执行测试
- Java代码编译运行测试
- Vue代码在浏览器中预览测试

---

## 📚 常见问题

### Q1：AI生成的代码可以直接使用吗？
**A**：大部分可以直接使用，但需要人工验证和测试。特别是SQL代码，要在测试环境中执行确认。

### Q2：如果AI生成的代码有错误怎么办？
**A**：可以把错误信息提供给AI，让它修复。例如："上面的SQL在执行时报ORA-00942错误，请修复"

### Q3：如何让AI生成更符合我需求的代码？
**A**：提供更详细的上下文和需求描述，包括：
- 使用的技术版本（SpringBoot 2.7.12）
- 数据库版本（Oracle 19c）
- 具体的功能需求
- 代码风格要求

### Q4：AI能帮我调试代码吗？
**A**：可以。把错误信息和相关代码提供给AI，它会帮你分析问题原因并提供解决方案。

### Q5：除了代码生成，AI还能做什么？
**A**：AI还可以：
- 优化现有代码
- 添加注释和文档
- 生成单元测试
- 代码审查和建议
- 性能优化建议

---

## 🎉 总结

本文档提供了15个详细的AI提示词，覆盖：
- 📊 数据库设计（5个提示词）
- 🎨 前端开发（5个提示词）
- 💻 后端开发（5个提示词）

每个提示词都经过优化，可以直接复制使用，帮助你快速完成游戏陪玩后台管理系统的开发。

**祝你期末大作业顺利完成！🎊**
