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

 Date: 04/09/2020 15:08:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_tableinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_tableinfo`;
CREATE TABLE `t_tableinfo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tablename` varchar(255) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `date_field` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tableinfo
-- ----------------------------
BEGIN;
-- INSERT INTO `t_tableinfo` VALUES (1, 'psn_order_info', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (1, 'SYS_ORGUNT_CHG_EVT_C', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (2, 'SYS_ORGUNT_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (3, 'SYS_ORGUNT_RLTS_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (4, 'SYS_ORGUNT_UACT_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (5, 'SYS_RESU_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (6, 'SYS_ROLE_PERM_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (7, 'SYS_ROLE_UACT_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (8, 'SYS_SUBSYS_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (9, 'SYS_UACT_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (10, 'SYS_UACT_EXT_D', '1', 'updt_time');
INSERT INTO `t_tableinfo` VALUES (11, 'SYS_USER_D', '1', 'updt_time');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

