/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : yygh_cmn

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/12/2021 15:56:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `user_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `user_birthday` date NULL DEFAULT NULL COMMENT '出生年月',
  `native_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `id_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (6, '齐端', '上海市宝山区祁华路58弄26号楼503室', '1998-05-05', '河南省南阳市新野县城郊乡齐马庄村6组', '411328199805050699', '2021-10-11 14:13:38', '2021-10-11 14:13:38', 0);
INSERT INTO `person` VALUES (7, '齐端', '河南省南阳市新野县城郊乡齐马庄村6组', '1998-05-05', '河南省南阳市新野县城郊乡齐马庄村6组', '411328199805050699', '2021-10-11 14:33:19', '2021-10-11 14:33:19', 0);
INSERT INTO `person` VALUES (8, '刘博', '上海市宝山区祁华路58弄26号楼503室', '2001-11-10', '河南省鹿邑县', '412725200111105711', '2021-10-11 17:46:22', '2021-10-11 17:46:22', 0);
INSERT INTO `person` VALUES (9, '刘博', '河南省鹿邑县', '2001-11-10', '河南省鹿邑县', '412725200111105711', '2021-10-12 09:50:27', '2021-10-12 09:50:27', 0);
INSERT INTO `person` VALUES (10, '张震', '河南省南阳市新野县王集镇', '1998-02-28', '河南省南阳市新野县王集镇', '411328199802285623', '2021-10-12 11:16:03', '2021-10-12 11:16:03', 0);
INSERT INTO `person` VALUES (11, '刘乐', '河南省南阳市溧河镇', '1999-11-04', '河南省南阳市溧河镇', '411328199911045689', '2021-10-12 15:11:06', '2021-10-12 15:11:06', 0);
INSERT INTO `person` VALUES (12, '刘乐', '河南省南阳市溧河镇', '1999-11-04', '河南省南阳市溧河镇', '411328199911045689', '2021-10-12 16:50:01', '2021-10-12 16:50:01', 0);

SET FOREIGN_KEY_CHECKS = 1;
