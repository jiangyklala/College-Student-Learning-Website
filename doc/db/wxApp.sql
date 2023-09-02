# 微信用户记录表
DROP TABLE IF EXISTS `wx_user_info`;

CREATE TABLE `wx_user_info`
(
    `open_id` varchar(32)  NOT NULL COMMENT '用户唯一标识',
    `points`  integer      NOT NULL COMMENT '用户积分',
    `other_1` varchar(100) NOT NULL,
    `other_2` integer      NOT NULL,
    PRIMARY KEY (`open_id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '微信用户记录表';