# Seata AT(Auto Transaction) 模式
## demo 说明

基于 Seata AT 模式，演示分布式事务的提交和回滚；

## demo 运行指南

1. 启动 Seata Server  
参考 [Seata server部署指导](https://github.com/bobbyz007/seata-samples/blob/main/README.md)，启动 Seata Server；

2. 在每个微服务的业务数据库中导入AT模式的 undo_log表 以及业务表  
   导入官方提供的undo_log表： [undo log tables](https://github.com/apache/incubator-seata/blob/v2.0.0/script/client/at/db/mysql.sql) ，自行下载导入。  
   <br>
   对于sbm-account-service(sbm表示springboot+mybatis):
   ```shell
   DROP SCHEMA IF EXISTS db_account;
   CREATE SCHEMA db_account;
   USE db_account;
   
   CREATE TABLE `account_tbl`
   (
   `id`      INT(11) NOT NULL AUTO_INCREMENT,
   `user_id` VARCHAR(255) DEFAULT NULL,
   `money`   INT(11) DEFAULT 0,
   PRIMARY KEY (`id`)
   ) ENGINE = InnoDB
   DEFAULT CHARSET = utf8;
   
   INSERT INTO account_tbl (id, user_id, money)
   VALUES (1, '1001', 10000);
   INSERT INTO account_tbl (id, user_id, money)
   VALUES (2, '1002', 10000);
   ```

   对于sbm-order-service:
   ```shell
   DROP SCHEMA IF EXISTS db_order;
   CREATE SCHEMA db_order;
   USE db_order;
   
   CREATE TABLE `order_tbl`
   (
       `id`             INT(11) NOT NULL AUTO_INCREMENT,
       `user_id`        VARCHAR(255) DEFAULT NULL,
       `commodity_code` VARCHAR(255) DEFAULT NULL,
       `count`          INT(11) DEFAULT '0',
       `money`          INT(11) DEFAULT '0',
       PRIMARY KEY (`id`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8;
   ```

   对于sbm-stock-service:
   ```shell
   DROP SCHEMA IF EXISTS db_stock;
   CREATE SCHEMA db_stock;
   USE db_stock;
   
   CREATE TABLE `stock_tbl`
   (
       `id`             INT(11) NOT NULL AUTO_INCREMENT,
       `commodity_code` VARCHAR(255) DEFAULT NULL,
       `count`          INT(11) DEFAULT '0',
       PRIMARY KEY (`id`),
       UNIQUE KEY `commodity_code` (`commodity_code`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8;
   
   
   INSERT INTO stock_tbl (id, commodity_code, count)
   VALUES (1, '2001', 1000);
   ```

4. 启动 各个微服务。
