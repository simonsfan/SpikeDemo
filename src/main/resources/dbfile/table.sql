CREATE TABLE `product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(50) NOT NULL COMMENT '商品名称',
  `product_stock` INT NOT NULL COMMENT '商品库存数量',
  `product_sold` INT(11) NOT NULL COMMENT '已售数量',
  `create_time` datetime DEFAULT '1970-01-01 00:00:00' COMMENT '参与时间',
  `update_time` datetime DEFAULT '1970-01-01 00:00:00' COMMENT '更新时间',
  `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁，版本号',
  PRIMARY KEY (`id`),
   INDEX idx_product_name(product_name)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT '商品库存信息表';

CREATE TABLE `pro_order` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL COMMENT '商品库存ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `create_time` datetime DEFAULT '1970-01-01 00:00:00' COMMENT '参与时间',
  `update_time` datetime DEFAULT '1970-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT '订单信息表';
