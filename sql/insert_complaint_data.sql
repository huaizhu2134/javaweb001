-- 插入投诉数据
DECLARE
  table_exists NUMBER := 0;
BEGIN
  -- 检查表是否存在
  SELECT COUNT(*) 
  INTO table_exists
  FROM USER_TABLES 
  WHERE TABLE_NAME = 'TB_ORDER';
  
  IF table_exists > 0 THEN
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
        BEGIN
          SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
          INTO v_order_id, v_customer_id, v_staff_id
          FROM (
            SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
            FROM TB_ORDER 
            ORDER BY DBMS_RANDOM.VALUE
          ) 
          WHERE ROWNUM = 1;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            -- 如果没有订单，使用默认值
            v_order_id := NULL;
            v_customer_id := 20001;
            v_staff_id := 10001;
        END;
        
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
  ELSE
    DBMS_OUTPUT.PUT_LINE('表 TB_ORDER 不存在，请先创建表');
  END IF;
END;
/