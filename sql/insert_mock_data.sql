-- 游戏陪玩管理系统 - 大量模拟数据插入脚本

-- 清空现有数据（如果需要）
-- DELETE FROM TB_EVALUATION;
-- DELETE FROM TB_COMPLAINT;
-- DELETE FROM TB_ORDER;
-- DELETE FROM TB_STAFF;
-- DELETE FROM TB_CUSTOMER;

-- 生成100个陪玩人员数据
BEGIN
  FOR i IN 1..100 LOOP
    INSERT INTO TB_STAFF (
      STAFF_ID, STAFF_NAME, REAL_NAME, GENDER, AGE, PHONE, EMAIL, 
      SKILL_LEVEL, SERVICE_TYPE, UNIT_PRICE, STATUS, TOTAL_ORDERS, 
      TOTAL_INCOME, AVG_SCORE, CERT_STATUS, CREATE_TIME
    ) VALUES (
      10000 + i,
      '陪玩人员' || i,
      '真实姓名' || i,
      CASE WHEN MOD(i, 2) = 0 THEN 'M' ELSE 'F' END,
      18 + MOD(i, 30),
      '138' || LPAD(ROUND(DBMS_RANDOM.VALUE(10000000, 99999999)), 8, '0'),
      'staff' || i || '@example.com',
      CASE MOD(i, 6)
        WHEN 0 THEN '青铜'
        WHEN 1 THEN '白银'
        WHEN 2 THEN '黄金'
        WHEN 3 THEN '铂金'
        WHEN 4 THEN '钻石'
        ELSE '王者'
      END,
      CASE MOD(i, 4)
        WHEN 0 THEN '单排,开黑'
        WHEN 1 THEN '双排,教学'
        WHEN 2 THEN '五排,语音'
        ELSE '陪练,视频'
      END,
      ROUND(DBMS_RANDOM.VALUE(30, 100), 2),
      CASE MOD(i, 4)
        WHEN 0 THEN '空闲'
        WHEN 1 THEN '忙碌'
        WHEN 2 THEN '离线'
        ELSE '封禁'
      END,
      ROUND(DBMS_RANDOM.VALUE(0, 200)),
      ROUND(DBMS_RANDOM.VALUE(0, 10000), 2),
      ROUND(3 + DBMS_RANDOM.VALUE(0, 2), 2),
      CASE WHEN MOD(i, 3) != 0 THEN '已认证' ELSE '未认证' END,
      SYSDATE - DBMS_RANDOM.VALUE(0, 365)
    );
  END LOOP;
  COMMIT;
END;
/

-- 生成200个客户数据
BEGIN
  FOR i IN 1..200 LOOP
    INSERT INTO TB_CUSTOMER (
      CUSTOMER_ID, USERNAME, PASSWORD, NICKNAME, PHONE, EMAIL, 
      GENDER, AGE, MEMBER_LEVEL, TOTAL_CONSUME, ORDER_COUNT, 
      BALANCE, STATUS, CREATE_TIME
    ) VALUES (
      20000 + i,
      'customer' || i,
      'e10adc3949ba59abbe56e057f20f883e', -- MD5加密的"123456"
      '客户' || i,
      '139' || LPAD(ROUND(DBMS_RANDOM.VALUE(10000000, 99999999)), 8, '0'),
      'customer' || i || '@example.com',
      CASE WHEN MOD(i, 2) = 0 THEN 'M' ELSE 'F' END,
      16 + MOD(i, 35),
      CASE MOD(i, 3)
        WHEN 0 THEN '普通会员'
        WHEN 1 THEN 'VIP会员'
        ELSE 'SVIP会员'
      END,
      ROUND(DBMS_RANDOM.VALUE(0, 5000), 2),
      ROUND(DBMS_RANDOM.VALUE(0, 50)),
      ROUND(DBMS_RANDOM.VALUE(0, 1000), 2),
      CASE WHEN MOD(i, 10) = 0 THEN '冻结' ELSE '正常' END,
      SYSDATE - DBMS_RANDOM.VALUE(0, 365)
    );
  END LOOP;
  COMMIT;
END;
/

