/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : oauth

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-09-03 10:56:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `pass_word` varchar(32) DEFAULT NULL COMMENT '密码',
  `user_sex` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('29', 'aaaa', '123', '1', '美丽');
