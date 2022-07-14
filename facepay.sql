/*
Navicat MySQL Data Transfer

Source Server         : 默认的数据库【本机】
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : facepay

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2022-07-14 13:01:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tab_course
-- ----------------------------
DROP TABLE IF EXISTS `tab_course`;
CREATE TABLE `tab_course` (
  `cid` varchar(100) NOT NULL COMMENT '课程id',
  `course_name` varchar(100) DEFAULT NULL COMMENT '课程名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `price` double DEFAULT NULL COMMENT '售价',
  `list_price` double DEFAULT NULL COMMENT '划线价',
  `img` varchar(255) DEFAULT NULL COMMENT '课程图片',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tab_order
-- ----------------------------
DROP TABLE IF EXISTS `tab_order`;
CREATE TABLE `tab_order` (
  `oid` varchar(32) NOT NULL COMMENT '订单id',
  `order_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `total` double DEFAULT NULL COMMENT '总金额',
  `state` int(11) DEFAULT '0' COMMENT '支付状态，1已支付，0未支付',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `contact` varchar(20) DEFAULT NULL COMMENT '联系人',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `uid` varchar(100) DEFAULT NULL COMMENT '用户id',
  `cid` varchar(100) DEFAULT NULL COMMENT '课程id',
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tab_user
-- ----------------------------
DROP TABLE IF EXISTS `tab_user`;
CREATE TABLE `tab_user` (
  `uid` varchar(100) NOT NULL COMMENT '主键【雪花算法】',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `telephone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别 男male女female',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `pic` varchar(500) DEFAULT NULL COMMENT '个人头像',
  `face_token` varchar(100) NOT NULL COMMENT '人脸标识，不唯一，两次人脸识别结果不一致',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
