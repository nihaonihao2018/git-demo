/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : yygh_manage

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/12/2021 15:56:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `schedule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排班id',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '就诊人id',
  `number` int(11) NULL DEFAULT NULL COMMENT '预约号序',
  `fetch_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建议取号时间',
  `fetch_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取号地点',
  `amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '医事服务费',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `quit_time` datetime NULL DEFAULT NULL COMMENT '退号时间',
  `order_status` tinyint(3) NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (5, '6099dfc7ba72256ae0f2ddd8', 1, 12, '0 14:00前', '一楼9号窗口', 100, NULL, NULL, 0, '2021-12-01 15:49:16', '2021-12-01 15:49:16', 0);
INSERT INTO `order_info` VALUES (6, '6099dfc7ba72256ae0f2ddd8', 1, 13, '0 14:00前', '一楼9号窗口', 100, NULL, NULL, 0, '2021-12-02 10:39:46', '2021-12-02 10:39:46', 0);
INSERT INTO `order_info` VALUES (7, '6099dfc8ba72256ae0f2dde1', 1, 12, '0 14:00前', '一楼9号窗口', 100, NULL, NULL, 0, '2021-12-03 14:19:52', '2021-12-03 14:19:52', 0);
INSERT INTO `order_info` VALUES (8, '6099dfc8ba72256ae0f2ddde', 1, 12, '0 14:00前', '一楼9号窗口', 100, '2021-12-03 17:35:44', NULL, 1, '2021-12-03 17:15:37', '2021-12-03 17:15:37', 0);
INSERT INTO `order_info` VALUES (11, '6099dfc9ba72256ae0f2dded', 1, 12, '0 14:00前', '一楼9号窗口', 100, '2021-12-06 09:48:09', '2021-12-07 10:51:14', -1, '2021-12-06 09:45:56', '2021-12-06 09:45:56', 0);
INSERT INTO `order_info` VALUES (12, '6099dfc9ba72256ae0f2ddf3', 1, 12, '0 14:00前', '一楼9号窗口', 100, '2021-12-06 09:55:17', '2021-12-06 15:40:15', -1, '2021-12-06 09:54:04', '2021-12-06 15:37:00', 0);
INSERT INTO `order_info` VALUES (13, '6099dfc8ba72256ae0f2dde7', 1, 12, '0 14:00前', '一楼9号窗口', 100, NULL, NULL, 0, '2021-12-06 15:56:07', '2021-12-06 15:56:07', 0);
INSERT INTO `order_info` VALUES (14, '6099dfc9ba72256ae0f2ddf0', 1, 14, '0 14:00前', '一楼9号窗口', 100, '2021-12-06 16:49:23', '2021-12-06 16:50:02', -1, '2021-12-06 16:40:19', '2021-12-06 16:40:19', 0);
INSERT INTO `order_info` VALUES (15, '6099dfcaba72256ae0f2ddf6', 1, 12, '0 14:00前', '一楼9号窗口', 100, '2021-12-06 16:59:51', '2021-12-06 17:00:02', -1, '2021-12-06 16:59:26', '2021-12-06 16:59:26', 0);
INSERT INTO `order_info` VALUES (16, '6099dfcaba72256ae0f2ddff', 1, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 10:53:22', -1, '2021-12-06 17:26:00', '2021-12-06 17:26:00', 0);
INSERT INTO `order_info` VALUES (17, '6099dfc8ba72256ae0f2dde7', 1, 13, '0 14:00前', '一楼9号窗口', 100, NULL, NULL, 0, '2021-12-07 10:46:27', '2021-12-07 10:46:27', 0);
INSERT INTO `order_info` VALUES (18, '6099dfc9ba72256ae0f2ddf0', 8, 14, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:24:27', -1, '2021-12-07 13:09:05', '2021-12-07 14:24:06', 0);
INSERT INTO `order_info` VALUES (19, '6099dfc9ba72256ae0f2ddf0', 9, 15, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:24:44', -1, '2021-12-07 13:09:47', '2021-12-07 14:24:07', 0);
INSERT INTO `order_info` VALUES (20, '6099dfc9ba72256ae0f2ddea', 8, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:12:42', -1, '2021-12-07 13:58:45', '2021-12-07 14:12:32', 0);
INSERT INTO `order_info` VALUES (21, '6099dfc9ba72256ae0f2ddea', 9, 13, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:12:55', -1, '2021-12-07 13:59:12', '2021-12-07 14:12:33', 0);
INSERT INTO `order_info` VALUES (22, '6099dfc9ba72256ae0f2dded', 8, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:04:41', -1, '2021-12-07 14:03:58', '2021-12-07 14:03:58', 0);
INSERT INTO `order_info` VALUES (23, '6099dfc9ba72256ae0f2dded', 9, 13, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:04:59', -1, '2021-12-07 14:04:11', '2021-12-07 14:04:11', 0);
INSERT INTO `order_info` VALUES (24, '6099dfc9ba72256ae0f2dded', 8, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:07:51', -1, '2021-12-07 14:05:15', '2021-12-07 14:05:15', 0);
INSERT INTO `order_info` VALUES (25, '6099dfc9ba72256ae0f2dded', 9, 13, '0 14:00前', '一楼9号窗口', 100, '2021-12-07 14:06:06', '2021-12-07 14:06:24', -1, '2021-12-07 14:05:34', '2021-12-07 14:05:34', 0);
INSERT INTO `order_info` VALUES (26, '6099dfc9ba72256ae0f2dded', 8, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:25:24', -1, '2021-12-07 14:25:21', '2021-12-07 14:25:21', 0);
INSERT INTO `order_info` VALUES (27, '6099dfc9ba72256ae0f2dded', 9, 12, '0 14:00前', '一楼9号窗口', 100, NULL, '2021-12-07 14:25:54', -1, '2021-12-07 14:25:37', '2021-12-07 14:25:37', 0);

SET FOREIGN_KEY_CHECKS = 1;
