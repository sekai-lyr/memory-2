-- 按名称关键词 CASE 短路分类（アクリルフィギュア=立牌优先于フィギュア=手办）
UPDATE ykd_ebusiness_product SET category_ids = CASE
  WHEN name LIKE '%ねんどろ%' OR name LIKE '%nendoroid%' OR name LIKE '%figma%' THEN '[1,9]'
  WHEN name LIKE '%アクリル%' OR name LIKE '%スタンド%' OR name LIKE '%アクスタ%' OR name LIKE '%立牌%' OR name LIKE '%acrylic%' OR name LIKE '%stand%' THEN '[1,8]'
  WHEN name LIKE '%フィギュア%' OR name LIKE '%figure%' OR name LIKE '%PVC%' OR name LIKE '%プライズ%' OR name LIKE '%手办%' THEN '[1,7]'
  WHEN name LIKE '%ぬいぐるみ%' OR name LIKE '%ぬい%' OR name LIKE '%plush%' THEN '[2,10]'
  WHEN name LIKE '%ブランケット%' OR name LIKE '%毛毯%' OR name LIKE '%blanket%' THEN '[2,11]'
  WHEN name LIKE '%缶バッジ%' OR name LIKE '%バッジ%' OR name LIKE '%badge%' OR name LIKE '%徽章%' OR name LIKE '%吧唧%' OR name LIKE '%ピンズ%' THEN '[3,12]'
  WHEN name LIKE '%キーホルダー%' OR name LIKE '%チャーム%' OR name LIKE '%ストラップ%' OR name LIKE '%アクキー%' OR name LIKE '%挂件%' OR name LIKE '%keychain%' THEN '[3,13]'
  WHEN name LIKE '%色紙%' OR name LIKE '%色纸%' OR name LIKE '%shikishi%' OR name LIKE '%ポストカード%' OR name LIKE '%明信片%' OR name LIKE '%クリアファイル%' THEN '[4,14]'
  WHEN name LIKE '%画集%' OR name LIKE '%artbook%' OR name LIKE '%Artbook%' OR name LIKE '%設定集%' OR name LIKE '%イラスト集%' OR name LIKE '%アートブック%' THEN '[4,15]'
  WHEN name LIKE '%CD%' OR name LIKE '%アルバム%' OR name LIKE '%OST%' OR name LIKE '%サントラ%' OR name LIKE '%シングル%' THEN '[5,16]'
  WHEN name LIKE '%同人誌%' OR name LIKE '%コミック%' OR name LIKE '%漫画%' OR name LIKE '%manga%' OR name LIKE '%新刊%' OR name LIKE '%コピー本%' OR name LIKE '%小説%' THEN '[5,17]'
  WHEN name LIKE '%Tシャツ%' OR name LIKE '%シャツ%' OR name LIKE '%パーカー%' OR name LIKE '%shirt%' OR name LIKE '%トレーナー%' OR name LIKE '%スウェット%' THEN '[6,18]'
  WHEN name LIKE '%抱き枕%' OR name LIKE '%抱枕%' OR name LIKE '%クッション%' OR name LIKE '%バッグ%' OR name LIKE '%ポーチ%' OR name LIKE '%巾着%' OR name LIKE '%サコッシュ%' OR name LIKE '%トート%' OR name LIKE '%リュック%' OR name LIKE '%鞄%' OR name LIKE '%dakimakura%' OR name LIKE '%pillow%' THEN '[6,19]'
  ELSE '[1,8]'
END;
