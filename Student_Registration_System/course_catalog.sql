/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : course_catalog

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 05/06/2020 16:34:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credit` int(0) NULL DEFAULT NULL,
  `prerequisite` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fee` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', '高等数学', 2, NULL, 100);
INSERT INTO `course` VALUES ('2', '离散数学', 1, '', 80);
INSERT INTO `course` VALUES ('3', '数据结构', 2, NULL, 60);
INSERT INTO `course` VALUES ('4', '算法设计', 3, '2', 120);
INSERT INTO `course` VALUES ('5', '英语', 2, NULL, 75);
INSERT INTO `course` VALUES ('6', '软件工程', 3, NULL, 90);
INSERT INTO `course` VALUES ('7', '计算机导论', 1, NULL, 60);
INSERT INTO `course` VALUES ('8', 'Java编程', 2, NULL, 100);

SET FOREIGN_KEY_CHECKS = 1;
