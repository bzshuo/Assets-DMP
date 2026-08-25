-- Schema + seed data for the 电子签章管理系统 (Electronic Seal Management System).
-- Reconstructed from the MyBatis-Plus entity/mapper definitions because the
-- repository does not ship a SQL dump. Idempotent: safe to re-run.
--
-- Usage: mysql -uroot -p123 mengs-bs < springboot/db/init.sql

CREATE DATABASE IF NOT EXISTS `mengs-bs` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `mengs-bs`;

-- ---------------------------------------------------------------------------
-- Tables
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` INT NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `name`    VARCHAR(64)  DEFAULT NULL COMMENT '权限名称',
  `grade`   VARCHAR(64)  DEFAULT NULL COMMENT '权限等级',
  `comment` VARCHAR(255) DEFAULT NULL COMMENT '权限说明',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

CREATE TABLE IF NOT EXISTS `menu` (
  `menu_id`   INT NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name`      VARCHAR(64)  DEFAULT NULL COMMENT '菜单名称',
  `path`      VARCHAR(128) DEFAULT NULL COMMENT '菜单路径',
  `comment`   VARCHAR(255) DEFAULT NULL COMMENT '说明',
  `icon`      VARCHAR(128) DEFAULT NULL COMMENT '菜单图标',
  `page_path` VARCHAR(128) DEFAULT NULL COMMENT '页面路径',
  `weight`    INT          DEFAULT NULL COMMENT '权重（排序使用）',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

