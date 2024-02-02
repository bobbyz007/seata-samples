# Seata Saga 模式 demo 工程

[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。

## demo 说明

基于 Seata Saga模式，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个Saga事务参与者，分别是: [InventoryAction](https://github.com/seata/seata-samples/blob/master/saga/local-saga-sample/src/main/java/io/seata/samples/saga/action/InventoryAction.java)
和 [BalanceAction](https://github.com/seata/seata-samples/blob/master/saga/local-saga-sample/src/main/java/io/seata/samples/saga/action/BalanceAction.java)
;分布式事务提交则两者均提交，分布式事务回滚则两者均回滚；

这2个Saga参与者均是 本地bean（非远程RPC服务），两个参与都有一个reduce方法，表示库存扣减或余额扣减，还有一个compensateReduce方法，表示补偿扣减操作。

- InventoryAction 接口定义如下：

```java
public interface InventoryAction {

    /**
     * reduce
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     * @param businessKey
     * @param params
     * @return
     */
    boolean compensateReduce(String businessKey, Map<String, Object> params);
}
```

## demo 运行指南

1. 启动 SEATA Server  
参考 [Seata server部署指导](https://github.com/bobbyz007/seata-samples/blob/main/README.md)，启动 Seata Server；

2. 在业务数据库中导入saga模式的数据库表  
导入官方提供对应版本的 [saga mysql tables](https://github.com/apache/incubator-seata/blob/v2.0.0/script/client/saga/db/mysql.sql) ，主要存储sage状态机定义及实例。
    ```
   mysql> show tables;
    +---------------------------+
    | Tables_in_test_local_saga |
    +---------------------------+
    | seata_state_inst          |
    | seata_state_machine_def   |
    | seata_state_machine_inst  |
    +---------------------------+
   ```
3. 启动 Saga Demo  
运行测试类 LocalSagaTransactionStarter, 启动 demo工程；

在配置文件 statelang/reduce_inventory_and_balance.json 里面 删除如下埋入的异常配置，就是事务提交，否则就会执行事务补偿。

ReduceBalance -> Input -> {throwException": "$.[mockReduceBalanceFail]"}

测试代码包含了Saga 事务提交场景和Saga事务回滚场景的演示。

