/*
 Navicat Premium Data Transfer

 Source Server         : 百度云
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 106.12.167.1:3309
 Source Schema         : cloud_disk

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 15/05/2022 16:57:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_files
-- ----------------------------
DROP TABLE IF EXISTS `t_files`;
CREATE TABLE `t_files`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一，自增',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '阿里云OSS对象名（即路径）',
  `owner_id` bigint(20) NOT NULL COMMENT '拥有者的id',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除，默认0，1表示已被用户删除，但是还存在于回收站中',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `username` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，用户邮箱,唯一，登录输入的账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话，可为空',
  `used_space` bigint(20) NOT NULL COMMENT '该用户已经使用的云盘空间，为后面扩展做可能',
  `max_space` bigint(20) NOT NULL COMMENT '该用户所拥有的最大的云盘空间，为后面扩展做可能',
  `identity` int(11) NOT NULL DEFAULT 0 COMMENT '身份，默认0，0为用户，1为管理员',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `'unqiue_user_name'`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
