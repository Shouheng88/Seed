--Create Table gt_app_config_detail
CREATE TABLE IF NOT EXISTS gt_app_config_detail (
id BIGINT UNSIGNED NOT NULL COMMENT 'ID',
app_config_id BIGINT UNSIGNED NOT NULL COMMENT 'Config id',
value_name VARCHAR(255) COMMENT 'Config detail item name',
config_value VARCHAR(255) NOT NULL COMMENT 'Config detail item value',
remark VARCHAR(255) COMMENT 'remark for column',
lock_version BIGINT UNSIGNED NOT NULL COMMENT 'Optimistic Concurrency Control version',
updated_time BIGINT UNSIGNED NOT NULL COMMENT 'last updated time of record',
created_time BIGINT UNSIGNED NOT NULL COMMENT 'Created time of record') COMMENT 'App Config Details';
--Add Primary Key 
ALTER TABLE gt_app_config_detail MODIFY id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT;
--Modify Primary Key 
ALTER TABLE gt_app_config_detail AUTO_INCREMENT = 66;
