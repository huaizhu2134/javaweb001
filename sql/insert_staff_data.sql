-- 插入陪玩人员数据
DECLARE
  table_exists NUMBER := 0;
BEGIN
  -- 检查表是否存在
  SELECT COUNT(*) INTO table_exists FROM USER_TABLES WHERE TABLE_NAME = 'TB_STAFF';
  
  IF table_exists > 0 THEN
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
  ELSE
    DBMS_OUTPUT.PUT_LINE('表 TB_STAFF 不存在，请先创建表');
  END IF;
END;
/