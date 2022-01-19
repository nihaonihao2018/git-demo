/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : yygh_order

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/12/2021 15:57:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `out_trade_no` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单交易号',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `depcode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室编号',
  `depname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室名称',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生职称',
  `hos_schedule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排班编号（医院自己的排班主键）',
  `reserve_date` date NULL DEFAULT NULL COMMENT '安排日期',
  `reserve_time` tinyint(3) NULL DEFAULT 0 COMMENT '安排时间（0：上午 1：下午）',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '就诊人id',
  `patient_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人名称',
  `patient_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人手机',
  `hos_record_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预约记录唯一标识（医院预约记录主键）',
  `number` int(11) NULL DEFAULT NULL COMMENT '预约号序',
  `fetch_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建议取号时间',
  `fetch_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取号地点',
  `amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '医事服务费',
  `quit_time` datetime NULL DEFAULT NULL COMMENT '退号时间',
  `order_status` tinyint(3) NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_hoscode`(`hoscode`) USING BTREE,
  INDEX `idx_hos_schedule_id`(`hos_schedule_id`) USING BTREE,
  INDEX `idx_hos_record_id`(`hos_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (16, 12, '163834495623833', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc7ba72256ae0f2ddd8', '2021-12-02', 0, 9, '端端', '15660158662', '6099dfc7ba72256ae0f2ddd8', 12, '2021-12-0209:00前', '一层114窗口', 100, '2021-12-01 15:30:00', 0, '2021-12-01 15:49:16', '2021-12-01 15:49:16', 0);
INSERT INTO `order_info` VALUES (17, 12, '163841278619262', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc7ba72256ae0f2ddd8', '2021-12-02', 0, 8, '瑶瑶', '15039712173', '6099dfc7ba72256ae0f2ddd8', 13, '2021-12-0209:00前', '一层114窗口', 100, '2021-12-01 15:30:00', 0, '2021-12-02 10:39:46', '2021-12-02 10:39:46', 0);
INSERT INTO `order_info` VALUES (18, 12, '163851239182959', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc8ba72256ae0f2dde1', '2021-12-05', 0, 8, '瑶瑶', '15039712173', '6099dfc8ba72256ae0f2dde1', 12, '2021-12-0509:00前', '一层114窗口', 100, '2021-12-04 15:30:00', 0, '2021-12-03 14:19:51', '2021-12-03 14:19:52', 0);
INSERT INTO `order_info` VALUES (19, 12, '16385229376528', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc8ba72256ae0f2ddde', '2021-12-04', 0, 9, '端端', '15660158662', '6099dfc8ba72256ae0f2ddde', 12, '2021-12-0409:00前', '一层114窗口', 100, '2021-12-03 15:30:00', 1, '2021-12-03 17:15:37', '2021-12-03 17:15:38', 0);
INSERT INTO `order_info` VALUES (22, 12, '163875515598136', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2dded', 12, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-06 09:45:55', '2021-12-06 09:45:56', 0);
INSERT INTO `order_info` VALUES (23, 12, '163875564364177', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddf3', '2021-12-11', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2ddf3', 12, '2021-12-1109:00前', '一层114窗口', 100, '2021-12-10 15:30:00', -1, '2021-12-06 09:54:03', '2021-12-06 09:54:04', 0);
INSERT INTO `order_info` VALUES (24, 12, '163877736762744', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc8ba72256ae0f2dde7', '2021-12-07', 0, 8, '瑶瑶', '15039712173', '6099dfc8ba72256ae0f2dde7', 12, '2021-12-0709:00前', '一层114窗口', 100, '2021-12-06 15:30:00', 0, '2021-12-06 15:56:07', '2021-12-06 15:56:07', 0);
INSERT INTO `order_info` VALUES (25, 12, '163878001969622', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddf0', '2021-12-10', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2ddf0', 14, '2021-12-1009:00前', '一层114窗口', 100, '2021-12-09 15:30:00', -1, '2021-12-06 16:40:19', '2021-12-06 16:40:19', 0);
INSERT INTO `order_info` VALUES (26, 12, '163878116680966', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfcaba72256ae0f2ddf6', '2021-12-12', 0, 9, '端端', '15660158662', '6099dfcaba72256ae0f2ddf6', 12, '2021-12-1209:00前', '一层114窗口', 100, '2021-12-11 15:30:00', -1, '2021-12-06 16:59:26', '2021-12-06 16:59:26', 0);
INSERT INTO `order_info` VALUES (27, 12, '16387827606229', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfcaba72256ae0f2ddff', '2021-12-15', 0, 9, '端端', '15660158662', '6099dfcaba72256ae0f2ddff', 12, '2021-12-1509:00前', '一层114窗口', 100, '2021-12-14 15:30:00', -1, '2021-12-06 17:26:00', '2021-12-06 17:26:00', 0);
INSERT INTO `order_info` VALUES (28, 12, '16388451863765', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc8ba72256ae0f2dde7', '2021-12-07', 0, 9, '端端', '15660158662', '6099dfc8ba72256ae0f2dde7', 13, '2021-12-0709:00前', '一层114窗口', 100, '2021-12-06 15:30:00', 0, '2021-12-07 10:46:26', '2021-12-07 10:46:27', 0);
INSERT INTO `order_info` VALUES (29, 12, '163885374543190', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddf0', '2021-12-10', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2ddf0', 14, '2021-12-1009:00前', '一层114窗口', 100, '2021-12-09 15:30:00', -1, '2021-12-07 13:09:05', '2021-12-07 13:09:05', 0);
INSERT INTO `order_info` VALUES (30, 12, '163885378701718', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddf0', '2021-12-10', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2ddf0', 15, '2021-12-1009:00前', '一层114窗口', 100, '2021-12-09 15:30:00', -1, '2021-12-07 13:09:47', '2021-12-07 13:09:47', 0);
INSERT INTO `order_info` VALUES (32, 12, '16388567249514', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddea', '2021-12-08', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2ddea', 12, '2021-12-0809:00前', '一层114窗口', 100, '2021-12-07 15:30:00', -1, '2021-12-07 13:58:44', '2021-12-07 13:58:45', 0);
INSERT INTO `order_info` VALUES (33, 12, '163885675228689', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2ddea', '2021-12-08', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2ddea', 13, '2021-12-0809:00前', '一层114窗口', 100, '2021-12-07 15:30:00', -1, '2021-12-07 13:59:12', '2021-12-07 13:59:12', 0);
INSERT INTO `order_info` VALUES (34, 12, '16388570388203', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2dded', 12, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:03:58', '2021-12-07 14:03:59', 0);
INSERT INTO `order_info` VALUES (35, 12, '163885705188996', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2dded', 13, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:04:11', '2021-12-07 14:04:11', 0);
INSERT INTO `order_info` VALUES (36, 12, '163885711540338', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2dded', 12, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:05:15', '2021-12-07 14:05:15', 0);
INSERT INTO `order_info` VALUES (37, 12, '163885713416377', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2dded', 13, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:05:34', '2021-12-07 14:05:34', 0);
INSERT INTO `order_info` VALUES (38, 12, '163885832138625', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 8, '瑶瑶', '15039712173', '6099dfc9ba72256ae0f2dded', 12, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:25:21', '2021-12-07 14:25:21', 0);
INSERT INTO `order_info` VALUES (39, 12, '16388583379564', '1000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '6099dfc9ba72256ae0f2dded', '2021-12-09', 0, 9, '端端', '15660158662', '6099dfc9ba72256ae0f2dded', 12, '2021-12-0909:00前', '一层114窗口', 100, '2021-12-08 15:30:00', -1, '2021-12-07 14:25:37', '2021-12-07 14:25:37', 0);

SET FOREIGN_KEY_CHECKS = 1;
