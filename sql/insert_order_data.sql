-- 插入订单数据
DECLARE
  staff_table_exists NUMBER := 0;
  customer_table_exists NUMBER := 0;
BEGIN
  -- 检查相关表是否存在
  SELECT COUNT(*) INTO staff_table_exists FROM USER_TABLES WHERE TABLE_NAME = 'TB_STAFF';
  SELECT COUNT(*) INTO customer_table_exists FROM USER_TABLES WHERE TABLE_NAME = 'TB_CUSTOMER';
  
  IF staff_table_exists > 0 AND customer_table_exists > 0 THEN
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
        -- 从现有的客户和陪玩人员中选择
        BEGIN
          SELECT CUSTOMER_ID INTO v_customer_id
          FROM (
            SELECT CUSTOMER_ID FROM TB_CUSTOMER ORDER BY DBMS_RANDOM.VALUE
          ) WHERE ROWNUM = 1;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_customer_id := 20001; -- 使用默认值
        END;
        
        BEGIN
          SELECT STAFF_ID INTO v_staff_id
          FROM (
            SELECT STAFF_ID FROM TB_STAFF ORDER BY DBMS_RANDOM.VALUE
          ) WHERE ROWNUM = 1;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_staff_id := 10001; -- 使用默认值
        END;
        
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
  ELSE
    DBMS_OUTPUT.PUT_LINE('表 TB_STAFF 或 TB_CUSTOMER 不存在，请先创建表');
  END IF;
END;
/