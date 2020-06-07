/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : administration_system

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 05/06/2020 16:32:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `cid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教授id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credit` int(0) NULL DEFAULT NULL,
  `prerequisite` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `semester` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fee` int(0) NULL DEFAULT NULL,
  `timeslot` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`cid`, `pid`) USING BTREE,
  INDEX `pid`(`pid`) USING BTREE,
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`pid`) REFERENCES `professor` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('2', '4', '离散数学', 1, NULL, '2020年第二学期', 80, '星期一', NULL);
INSERT INTO `course` VALUES ('4', '2', '算法设计', 3, '2', '2020年第二学期', 120, '星期四', NULL);
INSERT INTO `course` VALUES ('5', '5', '英语', 2, NULL, '2020年第二学期', 75, '星期二', NULL);
INSERT INTO `course` VALUES ('6', '1', '软件工程', 3, NULL, '2020年第二学期', 90, '星期二', NULL);
INSERT INTO `course` VALUES ('7', '3', '计算机导论', 1, NULL, '2020年第二学期', 60, '星期三', NULL);
INSERT INTO `course` VALUES ('8', '3', 'Java编程', 2, NULL, '2020年第二学期', 100, '星期五', NULL);

-- ----------------------------
-- Table structure for course_selection
-- ----------------------------
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `lesson1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一门主选课程id',
  `lesson2` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson3` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson4` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson5` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson6` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  CONSTRAINT `course_selection_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教授id',
  `cid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `cname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `credit` int(0) NULL DEFAULT NULL COMMENT '课程学分',
  `semester` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'xx年第xx学期',
  `grade` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`sid`, `pid`, `cid`) USING BTREE,
  INDEX `cid`(`cname`) USING BTREE,
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('1', '3', '1', '高等数学', 2, '2019年第一学期', 93);
INSERT INTO `grade` VALUES ('1', '4', '3', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('2', '3', '1', '高等数学', 2, '2020年第一学期', NULL);
INSERT INTO `grade` VALUES ('3', '4', '3', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('4', '3', '1', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('4', '4', '3', '数据结构', 2, '2019年第二学期', 59);

-- ----------------------------
-- Table structure for professor
-- ----------------------------
DROP TABLE IF EXISTS `professor`;
CREATE TABLE `professor`  (
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ssn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of professor
-- ----------------------------
INSERT INTO `professor` VALUES ('1', '123456', '李红', '19800101', '987654321', '副教授', '计算机');
INSERT INTO `professor` VALUES ('2', '123456', '王明', '19750202', '876543219', '教授', '软件');
INSERT INTO `professor` VALUES ('3', '123456', '陈芳', '19830303', '765432198', '讲师', '计算机');
INSERT INTO `professor` VALUES ('4', '123456', '张丽', '19900404', '654321987', '讲师', '数学');
INSERT INTO `professor` VALUES ('5', '123456', '马刚', '19880505', '543219876', '讲师', '英语');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ssn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '123456', '陈一', '20010101', '123456789', '一年级');
INSERT INTO `student` VALUES ('2', '123456', '王二', '20010202', '234567891', '二年级');
INSERT INTO `student` VALUES ('3', '123456', '张三', '20000303', '345678912', '三年级');
INSERT INTO `student` VALUES ('4', '123456', '李四', '20000101', '456789123', '四年级');

SET FOREIGN_KEY_CHECKS = 1;
