/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : yygh_hosp

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/12/2021 15:56:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hospital_set
-- ----------------------------
DROP TABLE IF EXISTS `hospital_set`;
CREATE TABLE `hospital_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `api_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'api基础路径',
  `sign_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名秘钥',
  `contacts_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contacts_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_hoscode`(`hoscode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '医院设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hospital_set
-- ----------------------------
INSERT INTO `hospital_set` VALUES (2, '北京协和医院', '1000', 'http://localhost:8201', '0ca1f91e1df8912575db5049c4943456', '张震', '18817635990', 1, '2021-04-21 10:20:51', '2021-05-07 17:21:20', 0);
INSERT INTO `hospital_set` VALUES (3, '上海人民医院', '1001', 'http://localhost:8899', '0ca1f91e1df8912575db5049c4943503', '刘乐', '15651960967', 1, '2021-04-21 16:41:49', '2021-04-26 10:32:24', 0);
INSERT INTO `hospital_set` VALUES (4, '上海仁和医院', '1002', 'http://localhost:8890', '0ca1f91e1df8912575db5049c4943536', '齐端', '15660158662', 1, '2021-04-23 10:50:02', '2021-04-26 10:29:45', 0);
INSERT INTO `hospital_set` VALUES (5, '新野县中医院', '1003', 'http://localhost:8898', '0ca1f91e1df8912575db5049c4943425', '张瑶', '15039712173', 1, '2021-04-23 10:51:40', '2021-04-23 17:48:05', 0);
INSERT INTO `hospital_set` VALUES (6, '新野县人民医院', '1004', 'http://localhost:8894', '0ca1f91e1df8912575db5049c4943789', '齐博', '17550359296', 1, '2021-04-23 10:53:14', '2021-04-23 17:48:06', 0);
INSERT INTO `hospital_set` VALUES (7, '鹿邑市人民医院', '1005', 'http://localhost:8896', '0ca1f91e1df8912575db5049c4943428', '刘博', '18240720989', 1, '2021-04-23 10:54:35', '2021-04-23 17:48:08', 0);
INSERT INTO `hospital_set` VALUES (8, '杭州市协和医院', '1006', 'http://localhost:8893', '0ca1f91e1df8912575db5049c4943536', '张金梁', '18339160351', 1, '2021-04-23 10:57:00', '2021-04-23 17:48:09', 0);
INSERT INTO `hospital_set` VALUES (9, '上海儿童医院', '1007', 'http://localhost:8562', '960f629e9484e6482f45fb24373f64b5', '赵一沣', '18796220262', 1, '2021-04-26 14:06:32', '2021-04-26 14:06:32', 0);
INSERT INTO `hospital_set` VALUES (10, '上海肿瘤医院', '1008', 'http://localhost:8462', '4bf15caafd5149bbdf7f8ffc7786ec1b', '许书豪', '16621786614', 1, '2021-04-26 14:09:29', '2021-10-12 18:03:42', 0);
INSERT INTO `hospital_set` VALUES (11, '上海浦东医院', '1009', 'http://localhost:8623', '7d6e7ee6768ca7fbcfee2c40d111149c', '吴立凡', '17765189501', 1, '2021-04-26 14:59:38', '2021-10-12 17:46:48', 0);

SET FOREIGN_KEY_CHECKS = 1;
