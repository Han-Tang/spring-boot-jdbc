/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 04/09/2020 15:09:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_datacollection_log
-- ----------------------------
DROP TABLE IF EXISTS `t_datacollection_log`;
CREATE TABLE `t_datacollection_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `biz_date` varchar(15) DEFAULT NULL,
  `tablename` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rtnCode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rtnMsg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_datacollection_log
-- ----------------------------
BEGIN;
INSERT INTO `t_datacollection_log` VALUES (9, '2020-08-21', 'psn_order_info', '2020-09-04 14:52:40', '-1', '非法上传:存在相同表、相同业务日期数据');
INSERT INTO `t_datacollection_log` VALUES (10, '2020-08-21', 'psn_order_info', '2020-09-04 14:54:05', '-1', '非法上传:存在相同表、相同业务日期数据');
INSERT INTO `t_datacollection_log` VALUES (11, '2020-08-21', 'psn_order_info', '2020-09-04 15:01:24', '-1', '非法上传:存在相同表、相同业务日期数据');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
