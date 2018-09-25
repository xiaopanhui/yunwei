CREATE TABLE `user` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户名',
  `role` TINYINT(4) NULL DEFAULT NULL COMMENT '角色',
  `password` VARCHAR(20) NULL DEFAULT NULL COMMENT '密码',
  `created_at` TIMESTAMP NULL DEFAULT '',
  `updated_at` TIMESTAMP NULL DEFAULT '',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_name` (`user_name`)
)
  COMMENT='用户表'
  COLLATE='utf8_general_ci'
  ENGINE=InnoDB
;
