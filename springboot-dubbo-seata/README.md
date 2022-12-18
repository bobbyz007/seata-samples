# SpringBoot + Dubbo + Mybatis-plus + Nacos + Seata

Integration SpringBoot + Dubbo + Mybatis-plus + Nacos + Seata

### 1. clone the code

- samples-common common module

- samples-account user account module

- samples-order order module

- samples-stock stock module

- samples-business business module

### 2. prepare database

create database （默认为：seata），import db_seata.sql to database

then you will see ：

```
+-------------------------+
| Tables_in_seata         |
+-------------------------+
| t_account               |
| t_order                 |
| t_stock               |
| undo_log                |
+-------------------------+
```

### 3. start Nacos
Nacos quickstart：https://nacos.io/en-us/docs/quick-start.html

enter the Nacos webconsole：http://127.0.0.1:8848/nacos/index.html

### 4. start Seata Server

download page：https://github.com/seata/seata/releases

基于1.6.0版本，download and unzip seata-server，cd the bin dictory, and run

```bash
bin/seata-server.sh -h 127.0.0.1 -p 8091
```

### 5. start the demo module

start samples-account、samples-order、samples-stock、samples-business

use Nacos webconsole to ensure the registry is ok: http://127.0.0.1:8848/nacos/#/serviceManagement

> check the datasource config in application.properties is right.

### 6. start the normal request

use postman to send a post request：http://localhost:8104/business/dubbo/buy

body：

```json
{
    "userId":"1",
    "commodityCode":"C201901140001",
    "name":"fan",
    "count":2,
    "amount":"100"
}
```

or use curl：

```bash
curl -H "Content-Type:application/json" -X POST -d '{"userId":"1","commodityCode":"C201901140001","name":"风扇","count":2,"amount":"100"}' localhost:8104/business/dubbo/buy
``` 

then this will send a pay request,and return code is 200

### 7. test the rollback request

enter samples-business , change BusinessServiceImpl, uncomment the following code ：

```
if (!flag) {
  throw new RuntimeException("测试抛异常后，分布式事务回滚！");
}
```

restart the samples-business module, and execute the step 6. 

如何验证的确回滚了： 手工往t_order插入一条记录，可以看到自增id跳过了一条记录，说明插入后又回滚删除了！
```roomsql
mysql> insert into t_order(order_no, user_id, commodity_code, count, amount) values('1','1','1',1,1);
Query OK, 1 row affected (0.02 sec)

mysql> select * from t_order;
+----+----------------------------------+---------+----------------+-------+--------+
| id | order_no                         | user_id | commodity_code | count | amount |
+----+----------------------------------+---------+----------------+-------+--------+
| 64 | 1de5911fef2349519168cd813ad36ad3 | 1       | C201901140001  |     5 |   5.00 |
| 65 | 121037b25ae74ad398b7f5ab1c9625f6 | 1       | C201901140001  |    10 |   5.00 |
| 66 | b9468d3e6af84359b4f905b7f03ecfb7 | 1       | C201901140001  |     8 |   9.00 |
| 68 | 1                                | 1       | 1              |     1 |   1.00 |
+----+----------------------------------+---------+----------------+-------+--------+
4 rows in set (0.00 sec)

```
