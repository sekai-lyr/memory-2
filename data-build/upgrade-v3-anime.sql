-- ============================================================
-- Sekai 商城 v3：二次元化改造（手办周边商品 + 二次元分类）
-- 图片位于 uploads/anime/（Booth/官方周边图源）
-- ============================================================

-- 0) 扩大商品名称长度（二次元商品名较长）
ALTER TABLE ykd_ebusiness_product MODIFY COLUMN `name` varchar(100) NOT NULL COMMENT '商品名称';

-- 1) 重置分类为二次元分类（6 个一级 + 12 个二级）
TRUNCATE TABLE ykd_ebusiness_category;
INSERT INTO ykd_ebusiness_category (gmt_created, gmt_modified, name, description, parent_category_id) VALUES
(NOW(), NOW(), '手办模型', 'PVC手办、亚克力立牌、黏土人figma', 0),
(NOW(), NOW(), '毛绒玩偶', 'ぬいぐるみ、主题毛毯、毛绒挂件', 0),
(NOW(), NOW(), '徽章挂件', '吧唧徽章、钥匙扣、推し活周边', 0),
(NOW(), NOW(), '文具周边', '色纸、画集、明信片、办公文具', 0),
(NOW(), NOW(), '音像书籍', 'CD专辑、漫画、同人志、设定集', 0),
(NOW(), NOW(), '服饰配件', 'T恤、痛包、抱枕、刺绣布贴', 0),
-- 二级
(NOW(), NOW(), 'PVC手办', '1/7、景品、GK手办', 1),
(NOW(), NOW(), '亚克力立牌', 'Acrylic figure、立牌', 1),
(NOW(), NOW(), '黏土人/figma', 'Nendoroid、figma 可动', 1),
(NOW(), NOW(), '毛绒玩偶', 'ぬいぐるみ、玩偶', 2),
(NOW(), NOW(), '主题毛毯', '毛毯、抱毯', 2),
(NOW(), NOW(), '徽章', '吧唧、徽章套装', 3),
(NOW(), NOW(), '钥匙扣挂件', '亚克力挂件、钥匙扣', 3),
(NOW(), NOW(), '色纸明信片', '色纸、明信片、文件夹', 4),
(NOW(), NOW(), '画集设定集', '官方画集、设定资料集', 4),
(NOW(), NOW(), 'CD专辑', '角色歌、动画原声', 5),
(NOW(), NOW(), '漫画同人志', '漫画、同人志', 5),
(NOW(), NOW(), '服饰', 'T恤、卫衣', 6),
(NOW(), NOW(), '包袋抱枕', '痛包、斜挎包、抱枕', 6);

-- 2) 更新商品：二次元名称 / 描述 / 价格 / 库存 / 分类 / 图片
UPDATE ykd_ebusiness_product SET
  name = '初音未来 亚克力立牌「凝望你的你」',
  description = '初音ミク Acrylic figure 亚克力立牌，官方授权同人周边，透明亚克力材质，含底座。',
  price = 79.00, stock = 80,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a1-main.jpg"]', detail = '["/uploads/anime/a1-detail.jpg"]'
WHERE id = 1;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 MIKU LAND 收藏系列',
  description = '初音ミク MIKU LAND COLLECTION 系列周边，限定款收藏品。',
  price = 129.00, stock = 60,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a2-main.jpg"]', detail = '["/uploads/anime/a2-detail.jpg"]'
WHERE id = 2;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 官方画集《Stargazer & Lucid Dream》',
  description = '初音ミク 官方画集，收录人气画师精美插画，全彩印刷。',
  price = 108.00, stock = 40,
  category_ids = '[4,15]',
  images = '["/uploads/anime/a3-main.jpg"]', detail = '["/uploads/anime/a3-detail.jpg"]'
WHERE id = 3;

UPDATE ykd_ebusiness_product SET
  name = '新世纪福音战士 官方画集 Artbook',
  description = 'EVANGELION 官方画集，EVA 系列经典插画收录，EVA 粉丝必藏。',
  price = 128.00, stock = 35,
  category_ids = '[4,15]',
  images = '["/uploads/anime/a4-main.jpg"]', detail = '["/uploads/anime/a4-detail.jpg"]'