-- 生成500个订单数据
BEGIN
  FOR i IN 1..500 LOOP
    DECLARE
      v_customer_id NUMBER;
      v_staff_id NUMBER;
      v_game_type VARCHAR2(50);
      v_service_hours NUMBER;
      v_unit_price NUMBER(10,2);
      v_total_amount NUMBER(10,2);
      v_order_status VARCHAR2(20);
      v_pay_time DATE := NULL;
      v_create_time DATE;
    BEGIN
      v_customer_id := 20000 + ROUND(DBMS_RANDOM.VALUE(1, 200));
      v_staff_id := 10000 + ROUND(DBMS_RANDOM.VALUE(1, 100));
      v_game_type := CASE MOD(i, 10)
        WHEN 0 THEN '英雄联盟'
        WHEN 1 THEN '王者荣耀'
        WHEN 2 THEN '和平精英'
        WHEN 3 THEN '绝地求生'
        WHEN 4 THEN '原神'
        WHEN 5 THEN 'DNF'
        WHEN 6 THEN 'CS2'
        WHEN 7 THEN 'Apex英雄'
        WHEN 8 THEN '炉石传说'
        ELSE 'DOTA2'
      END;
      v_service_hours := 1 + MOD(i, 8);
      v_unit_price := ROUND(DBMS_RANDOM.VALUE(30, 100), 2);
      v_total_amount := v_unit_price * v_service_hours;
      v_order_status := CASE MOD(i, 5)
        WHEN 0 THEN '待支付'
        WHEN 1 THEN '已支付'
        WHEN 2 THEN '服务中'
        WHEN 3 THEN '已完成'
        ELSE '已取消'
      END;
      v_create_time := SYSDATE - DBMS_RANDOM.VALUE(0, 180); -- 近180天内
      
      IF v_order_status IN ('已支付', '服务中', '已完成') THEN
        v_pay_time := v_create_time + DBMS_RANDOM.VALUE(0, 1); -- 创建后0-1天内支付
      END IF;
      
      INSERT INTO TB_ORDER (
        ORDER_ID, ORDER_NO, CUSTOMER_ID, STAFF_ID, GAME_TYPE, 
        SERVICE_HOURS, UNIT_PRICE, TOTAL_AMOUNT, ORDER_STATUS, 
        PAY_TIME, CREATE_TIME
      ) VALUES (
        3000000000 + i,
        'PM' || TO_CHAR(SYSDATE, 'YYYYMMDD') || LPAD(i, 6, '0'),
        v_customer_id,
        v_staff_id,
        v_game_type,
        v_service_hours,
        v_unit_price,
        v_total_amount,
        v_order_status,
        v_pay_time,
        v_create_time
      );
    END;
  END LOOP;
  COMMIT;
END;
/

-- 生成400个评价数据
BEGIN
  FOR i IN 1..400 LOOP
    DECLARE
      v_order_id NUMBER;
      v_customer_id NUMBER;
      v_staff_id NUMBER;
      v_score NUMBER;
      v_content VARCHAR2(500);
      v_tags VARCHAR2(200);
      v_create_time DATE;
    BEGIN
      -- 选择已完成的订单
      SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
      INTO v_order_id, v_customer_id, v_staff_id
      FROM (
        SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
        FROM TB_ORDER 
        WHERE ORDER_STATUS = '已完成'
        ORDER BY DBMS_RANDOM.VALUE
      ) 
      WHERE ROWNUM = 1;
      
      v_score := 3 + ROUND(DBMS_RANDOM.VALUE(0, 2));
      v_content := CASE MOD(i, 10)
        WHEN 0 THEN '服务态度很好，技术也很棒！'
        WHEN 1 THEN '还不错，下次还会约'
        WHEN 2 THEN '技术很厉害，胜率高'
        WHEN 3 THEN '沟通顺畅，配合默契'
        WHEN 4 THEN '价格合理，服务周到'
        WHEN 5 THEN '响应速度快，很专业'
        WHEN 6 THEN '很有耐心，适合新手'
        WHEN 7 THEN '技术不错，态度也很好'
        WHEN 8 THEN '性价比很高，值得推荐'
        ELSE '非常满意，会再次选择'
      END;
      v_tags := CASE MOD(i, 5)
        WHEN 0 THEN '技术好,态度好'
        WHEN 1 THEN '准时,耐心'
        WHEN 2 THEN '专业,配合度高'
        WHEN 3 THEN '声音好听,经验丰富'
        ELSE '颜值高,沟通顺畅'
      END;
      v_create_time := SYSDATE - DBMS_RANDOM.VALUE(0, 180);
      
      INSERT INTO TB_EVALUATION (
        EVAL_ID, ORDER_ID, CUSTOMER_ID, STAFF_ID, SCORE, 
        CONTENT, TAGS, CREATE_TIME
      ) VALUES (
        4000000000 + i,
        v_order_id,
        v_customer_id,
        v_staff_id,
        v_score,
        v_content,
        v_tags,
        v_create_time
      );
    END;
  END LOOP;
  COMMIT;
