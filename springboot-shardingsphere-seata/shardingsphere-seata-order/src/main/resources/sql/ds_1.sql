create database ds_1;
USE ds_1;

--
-- Definition for table t_order_0
--
DROP TABLE IF EXISTS t_order_0;
CREATE TABLE t_order_0
(
    id       VARCHAR(64) NOT NULL COMMENT '主键',
    order_id BIGINT(20) NOT NULL,
    user_id  INT(11) NOT NULL,
    status   VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;

--
-- Definition for table t_order_1
--
DROP TABLE IF EXISTS t_order_1;
CREATE TABLE t_order_1
(
    id       VARCHAR(64) NOT NULL COMMENT '主键',
    order_id BIGINT(20) NOT NULL,
    user_id  INT(11) NOT NULL,
    status   VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;

--
-- Definition for table t_order_item_0
--
DROP TABLE IF EXISTS t_order_item_0;
CREATE TABLE t_order_item_0
(
    id            VARCHAR(64) NOT NULL COMMENT '主键',
    order_item_id BIGINT(20) NOT NULL,
    order_id      BIGINT(20) NOT NULL,
    user_id       INT(11) NOT NULL,
    status        VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;

--
-- Definition for table t_order_item_1
--
DROP TABLE IF EXISTS t_order_item_1;
CREATE TABLE t_order_item_1
(
    id            VARCHAR(64) NOT NULL COMMENT '主键',
    order_item_id BIGINT(20) NOT NULL,
    order_id      BIGINT(20) NOT NULL,
    user_id       INT(11) NOT NULL,
    status        VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;

--
-- Definition for table undo_log
--
DROP TABLE IF EXISTS undo_log;
-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';