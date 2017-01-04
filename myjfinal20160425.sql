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
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) DEFAULT NULL COMMENT '渠道名',
  `prov` varchar(255) DEFAULT '' COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `address` varchar(255) DEFAULT '' COMMENT '联系地址',
  `content` varchar(255) DEFAULT NULL COMMENT '说明',
  `rate` double(10,5) DEFAULT '0.00000' COMMENT '费率',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='渠道表';

#
# Dumping data for table biz_channel
#

LOCK TABLES `biz_channel` WRITE;
/*!40000 ALTER TABLE `biz_channel` DISABLE KEYS */;
INSERT INTO `biz_channel` VALUES (2,7,'测试渠道','广东','深圳','南山区','好玩的',0,1,'2016-04-09 23:41:08');
/*!40000 ALTER TABLE `biz_channel` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_sms
#

DROP TABLE IF EXISTS `biz_channel_sms`;
CREATE TABLE `biz_channel_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `advertiser` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `channelid` int(11) DEFAULT NULL,
  `geturl` varchar(1000) DEFAULT NULL COMMENT '发送号码地址',
  `posturl` varchar(1000) DEFAULT NULL COMMENT '发送手机号+验证码地址',
  `maxcnt` int(11) DEFAULT NULL,
  `daycnt` int(11) DEFAULT NULL,
  `theindex` int(11) DEFAULT '0',
  `status` int(11) DEFAULT '0',
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='短信接口表';

#
# Dumping data for table biz_channel_sms
#

LOCK TABLES `biz_channel_sms` WRITE;
/*!40000 ALTER TABLE `biz_channel_sms` DISABLE KEYS */;
INSERT INTO `biz_channel_sms` VALUES (1,'接口名称 ','advertisers',NULL,2,'http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms',NULL,1000,NULL,1,'2016-04-10 12:06:02');
/*!40000 ALTER TABLE `biz_channel_sms` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_sms_cnt
#

DROP TABLE IF EXISTS `biz_channel_sms_cnt`;
CREATE TABLE `biz_channel_sms_cnt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channelid` int(11) DEFAULT NULL COMMENT '渠道id',
  `urlid` int(11) DEFAULT NULL COMMENT '链接id',
  `cnt` int(11) DEFAULT NULL COMMENT '数量',
  `thedate` varchar(30) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='渠道链接统计表';

#
# Dumping data for table biz_channel_sms_cnt
#

LOCK TABLES `biz_channel_sms_cnt` WRITE;
/*!40000 ALTER TABLE `biz_channel_sms_cnt` DISABLE KEYS */;
/*!40000 ALTER TABLE `biz_channel_sms_cnt` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_sms_log
#

DROP TABLE IF EXISTS `biz_channel_sms_log`;
CREATE TABLE `biz_channel_sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fr` varchar(1000) DEFAULT NULL COMMENT '来源',
  `dest` varchar(1000) DEFAULT NULL COMMENT '目标',
  `ua` varchar(1000) DEFAULT NULL COMMENT 'ua',
  `rf` varchar(1000) DEFAULT NULL COMMENT '上一个页面',
  `channelid` int(11) DEFAULT NULL,
  `urlid` int(11) DEFAULT NULL COMMENT '链接id',
  `ip` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `prov` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='渠道链接访问日志';

#
# Dumping data for table biz_channel_sms_log
#

LOCK TABLES `biz_channel_sms_log` WRITE;
/*!40000 ALTER TABLE `biz_channel_sms_log` DISABLE KEYS */;
INSERT INTO `biz_channel_sms_log` VALUES (1,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 15:58:03');
INSERT INTO `biz_channel_sms_log` VALUES (2,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 15:59:59');
INSERT INTO `biz_channel_sms_log` VALUES (3,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 16:47:09');
INSERT INTO `biz_channel_sms_log` VALUES (4,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 16:47:10');
INSERT INTO `biz_channel_sms_log` VALUES (5,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 17:06:58');
INSERT INTO `biz_channel_sms_log` VALUES (6,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 17:06:59');
INSERT INTO `biz_channel_sms_log` VALUES (7,'http://127.0.0.1/req/2?phone=13378485310',NULL,'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 17:07:39');
INSERT INTO `biz_channel_sms_log` VALUES (8,'http://127.0.0.1/req/2?phone=13378485310','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,1,'127.0.0.1',NULL,NULL,'2016-04-10 17:10:44');
INSERT INTO `biz_channel_sms_log` VALUES (9,'http://127.0.0.1/req/2?phone=13378485310','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,1,'127.0.0.1',NULL,NULL,'2016-04-10 17:11:04');
INSERT INTO `biz_channel_sms_log` VALUES (10,'http://127.0.0.1/req/2?phone=13378485310','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,1,'127.0.0.1',NULL,NULL,'2016-04-10 17:11:13');
INSERT INTO `biz_channel_sms_log` VALUES (11,'http://127.0.0.1/req/2?phone=13378485310','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,1,'127.0.0.1',NULL,NULL,'2016-04-10 17:13:17');
/*!40000 ALTER TABLE `biz_channel_sms_log` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_sms_req
#

DROP TABLE IF EXISTS `biz_channel_sms_req`;
CREATE TABLE `biz_channel_sms_req` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channelid` int(11) DEFAULT NULL COMMENT '渠道id',
  `smsid` int(11) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL COMMENT '号码',
  `geturl` varchar(1000) DEFAULT NULL,
  `posturl` varchar(1000) DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `vcode` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态:0=未处理,1=已处理',
  `created_at` varchar(30) DEFAULT NULL,
  `update_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='请求号码表';

#
# Dumping data for table biz_channel_sms_req
#

LOCK TABLES `biz_channel_sms_req` WRITE;
/*!40000 ALTER TABLE `biz_channel_sms_req` DISABLE KEYS */;
INSERT INTO `biz_channel_sms_req` VALUES (1,2,NULL,'13378485310','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms','http://127.0.0.1/admin/?pg=biz/channelSms&m1=m_biz&m2=m_biz_channelSms&path=/biz/channelSms',NULL,'321',10,'2016-04-10 17:13:17','2016-04-10 23:56:02');
/*!40000 ALTER TABLE `biz_channel_sms_req` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_url
#

DROP TABLE IF EXISTS `biz_channel_url`;
CREATE TABLE `biz_channel_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channelid` int(11) DEFAULT NULL COMMENT '渠道id',
  `adid` int(11) DEFAULT '0',
  `advertisers` varchar(255) DEFAULT NULL COMMENT '广告主',
  `name` varchar(255) DEFAULT NULL COMMENT '链接名称',
  `content` varchar(255) DEFAULT NULL COMMENT '说明',
  `url` varchar(1000) DEFAULT NULL COMMENT '链接地址',
  `maxcnt` int(11) DEFAULT '0' COMMENT '最大投放量',
  `daycnt` int(11) DEFAULT '0' COMMENT '日投放量',
  `theindex` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='渠道链接表';

#
# Dumping data for table biz_channel_url
#

LOCK TABLES `biz_channel_url` WRITE;
/*!40000 ALTER TABLE `biz_channel_url` DISABLE KEYS */;
INSERT INTO `biz_channel_url` VALUES (3,2,0,'baidu','baidu','1000','http://m.baidu.com',0,1000,1,1,'2016-04-10 11:04:31');
INSERT INTO `biz_channel_url` VALUES (4,2,0,'qq','qq','1000','http://m.qq.com',0,1000,0,1,'2016-04-11 00:17:51');
/*!40000 ALTER TABLE `biz_channel_url` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_url_cnt
#

DROP TABLE IF EXISTS `biz_channel_url_cnt`;
CREATE TABLE `biz_channel_url_cnt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channelid` int(11) DEFAULT NULL COMMENT '渠道id',
  `urlid` int(11) DEFAULT NULL COMMENT '链接id',
  `cnt` int(11) DEFAULT NULL COMMENT '数量',
  `thedate` varchar(30) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='渠道链接统计表';

#
# Dumping data for table biz_channel_url_cnt
#

LOCK TABLES `biz_channel_url_cnt` WRITE;
/*!40000 ALTER TABLE `biz_channel_url_cnt` DISABLE KEYS */;
INSERT INTO `biz_channel_url_cnt` VALUES (1,2,4,10,'2016-04-11');
INSERT INTO `biz_channel_url_cnt` VALUES (2,2,3,8,'2016-04-11');
INSERT INTO `biz_channel_url_cnt` VALUES (3,2,3,1,'2016-04-24');
/*!40000 ALTER TABLE `biz_channel_url_cnt` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table biz_channel_url_log
#

DROP TABLE IF EXISTS `biz_channel_url_log`;
CREATE TABLE `biz_channel_url_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fr` varchar(1000) DEFAULT NULL COMMENT '来源',
  `dest` varchar(1000) DEFAULT NULL COMMENT '目标',
  `ua` varchar(1000) DEFAULT NULL COMMENT 'ua',
  `rf` varchar(1000) DEFAULT NULL COMMENT '上一个页面',
  `channelid` int(11) DEFAULT NULL,
  `urlid` int(11) DEFAULT NULL COMMENT '链接id',
  `ip` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `prov` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='渠道链接访问日志';

#
# Dumping data for table biz_channel_url_log
#

LOCK TABLES `biz_channel_url_log` WRITE;
/*!40000 ALTER TABLE `biz_channel_url_log` DISABLE KEYS */;
INSERT INTO `biz_channel_url_log` VALUES (1,'http://127.0.0.1:80/','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 14:15:31');
INSERT INTO `biz_channel_url_log` VALUES (2,'http://127.0.0.1/go/2','http://www.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 14:23:11');
INSERT INTO `biz_channel_url_log` VALUES (3,'http://127.0.0.1/go/2?id=3234234','http://www.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,0,'127.0.0.1',NULL,NULL,'2016-04-10 14:23:39');
INSERT INTO `biz_channel_url_log` VALUES (4,'http://127.0.0.1/go/3','http://www.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,3,0,'127.0.0.1',NULL,NULL,'2016-04-10 14:25:51');
INSERT INTO `biz_channel_url_log` VALUES (5,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-10 14:26:39');
INSERT INTO `biz_channel_url_log` VALUES (6,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:28');
INSERT INTO `biz_channel_url_log` VALUES (7,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:37');
INSERT INTO `biz_channel_url_log` VALUES (8,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:39');
INSERT INTO `biz_channel_url_log` VALUES (9,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:41');
INSERT INTO `biz_channel_url_log` VALUES (10,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:44');
INSERT INTO `biz_channel_url_log` VALUES (11,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:44');
INSERT INTO `biz_channel_url_log` VALUES (12,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:46');
INSERT INTO `biz_channel_url_log` VALUES (13,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:46');
INSERT INTO `biz_channel_url_log` VALUES (14,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:48');
INSERT INTO `biz_channel_url_log` VALUES (15,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:48');
INSERT INTO `biz_channel_url_log` VALUES (16,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:50');
INSERT INTO `biz_channel_url_log` VALUES (17,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:53');
INSERT INTO `biz_channel_url_log` VALUES (18,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:53');
INSERT INTO `biz_channel_url_log` VALUES (19,'http://127.0.0.1/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:18:55');
INSERT INTO `biz_channel_url_log` VALUES (20,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:21:41');
INSERT INTO `biz_channel_url_log` VALUES (21,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:21:53');
INSERT INTO `biz_channel_url_log` VALUES (22,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:21:56');
INSERT INTO `biz_channel_url_log` VALUES (23,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:22:00');
INSERT INTO `biz_channel_url_log` VALUES (24,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:22:02');
INSERT INTO `biz_channel_url_log` VALUES (25,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:22:11');
INSERT INTO `biz_channel_url_log` VALUES (26,'http://127.0.0.1/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:22:14');
INSERT INTO `biz_channel_url_log` VALUES (27,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:02');
INSERT INTO `biz_channel_url_log` VALUES (28,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:14');
INSERT INTO `biz_channel_url_log` VALUES (29,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:21');
INSERT INTO `biz_channel_url_log` VALUES (30,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:24');
INSERT INTO `biz_channel_url_log` VALUES (31,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:24');
INSERT INTO `biz_channel_url_log` VALUES (32,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:31');
INSERT INTO `biz_channel_url_log` VALUES (33,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:31');
INSERT INTO `biz_channel_url_log` VALUES (34,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:34');
INSERT INTO `biz_channel_url_log` VALUES (35,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:34');
INSERT INTO `biz_channel_url_log` VALUES (36,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:36');
INSERT INTO `biz_channel_url_log` VALUES (37,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:38');
INSERT INTO `biz_channel_url_log` VALUES (38,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:38');
INSERT INTO `biz_channel_url_log` VALUES (39,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:42');
INSERT INTO `biz_channel_url_log` VALUES (40,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:44');
INSERT INTO `biz_channel_url_log` VALUES (41,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:44');
INSERT INTO `biz_channel_url_log` VALUES (42,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:47');
INSERT INTO `biz_channel_url_log` VALUES (43,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:50');
INSERT INTO `biz_channel_url_log` VALUES (44,'http://127.0.0.1/go/2','http://m.qq.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,4,'127.0.0.1',NULL,NULL,'2016-04-11 00:25:50');
INSERT INTO `biz_channel_url_log` VALUES (45,'http://127.0.0.1/go/2','http://m.baidu.com','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36',NULL,2,3,'127.0.0.1',NULL,NULL,'2016-04-24 19:11:56');
/*!40000 ALTER TABLE `biz_channel_url_log` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table cp_info
#

DROP TABLE IF EXISTS `cp_info`;
CREATE TABLE `cp_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `packname` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL COMMENT '资源提供方',
  `price` double(10,3) DEFAULT NULL COMMENT '单价',
  `month_retention` varchar(255) DEFAULT NULL COMMENT '月留存要求',
  `week_retention` varchar(255) DEFAULT NULL COMMENT '周留存要求',
  `quarter_retention` int(11) DEFAULT NULL COMMENT '季度留存要求',
  `halfyear_retention` int(11) DEFAULT NULL COMMENT '半年留存',
  `year_retention` int(11) DEFAULT NULL COMMENT '年度留存',
  `url` varchar(500) DEFAULT NULL COMMENT '地址',
  `content` varchar(1000) DEFAULT NULL COMMENT '说明',
  `created_at` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='cp信息表';

#
# Dumping data for table cp_info
#

LOCK TABLES `cp_info` WRITE;
/*!40000 ALTER TABLE `cp_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_info` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='新增cp表';

#
# Dumping data for table cp_new_log
#

LOCK TABLES `cp_new_log` WRITE;
/*!40000 ALTER TABLE `cp_new_log` DISABLE KEYS */;
INSERT INTO `cp_new_log` VALUES (4,1,NULL,'com.pinganfang.haofang','1','172.17.108.13',NULL,NULL,NULL,'files/admin/2016/04/21/13727529896-0dvfv4uy0p-172.17.108.13.xml',1,'13727529896','0dvfv4uy0p',1,'2016-04-21 17:02:16','2016-04-21 17:02:16');
/*!40000 ALTER TABLE `cp_new_log` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table cp_task
#

DROP TABLE IF EXISTS `cp_task`;
CREATE TABLE `cp_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpid` int(11) DEFAULT NULL COMMENT 'cp名',
  `name` varchar(255) DEFAULT NULL COMMENT '任务名',
  `type` int(11) DEFAULT NULL COMMENT '类型:1=新增,2=留存',
  `cnt` int(11) DEFAULT NULL COMMENT '量级',
  `daycnt` varchar(255) DEFAULT NULL COMMENT '每天推送',
  `timeslot` varchar(255) DEFAULT NULL COMMENT '时间段',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `created_at` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';

#
# Dumping data for table cp_task
#

LOCK TABLES `cp_task` WRITE;
/*!40000 ALTER TABLE `cp_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_task` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table cp_task_cnt
#

DROP TABLE IF EXISTS `cp_task_cnt`;
CREATE TABLE `cp_task_cnt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channelid` int(11) DEFAULT NULL COMMENT '渠道id',
  `cpid` int(11) DEFAULT NULL COMMENT 'cpid',
  `cnt` int(11) DEFAULT NULL COMMENT '数量',
  `thedate` varchar(30) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='渠道cp统计表';

#
# Dumping data for table cp_task_cnt
#

LOCK TABLES `cp_task_cnt` WRITE;
/*!40000 ALTER TABLE `cp_task_cnt` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_task_cnt` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table cp_task_log
#

DROP TABLE IF EXISTS `cp_task_log`;
CREATE TABLE `cp_task_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imei` varchar(255) DEFAULT NULL,
  `android_id` varchar(255) DEFAULT NULL COMMENT '安卓id',
  `mac` varchar(255) DEFAULT NULL COMMENT 'wifi 的mac地址',
  `ssid` varchar(255) DEFAULT NULL COMMENT 'wifi的ssid',
  `phone` varchar(30) DEFAULT NULL COMMENT '手机号',
  `width` varchar(255) DEFAULT NULL COMMENT '分辨率',
  `hight` varchar(255) DEFAULT NULL COMMENT '分辨率',
  `simid` varchar(255) DEFAULT NULL COMMENT 'sim序列号',
  `imsi` varchar(50) DEFAULT NULL,
  `simstate` varchar(255) DEFAULT '5' COMMENT 'sim卡状态',
  `simoperator` varchar(30) DEFAULT NULL COMMENT '运营商id',
  `simoperatorname` varchar(255) DEFAULT NULL COMMENT '运营商名称',
  `simcountryiso` varchar(30) DEFAULT 'cn' COMMENT '国家编码',
  `brand` varchar(255) DEFAULT NULL COMMENT '品牌',
  `model` varchar(50) DEFAULT NULL COMMENT '机型',
  `manufacturer` varchar(255) DEFAULT NULL COMMENT '厂商',
  `hardware` varchar(255) DEFAULT NULL COMMENT '硬件',
  `radioversion` varchar(255) DEFAULT NULL COMMENT '基带版本',
  `sdk` varchar(255) DEFAULT NULL COMMENT 'android版本等级',
  `release` varchar(255) DEFAULT NULL COMMENT 'android版本名称',
  `fingerprint` varchar(255) DEFAULT NULL,
  `board` varchar(255) DEFAULT NULL,
  `device` varchar(255) DEFAULT NULL,
  `cpu_abi` varchar(255) DEFAULT NULL,
  `cpu_adi2` varchar(255) DEFAULT NULL,
  `product` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `display` varchar(255) DEFAULT NULL,
  `buildid` varchar(255) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL COMMENT 'cpid',
  `regphone` varchar(255) DEFAULT NULL COMMENT '注册手机',
  `pwd` varchar(255) DEFAULT NULL COMMENT '密码',
  `vcode` varchar(30) DEFAULT NULL COMMENT '验证码',
  `logincnt` int(11) DEFAULT NULL COMMENT '登录次数',
  `created_at` varchar(30) DEFAULT NULL,
  `updated_at` varchar(30) DEFAULT NULL COMMENT '最后登录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='手机设备信息表';

#
# Dumping data for table cp_task_log
#

LOCK TABLES `cp_task_log` WRITE;
/*!40000 ALTER TABLE `cp_task_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_task_log` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table ip_info
#

DROP TABLE IF EXISTS `ip_info`;
CREATE TABLE `ip_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prov` varchar(100) DEFAULT NULL COMMENT '省份',
  `city` varchar(100) DEFAULT NULL,
  `op` varchar(100) DEFAULT NULL COMMENT '运营商',
  `ip` varchar(30) DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `adddate` varchar(30) DEFAULT NULL COMMENT '时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='ip表';

#
# Dumping data for table ip_info
#

LOCK TABLES `ip_info` WRITE;
/*!40000 ALTER TABLE `ip_info` DISABLE KEYS */;
INSERT INTO `ip_info` VALUES (2,'广东','深圳','电信','58.60.114.175','9999\r\n','2016-04-24 18:37:48',0);
INSERT INTO `ip_info` VALUES (3,'广东','深圳','电信','113.89.8.196','9999\r\n','2016-04-24 18:37:48',0);
INSERT INTO `ip_info` VALUES (4,'广东','深圳','电信','183.62.206.210','3128\r\n','2016-04-24 18:37:53',0);
INSERT INTO `ip_info` VALUES (5,'广东','深圳','电信','14.154.183.254','9999\r\n','2016-04-24 18:37:58',0);
INSERT INTO `ip_info` VALUES (6,'广东','深圳','电信','121.35.164.99','9999\r\n','2016-04-24 18:38:03',0);
INSERT INTO `ip_info` VALUES (10,'广东','深圳','电信','218.17.22.170','3128\r\n','2016-04-24 18:38:24',0);
INSERT INTO `ip_info` VALUES (13,'广东','深圳','电信','113.105.93.15','80\r\n','2016-04-24 18:51:15',0);
INSERT INTO `ip_info` VALUES (14,'广东','深圳','电信','119.123.104.151','9999\r\n','2016-04-24 18:51:41',0);
INSERT INTO `ip_info` VALUES (15,'广东','深圳','电信','58.60.171.53','9999\r\n','2016-04-24 18:51:52',0);
INSERT INTO `ip_info` VALUES (16,'广东','深圳','电信','121.34.110.59','8090\r\n','2016-04-24 18:52:34',0);
INSERT INTO `ip_info` VALUES (17,'广东','深圳','电信','61.144.194.80','9000\r\n','2016-04-24 19:15:09',0);
INSERT INTO `ip_info` VALUES (18,'广东','深圳','电信','116.24.92.200','3128\r\n','2016-04-24 19:16:46',0);
INSERT INTO `ip_info` VALUES (19,'广东','深圳','电信','121.34.164.31','8118\r\n','2016-04-24 19:23:48',0);
INSERT INTO `ip_info` VALUES (20,'广东','深圳','电信','183.60.254.14','8080\r\n','2016-04-24 19:23:59',0);
INSERT INTO `ip_info` VALUES (21,'广东','深圳','电信','183.60.254.10','8080\r\n','2016-04-24 19:24:04',0);
INSERT INTO `ip_info` VALUES (22,'广东','深圳','电信','113.104.200.32','8118\r\n','2016-04-24 19:24:15',1);
INSERT INTO `ip_info` VALUES (23,'广东','深圳','电信','183.11.145.73','8118\r\n','2016-04-24 19:24:53',1);
INSERT INTO `ip_info` VALUES (24,'广东','深圳','电信','113.89.156.66','9999\r\n','2016-04-24 19:25:30',1);
INSERT INTO `ip_info` VALUES (25,'广东','深圳','电信','218.18.101.241','3128\r\n','2016-04-24 19:26:02',1);
INSERT INTO `ip_info` VALUES (26,'广东','深圳','电信','113.89.85.192','9000\r\n','2016-04-24 19:26:07',1);
INSERT INTO `ip_info` VALUES (27,'广东','深圳','电信','183.60.254.11','8080\r\n','2016-04-24 19:26:39',1);
INSERT INTO `ip_info` VALUES (28,'广东','深圳','电信','61.141.159.9','9000\r\n','2016-04-24 19:26:45',1);
INSERT INTO `ip_info` VALUES (29,'广东','深圳','电信','119.147.86.212','9090\r\n','2016-04-24 22:29:36',1);
INSERT INTO `ip_info` VALUES (30,'广东','深圳','电信','119.136.74.50','8118\r\n','2016-04-24 22:30:12',1);
INSERT INTO `ip_info` VALUES (31,'广东','深圳','电信','58.60.35.116','9797\r\n','2016-04-24 23:12:08',1);
/*!40000 ALTER TABLE `ip_info` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table ip_log
#

DROP TABLE IF EXISTS `ip_log`;
CREATE TABLE `ip_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` varchar(255) DEFAULT NULL,
  `packname` varchar(255) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `adddate` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

#
# Dumping data for table ip_log
#

LOCK TABLES `ip_log` WRITE;
/*!40000 ALTER TABLE `ip_log` DISABLE KEYS */;
INSERT INTO `ip_log` VALUES (1,'1','ph.com',1,0,'2016-04-24 19:15:08');
INSERT INTO `ip_log` VALUES (2,'1','ph.com',1,0,'2016-04-24 19:16:23');
INSERT INTO `ip_log` VALUES (3,'1','ph.com',1,0,'2016-04-24 19:16:26');
INSERT INTO `ip_log` VALUES (4,'1','ph.com',1,0,'2016-04-24 19:16:28');
INSERT INTO `ip_log` VALUES (5,'1','ph.com',1,0,'2016-04-24 19:16:28');
INSERT INTO `ip_log` VALUES (6,'1','ph.com',1,0,'2016-04-24 19:16:29');
INSERT INTO `ip_log` VALUES (7,'1','ph.com',1,0,'2016-04-24 19:16:29');
INSERT INTO `ip_log` VALUES (8,'1','ph.com',1,0,'2016-04-24 19:16:29');
INSERT INTO `ip_log` VALUES (9,'1','ph.com',1,0,'2016-04-24 19:16:29');
INSERT INTO `ip_log` VALUES (10,'1','ph.com',1,0,'2016-04-24 19:16:29');
INSERT INTO `ip_log` VALUES (11,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (12,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (13,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (14,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (15,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (16,'1','ph.com',1,0,'2016-04-24 19:16:30');
INSERT INTO `ip_log` VALUES (17,'1','ph.com',1,0,'2016-04-24 19:16:31');
INSERT INTO `ip_log` VALUES (18,'1','ph.com',1,0,'2016-04-24 19:24:12');
INSERT INTO `ip_log` VALUES (19,'1','ph.com',1,0,'2016-04-24 22:29:33');
INSERT INTO `ip_log` VALUES (20,'1','ph.com',1,0,'2016-04-24 22:30:08');
INSERT INTO `ip_log` VALUES (21,'1','ph.com',1,0,'2016-04-24 23:12:07');
/*!40000 ALTER TABLE `ip_log` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table ip_task
#

DROP TABLE IF EXISTS `ip_task`;
CREATE TABLE `ip_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(500) DEFAULT NULL,
  `prov` varchar(255) DEFAULT NULL COMMENT '区域',
  `city` varchar(100) DEFAULT NULL,
  `op` varchar(30) DEFAULT NULL COMMENT '运营商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table ip_task
#

LOCK TABLES `ip_task` WRITE;
/*!40000 ALTER TABLE `ip_task` DISABLE KEYS */;
INSERT INTO `ip_task` VALUES (1,'http://www.tkdaili.com/api/getiplist.aspx?vkey=5E094C581EBC3E84DEDD2A8F3BCE9994&num=1&password=fucking2016&style=3&filter=','广东','深圳','电信');
/*!40000 ALTER TABLE `ip_task` ENABLE KEYS */;
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