END;
/

-- 生成50个投诉数据
BEGIN
  FOR i IN 1..50 LOOP
    DECLARE
      v_order_id NUMBER;
      v_customer_id NUMBER;
      v_staff_id NUMBER;
      v_complaint_type VARCHAR2(50);
      v_content VARCHAR2(1000);
      v_status VARCHAR2(20);
      v_create_time DATE;
    BEGIN
      -- 选择一个订单
      SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
      INTO v_order_id, v_customer_id, v_staff_id
      FROM (
        SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
        FROM TB_ORDER 
        ORDER BY DBMS_RANDOM.VALUE
      ) 
      WHERE ROWNUM = 1;
      
      v_complaint_type := CASE MOD(i, 4)
        WHEN 0 THEN '服务态度'
        WHEN 1 THEN '服务质量'
        WHEN 2 THEN '收费问题'
        ELSE '其他'
      END;
      v_content := CASE MOD(i, 10)
        WHEN 0 THEN '陪玩态度不好，不积极'
        WHEN 1 THEN '技术不够好，胜率低'
        WHEN 2 THEN '价格与约定不符'
        WHEN 3 THEN '服务时间不足'
        WHEN 4 THEN '沟通不畅'
        WHEN 5 THEN '未按约定时间上线'
        WHEN 6 THEN '服务态度恶劣'
        WHEN 7 THEN '技术与描述不符'
        WHEN 8 THEN '中途离开'
        ELSE '未提供承诺服务'
      END;
      v_status := CASE MOD(i, 4)
        WHEN 0 THEN '待处理'
        WHEN 1 THEN '处理中'
        WHEN 2 THEN '已解决'
        ELSE '已关闭'
      END;
      v_create_time := SYSDATE - DBMS_RANDOM.VALUE(0, 90);
      
      INSERT INTO TB_COMPLAINT (
        COMPLAINT_ID, ORDER_ID, CUSTOMER_ID, STAFF_ID, 
        COMPLAINT_TYPE, COMPLAINT_CONTENT, COMPLAINT_STATUS, CREATE_TIME
      ) VALUES (
        5000000000 + i,
        v_order_id,
        v_customer_id,
        v_staff_id,
        v_complaint_type,
        v_content,
        v_status,
        v_create_time
      );
    END;
  END LOOP;
  COMMIT;
END;
/

-- 更新陪玩人员的订单数和收入（基于订单数据）
UPDATE TB_STAFF SET 
  TOTAL_ORDERS = (
    SELECT COUNT(*) 
    FROM TB_ORDER 
    WHERE TB_ORDER.STAFF_ID = TB_STAFF.STAFF_ID 
    AND TB_ORDER.ORDER_STATUS = '已完成'
  ),
  TOTAL_INCOME = (
    SELECT COALESCE(SUM(TOTAL_AMOUNT), 0) 
    FROM TB_ORDER 
    WHERE TB_ORDER.STAFF_ID = TB_STAFF.STAFF_ID 
    AND TB_ORDER.ORDER_STATUS = '已完成'
  );

-- 更新客户的订单数和消费金额（基于订单数据）
UPDATE TB_CUSTOMER SET 
  ORDER_COUNT = (
    SELECT COUNT(*) 
    FROM TB_ORDER 
    WHERE TB_ORDER.CUSTOMER_ID = TB_CUSTOMER.CUSTOMER_ID
  ),
  TOTAL_CONSUME = (
    SELECT COALESCE(SUM(TOTAL_AMOUNT), 0) 
    FROM TB_ORDER 
    WHERE TB_ORDER.CUSTOMER_ID = TB_CUSTOMER.CUSTOMER_ID
  );

COMMIT;

-- 显示各表数据统计
SELECT 'TB_STAFF' AS TABLE_NAME, COUNT(*) AS COUNT FROM TB_STAFF
UNION ALL
SELECT 'TB_CUSTOMER' AS TABLE_NAME, COUNT(*) AS COUNT FROM TB_CUSTOMER
UNION ALL
SELECT 'TB_ORDER' AS TABLE_NAME, COUNT(*) AS COUNT FROM TB_ORDER
UNION ALL
SELECT 'TB_EVALUATION' AS TABLE_NAME, COUNT(*) AS COUNT FROM TB_EVALUATION
UNION ALL
SELECT 'TB_COMPLAINT' AS TABLE_NAME, COUNT(*) AS COUNT FROM TB_COMPLAINT;