WHERE id = 4;

UPDATE ykd_ebusiness_product SET
  name = '明日方舟 曼提柯尔 亚克力立牌',
  description = 'Arknights Acrylic figure 亚克力立牌，罗德岛干员曼提柯尔，透明亚克力+底座。',
  price = 88.00, stock = 75,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a5-main.jpg"]', detail = '["/uploads/anime/a5-detail.jpg"]'
WHERE id = 5;

UPDATE ykd_ebusiness_product SET
  name = '原神 刻晴 角色立牌',
  description = 'Genshin Impact 刻晴角色主题立牌，璃月七星玉衡星，官方画师插图。',
  price = 69.00, stock = 90,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a6-main.jpg"]', detail = '["/uploads/anime/a6-detail.jpg"]'
WHERE id = 6;

UPDATE ykd_ebusiness_product SET
  name = '咒术回战 五条悟 气球兔 亚克力立牌',
  description = 'ふうせんうさぎ 五条悟 亚克力立牌，Q版可爱风，摆件收藏两相宜。',
  price = 75.00, stock = 70,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a7-main.jpg"]', detail = '["/uploads/anime/a7-detail.jpg"]'
WHERE id = 7;

UPDATE ykd_ebusiness_product SET
  name = '间谍过家家 阿尼亚 Idol 亚克力立牌',
  description = 'アーニャ・フォージャー Idol Acrylic，阿尼亚偶像造型立牌，Waku Waku！',
  price = 65.00, stock = 85,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a8-main.jpg"]', detail = '["/uploads/anime/a8-detail.jpg"]'
WHERE id = 8;

UPDATE ykd_ebusiness_product SET
  name = '孤独摇滚！后藤一里 同人周边套装',
  description = 'ぼっち・ざ・ろっく！后藤一里（小孤独）同人周边，社恐吉他手的日常。',
  price = 99.00, stock = 55,
  category_ids = '[5,17]',
  images = '["/uploads/anime/a9-main.jpg"]', detail = '["/uploads/anime/a9-detail.jpg"]'
WHERE id = 9;

UPDATE ykd_ebusiness_product SET
  name = '赛马娘 小栗帽 手办 Oguri Cap',
  description = 'ウマ娘 オグリキャップ 手办，赛马娘大逃げ传说，疾驰姿态生动还原。',
  price = 599.00, stock = 8,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a10-main.jpg"]', detail = '["/uploads/anime/a10-detail.jpg"]'
WHERE id = 10;

UPDATE ykd_ebusiness_product SET
  name = 'FGO 亚克力立牌（原创底座）',
  description = 'Fate/Grand Order 亚克力立牌，附原创底座，从者展示专用。',
  price = 95.00, stock = 65,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a11-main.jpg"]', detail = '["/uploads/anime/a11-detail.jpg"]'
WHERE id = 11;

UPDATE ykd_ebusiness_product SET
  name = '鬼灭之刃 炭治郎「頑張ります！」亚克力立牌',
  description = '炭治郎 Acrylic figure，经典台词立牌，全集中呼吸！',
  price = 58.00, stock = 100,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a12-main.jpg"]', detail = '["/uploads/anime/a12-detail.jpg"]'
WHERE id = 12;

UPDATE ykd_ebusiness_product SET
  name = '东方Project 博丽灵梦 手办',
  description = '博麗霊夢 Hakurei Reimu 手办，幻想乡的红白巫女，弹幕决斗的顶点。',
  price = 268.00, stock = 15,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a13-main.jpg"]', detail = '["/uploads/anime/a13-detail.jpg"]'
WHERE id = 13;

UPDATE ykd_ebusiness_product SET
  name = '碧蓝航线 1/7 乔治五世 手办',
  description = 'アズールレーン 1/7 スケールフィギュア，皇家海军战列舰拟人化，做工精细。',
  price = 899.00, stock = 5,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a14-main.jpg"]', detail = '["/uploads/anime/a14-detail.jpg"]'
