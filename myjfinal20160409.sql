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
# Server version 5.6.26

DROP DATABASE IF EXISTS `myjfinal`;
CREATE DATABASE `myjfinal` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `myjfinal`;

#
# Source for table biz_channel
#

DROP TABLE IF EXISTS `biz_channel`;
CREATE TABLE `biz_channel` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) DEFAULT NULL COMMENT '渠道名',
  `content` varchar(255) DEFAULT NULL COMMENT '说明',
  `rate` double(10,5) DEFAULT '0.00000' COMMENT '费率',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道表';

#
# Dumping data for table biz_channel
#

LOCK TABLES `biz_channel` WRITE;
/*!40000 ALTER TABLE `biz_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `biz_channel` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_group
#

DROP TABLE IF EXISTS `sec_group`;
CREATE TABLE `sec_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `intro` varchar(255) DEFAULT NULL COMMENT '简介',
  `theindex` int(11) DEFAULT '0' COMMENT '排序',
  `created_at` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `updated_at` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `deleted_at` varchar(30) DEFAULT NULL COMMENT '删除时间',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Dumping data for table sec_group
#

LOCK TABLES `sec_group` WRITE;
/*!40000 ALTER TABLE `sec_group` DISABLE KEYS */;
INSERT INTO `sec_group` VALUES (1,'内部人员','内部人员',0,'2016-03-15 10:43:52',NULL,NULL,1);
INSERT INTO `sec_group` VALUES (2,'渠道商','渠道商',0,'2016-03-15 10:44:40',NULL,NULL,1);
INSERT INTO `sec_group` VALUES (3,'广告主','广告主',0,'2016-03-15 10:45:27',NULL,NULL,1);
/*!40000 ALTER TABLE `sec_group` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_permission
#

DROP TABLE IF EXISTS `sec_permission`;
CREATE TABLE `sec_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL COMMENT '值',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `intro` varchar(255) DEFAULT NULL COMMENT '简介',
  `pid` int(11) DEFAULT '0' COMMENT '父级id',
  `left_code` int(11) DEFAULT '0' COMMENT '数据左边码',
  `right_code` int(11) DEFAULT '0' COMMENT '数据右边码',
  `created_at` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `updated_at` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `deleted_at` varchar(30) DEFAULT NULL COMMENT '删除时间',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='--权限表';

#
# Dumping data for table sec_permission
#

LOCK TABLES `sec_permission` WRITE;
/*!40000 ALTER TABLE `sec_permission` DISABLE KEYS */;
INSERT INTO `sec_permission` VALUES (1,'管理员目录','P_D_ADMIN','/admin/**','',0,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (2,'角色权限管理','P_ROLE','/admin/role/**','',1,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (3,'用户管理','P_USER','/admin/user/**','',1,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (4,'总部目录','P_D_MEMBER','/member/**','',0,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (5,'分部目录','P_D_USER','/user/**','',0,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (6,'用户处理','P_USER_CONTROL','/user/branch**','',5,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (7,'订单','P_ORDER','/order/**','',0,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (8,'订单处理','P_ORDER_CONTROL','/order/deliver**','',7,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (9,'订单更新','P_ORDER_UPDATE','/order/update**','',7,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (10,'支部订单','P_ORDER_BRANCH','/order/branch**','',7,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (11,'区域支行处理','P_REGION_CONTROL','/order/region**','',7,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
INSERT INTO `sec_permission` VALUES (12,'收货地址','P_Address','/address/**','',0,0,0,'2016-03-11 10:56:33',NULL,NULL,0);
/*!40000 ALTER TABLE `sec_permission` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_role
#

DROP TABLE IF EXISTS `sec_role`;
CREATE TABLE `sec_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL COMMENT '值',
  `intro` varchar(255) DEFAULT NULL COMMENT '简介',
  `pid` int(11) DEFAULT '0' COMMENT '父级id',
  `left_code` int(11) DEFAULT '0' COMMENT '数据左边码',
  `right_code` int(11) DEFAULT '0' COMMENT '数据右边码',
  `created_at` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `updated_at` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `deleted_at` varchar(30) DEFAULT NULL COMMENT '删除时间',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色表';

#
# Dumping data for table sec_role
#

LOCK TABLES `sec_role` WRITE;
/*!40000 ALTER TABLE `sec_role` DISABLE KEYS */;
INSERT INTO `sec_role` VALUES (1,'超级管理员','R_ADMIN','',0,0,0,'2016-03-11 09:37:58',NULL,NULL,0);
INSERT INTO `sec_role` VALUES (2,'系统管理员','R_MANAGER','',1,0,0,'2016-03-11 09:37:58',NULL,NULL,0);
INSERT INTO `sec_role` VALUES (3,'总部','R_MEMBER','',2,0,0,'2016-03-11 09:37:58',NULL,NULL,0);
INSERT INTO `sec_role` VALUES (4,'分部','R_USER','',2,0,0,'2016-03-11 09:37:58',NULL,NULL,0);
/*!40000 ALTER TABLE `sec_role` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_role_permission
#

DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='--角色权限关系表';

#
# Dumping data for table sec_role_permission
#

LOCK TABLES `sec_role_permission` WRITE;
/*!40000 ALTER TABLE `sec_role_permission` DISABLE KEYS */;
INSERT INTO `sec_role_permission` VALUES (1,1,1);
INSERT INTO `sec_role_permission` VALUES (2,1,2);
INSERT INTO `sec_role_permission` VALUES (3,1,3);
INSERT INTO `sec_role_permission` VALUES (4,1,4);
INSERT INTO `sec_role_permission` VALUES (5,1,5);
INSERT INTO `sec_role_permission` VALUES (6,1,6);
INSERT INTO `sec_role_permission` VALUES (7,1,7);
INSERT INTO `sec_role_permission` VALUES (8,1,8);
INSERT INTO `sec_role_permission` VALUES (9,1,9);
INSERT INTO `sec_role_permission` VALUES (10,1,10);
INSERT INTO `sec_role_permission` VALUES (11,1,11);
INSERT INTO `sec_role_permission` VALUES (12,1,12);
INSERT INTO `sec_role_permission` VALUES (13,2,1);
INSERT INTO `sec_role_permission` VALUES (14,2,3);
INSERT INTO `sec_role_permission` VALUES (15,2,4);
INSERT INTO `sec_role_permission` VALUES (16,2,5);
INSERT INTO `sec_role_permission` VALUES (17,2,6);
INSERT INTO `sec_role_permission` VALUES (18,2,7);
INSERT INTO `sec_role_permission` VALUES (19,2,8);
INSERT INTO `sec_role_permission` VALUES (20,2,9);
INSERT INTO `sec_role_permission` VALUES (21,2,10);
INSERT INTO `sec_role_permission` VALUES (22,2,11);
INSERT INTO `sec_role_permission` VALUES (23,2,12);
INSERT INTO `sec_role_permission` VALUES (24,3,4);
INSERT INTO `sec_role_permission` VALUES (25,3,5);
INSERT INTO `sec_role_permission` VALUES (26,3,6);
INSERT INTO `sec_role_permission` VALUES (27,3,11);
INSERT INTO `sec_role_permission` VALUES (54,4,7);
INSERT INTO `sec_role_permission` VALUES (55,4,9);
INSERT INTO `sec_role_permission` VALUES (56,4,12);
INSERT INTO `sec_role_permission` VALUES (57,4,11);
INSERT INTO `sec_role_permission` VALUES (58,4,10);
INSERT INTO `sec_role_permission` VALUES (59,4,1);
INSERT INTO `sec_role_permission` VALUES (60,4,2);
INSERT INTO `sec_role_permission` VALUES (61,4,3);
INSERT INTO `sec_role_permission` VALUES (62,4,4);
INSERT INTO `sec_role_permission` VALUES (63,4,6);
/*!40000 ALTER TABLE `sec_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_user
#

DROP TABLE IF EXISTS `sec_user`;
CREATE TABLE `sec_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '登录名',
  `providername` varchar(50) DEFAULT NULL COMMENT '提供者',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `hasher` varchar(200) DEFAULT NULL COMMENT '加密类型',
  `salt` varchar(200) DEFAULT NULL COMMENT '加密盐',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `first_name` varchar(10) DEFAULT NULL COMMENT '名字',
  `last_name` varchar(10) DEFAULT NULL COMMENT '姓氏',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `department_id` int(11) DEFAULT '0' COMMENT '部门id',
  `group_id` int(11) DEFAULT '0' COMMENT '分组id',
  `created_at` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `updated_at` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `deleted_at` varchar(30) DEFAULT NULL COMMENT '删除时间',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Dumping data for table sec_user
#

LOCK TABLES `sec_user` WRITE;
/*!40000 ALTER TABLE `sec_user` DISABLE KEYS */;
INSERT INTO `sec_user` VALUES (1,'admin','shengmu','wangrenhui1990@gmail.com','15611434500','admin','default_hasher','','','管理员','圣牧','圣牧.管理员',1,1,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (2,'asd','asd','asd','asd','asd','asd','asd','asd','ad','asd','asd',2,2,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (3,'ewe','ee','e','e','ewr','we','wer',NULL,NULL,NULL,NULL,1,1,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (4,'dsf','sdf','sdf','s','df','sdf','sdf',NULL,NULL,NULL,'sdf',1,2,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (5,'yutyu','tyu','tyu@qq.com','tyu','zcj123zcj123','','',NULL,NULL,NULL,'tyu',1,3,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (6,'hgj','dfg','zcj123zcj123@qq.com','dfg','zcj123zcj123','','',NULL,NULL,NULL,'zcj123zcj123',1,3,'2016-03-11 10:58:26',NULL,NULL,1);
INSERT INTO `sec_user` VALUES (7,'sdf','sdf','zcj123@11.com','sdf','zcj123','','',NULL,NULL,NULL,'zcj123',0,2,'2016-03-11 10:58:26',NULL,NULL,0);
INSERT INTO `sec_user` VALUES (8,'cpcyy',NULL,NULL,NULL,'111111',NULL,NULL,NULL,NULL,NULL,'cpcyy',0,1,'2016-03-14 10:01:31',NULL,NULL,1);
/*!40000 ALTER TABLE `sec_user` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_user_info
#

DROP TABLE IF EXISTS `sec_user_info`;
CREATE TABLE `sec_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `creator_id` int(11) DEFAULT '0' COMMENT '创建者id',
  `gender` int(11) DEFAULT '0' COMMENT '性别0男，1女',
  `province_id` int(11) DEFAULT '0' COMMENT '省id',
  `city_id` int(11) DEFAULT '0' COMMENT '市id',
  `county_id` int(11) DEFAULT '0' COMMENT '县id',
  `street` varchar(500) DEFAULT NULL COMMENT '街道',
  `zip_code` varchar(50) DEFAULT NULL COMMENT '邮编',
  `created_at` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `updated_at` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `deleted_at` varchar(30) DEFAULT NULL COMMENT '删除时间',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户详细信息表';

#
# Dumping data for table sec_user_info
#

LOCK TABLES `sec_user_info` WRITE;
/*!40000 ALTER TABLE `sec_user_info` DISABLE KEYS */;
INSERT INTO `sec_user_info` VALUES (1,1,0,0,1,2,3,'人民大学',NULL,'2016-03-11 10:58:30',NULL,NULL,0);
/*!40000 ALTER TABLE `sec_user_info` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sec_user_role
#

DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

#
# Dumping data for table sec_user_role
#

LOCK TABLES `sec_user_role` WRITE;
/*!40000 ALTER TABLE `sec_user_role` DISABLE KEYS */;
INSERT INTO `sec_user_role` VALUES (1,1,1);
INSERT INTO `sec_user_role` VALUES (2,1,2);
INSERT INTO `sec_user_role` VALUES (3,7,1);
INSERT INTO `sec_user_role` VALUES (4,6,1);
/*!40000 ALTER TABLE `sec_user_role` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
