# SpringBoot-Dubbo使用 Seata 实现分布式事务 - MyBatisPlus动态数据源-Nacos为配置/注册中心

> 使用 Seata 作为分布式事务组件，使用 MySQL 数据库，使用 MyBatis 作为数据访问层实现多数据源下事务一致，使用 MyBatisPlus 作为 MyBatis 的辅助工具，Nacos为配置/注册中心

## 环境准备

### 创建数据库及表

create database （默认为：seata），import db_seata.sql to database。  
导入官方提供的undo_log表： [undo log tables](https://github.com/apache/incubator-seata/blob/v2.0.0/script/client/at/db/mysql.sql) ，自行下载导入。

### 安装Seata跟Nacos
参考父项目README.md安装seata跟nacos配置

## 测试
先后启动provider跟comsumer
- 测试事务提交: http://127.0.0.1:28888/test/testCommit  
查看test_order.orders是否正常插入数据。


- 测试事务回滚: http://127.0.0.1:28888/test/testRollback
  手工往test_order.orders插入数据，发现自动生成的id跳变，表示有生成记录后被回滚了。
  ```sql
  insert into orders (account_id, product_id, amount, sum) values(1, 1, 2000, 2);
  ```
  

