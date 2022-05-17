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

 Date: 17/05/2022 15:57:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_files
-- ----------------------------
DROP TABLE IF EXISTS `t_files`;
CREATE TABLE `t_files`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，唯一，自增',
  `file_name` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '阿里云OSS对象名（即路径）',
  `owner_id` bigint(20) NOT NULL COMMENT '拥有者的id',
  `size` bigint(20) NOT NULL DEFAULT 0 COMMENT '文件大小，单位字节',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类别:dir or file',
  `update_time` datetime(0) NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `'unique_file_name'`(`file_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_files
-- ----------------------------
INSERT INTO `t_files` VALUES (1, '426671-72c8aa0d72d12ae9.webp', 'cloud-disk/files/1/foo/bar/426671-72c8aa0d72d12ae9.webp', 1, 18558, '2022-05-17 12:25:12', 'file', '2022-05-17 12:25:12');

-- ----------------------------
-- Table structure for t_garbage
-- ----------------------------
DROP TABLE IF EXISTS `t_garbage`;
CREATE TABLE `t_garbage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_id` bigint(20) NOT NULL COMMENT '拥有者ID',
  `fileName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `size` bigint(20) NOT NULL COMMENT '大小',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_garbage
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `username` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，用户邮箱,唯一，登录输入的账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `used_space` bigint(20) NOT NULL COMMENT '该用户已经使用的云盘空间，为后面扩展做可能',
  `max_space` bigint(20) NOT NULL COMMENT '该用户所拥有的最大的云盘空间，为后面扩展做可能',
  `identity` int(11) NOT NULL DEFAULT 0 COMMENT '身份，默认0，0为用户，1为管理员',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `'unqiue_user_name'`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'pymjl@qq.com', '9ee416167bdcc1bb0443b636d5b9bb239e00e2a20e99ce069258c9d1f4f64628', 'Pymjl', 'user/avatar/1/t017e0ddfed0f0bdeb7.png', 0, 2048, 1, '2022-05-15 23:55:06', '2022-05-16 16:06:02');

SET FOREIGN_KEY_CHECKS = 1;
