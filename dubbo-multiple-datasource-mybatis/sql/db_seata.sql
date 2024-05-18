-- order
create database test_order;
use test_order;

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `amount` double(11,2) DEFAULT NULL,
  `sum` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `replace_time` datetime DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO orders(product_id, amount, sum, create_time, replace_time, account_id)
VALUES(0, 0, 0, '2022-01-01 01:01:00', '2022-01-01 01:01:00', 0);

-- account
create database test_pay;
use test_pay;

CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sum` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO account (user_name, last_update_time, sum)
VALUES('', CURRENT_TIMESTAMP, 0);

-- storage
create database test_storage;
use test_storage;

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` double DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `product`(price, stock, last_update_time) VALUES(1000.32, 1000, now());