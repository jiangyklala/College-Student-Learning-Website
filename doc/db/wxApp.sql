# 微信用户记录表
DROP TABLE IF EXISTS `wx_user_info`;

CREATE TABLE `wx_user_info`
(
    `id`      integer AUTO_INCREMENT NOT NULL,
    `open_id` varchar(32)            NOT NULL COMMENT '用户唯一标识',
    `points`  integer                NOT NULL COMMENT '用户积分',
    `other_1` varchar(100),
    `other_2` integer,
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '微信用户记录表';

# 检查某个列的某个字段是否存在的高性能写法
SELECT 1
FROM wx_user_info
WHERE open_id = '111'
LIMIT 1;

SELECT *
FROM wx_user_info
WHERE open_id = "1111"
