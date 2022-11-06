/*
 Navicat Premium Data Transfer

 Source Server         : remote-se-iii
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 8.214.110.98:3306
 Source Schema         : collect

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 24/03/2022 19:55:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
                            `cid` int(0) NOT NULL AUTO_INCREMENT COMMENT '评论id',
                            `rid` int(0) NOT NULL COMMENT '对应报告id',
                            `uid` int(0) NOT NULL COMMENT '评论人id',
                            `score` decimal(10, 2) NOT NULL COMMENT '评分',
                            `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文本评论',
                            PRIMARY KEY (`cid`) USING BTREE,
                            INDEX `commontFK_rid`(`rid`) USING BTREE,
                            INDEX `commentFK_uid`(`uid`) USING BTREE,
                            CONSTRAINT `commentFK_uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                            CONSTRAINT `commontFK_rid` FOREIGN KEY (`rid`) REFERENCES `report` (`rid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, 4, 4.00, '这个报告写得还行');

-- ----------------------------
-- Table structure for file_picture
-- ----------------------------
DROP TABLE IF EXISTS `file_picture`;
CREATE TABLE `file_picture`  (
                                 `fid` int(0) NOT NULL AUTO_INCREMENT COMMENT '文件（图片）id',
                                 `tid` int(0) NULL DEFAULT NULL COMMENT '任务id',
                                 `rid` int(0) NULL DEFAULT NULL COMMENT '报告id',
                                 `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件标题',
                                 `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
                                 `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
                                 `file_size` double NULL DEFAULT NULL COMMENT '文件大小',
                                 `upload_time` datetime(0) NOT NULL COMMENT '文件上传时间',
                                 PRIMARY KEY (`fid`) USING BTREE,
                                 INDEX `fk_tid_file`(`tid`) USING BTREE,
                                 INDEX `fk_rid_picture`(`rid`) USING BTREE,
                                 CONSTRAINT `fk_rid_picture` FOREIGN KEY (`rid`) REFERENCES `report` (`rid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                                 CONSTRAINT `fk_tid_file` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_picture
-- ----------------------------
-- INSERT INTO `file_picture` VALUES (1, 2, NULL, 'title', 'file.2.(tid).1.COLLECT.pptx', NULL, NULL, '2022-03-05 00:02:38');
INSERT INTO `file_picture` VALUES (1, NULL, 1, '截图_20222830052837.png', '98c1d400-e308-431b-906e-26c4e6a1e52c.png', '.png', 0.03, '2022-03-30 17:29:39');

-- ----------------------------
-- Table structure for portrait
-- ----------------------------
DROP TABLE IF EXISTS `portrait`;
CREATE TABLE `portrait`  (
                             `pid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户画像主键',
                             `uid` int(0) NOT NULL COMMENT '用户id',
                             `ability` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '能力值',
                             `preference` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务偏好',
                             `activity` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '活跃度',
                             `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '测试设备',
                             `commentsNum` int(0) NOT NULL DEFAULT 1 COMMENT '对应的用户的所有报告的累计被评论次数',
                             PRIMARY KEY (`pid`) USING BTREE,
                             INDEX `portraitFK_uid`(`uid`) USING BTREE,
                             CONSTRAINT `portraitFK_uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portrait
-- ----------------------------
INSERT INTO `portrait` VALUES (1, 2, 3.25, '3,0,0', 1.30, '1,0,0,0,0,0',2);-- 因为发过一个报告，报告也有评分，能力值按照评分的均值计算
INSERT INTO `portrait` VALUES (2, 4, 2.50, '1,0,0', 0.30, '0,0,0,0,0,0',1);
INSERT INTO `portrait` VALUES (3, 5, 2.50, '1,0,0', 0.30, '0,0,0,0,0,0',1);
INSERT INTO `portrait` VALUES (4, 1, 2.50, '0,0,0', 0.00, '0,0,0,0,0,0',1);
INSERT INTO `portrait` VALUES (5, 6, 2.50, '0,0,0', 0.00, '0,0,0,0,0,0',1);
INSERT INTO `portrait` VALUES (6, 3, 2.50, '0,0,0', 0.00, '0,0,0,0,0,0',1);
INSERT INTO `portrait` VALUES (7, 7, 2.50, '0,0,0', 0.00, '0,0,0,0,0,0',1);

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
                           `rid` int(0) NOT NULL AUTO_INCREMENT COMMENT '报告id',
                           `uid` int(0) NOT NULL COMMENT '众包工人id',
                           `tid` int(0) NOT NULL COMMENT '任务id',
                           `parentId` int(0) NULL DEFAULT -1 COMMENT '父报告id',
                           `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缺陷情况说明',
                           `recoverTips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缺陷复现步骤',
                           `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试设备信息',
                           `score` decimal(10, 2) NULL DEFAULT 2.50 COMMENT '综合评分',
                           `creatTime` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',#后来添加的
                           `commentNum` int(0) NULL DEFAULT 1 COMMENT '报告被评论的次数',
                           PRIMARY KEY (`rid`) USING BTREE,
                           INDEX `fk_uid`(`uid`) USING BTREE,
                           INDEX `fk_tid`(`tid`) USING BTREE,
                           CONSTRAINT `fk_tid` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT `fk_uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 2, 1, -1, '一篇对于任务1的测试报告', '错误点1：没有考虑边界条件 错误点2：没有添加登陆验证功能，可能遭受攻击 建议：1.添加判断语句 2.在后端添加校验的代码', 'Android', 3.25, '2022-02-27 21:04:51',2);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                         `tid` int(0) NOT NULL AUTO_INCREMENT COMMENT '任务id',
                         `uid` int(0) NOT NULL COMMENT '发包方用户id',
                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
                         `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '介绍',
                         `max_workers` int(0) NOT NULL COMMENT '招募工人数',
                         `worker_cnt` int(0) NOT NULL DEFAULT 0 COMMENT '已有工人数',
                         `begin_time` datetime(0) NOT NULL COMMENT '开始时间',
                         `end_time` datetime(0) NULL Default NUll COMMENT '结束时间',
                         `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试类型',
                         `cost` decimal(10, 2) NULL DEFAULT NULL,
                         `difficulty` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '难度',
                         `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '测试设备需求',
                         PRIMARY KEY (`tid`) USING BTREE,
                         INDEX `uid`(`uid`) USING BTREE,
                         CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 1, 'task1', 'intro1', 100, 3, '2022-02-27 21:04:51', '2022-05-03 21:04:53', 'FunctionTest', NULL, 1.00, 'Android');
INSERT INTO `task` VALUES (2, 1, 'task2', 'intro2', 200, 1, '2022-02-27 21:12:43', '2022-05-04 21:12:47', 'FunctionTest', NULL, 2.00, 'MacOs');
INSERT INTO `task` VALUES (3, 1, 'task3', 'intro3', 10, 1, '2022-03-24 15:15:15', '2022-05-29 15:15:15', 'FunctionTest', NULL, 1.00, 'MacOs');
INSERT INTO `task` VALUES (4, 1, 'task4', 'intro4', 100, 0, '2022-02-27 21:04:51', '2022-05-03 21:04:53', 'FunctionTest', NULL, 1.00, 'Android');
INSERT INTO `task` VALUES (5, 1, 'task5', 'intro5', 200, 0, '2022-02-27 21:12:43', '2022-05-04 21:12:47', 'FunctionTest', NULL, 2.00, 'Android');
INSERT INTO `task` VALUES (6, 1, 'task6', 'intro6', 100, 0, '2022-03-24 15:15:15', '2022-05-29 15:15:15', 'FunctionTest', NULL, 3.00, 'Android');

-- ----------------------------
-- Table structure for task_order
-- ----------------------------
DROP TABLE IF EXISTS `task_order`;
CREATE TABLE `task_order`  (
                               `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '订单id',
                               `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '众包工人可获得的报酬',
                               `tid` int(0) NOT NULL COMMENT '任务id',
                               `uid` int(0) NOT NULL COMMENT '众包工人id',
                               `create_time` datetime(0) NOT NULL COMMENT '接单时间',
                               `end_time` datetime(0) NULL DEFAULT NULL COMMENT '截止时间',
                               `finished` int(0) NOT NULL DEFAULT 0 COMMENT '任务状态',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `fk_tid_order`(`tid`) USING BTREE,
                               INDEX `fk_uid_order`(`uid`) USING BTREE,
                               CONSTRAINT `fk_tid_order` FOREIGN KEY (`tid`) REFERENCES `task` (`tid`) ON DELETE RESTRICT ON UPDATE CASCADE,
                               CONSTRAINT `fk_uid_order` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_order
-- ----------------------------
INSERT INTO `task_order` VALUES (1, NULL, 1, 2, '2022-02-27 21:05:29', NULL, 1);
INSERT INTO `task_order` VALUES (2, NULL, 2, 2, '2022-02-27 21:13:37', NULL, 0);
INSERT INTO `task_order` VALUES (3, NULL, 1, 4, '2022-03-21 14:02:00', NULL, 0);
INSERT INTO `task_order` VALUES (4, NULL, 3, 2, '2022-03-24 17:00:00', NULL, 0);
INSERT INTO `task_order` VALUES (5, NULL, 1, 5, '2022-03-24 17:00:00', NULL, 0);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
                              `uid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `uname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                              `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册手机号',
                              `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                              `user_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
                              `create_time` datetime(0) NOT NULL COMMENT '创建时间',
                              PRIMARY KEY (`uid`) USING BTREE,
                              UNIQUE INDEX `phone`(`phone`) USING BTREE COMMENT 'phone唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'user1', '1', '1', 'TASKPOSTER', '2022-02-21 10:53:09');
INSERT INTO `user_info` VALUES (2, 'user2', '2', '2', 'WORKER', '2022-02-27 21:07:55');
INSERT INTO `user_info` VALUES (3, 'user3', '3', '3', 'ADMIN', '2022-03-21 14:00:04');
INSERT INTO `user_info` VALUES (4, 'user4', '4', '4', 'WORKER', '2022-03-21 14:01:25');
INSERT INTO `user_info` VALUES (5, 'user5', '5', '5', 'WORKER', '2022-03-21 14:01:25');
INSERT INTO `user_info` VALUES (6, 'user6', '6', '6', 'TASKPOSTER', '2022-02-21 10:53:09');
INSERT INTO `user_info` VALUES (7, 'user7', '7', '7', 'ADMIN', '2022-03-21 14:00:04');

-- ----------------------------
-- Table structure for taskrec_strategy
-- ----------------------------
DROP TABLE IF EXISTS `taskrec_strategy`;
CREATE TABLE `taskrec_strategy`  (
                                     `sid` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务推荐策略id',
                                     `abilityPercent` decimal(10, 2) NOT NULL COMMENT '能力占比',
                                     `preferPercent` decimal(10, 2) NOT NULL COMMENT '用户偏好占比',
                                     `activityPercent` decimal(10, 2) NOT NULL COMMENT '用户活跃度占比',
                                     `devicePercent` decimal(10, 2) NOT NULL COMMENT '设备占比',
                                     `uid` int(11) NULL DEFAULT NULL COMMENT '有没有被管理员使用',
                                     `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '策略的标题',
                                     PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;
#uid本来是用来表示哪个管理员设置的该条规则的，假设所有管理员看见的规则都是一样的，uid取1表示它正在被管理员使用，为0不在使用
-- ----------------------------
-- Records of taskrec_strategy
-- ----------------------------
INSERT INTO `taskrec_strategy` VALUES (1, 0.25, 0.25, 0.25, 0.25, 0,'推荐策略一');
INSERT INTO `taskrec_strategy` VALUES (2, 0.3, 0.25, 0.25, 0.20, 1,'推荐策略二');

SET FOREIGN_KEY_CHECKS = 1;
