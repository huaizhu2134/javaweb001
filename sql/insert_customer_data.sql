-- 插入客户数据
DECLARE
  table_exists NUMBER := 0;
BEGIN
  -- 检查表是否存在
  SELECT COUNT(*) INTO table_exists FROM USER_TABLES WHERE TABLE_NAME = 'TB_CUSTOMER';
  
  IF table_exists > 0 THEN
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
  ELSE
    DBMS_OUTPUT.PUT_LINE('表 TB_CUSTOMER 不存在，请先创建表');
  END IF;
END;
/