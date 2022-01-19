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

 Date: 14/12/2021 15:57:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `payment_type` tinyint(3) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `refund_status` tinyint(3) NULL DEFAULT NULL COMMENT '退款状态',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `callback_time` datetime NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退款信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_info
-- ----------------------------
INSERT INTO `refund_info` VALUES (1, '163875564364177', 23, 2, '50300800212021120615121056208', 100.00, '2021-12-11|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001183202112067794736537\",\"nonce_str\":\"MD8YPo7SJU6Zoo8P\",\"out_refund_no\":\"tk163875564364177\",\"sign\":\"DF1EF77144A816CF1DA7EAD9C78831B2\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50300800212021120615121056208\",\"cash_fee\":\"1\",\"out_trade_no\":\"163875564364177\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-12-06 15:40:16', '2021-12-06 15:40:15', '2021-12-06 15:40:15', 0);
INSERT INTO `refund_info` VALUES (2, '163878001969622', 25, 2, '50301000112021120615136340902', 100.00, '2021-12-10|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001322202112063204410676\",\"nonce_str\":\"Mvy0nVezJuAF1Ug5\",\"out_refund_no\":\"tk163878001969622\",\"sign\":\"8C0B60D04C0CA0F6909004876721164E\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50301000112021120615136340902\",\"cash_fee\":\"1\",\"out_trade_no\":\"163878001969622\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-12-06 16:50:03', '2021-12-06 16:50:02', '2021-12-06 16:50:03', 0);
INSERT INTO `refund_info` VALUES (3, '163878116680966', 26, 2, '50300900092021120615134017442', 100.00, '2021-12-12|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001183202112061862826332\",\"nonce_str\":\"ZOa4y6HHJlJziUvK\",\"out_refund_no\":\"tk163878116680966\",\"sign\":\"8918B476514610AAF4219860E34E2268\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50300900092021120615134017442\",\"cash_fee\":\"1\",\"out_trade_no\":\"163878116680966\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-12-06 17:00:03', '2021-12-06 17:00:02', '2021-12-06 17:00:02', 0);
INSERT INTO `refund_info` VALUES (4, '163875515598136', 22, 2, '50301300082021120715140396855', 100.00, '2021-12-09|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001190202112069005556240\",\"nonce_str\":\"Y6imXosRQRNEIAOm\",\"out_refund_no\":\"tk163875515598136\",\"sign\":\"0E4B26AA7ACF646AA0161D92C1535261\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50301300082021120715140396855\",\"cash_fee\":\"1\",\"out_trade_no\":\"163875515598136\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-12-07 10:51:15', '2021-12-07 10:51:14', '2021-12-07 10:51:15', 0);
INSERT INTO `refund_info` VALUES (5, '163885713416377', 37, 2, '50300900152021120715145756699', 100.00, '2021-12-09|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001198202112078474561752\",\"nonce_str\":\"FisK1hoqivghO1lM\",\"out_refund_no\":\"tk163885713416377\",\"sign\":\"5967451AF69C01143949436FF4A51AA6\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50300900152021120715145756699\",\"cash_fee\":\"1\",\"out_trade_no\":\"163885713416377\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-12-07 14:06:25', '2021-12-07 14:06:24', '2021-12-07 14:06:24', 0);

SET FOREIGN_KEY_CHECKS = 1;
