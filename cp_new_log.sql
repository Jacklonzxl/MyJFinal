# MySQL-Front 5.1  (Build 4.2)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: myjfinal
# ------------------------------------------------------
# Server version 5.6.24-ndb-7.4.6-cluster-gpl

USE `myjfinal`;

#
# Source for table cp_new_log
#

DROP TABLE IF EXISTS `cp_new_log`;
CREATE TABLE `cp_new_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpid` int(11) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `packname` varchar(60) DEFAULT NULL,
  `channel` varchar(30) DEFAULT NULL,
  `ip` varchar(30) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `prov` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `deviceinfo` varchar(255) DEFAULT NULL COMMENT '设备信息',
  `logincnt` int(11) DEFAULT NULL COMMENT '登录次数',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(30) DEFAULT NULL COMMENT '密码',
  `status` int(11) DEFAULT NULL,
  `adddate` varchar(30) DEFAULT NULL COMMENT '新增时间',
  `lastdate` varchar(30) DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新增cp表';

#
# Dumping data for table cp_new_log
#

LOCK TABLES `cp_new_log` WRITE;
/*!40000 ALTER TABLE `cp_new_log` DISABLE KEYS */;
INSERT INTO `cp_new_log` VALUES (1,1,NULL,NULL,'1','172.17.108.13.xml',NULL,NULL,NULL,'files\\admin\\2016\\04\\21\\13727529896-0dvfv4uy0p-172.17.108.13.xml',NULL,'13727529896','0dvfv4uy0p',1,'2016-04-21 16:57:51','2016-04-21 16:57:51');
INSERT INTO `cp_new_log` VALUES (2,1,NULL,'com.pinganfang.haofang','1','172.17.108.13.xml',NULL,NULL,NULL,'files\\admin\\2016\\04\\21\\13727529896-0dvfv4uy0p-172.17.108.13.xml',NULL,'13727529896','0dvfv4uy0p',1,'2016-04-21 16:59:41','2016-04-21 16:59:41');
INSERT INTO `cp_new_log` VALUES (3,1,NULL,'com.pinganfang.haofang','1','172.17.108.13',NULL,NULL,NULL,'files\\admin\\2016\\04\\21\\13727529896-0dvfv4uy0p-172.17.108.13.xml',NULL,'13727529896','0dvfv4uy0p',1,'2016-04-21 17:01:09','2016-04-21 17:01:09');
INSERT INTO `cp_new_log` VALUES (4,1,NULL,'com.pinganfang.haofang','1','172.17.108.13',NULL,NULL,NULL,'files\\admin\\2016\\04\\21\\13727529896-0dvfv4uy0p-172.17.108.13.xml',1,'13727529896','0dvfv4uy0p',1,'2016-04-21 17:02:16','2016-04-21 17:02:16');
/*!40000 ALTER TABLE `cp_new_log` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
