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

 Date: 27/02/2022 21:15:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_picture
-- ----------------------------
DROP TABLE IF EXISTS `file_picture`;
CREATE TABLE `file_picture`  (
                                 `fid` int NOT NULL AUTO_INCREMENT COMMENT '文件（图片）id',
                                 `tid` int NULL DEFAULT NULL COMMENT '任务id',
                                 `rid` int NULL DEFAULT NULL COMMENT '报告id',
                                 `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件标题',
                                 `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
                                 `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
                                 `file_size` double NULL DEFAULT NULL COMMENT '文件大小',
                                 `upload_time` datetime NOT NULL COMMENT '文件上传时间',
                                 PRIMARY KEY (`fid`) USING BTREE,
                                 INDEX `fk_tid_file`(`tid` ASC) USING BTREE,
                                 INDEX `fk_rid_picture`(`rid` ASC) USING BTREE,
                                 CONSTRAINT `fk_rid_picture` FOREIGN KEY (`rid`) REFERENCES `report` (`rid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                                 CONSTRAINT `fk_tid_file` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_picture
-- ----------------------------

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
                           `rid` int NOT NULL AUTO_INCREMENT COMMENT '报告id',
                           `uid` int NOT NULL COMMENT '众包工人id',
                           `tid` int NOT NULL COMMENT '任务id',
                           `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缺陷情况说明',
                           `recoverTips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缺陷复现步骤',
                           `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试设备信息',
                           PRIMARY KEY (`rid`) USING BTREE,
                           INDEX `fk_uid`(`uid` ASC) USING BTREE,
                           INDEX `fk_tid`(`tid` ASC) USING BTREE,
                           CONSTRAINT `fk_tid` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT `fk_uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 2, 1, 'reportIntro', 'no', 'no');

-- ----------------------------
-- Table structure for task               cost字段暂时没有使用到
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
                         `cost` decimal(10, 2) NULL DEFAULT NULL,
                         PRIMARY KEY (`tid`) USING BTREE,
                         INDEX `uid`(`uid` ASC) USING BTREE,
                         CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 1, 'task1', 'intro1', 100, 1, '2022-02-27 21:04:51', '2022-03-03 21:04:53', 'A', NULL);
INSERT INTO `task` VALUES (2, 2, 'task2', 'intro2', 200, 1, '2022-02-27 21:12:43', '2022-03-04 21:12:47', 'B', NULL);

-- ----------------------------
-- Table structure for task_order
-- ----------------------------
DROP TABLE IF EXISTS `task_order`;
CREATE TABLE `task_order`  (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT '订单id',
                               `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '众包工人可获得的报酬',
                               `tid` int NOT NULL COMMENT '任务id',
                               `uid` int NOT NULL COMMENT '众包工人id',
                               `create_time` datetime NOT NULL COMMENT '接单时间',
                               `end_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
                               `finished` int NOT NULL DEFAULT 0 COMMENT '任务状态',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `fk_tid_order`(`tid` ASC) USING BTREE,
                               INDEX `fk_uid_order`(`uid` ASC) USING BTREE,
                               CONSTRAINT `fk_tid_order` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                               CONSTRAINT `fk_uid_order` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_order
-- ----------------------------
INSERT INTO `task_order` VALUES (1, NULL, 1, 2, '2022-02-27 21:05:29', NULL, 1);
INSERT INTO `task_order` VALUES (2, NULL, 2, 2, '2022-02-27 21:13:37', NULL, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'user1', '1', '1', 'TASKPOSTER', '2022-02-21 10:53:09');
INSERT INTO `user_info` VALUES (2, 'user2', '2', '2', 'WORKER', '2022-02-27 21:07:55');

SET FOREIGN_KEY_CHECKS = 1;
