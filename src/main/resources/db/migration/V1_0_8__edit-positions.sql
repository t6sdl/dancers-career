START TRANSACTION;

-- positionsテーブルとsenior_positionsテーブルを更新
INSERT INTO positions SELECT '公演制作', email FROM positions WHERE position IN ('公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作') GROUP BY email ON DUPLICATE KEY UPDATE position = position, email = email;
INSERT INTO positions SELECT 'その他', email FROM positions WHERE position IN ('新歓係', '合宿統括') GROUP BY email ON DUPLICATE KEY UPDATE position = position, email = email;
DELETE FROM positions WHERE position IN ('公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作', '新歓係', '合宿統括');
INSERT INTO senior_positions SELECT '公演制作', exp_id FROM senior_positions WHERE position IN ('公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作') GROUP BY exp_id ON DUPLICATE KEY UPDATE position = position, exp_id = exp_id;
INSERT INTO senior_positions SELECT 'その他', exp_id FROM senior_positions WHERE position IN ('新歓係', '合宿統括') GROUP BY exp_id ON DUPLICATE KEY UPDATE position = position, exp_id = exp_id;
DELETE FROM senior_positions WHERE position IN ('公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作', '新歓係', '合宿統括');

-- profilesテーブルとexperiencesテーブルを更新
UPDATE profiles SET position = IFNULL((
  SELECT GROUP_CONCAT(position) FROM positions WHERE positions.email = profiles.email GROUP BY positions.email
), '');
UPDATE experiences SET position = IFNULL((
  SELECT GROUP_CONCAT(position) FROM senior_positions WHERE senior_positions.exp_id = experiences.id GROUP BY senior_positions.exp_id
), '');

COMMIT;