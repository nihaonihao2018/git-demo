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

 Date: 14/12/2021 15:57:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `payment_type` tinyint(1) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` tinyint(3) NULL DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES (3, '163841278619262', 17, 2, NULL, 100.00, '2021-12-02|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2021-12-03 16:40:15', '2021-12-03 16:40:15', 0);
INSERT INTO `payment_info` VALUES (4, '16385229376528', 19, 2, '4200001319202112033827645920', 100.00, '2021-12-04|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-03 17:35:44', '{transaction_id=4200001319202112033827645920, nonce_str=Sa9gEcnKN9Um3o9d, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuLm8zpdJcsi6TAUY-eFe3y4, sign=F80B794163D5358A2A42DA1D308B69F8, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=16385229376528, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211203171607, is_subscribe=N, return_code=SUCCESS}', '2021-12-03 17:15:57', '2021-12-03 17:15:56', 0);
INSERT INTO `payment_info` VALUES (5, '163875406962087', 20, 2, NULL, 100.00, '2021-12-10|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-06 09:29:17', '{nonce_str=cTU5HLWkcuOuJ1ov, device_info=, trade_state=NOTPAY, out_trade_no=163875406962087, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=订单未支付, sign=23100B989F5ED25ADFB0CB82B55FD5BF, return_msg=OK, result_code=SUCCESS, mch_id=1558950191, return_code=SUCCESS}', '2021-12-06 09:29:14', '2021-12-06 09:29:13', 0);
INSERT INTO `payment_info` VALUES (6, '163875482108038', 21, 2, NULL, 100.00, '2021-12-10|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2021-12-06 09:41:19', '2021-12-06 09:41:19', 0);
INSERT INTO `payment_info` VALUES (7, '163875515598136', 22, 2, '4200001190202112069005556240', 100.00, '2021-12-09|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-06 09:48:09', '{transaction_id=4200001190202112069005556240, nonce_str=xCJvfLM4yShwQceh, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuLm8zpdJcsi6TAUY-eFe3y4, sign=424A16E7500A168134EEA0202EDB5B91, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163875515598136, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211206094641, is_subscribe=N, return_code=SUCCESS}', '2021-12-06 09:46:27', '2021-12-06 09:46:26', 0);
INSERT INTO `payment_info` VALUES (8, '163875564364177', 23, 2, '4200001183202112067794736537', 100.00, '2021-12-11|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-06 09:55:17', '{transaction_id=4200001183202112067794736537, nonce_str=m8aFX9BgGlD001rb, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuLm8zpdJcsi6TAUY-eFe3y4, sign=02BD8891E7B82FDCFD523C0E7A4B38C8, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163875564364177, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211206095453, is_subscribe=N, return_code=SUCCESS}', '2021-12-06 09:55:00', '2021-12-06 09:55:00', 0);
INSERT INTO `payment_info` VALUES (9, '163878001969622', 25, 2, '4200001322202112063204410676', 100.00, '2021-12-10|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-06 16:49:23', '{transaction_id=4200001322202112063204410676, nonce_str=MEZfMCNX9M7JEkeY, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuPbs3uYpi4jwIvv9nT1gnLs, sign=811E28A18EBB25777B6821BBDDE76B9A, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163878001969622, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211206164034, is_subscribe=N, return_code=SUCCESS}', '2021-12-06 16:40:23', '2021-12-06 16:40:23', 0);
INSERT INTO `payment_info` VALUES (10, '163878116680966', 26, 2, '4200001183202112061862826332', 100.00, '2021-12-12|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-06 16:59:51', '{transaction_id=4200001183202112061862826332, nonce_str=8sj72eSK4fLK1Ooi, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuInazT7yZxNvLnQQQigkulA, sign=C82E7813604577E97DFDA7202E01C36E, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163878116680966, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211206165948, is_subscribe=N, return_code=SUCCESS}', '2021-12-06 16:59:29', '2021-12-06 16:59:29', 0);
INSERT INTO `payment_info` VALUES (11, '16387827606229', 27, 2, NULL, 100.00, '2021-12-15|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2021-12-06 17:26:03', '2021-12-06 17:26:03', 0);
INSERT INTO `payment_info` VALUES (12, '163885713416377', 37, 2, '4200001198202112078474561752', 100.00, '2021-12-09|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-12-07 14:06:06', '{transaction_id=4200001198202112078474561752, nonce_str=z8n888aWdEVlnWsy, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuLm8zpdJcsi6TAUY-eFe3y4, sign=C0AE748D02144430159429DC25DD5518, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163885713416377, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20211207140605, is_subscribe=N, return_code=SUCCESS}', '2021-12-07 14:05:41', '2021-12-07 14:05:41', 0);

SET FOREIGN_KEY_CHECKS = 1;
