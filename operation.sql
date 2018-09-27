/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 operation 的数据库结构
CREATE DATABASE IF NOT EXISTS `operation` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `operation`;

-- 导出  表 operation.config_info 结构
CREATE TABLE IF NOT EXISTS `config_info` (
  `config_if` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL DEFAULT 0 COMMENT '服务id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `db_id` int(11) NOT NULL COMMENT '数据库连接信息',
  `table_name` varchar(50) NOT NULL DEFAULT '' COMMENT '表名',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `is_del` tinyint(1) NOT NULL DEFAULT 0,
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`config_if`),
  UNIQUE KEY `config_info_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 导出  表 operation.db_info 结构
CREATE TABLE IF NOT EXISTS `db_info` (
  `db_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '连接名称',
  `url` varchar(30) NOT NULL COMMENT '连接url',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `pool_size` int(11) NOT NULL DEFAULT 1 COMMENT '连接池大小',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `uesrname` varchar(30) NOT NULL DEFAULT '' COMMENT '用户名',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT 0,
  `is_del` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`db_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库信息';

-- 导出  表 operation.file 结构
CREATE TABLE IF NOT EXISTS `file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) NOT NULL COMMENT '文件名称',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `created_by` int(11) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `is_del` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `file_file_name_uindex` (`file_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传文件表';

-- 导出  表 operation.file_version 结构
CREATE TABLE IF NOT EXISTS `file_version` (
  `file_version_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) NOT NULL COMMENT '文件id',
  `version` int(11) NOT NULL COMMENT '版本',
  `update_log` varchar(200) NOT NULL DEFAULT '' COMMENT '更新日志',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_by` int(11) DEFAULT NULL COMMENT '上传人',
  `is_del` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`file_version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 导出  表 operation.log_field 结构
CREATE TABLE IF NOT EXISTS `log_field` (
  `fie_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NOT NULL COMMENT '日志id',
  `field_name` varchar(30) NOT NULL COMMENT '字段名称',
  `display` varchar(10) NOT NULL COMMENT '字段中文名称',
  `can_search` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否可搜索',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT 0,
  `is_del` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`fie_id`),
  UNIQUE KEY `log_field_log_id_field_name_uindex` (`log_id`,`field_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字段映射';

-- 导出  表 operation.log_info 结构
CREATE TABLE IF NOT EXISTS `log_info` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '名称',
  `log_path` varchar(50) NOT NULL DEFAULT '' COMMENT '日志路径',
  `db_id` int(11) NOT NULL DEFAULT 0 COMMENT '数据库id',
  `time_field` varchar(20) NOT NULL DEFAULT '' COMMENT '日志时间字段',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `sql` text NOT NULL COMMENT 'sql查询语句',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_by` int(11) NOT NULL DEFAULT 0,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`log_id`),
  UNIQUE KEY `log_info_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志信息';

-- 导出  表 operation.service 结构
CREATE TABLE IF NOT EXISTS `service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(50) NOT NULL COMMENT '服务名称',
  `file_id` int(11) NOT NULL COMMENT '文件id',
  `file_veriosn` int(11) NOT NULL COMMENT '文件版本号',
  `type` int(11) DEFAULT NULL COMMENT '服务类型',
  `start_cmd` varchar(200) DEFAULT NULL COMMENT '开始命令',
  `stop_cmd` varchar(200) DEFAULT NULL COMMENT '停止脚本',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '服务描述',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT 0,
  `is_del` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_service_name_uindex` (`service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务表';

-- 导出  表 operation.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `role` tinyint(4) DEFAULT NULL COMMENT '角色',
  `password` varchar(40) DEFAULT 'NULL' COMMENT '密码',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` int(11) NOT NULL DEFAULT 0,
  `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;