WHERE id = 14;

UPDATE ykd_ebusiness_product SET
  name = '崩坏：星穹铁道 卡芙卡（兔女郎）亚克力立牌',
  description = '崩壊スターレイル カフカ バニーガール 亚克力立牌，星核猎手卡芙卡限定造型。',
  price = 92.00, stock = 72,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a15-main.jpg"]', detail = '["/uploads/anime/a15-detail.jpg"]'
WHERE id = 15;

UPDATE ykd_ebusiness_product SET
  name = '宝可梦 皮卡丘 主题毛毯',
  description = 'Pokemon 皮卡丘主题毛毯，动漫周边，柔软亲肤，沙发必备。',
  price = 159.00, stock = 45,
  category_ids = '[2,11]',
  images = '["/uploads/anime/a16-main.jpg"]', detail = '["/uploads/anime/a16-detail.jpg"]'
WHERE id = 16;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 PIAPRO 水引夹 文具周边',
  description = 'ピアプロキャラクターズ 水引クリップ，初音系文具周边，办公学习好物。',
  price = 35.00, stock = 120,
  category_ids = '[4,14]',
  images = '["/uploads/anime/a17-main.jpg"]', detail = '["/uploads/anime/a17-detail.jpg"]'
WHERE id = 17;

UPDATE ykd_ebusiness_product SET
  name = '女仆Q版 毛绒玩偶 ぬいぐるみ',
  description = 'メイドちびシリーズ ぬいぐるみ，Q版女仆毛绒玩偶，软萌手感。',
  price = 168.00, stock = 50,
  category_ids = '[2,10]',
  images = '["/uploads/anime/a18-main.jpg"]', detail = '["/uploads/anime/a18-detail.jpg"]'
WHERE id = 18;

UPDATE ykd_ebusiness_product SET
  name = '孤独摇滚！广井菊里 中国风亚克力立牌',
  description = '廣井きくり チャイナアクリルスタンド，贝斯手广井菊里旗袍造型立牌。',
  price = 82.00, stock = 68,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a19-main.jpg"]', detail = '["/uploads/anime/a19-detail.jpg"]'
WHERE id = 19;

UPDATE ykd_ebusiness_product SET
  name = '原神 主题亚克力立牌套装',
  description = 'Genshin Impact 主题亚克力立牌套装，多角色组合展示。',
  price = 85.00, stock = 88,
  category_ids = '[1,8]',
  images = '["/uploads/anime/a20-main.jpg"]', detail = '["/uploads/anime/a20-detail.jpg"]'
WHERE id = 20;

UPDATE ykd_ebusiness_product SET
  name = '咒术回战 主题徽章套装',
  description = '呪術廻戦 主题吧唧徽章，五条悟/虎杖悠仁人气角色。',
  price = 45.00, stock = 110,
  category_ids = '[3,12]',
  images = '["/uploads/anime/a21-main.jpg"]', detail = '["/uploads/anime/a21-detail.jpg"]'
WHERE id = 21;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 小徽章 ミクちゃんバッジ',
  description = '初音ミク 徽章吧唧，Q版初音小徽章，痛包必备。',
  price = 29.00, stock = 150,
  category_ids = '[3,12]',
  images = '["/uploads/anime/a22-main.jpg"]', detail = '["/uploads/anime/a22-detail.jpg"]'
WHERE id = 22;

UPDATE ykd_ebusiness_product SET
  name = '咒术回战 色纸 シキシマ',
  description = '呪術廻戦 色纸，官方插画色纸，签名板尺寸。',
  price = 39.00, stock = 95,
  category_ids = '[4,14]',
  images = '["/uploads/anime/a23-main.jpg"]', detail = '["/uploads/anime/a23-detail.jpg"]'
WHERE id = 23;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 刺绣布贴 迷你斜挎包',
  description = '刺繍ワッペン ミニサコッシュ MIKU，初音刺绣布贴迷你斜挎包，出门百搭。',
  price = 128.00, stock = 38,
  category_ids = '[6,19]',
  images = '["/uploads/anime/a24-main.jpg"]', detail = '["/uploads/anime/a24-detail.jpg"]'
