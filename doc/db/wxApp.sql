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
WHERE open_id = '1111';

# 题库表
DROP TABLE IF EXISTS `wx_question`;

CREATE TABLE `wx_question`
(
    `id`       integer AUTO_INCREMENT NOT NULL,
    `title`    varchar(128)           NOT NULL COMMENT '题目标识',
    `category` bigint                 NOT NULL COMMENT '分类',
    `answer`   mediumtext             NOT NULL COMMENT '答案',
    `like`     integer DEFAULT 0 COMMENT '点赞数',
    `collect`  integer DEFAULT 0 COMMENT '收藏数',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '题库表';
