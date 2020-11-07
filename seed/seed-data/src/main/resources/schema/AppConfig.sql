--Create Table gt_app_config
CREATE TABLE IF NOT EXISTS gt_app_config (
id BIGINT UNSIGNED NOT NULL COMMENT 'ID',
app_id BIGINT UNSIGNED NOT NULL COMMENT 'App id',
platform SMALLINT NOT NULL COMMENT 'Target platform',
target_version VARCHAR(255) NOT NULL COMMENT 'Target version',
target_channel VARCHAR(255) NOT NULL COMMENT 'Target channel',
latest_version VARCHAR(255) NOT NULL COMMENT 'Latest version',
force_upgrade TINYINT(1) NOT NULL COMMENT 'Force upgrade',
remark VARCHAR(255) COMMENT 'remark for column',
lock_version BIGINT UNSIGNED NOT NULL COMMENT 'Optimistic Concurrency Control version',
updated_time BIGINT UNSIGNED NOT NULL COMMENT 'last updated time of record',
created_time BIGINT UNSIGNED NOT NULL COMMENT 'Created time of record') COMMENT 'Basic App Config Item';
--Add Primary Key 
ALTER TABLE gt_app_config MODIFY id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT;
--Modify Primary Key 
ALTER TABLE gt_app_config AUTO_INCREMENT = 66;
