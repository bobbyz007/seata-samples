# seata-samples
Seata samples bases on https://github.com/seata/seata-samples

As the official samples project includes too many sub projects and the dependencies are so old here we optimize it.

## How to setup?
所有示例都需要启动seata server，而seata又依赖配置中心和注册中心，此处以nacos作为配置和注册中心。
1. 启动nacos server以及mysql数据库，参考 [部署启动nacos server](https://github.com/bobbyz007/java-demo/blob/master/dubbo-samples/README.md)
2. 启动seata server，下载最新版本并解压 [seata release](https://github.com/seata/seata/releases)
   1. 初始化seata server的数据库脚本(位于解压目录的 seata-server-2.0.0\seata\script\server\db\mysql.sql)
       ```shell
       mysql> create database seata;
       mysql> use seata
       mysql> source D:\software\seata-server-2.0.0\seata\script\server\db\mysql.sql
       ```
   2. 修改seata server的配置文件(位于解压目录的seata-server-2.0.0\seata\conf\application.yml)，可以参考目录中提供的样本application.example.yml进行修改。  
   此处仅列出配置文件中需要适配的部分配置，其他配置保持默认即可。
      ```yaml
      seata:
         config:
            # support: nacos, consul, apollo, zk, etcd3
            type: nacos
            nacos:
               server-addr: 127.0.0.1:8848
               namespace: seata-server
               group: SEATA_GROUP
               #username: nacos
               #password: nacos
               ##if use MSE Nacos with auth, mutex with username/password attribute
               #access-key: ""
               #secret-key: ""
               data-id: seataServer.properties
         registry:
            # support: nacos, eureka, redis, zk, consul, etcd3, sofa
            type: nacos
            nacos:
               server-addr: 127.0.0.1:8848
               namespace: seata-server
               group: SEATA_GROUP
               application: seata-server
               cluster: default
               ##if use MSE Nacos with auth, mutex with username/password attribute
               #access-key:
               #secret-key:
         store:
            # support: file 、 db 、 redis 、 raft
            mode: db
            db:
               datasource: druid
               db-type: mysql
               driver-class-name: com.mysql.cj.jdbc.Driver
               url: jdbc:mysql://127.0.0.1:33306/seata?rewriteBatchedStatements=true
               user: mysql
               password: mysql
               min-conn: 5
               max-conn: 100
               global-table: global_table
               branch-table: branch_table
               lock-table: lock_table
               distributed-lock-table: distributed_lock
               query-limit: 100
               max-wait: 5000
               #  server:
               #    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
      ```
   3. 登录nacos控制台( http://localhost:8848/nacos )，根据上述seata配置文件config段指定的data-id, group, namespace新增配置项(界面中格式选择TEXT)。配置项的内容参考 seata-server-2.0.0\seata\script\config-center\config.txt 进行裁剪。  
   config.txt提供了事务存储的三种配置样例，分别是file,db,redis。此处以db为例，删除对应file和redis的配置，其他配置保留默认即可。  
   主要适配修改service和store打头的配置，参考如下：
      ```yaml
      #Transaction routing rules configuration, only for the client. transaction group -> cluster
      service.vgroupMapping.default_tx_group=default
      #If you use a registry, you can ignore it
      service.default.grouplist=127.0.0.1:8091
      service.enableDegrade=false
      service.disableGlobalTransaction=false

      #Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
      store.mode=db
      store.lock.mode=db
      store.session.mode=db
      #Used for password encryption
      store.publicKey=
      
      #These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
      store.db.datasource=druid
      store.db.dbType=mysql
      store.db.driverClassName=com.mysql.cj.jdbc.Driver
      store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
      store.db.user=mysql
      store.db.password=mysql
      store.db.minConn=5
      store.db.maxConn=30
      store.db.globalTable=global_table
      store.db.branchTable=branch_table
      store.db.distributedLockTable=distributed_lock
      store.db.queryLimit=100
      store.db.lockTable=lock_table
      store.db.maxWait=5000
      ```
   4. 启动seata server，进入解压目录执行命令：
      ```shell
      .\bin\seata-server.bat -h 127.0.0.1 -p 8091
      ```
      控制台显示类似如下信息，则表示启动成功： "seata server started in 1546 millSeconds"