WHERE id = 24;

UPDATE ykd_ebusiness_product SET
  name = '间谍过家家 阿尼亚 T恤 XL',
  description = 'Anya Forger T-shirt，阿尼亚大头印花T恤，Waku Waku 出街。',
  price = 119.00, stock = 42,
  category_ids = '[6,18]',
  images = '["/uploads/anime/a25-main.jpg"]', detail = '["/uploads/anime/a25-detail.jpg"]'
WHERE id = 25;

UPDATE ykd_ebusiness_product SET
  name = 'Lycoris Recoil 红色彼岸花 挂件',
  description = 'リコリス・リコイル 主题挂件，红色彼岸花元素，锦木千束同款风格。',
  price = 49.00, stock = 130,
  category_ids = '[3,13]',
  images = '["/uploads/anime/a26-main.jpg"]', detail = '["/uploads/anime/a26-detail.jpg"]'
WHERE id = 26;

UPDATE ykd_ebusiness_product SET
  name = '孤独摇滚！结束乐队 CD 专辑',
  description = 'Bocchi the Rock! 結束バンド CD，动画《孤独摇滚！》剧中乐队专辑。',
  price = 198.00, stock = 25,
  category_ids = '[5,16]',
  images = '["/uploads/anime/a27-main.jpg"]', detail = '["/uploads/anime/a27-detail.jpg"]'
WHERE id = 27;

UPDATE ykd_ebusiness_product SET
  name = '鬼灭之刃 同人志《恋春語り》',
  description = '鬼滅の刃 同人志，炭治郎主题同人本，全彩收录。',
  price = 65.00, stock = 60,
  category_ids = '[5,17]',
  images = '["/uploads/anime/a28-main.jpg"]', detail = '["/uploads/anime/a28-detail.jpg"]'
WHERE id = 28;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 Vampire 抱枕',
  description = 'Miku [Vampire] Dakimakura，初音吸血鬼主题抱枕，双面印刷。',
  price = 288.00, stock = 20,
  category_ids = '[6,19]',
  images = '["/uploads/anime/a29-main.jpg"]', detail = '["/uploads/anime/a29-detail.jpg"]'
WHERE id = 29;

UPDATE ykd_ebusiness_product SET
  name = '新世纪福音战士 式波·明日香 周边',
  description = '式波・アスカ・ラングレー EVA 周边，二号机驾驶员明日香主题商品。',
  price = 86.00, stock = 75,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a30-main.jpg"]', detail = '["/uploads/anime/a30-detail.jpg"]'
WHERE id = 30;

UPDATE ykd_ebusiness_product SET
  name = '东方Project 同人志《Help me, ERINNNNNN!!》',
  description = '東方Project 同人志，永夜抄 铃仙主题欢乐同人本。',
  price = 88.00, stock = 58,
  category_ids = '[5,17]',
  images = '["/uploads/anime/a31-main.jpg"]', detail = '["/uploads/anime/a31-detail.jpg"]'
WHERE id = 31;

UPDATE ykd_ebusiness_product SET
  name = '赛马娘 同人 应援徽章',
  description = 'ウマ娘 同人 推しピン，马娘应援徽章，配色对应人气马娘。',
  price = 42.00, stock = 140,
  category_ids = '[3,12]',
  images = '["/uploads/anime/a32-main.jpg"]', detail = '["/uploads/anime/a32-detail.jpg"]'
WHERE id = 32;

UPDATE ykd_ebusiness_product SET
  name = '初音未来 Little Diva Q版手办',
  description = 'Little Diva Miku 初音未来 Q版手办，小体态大可爱，桌面摆件首选。',
  price = 218.00, stock = 12,
  category_ids = '[1,7]',
  images = '["/uploads/anime/a33-main.jpg"]', detail = '["/uploads/anime/a33-detail.jpg"]'
WHERE id = 33;
