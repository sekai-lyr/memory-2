CREATE TABLE IF NOT EXISTS ykd_ebusiness_user (
                      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
                      `gmt_created` datetime COMMENT '创建时间',
                      `gmt_modified` datetime COMMENT '修改时间',
                      `user_name` VARCHAR (20) NOT NULL COMMENT '用户名',
                      `password` VARCHAR(50) NOT NULL COMMENT '加密过的密码',
                      `mobile` VARCHAR(20) NOT NULL COMMENT '手机号',
                      `email` VARCHAR(50) COMMENT '邮箱',
                      `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                      `gender` VARCHAR(10) NOT NULL COMMENT '性别：male（男）/female（女）',
                      PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT IGNORE INTO ykd_ebusiness_user (id, gmt_created, gmt_modified, user_name, password, mobile, email, name, gender)
VALUES
    (1, NOW(), NOW(), 'zhangsan', '123456', '13800138001', 'zhangsan@qq.com', '张三', 'male'),
    (2, NOW(), NOW(), 'lisi', '123456', '13800138002', 'lisi@qq.com', '李四', 'male'),
    (3, NOW(), NOW(), 'wangfang', '123456', '13800138003', 'wangfang@163.com', '王芳', 'female'),
    (4, NOW(), NOW(), 'zhaoliu', '123456', '13800138004', 'zhaoliu@gmail.com', '赵六', 'male'),
    (5, NOW(), NOW(), 'sunqi', '123456', '13800138005', 'sunqi@hotmail.com', '孙七', 'male'),
    (6, NOW(), NOW(), 'zhouyi', '123456', '13800138006', 'zhouyi@foxmail.com', '周怡', 'female'),
    (7, NOW(), NOW(), 'wuer', '123456', '13800138007', 'wuer@126.com', '吴二', 'male'),
    (8, NOW(), NOW(), 'zhengsan', '123456', '13800138008', 'zhengsan@qq.com', '郑三', 'male'),
    (9, NOW(), NOW(), 'wangsi', '123456', '13800138009', 'wangsi@163.com', '王思', 'female'),
    (10, NOW(), NOW(), 'liuwu', '123456', '13800138010', 'liuwu@gmail.com', '刘五', 'male');


CREATE TABLE IF NOT EXISTS `ykd_ebusiness_category` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
                            `gmt_created` datetime COMMENT '创建时间',
                            `gmt_modified` datetime COMMENT '修改时间',
                            `name` varchar(32) NOT NULL COMMENT '类目名称',
                            `description` varchar(1000) COMMENT '类目描述',
                            `parent_category_id` BIGINT default 0 COMMENT '父类目id',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入 30 条分类数据（一级+二级+三级）
INSERT IGNORE INTO `ykd_ebusiness_category` (`id`, `gmt_created`, `gmt_modified`, `name`, `description`, `parent_category_id`)
VALUES
-- 一级分类（6 条）
(1, NOW(), NOW(), '电子产品', '各类电子数码产品总分类', 0),
(2, NOW(), NOW(), '服装服饰', '男女服装、鞋包配饰', 0),
(3, NOW(), NOW(), '食品饮料', '零食、生鲜、饮品等', 0),
(4, NOW(), NOW(), '家居用品', '家具、厨具、日用百货', 0),
(5, NOW(), NOW(), '图书音像', '书籍、杂志、音像制品', 0),
(6, NOW(), NOW(), '运动户外', '运动装备、户外用品', 0),

-- 二级分类（12 条）
(7, NOW(), NOW(), '手机', '智能手机、功能机', 1),
(8, NOW(), NOW(), '电脑', '笔记本、台式机、平板', 1),
(9, NOW(), NOW(), '男装', '上衣、裤子、外套', 2),
(10, NOW(), NOW(), '女装', '连衣裙、衬衫、风衣', 2),
(11, NOW(), NOW(), '休闲零食', '薯片、巧克力、糖果', 3),
(12, NOW(), NOW(), '生鲜水果', '新鲜水果、蔬菜、肉类', 3),
(13, NOW(), NOW(), '家具', '沙发、床、桌椅', 4),
(14, NOW(), NOW(), '厨具', '锅具、餐具、小家电', 4),
(15, NOW(), NOW(), '小说图书', '网络小说、文学小说', 5),
(16, NOW(), NOW(), '教育考试', '考研、公考、教材', 5),
(17, NOW(), NOW(), '运动装备', '运动鞋、运动服、护具', 6),
(18, NOW(), NOW(), '户外用品', '帐篷、背包、登山鞋', 6),

-- 三级分类（12 条）
(19, NOW(), NOW(), '安卓手机', '华为、小米、OPPO、vivo', 7),
(20, NOW(), NOW(), '苹果手机', 'iPhone 全系列', 7),
(21, NOW(), NOW(), '游戏本', '高性能游戏笔记本', 8),
(22, NOW(), NOW(), '轻薄本', '商务办公轻薄笔记本', 8),
(23, NOW(), NOW(), '男士T恤', '夏季短袖、长袖T恤', 9),
(24, NOW(), NOW(), '男士牛仔裤', '直筒、修身牛仔裤', 9),
(25, NOW(), NOW(), '女士连衣裙', '夏季、春秋连衣裙', 10),
(26, NOW(), NOW(), '女士外套', '风衣、夹克、西装', 10),
(27, NOW(), NOW(), '膨化零食', '薯片、虾条、爆米花', 11),
(28, NOW(), NOW(), '巧克力', '牛奶巧克力、黑巧克力', 11),
(29, NOW(), NOW(), '篮球装备', '篮球、球衣、护膝', 17),
(30, NOW(), NOW(), '瑜伽用品', '瑜伽垫、瑜伽服、弹力带', 17);

CREATE TABLE IF NOT EXISTS `ykd_ebusiness_product` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
                           `gmt_created` datetime COMMENT '创建时间',
                           `gmt_modified` datetime COMMENT '修改时间',
                           `user_id` BIGINT NOT NULL COMMENT '发布人id',
                           `name` varchar(32) NOT NULL COMMENT '类目名称',
                           `description` varchar(1000) COMMENT '类目描述',
                           `images` varchar(2000) COMMENT '商品图片，JSON格式图片列表',
                           `detail` varchar(2000) COMMENT '商品详情，多个图片地址。JSON格式图片列表',
                           `status` varchar(16) COMMENT '商品状态',
                           `category_ids` varchar(2000) COMMENT '商品类目，一个商品可以有多个类目。JSON格式类目 id 列表',
                           `price` DOUBLE default 0.00 COMMENT '商品参考价格',
                           `stock` INTEGER default 0 COMMENT '库存',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入 30 条真实商品数据
INSERT IGNORE INTO `ykd_ebusiness_product` (
    `id`, `gmt_created`, `gmt_modified`, `user_id`, `name`, `description`,
    `images`, `detail`, `status`, `category_ids`, `price`, `stock`
)
VALUES
-- 电子产品
(1, NOW(), NOW(), 1001, 'iPhone 15 Pro 256G', '苹果最新旗舰手机，钛金属设计，A17 Pro芯片', '["https://img.xxx.com/iphone1.jpg","https://img.xxx.com/iphone2.jpg"]', '["https://img.xxx.com/iphone_detail1.jpg","https://img.xxx.com/iphone_detail2.jpg"]', 'onsale', '[1,7,13]', 7999.00, 50),
(2, NOW(), NOW(), 1001, '小米14 骁龙8 Gen3', '小米旗舰手机，徕卡光学Summilux高速镜头', '["https://img.xxx.com/mi14_1.jpg","https://img.xxx.com/mi14_2.jpg"]', '["https://img.xxx.com/mi14_detail1.jpg"]', 'onsale', '[1,7,13]', 4299.00, 80),
(3, NOW(), NOW(), 1002, 'MacBook Air M2', '苹果轻薄笔记本，无风扇设计，超长续航', '["https://img.xxx.com/macbook1.jpg"]', '["https://img.xxx.com/macbook_detail1.jpg"]', 'onsale', '[1,8,15]', 8999.00, 30),
(4, NOW(), NOW(), 1002, '华为MateBook 14', '2K触控全面屏，轻薄商务本', '["https://img.xxx.com/huawei_mate1.jpg"]', '["https://img.xxx.com/huawei_detail1.jpg"]', 'onsale', '[1,8,15]', 5699.00, 45),
(5, NOW(), NOW(), 1003, 'AirPods Pro 2', '主动降噪无线耳机，空间音频', '["https://img.xxx.com/airpods1.jpg"]', '["https://img.xxx.com/airpods_detail1.jpg"]', 'onsale', '[1]', 1899.00, 100),
(6, NOW(), NOW(), 1003, 'iPad 10.9英寸', '苹果入门级平板电脑，适合学习娱乐', '["https://img.xxx.com/ipad1.jpg"]', '["https://img.xxx.com/ipad_detail1.jpg"]', 'onsale', '[1,8]', 3599.00, 60),

-- 服装服饰
(7, NOW(), NOW(), 1004, '男士纯棉短袖T恤', '夏季宽松透气纯色T恤，多色可选', '["https://img.xxx.com/tshirt1.jpg"]', '["https://img.xxx.com/tshirt_detail1.jpg"]', 'onsale', '[2,9,21]', 59.00, 200),
(8, NOW(), NOW(), 1004, '男士直筒牛仔裤', '春秋百搭休闲长裤，弹力面料', '["https://img.xxx.com/jeans1.jpg"]', '["https://img.xxx.com/jeans_detail1.jpg"]', 'onsale', '[2,9,22]', 129.00, 150),
(9, NOW(), NOW(), 1005, '女士雪纺连衣裙', '夏季收腰显瘦中长款仙女裙', '["https://img.xxx.com/dress1.jpg"]', '["https://img.xxx.com/dress_detail1.jpg"]', 'onsale', '[2,10,23]', 169.00, 120),
(10, NOW(), NOW(), 1005, '女士休闲风衣', '春秋中长款气质外套', '["https://img.xxx.com/coat1.jpg"]', '["https://img.xxx.com/coat_detail1.jpg"]', 'onsale', '[2,10,24]', 299.00, 80),
(11, NOW(), NOW(), 1006, '耐克运动鞋', '透气缓震跑步鞋，男女同款', '["https://img.xxx.com/nike1.jpg"]', '["https://img.xxx.com/nike_detail1.jpg"]', 'onsale', '[2,17]', 599.00, 90),
(12, NOW(), NOW(), 1006, '女士斜挎包包', '时尚百搭单肩小包，质感PU皮', '["https://img.xxx.com/bag1.jpg"]', '["https://img.xxx.com/bag_detail1.jpg"]', 'onsale', '[2]', 199.00, 110),

-- 食品饮料
(13, NOW(), NOW(), 1007, '乐事薯片大礼包', '混合口味薯片组合装，休闲零食', '["https://img.xxx.com/potato1.jpg"]', '["https://img.xxx.com/potato_detail1.jpg"]', 'onsale', '[3,11,25]', 39.90, 300),
(14, NOW(), NOW(), 1007, '德芙巧克力礼盒', '丝滑牛奶巧克力，节日送礼佳品', '["https://img.xxx.com/dove1.jpg"]', '["https://img.xxx.com/dove_detail1.jpg"]', 'onsale', '[3,11,26]', 89.00, 180),
(15, NOW(), NOW(), 1008, '新鲜烟台红富士苹果', '脆甜多汁，5斤装', '["https://img.xxx.com/apple1.jpg"]', '["https://img.xxx.com/apple_detail1.jpg"]', 'onsale', '[3,12]', 29.90, 500),
(16, NOW(), NOW(), 1008, '泰国进口龙眼', '新鲜桂圆水果，2斤装', '["https://img.xxx.com/longan1.jpg"]', '["https://img.xxx.com/longan_detail1.jpg"]', 'onsale', '[3,12]', 35.80, 400),
(17, NOW(), NOW(), 1009, '三只松鼠坚果礼盒', '每日坚果混合装，健康零食', '["https://img.xxx.com/nut1.jpg"]', '["https://img.xxx.com/nut_detail1.jpg"]', 'onsale', '[3,11]', 138.00, 120),
(18, NOW(), NOW(), 1009, '可口可乐整箱', '330ml*24罐碳酸饮料', '["https://img.xxx.com/coke1.jpg"]', '["https://img.xxx.com/coke_detail1.jpg"]', 'onsale', '[3]', 59.90, 200),

-- 家居用品
(19, NOW(), NOW(), 1010, '北欧简约布艺沙发', '小户型三人位沙发，可拆洗', '["https://img.xxx.com/sofa1.jpg"]', '["https://img.xxx.com/sofa_detail1.jpg"]', 'onsale', '[4,13]', 1899.00, 20),
(20, NOW(), NOW(), 1010, '家用不粘炒锅', '电磁炉燃气灶通用，32cm', '["https://img.xxx.com/pan1.jpg"]', '["https://img.xxx.com/pan_detail1.jpg"]', 'onsale', '[4,14]', 89.00, 150),
(21, NOW(), NOW(), 1011, '纯棉四件套床上用品', '1.8m床全棉床单被套', '["https://img.xxx.com/bedding1.jpg"]', '["https://img.xxx.com/bedding_detail1.jpg"]', 'onsale', '[4]', 239.00, 80),
(22, NOW(), NOW(), 1011, '家用多功能电饭煲', '4L智能预约电饭锅，2-6人', '["https://img.xxx.com/ricecooker1.jpg"]', '["https://img.xxx.com/ricecooker_detail1.jpg"]', 'onsale', '[4,14]', 199.00, 100),
(23, NOW(), NOW(), 1012, '简约现代落地灯', '客厅卧室护眼阅读灯', '["https://img.xxx.com/lamp1.jpg"]', '["https://img.xxx.com/lamp_detail1.jpg"]', 'onsale', '[4]', 129.00, 70),
(24, NOW(), NOW(), 1012, '卫生间置物架', '免打孔浴室收纳架', '["https://img.xxx.com/rack1.jpg"]', '["https://img.xxx.com/rack_detail1.jpg"]', 'onsale', '[4]', 49.00, 300),

-- 图书音像
(25, NOW(), NOW(), 1013, '三体全集 刘慈欣', '科幻小说经典作品', '["https://img.xxx.com/santi1.jpg"]', '["https://img.xxx.com/santi_detail1.jpg"]', 'onsale', '[5,15]', 58.00, 200),
(26, NOW(), NOW(), 1013, '考研数学历年真题', '数学一/二/三通用复习资料', '["https://img.xxx.com/kaoyan1.jpg"]', '["https://img.xxx.com/kaoyan_detail1.jpg"]', 'onsale', '[5,16]', 45.00, 150),
(27, NOW(), NOW(), 1014, '活着 余华', '经典文学小说书籍', '["https://img.xxx.com/huozhe1.jpg"]', '["https://img.xxx.com/huozhe_detail1.jpg"]', 'onsale', '[5,15]', 28.00, 250),
(28, NOW(), NOW(), 1014, 'JavaScript高级程序设计', '前端开发编程入门教程', '["https://img.xxx.com/js1.jpg"]', '["https://img.xxx.com/js_detail1.jpg"]', 'onsale', '[5,16]', 129.00, 80),

-- 运动户外
(29, NOW(), NOW(), 1015, '7号标准篮球', '室内外通用耐磨篮球', '["https://img.xxx.com/basketball1.jpg"]', '["https://img.xxx.com/basketball_detail1.jpg"]', 'onsale', '[6,17,27]', 99.00, 120),
(30, NOW(), NOW(), 1015, '专业瑜伽垫', '防滑加厚健身垫', '["https://img.xxx.com/yoga1.jpg"]', '["https://img.xxx.com/yoga_detail1.jpg"]', 'onsale', '[6,17,28]', 69.00, 180),
(31, NOW(), NOW(), 1016, '户外双肩背包', '大容量旅行登山包', '["https://img.xxx.com/backpack1.jpg"]', '["https://img.xxx.com/backpack_detail1.jpg"]', 'onsale', '[6,18]', 159.00, 90),
(32, NOW(), NOW(), 1016, '速干运动T恤', '男女透气健身跑步上衣', '["https://img.xxx.com/sportshirt1.jpg"]', '["https://img.xxx.com/sportshirt_detail1.jpg"]', 'onsale', '[6,17]', 45.00, 220);
