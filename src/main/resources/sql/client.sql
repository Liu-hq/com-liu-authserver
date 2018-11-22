/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : oauth

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-09-03 10:56:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) DEFAULT NULL,
  `client_name` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `redirect_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client` VALUES ('3', '111', '222', '333', '8888888', 'http://localhost:8051/test');
INSERT INTO `client` VALUES ('4', 'cd2d872a-09f8-4783-b0a8-721f0051e968', '应用名称', 'ba1a5180-0259-43a5-a2de-7d0005c010f7', '应用描述', 'http://localhost:8051/test');
INSERT INTO `client` VALUES ('5', 'dfd6e6c0-4856-4525-8fed-68e450828665', '应用名称', '62f16478-7b6c-4194-9a19-cc804c85fe27', '应用描述', 'http://localhost:8051/test');
