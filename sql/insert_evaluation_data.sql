-- 插入评价数据
DECLARE
  table_exists NUMBER := 0;
BEGIN
  -- 检查表是否存在
  SELECT COUNT(*) 
  INTO table_exists
  FROM USER_TABLES 
  WHERE TABLE_NAME = 'TB_ORDER';
  
  IF table_exists > 0 THEN
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
        BEGIN
          SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
          INTO v_order_id, v_customer_id, v_staff_id
          FROM (
            SELECT ORDER_ID, CUSTOMER_ID, STAFF_ID 
            FROM TB_ORDER 
            WHERE ORDER_STATUS = '已完成'
            ORDER BY DBMS_RANDOM.VALUE
          ) 
          WHERE ROWNUM = 1;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            -- 如果没有已完成的订单，就从所有订单中选一个
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
                v_order_id := 3000000001;
                v_customer_id := 20001;
                v_staff_id := 10001;
            END;
        END;
        
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
  ELSE
    DBMS_OUTPUT.PUT_LINE('表 TB_ORDER 不存在，请先创建表');
  END IF;
END;
/