/*
 Navicat MySQL Data Transfer

 Source Server         : sqlOne
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : localhost:3306
 Source Schema         : collect

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 21/02/2022 11:34:18
*/

-- ----------------------------
-- 设定字符集，消除外键约束，在末尾重新设置外键约束
-- ----------------------------
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                         `tid` int NOT NULL AUTO_INCREMENT COMMENT '任务id',
                         `uid` int NOT NULL COMMENT '发包方用户id',
                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
                         `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '介绍',
                         `max_workers` int NOT NULL COMMENT '招募工人数',
                         `worker_cnt` int NOT NULL DEFAULT 0 COMMENT '已有工人数',
                         `begin_time` datetime NOT NULL COMMENT '开始时间',
                         `end_time` datetime NOT NULL COMMENT '结束时间',
                         `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试类型',
                         PRIMARY KEY (`tid`) USING BTREE,
                         INDEX `uid`(`uid` ASC) USING BTREE,
                         CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 1, 'task1', 'intro', 100, 0, '2022-02-21 11:18:10', '2022-03-02 11:18:14', 'test');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
                              `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `uname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                              `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册手机号',
                              `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                              `user_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
                              `create_time` datetime NOT NULL COMMENT '创建时间',
                              PRIMARY KEY (`uid`) USING BTREE,
                              UNIQUE INDEX `phone`(`phone` ASC) USING BTREE COMMENT 'phone唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'user1', '1', '1', 'TASKPOSTER', '2022-02-21 10:53:09');

SET FOREIGN_KEY_CHECKS = 1;