CREATE TABLE IF NOT EXISTS `role_menu` (
  `role_id` INT DEFAULT NULL COMMENT '权限id',
  `menu_id` INT DEFAULT NULL COMMENT '菜单id',
  KEY `idx_role_menu_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限菜单表';

CREATE TABLE IF NOT EXISTS `user` (
  `user_id`     INT NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role_id`     INT          DEFAULT NULL COMMENT '权限id',
  `username`    VARCHAR(64)  DEFAULT NULL COMMENT '账号',
  `password`    VARCHAR(128) DEFAULT NULL COMMENT '密码',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `college`     VARCHAR(128) DEFAULT NULL COMMENT '归属',
  `email`       VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `phone`       VARCHAR(32)  DEFAULT NULL COMMENT '手机号',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
  `avatar_url`  VARCHAR(512) DEFAULT NULL COMMENT '头像地址',
  `is_delete`   VARCHAR(8)   DEFAULT NULL COMMENT '删除状态：1-删除 其他-未删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本表';

CREATE TABLE IF NOT EXISTS `usingseal` (
  `seal_id`     INT NOT NULL AUTO_INCREMENT COMMENT '用章id',
  `seal_name`   VARCHAR(128) DEFAULT NULL COMMENT '用章名称',
  `type`        VARCHAR(64)  DEFAULT NULL COMMENT '用章类型',
  `url_path`    VARCHAR(512) DEFAULT NULL COMMENT '用章图片地址',
  `state`       VARCHAR(32)  DEFAULT NULL COMMENT '状态:颁发 失效',
  `staff`       VARCHAR(64)  DEFAULT NULL COMMENT '用章人',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`seal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用章管理表';

CREATE TABLE IF NOT EXISTS `sendseal` (
  `send_id`         INT NOT NULL AUTO_INCREMENT COMMENT '申请签章id',
  `send_title`      VARCHAR(255) DEFAULT NULL,
  `user_id`         INT          DEFAULT NULL COMMENT '申请用户id',
  `seal_id`         INT          DEFAULT NULL COMMENT '用章id',
  `state`           VARCHAR(32)  DEFAULT NULL COMMENT '状态:0-初始 1-审批 2-加印(完成)',
  `remark`          VARCHAR(512) DEFAULT NULL COMMENT '申请描述',
  `rsa_system_id`   VARCHAR(128) DEFAULT NULL COMMENT 'RSA系统秘钥id',
  `rsa_user_id`     VARCHAR(128) DEFAULT NULL COMMENT 'RSA用户秘钥id',
  `origin_file_url` VARCHAR(512) DEFAULT NULL COMMENT '加印前文件',
  `origin_file_sha` VARCHAR(128) DEFAULT NULL COMMENT '加印前文件哈希SHA1',
  `new_file_url`    VARCHAR(512) DEFAULT NULL COMMENT '加印后文件',
  `new_file_sha`    VARCHAR(128) DEFAULT NULL COMMENT '加印后文件哈希SHA1',
  `approver`        INT          DEFAULT NULL COMMENT '审批人',
  `over`            INT          DEFAULT NULL COMMENT '加印人',
  `create_time`     DATETIME     DEFAULT NULL COMMENT '创建时间',
  `appr_time`       DATETIME     DEFAULT NULL COMMENT '审批时间',
  `over_time`       DATETIME     DEFAULT NULL COMMENT '用印时间',
  PRIMARY KEY (`send_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用章管理表';

CREATE TABLE IF NOT EXISTS `secretkey` (
  `key_id`      VARCHAR(128) NOT NULL COMMENT '秘钥id',
  `key_type`    INT          DEFAULT NULL COMMENT '秘钥类型',
  `key_public`  TEXT         DEFAULT NULL COMMENT '公钥',
  `key_private` TEXT         DEFAULT NULL COMMENT '私钥',
  `state`       VARCHAR(8)   DEFAULT NULL COMMENT '状态:0-有效 1-失效',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`key_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秘钥管理表';

-- ---------------------------------------------------------------------------
-- Seed data
-- ---------------------------------------------------------------------------
INSERT INTO `role` (`role_id`, `name`, `grade`, `comment`) VALUES
  (1,   '超级管理员', '1',   '系统超级管理员，拥有全部权限'),
  (99,  '普通用户',   '99',  '普通注册用户'),
  (100, '审批员',     '100', '负责签章审批')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `grade`=VALUES(`grade`), `comment`=VALUES(`comment`);

INSERT INTO `menu` (`menu_id`, `name`, `path`, `comment`, `icon`, `page_path`, `weight`) VALUES
  (1, '首页',       '/home',       '系统首页',   'el-icon-s-home',    'Home',        1),
  (2, '用户管理',   '/user',       '用户管理',   'el-icon-user',      'User',        2),
  (3, '角色管理',   '/role',       '角色管理',   'el-icon-s-check',   'Role',        3),
  (4, '菜单管理',   '/menu',       '菜单管理',   'el-icon-menu',      'Menu',        4),
  (5, '用章管理',   '/usingSeal',  '用章管理',   'el-icon-picture',   'UsingSeal',   5),
  (6, '用印申请',   '/sendSeal',   '用印申请',   'el-icon-document',  'SendSeal',    6),
  (7, '签章审批',   '/sealApprove','签章审批',   'el-icon-s-claim',   'SealApprove', 7),
  (8, '加印',       '/seal',       '加印',       'el-icon-edit',      'Seal',        8),
  (9, '验章',       '/verifyCA',   'CA验章',     'el-icon-circle-check','VerifyCA',  9)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `path`=VALUES(`path`), `comment`=VALUES(`comment`), `icon`=VALUES(`icon`), `page_path`=VALUES(`page_path`), `weight`=VALUES(`weight`);

-- Admin user (username: admin / password: 123)
INSERT INTO `user` (`user_id`, `role_id`, `username`, `password`, `nickname`, `college`, `email`, `phone`, `create_time`, `avatar_url`, `is_delete`) VALUES
  (1, 1, 'admin', '123', '超级管理员', '系统', 'admin@example.com', '13800000000', NOW(),
   'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png', '0')
ON DUPLICATE KEY UPDATE `role_id`=VALUES(`role_id`), `username`=VALUES(`username`), `password`=VALUES(`password`);

-- Grant the super admin (role 1) every menu.
DELETE FROM `role_menu` WHERE `role_id` = 1;
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
  (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9);

-- Normal users (role 99) get the home + seal application/verification menus.
DELETE FROM `role_menu` WHERE `role_id` = 99;
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
  (99,1),(99,6),(99,9);

-- Approvers (role 100) get home + approval menus.
DELETE FROM `role_menu` WHERE `role_id` = 100;
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
  (100,1),(100,7),(100,8),(100,9);

-- A sample seal so the 用章管理 page is not empty.
INSERT INTO `usingseal` (`seal_id`, `seal_name`, `type`, `url_path`, `state`, `staff`, `create_time`) VALUES
  (1, '公司公章', '公章', '', '颁发', 'admin', NOW())
ON DUPLICATE KEY UPDATE `seal_name`=VALUES(`seal_name`);
