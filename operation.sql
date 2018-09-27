

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 operation 的数据库结构
DROP DATABASE IF EXISTS `operation`;
CREATE DATABASE IF NOT EXISTS `operation` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `operation`;

-- 导出  表 operation.file 结构
DROP TABLE IF EXISTS `file`;
CREATE TABLE IF NOT EXISTS `file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) NOT NULL COMMENT '文件名称',
  `version` int(11) NOT NULL COMMENT '版本号',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '创建人',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '创建日期',
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `file_file_name_uindex` (`file_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传文件表';

-- 导出  表 operation.service 结构
DROP TABLE IF EXISTS `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(50) NOT NULL COMMENT '服务名称',
  `file_id` int(11) NOT NULL COMMENT '文件id',
  `file_veriosn` int(11) NOT NULL COMMENT '文件版本号',
  `type` int(11) DEFAULT NULL COMMENT '服务类型',
  `start_cmd` varchar(200) DEFAULT NULL COMMENT '开始命令',
  `stop_cmd` varchar(200) DEFAULT NULL COMMENT '停止脚本',
  `created_by` int(11) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL,
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '服务描述',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_service_name_uindex` (`service_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务表';

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `role` tinyint(4) DEFAULT NULL COMMENT '角色',
  `password` varchar(40) DEFAULT 'NULL' COMMENT '密码',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
