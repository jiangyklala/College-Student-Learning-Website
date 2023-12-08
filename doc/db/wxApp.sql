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

ALTER TABLE wx_user_info
    CHANGE other_2 type int DEFAULT 0 NOT NULL COMMENT '用户类型';

ALTER TABLE wx_user_info
    CHANGE other_1 name varchar(32) UNIQUE NOT NULL COMMENT '用户类型';

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
    `id`               integer AUTO_INCREMENT NOT NULL,
    `title`            varchar(128)           NOT NULL COMMENT '题目标识',
    `category_id`      bigint                 NOT NULL COMMENT '分类',
    `answer_id`        bigint                 NOT NULL COMMENT '答案表项ID',
    `like`             integer DEFAULT 0 COMMENT '点赞数',
    `collect`          integer DEFAULT 0 COMMENT '收藏数',
    `points`           integer                NOT NULL COMMENT '所需积分数',
    `importance_level` integer DEFAULT 1 COMMENT '重要程度',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '题库表';

alter table wx_question
    change column category_id
        category_id int not null;

# 题目答案表
DROP TABLE IF EXISTS `wx_question_answer`;

CREATE TABLE `wx_question_answer`
(
    `id`     bigint     NOT NULL COMMENT '雪花ID',
    `answer` mediumtext NOT NULL COMMENT '答案',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '题目答案表';

SELECT points
FROM wx_user_info
WHERE id = 1;

UPDATE wx_user_info
SET points = points - 10
WHERE id = 1;

# 用户题目信息记录表
DROP TABLE IF EXISTS `question_user_info`;

CREATE TABLE `question_user_info`
(
    `id`            integer AUTO_INCREMENT NOT NULL,
    `user_id`       integer                NOT NULL COMMENT '用户ID',
    `payed_id_set`  BLOB                   NOT NULL COMMENT '已付费题目集合',
    `marked_id_set` BLOB                   NOT NULL COMMENT '收藏题目集合',
    `other`         BLOB COMMENT 'other',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`user_id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户题目信息记录表';

# 触发器: 题目表增加一道题, 就在其对应分类下的 total 字段 + 1
DELIMITER //
CREATE TRIGGER incr_category_total
    AFTER INSERT
    ON wx_question
    FOR EACH ROW
BEGIN
    -- 获取插入行的信息
    DECLARE category_id BIGINT;
    SET category_id = NEW.category_id;

    -- 在这里执行更新操作
    UPDATE category SET total = total + 1 WHERE id = category_id;
END;
//
DELIMITER ;

SHOW TRIGGERS FROM yiti_dev_101;

UPDATE wx_question
SET points = 30
WHERE category_id BETWEEN 5 AND 79;
