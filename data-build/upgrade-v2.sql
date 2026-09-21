-- ============================================================
-- Sekai 商城 v2 数据库升级脚本
-- 1) 修复种子数据图片（指向本地 /uploads/seed 占位图）与状态
-- 2) 新增购物车表 ykd_ebusiness_cart
-- 3) 新增订单表 ykd_ebusiness_order
-- ============================================================

-- 1) 种子数据图片与状态修复
UPDATE ykd_ebusiness_product SET status = 'ON' WHERE status = 'onsale';

-- 1.1) 种子商品 user_id 修正：1001~1016 假 id 映射到真实用户 id（1~10）
UPDATE ykd_ebusiness_product SET user_id = user_id - 1000 WHERE user_id >= 1001 AND user_id <= 1010;
UPDATE ykd_ebusiness_product SET user_id = user_id - 1010 WHERE user_id >= 1011 AND user_id <= 1016;

-- 1.2) 商品图片指向真实商品照片（/uploads/real/，图片需先放置到 uploads/real/ 目录）
UPDATE ykd_ebusiness_product SET
  images = CONCAT('["/uploads/real/p', id, '-main.jpg"]'),
  detail = CONCAT('["/uploads/real/p', id, '-detail.jpg"]');

-- 2) 购物车表
CREATE TABLE IF NOT EXISTS `ykd_ebusiness_cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `gmt_created` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '修改时间',
  `user_id` BIGINT NOT NULL COMMENT '用户id',
  `product_id` BIGINT NOT NULL COMMENT '商品id',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_user_product` (`user_id`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车';

-- 3) 订单表
CREATE TABLE IF NOT EXISTS `ykd_ebusiness_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `gmt_created` datetime COMMENT '创建时间',
  `gmt_modified` datetime COMMENT '修改时间',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `buyer_id` BIGINT NOT NULL COMMENT '买家id',
  `seller_id` BIGINT NOT NULL COMMENT '卖家id',
  `product_id` BIGINT NOT NULL COMMENT '商品id',
  `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称快照',
  `product_image` VARCHAR(500) COMMENT '商品图片快照',
  `price` DOUBLE NOT NULL DEFAULT 0 COMMENT '成交单价',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  `total_amount` DOUBLE NOT NULL DEFAULT 0 COMMENT '订单总金额',
  `status` VARCHAR(16) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING待支付/PAID已支付/SHIPPED已发货/DONE已完成/CANCELLED已取消',
  `receiver_name` VARCHAR(50) COMMENT '收货人',
  `receiver_phone` VARCHAR(20) COMMENT '收货电话',
  `receiver_address` VARCHAR(255) COMMENT '收货地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_buyer` (`buyer_id`),
  KEY `idx_order_seller` (`seller_id`),
  KEY `idx_order_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';
