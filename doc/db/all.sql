# 查看某个表状态
SHOW TABLE STATUS LIKE 'chat_history';



drop table if exists `demo`;
create table `demo`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='测试';

insert into `demo` (id, name)
values (1, '测试');

# 下载列表
drop table if exists `download_list`;
create table `download_list`
(
    `id`             bigint      not null comment 'id',
    `name`           varchar(25) not null comment '名称',
    `category_id1`   bigint      not null comment '分类1',
    `category_id2`   bigint comment '分类2',
    `download_count` int          default 0 comment '下载量',
    `size`           varchar(25)  default '' comment '大小',
    `download_link`  varchar(500) default '' comment '下载链接',
    primary key (`id`),
    index idx_name (`name`),
    index idx_cid2 (`category_id2`)
) engine = innodb
  default charset = utf8mb4 comment ='下载列表';

alter table download_list
    change column download_count
        download_count int default 0 not null;

alter table download_list
    change column size
        size varchar(25) default '';

alter table download_list
    change column download_link
        download_link varchar(500) default '' not null;

insert into `download_list` (id, name, category_id1, download_count, size)
values (1, '资料1', 000, 0, '1.00MB');
insert into `download_list` (id, name, category_id1, download_count, size)
values (2, '资料2', 000, 0, '1.00MB');
insert into `download_list` (id, name, category_id1, download_count, size)
values (3, '资料3', 000, 0, '1.00MB');
insert into `download_list` (id, name, category_id1, download_count, size)
values (4, '资料4', 000, 0, '1.00MB');
insert into `download_list` (id, name, category_id1, download_count, size)
values (5, '资料5', 000, 0, '1.00MB');
insert into `download_list` (id, name, category_id1, download_count, size)
values (6, '资料6', 000, 0, '1.00MB');

# 分类表
drop table if exists `category`;
create table `category`
(
    `id`     int auto_increment not null comment 'id',
    `parent` int                not null default 0 comment '父id',
    `name`   varchar(50)        not null comment '名称',
    `level`  int                         default 0 comment '层级',
    `type`   int                not null comment '分类的类型, 小程序专用的, 网站专用的',
    `total`  int                         default 0 comment '该分类下的题目总数',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='分类';

# 增加 avatar_link 列
ALTER TABLE category
    ADD avatar_link varchar(100) default '' COMMENT '该分类对应的图片';

UPDATE category
SET level = 0
WHERE parent = 0;



UPDATE category
SET total = (SELECT COUNT(*)
             FROM wx_question
             WHERE category_id = category.id);


# 课程目录表
drop table if exists `course_list`;
create table `course_list`
(
    `id`           bigint      not null comment 'id',
    `name`         varchar(25) not null comment '课程名称',
    `avatar_link`  varchar(100) comment '封面',
    `category_id1` bigint      not null default 0 comment '分类1',
    `category_id2` bigint comment '分类2',
    `click_count`  int         not null default 0 comment '点击量',
    `description`  varchar(100) comment '课程描述',
    primary key (`id`),
    index idx_name (`name`),
    index idx_category (`category_id2`)
) engine = innodb
  default charset = utf8mb4 comment ='课程目录表';

alter table course_list
    change column click_count
        click_count int default 0 not null;

alter table course_list
    change column category_id1
        category_id1 bigint default 0 not null;

insert into `course_list` (id, name, avatar_link, category_id1, category_id2, click_count,
                           description)
values (1, '视频1', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15, '先看啊粉丝哦b');
insert into `course_list` (id, name, avatar_link, category_id1, category_id2, click_count,
                           description)
values (2, '视频2', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15, '撒扥初步不啊');
insert into `course_list` (id, name, avatar_link, category_id1, category_id2, click_count,
                           description)
values (3, '视频3', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15, '身上或适当懂');
insert into `course_list` (id, name, avatar_link, category_id1, category_id2, click_count,
                           description)
values (4, '视频4', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15, '不制动哦哈');
insert into `course_list` (id, name, avatar_link, category_id1, category_id2, click_count,
                           description)
values (5, '视频5', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15,
        '人马森是哦阿森');

# 课程项表  -- 是否需要留一列做预备
drop table if exists `course_item`;
create table `course_item`
(
    `id`          bigint       not null comment 'id',
    `course`      varchar(25)  not null comment '所属课程名称',
    `sort`        int          not null comment '排序',
    `video_link`  varchar(500) not null comment '视频链接',
    `description` varchar(50) default '' comment '这节课的描述信息',
    primary key (`course`, `sort`),
    index idx_course (`course`),
    index idx_id (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='课程视频表';

insert into `course_item` (id, course, sort, video_link)
values (1, '视频1', 101, 'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/1231223123123.mp4');
insert into `course_item` (id, course, sort, video_link)
values (2, '视频1', 102, 'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/wrwerw.mp4');
insert into `course_item` (id, course, sort, video_link)
values (3, '视频3', 201, 'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/4097_1667126621.mp4');
insert into `course_item` (id, course, sort, video_link)
values (4, '视频4', 301, 'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/4097_1667126621.mp4');
insert into `course_item` (id, course, sort, video_link)
values (5, '视频5', 401, 'https://yiti-download-1309630359.cos.ap-shanghai.myqcloud.com/4097_1667126621.mp4');

# 专栏列表
drop table if exists `column_list`;
create table `column_list`
(
    `id`           bigint      not null comment 'id',
    `name`         varchar(50) not null default '' comment '专栏名称',
    `category_id1` bigint      not null default 0 comment '分类1',
    `category_id2` bigint comment '分类2',
    `description`  varchar(200)         default '' comment '描述',
    `avatar_link`  varchar(200)         default '' comment '封面',
    `doc_count`    int comment '所含文档数',
    `view_count`   int comment '阅读数',
    `vote_count`   int comment '点赞数',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='专栏列表';

insert into `column_list` (id, name, description)
values (1, 'SpringBoot入门教程', '零基础入门Java开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description)
values (2, 'Vue 入门教程', '零基础入门 Vue开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description)
values (3, 'Python 入门教程', '零基础入门 Python开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description)
values (4, 'Mysql 入门教程', '零基础入门 Mysql开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description)
values (5, 'Oracle 入门教程', '零基础入门 Oracle开发,企业级应用开发最佳首选框架');

-- 文档表
drop table if exists `doc`;
create table `doc`
(
    `id`        bigint      not null comment 'id',
    `column_id` bigint      not null default 0 comment '所属专栏id',
    `parent`    bigint      not null default 0 comment '父id',
    `name`      varchar(50) not null comment '名称',
    `sort`      int comment '顺序',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='文档表';

insert into `doc` (id, column_id, parent, name, sort)
values (1, 1, 0, '文档1', 1);
insert into `doc` (id, column_id, parent, name, sort)
values (2, 1, 1, '文档1.1', 1);
insert into `doc` (id, column_id, parent, name, sort)
values (3, 1, 0, '文档2', 2);
insert into `doc` (id, column_id, parent, name, sort)
values (4, 1, 3, '文档2.1', 1);
insert into `doc` (id, column_id, parent, name, sort)
values (5, 1, 3, '文档2.2', 2);
insert into `doc` (id, column_id, parent, name, sort)
values (6, 1, 5, '文档2.2.1', 1);

# 文档内容表, 大字段分表
drop table if exists `doc_content`;
create table `doc_content`
(
    `id`      bigint     not null comment 'id',
    `content` mediumtext not null comment '文档内容',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='文档内容表';

-- 用户表
drop table if exists `user`;
create table `user`
(
    `id`          bigint auto_increment not null,
    `username`    varchar(50)        default ('新用户') comment '用户名',
    `useraccount` varchar(50) comment '用户账号',
    `password`    char(32) comment '用户密码',
    `salt`        varchar(50)        default ('') comment '密码盐值',
    `github_id`   varchar(50) comment 'github_id',
    `email`       varchar(50) unique default (NULL) comment '邮箱',
    `balance`     bigint             default 0 comment '用户余额',
    `others`      varchar(50) comment '其它',
    primary key (`id`),
    index idx_username (`username`),
    index idx_useraccount (`useraccount`),
    index idx_github_id (`github_id`)
) engine = innodb
  default charset = utf8mb4 comment ='普通用户表';

ALTER TABLE user
    CHANGE others type int default 0 COMMENT '用户类型';

ALTER TABLE user
    ADD invite_balance int default 0 COMMENT '邀请余额';
ALTER TABLE user
    DROP invite_balance;


#
# -- 用户表
# drop table if exists `user_github`;
# create table `user_github` (
#                         `id` bigint auto_increment not null,
#                         `github_id` varchar(50) not null comment 'github_id',
#                         `others` varchar(50) default(NULL) comment '其它',
#                         primary key (`id`)
# ) engine=innodb default charset=utf8mb4 comment='github用户表';
#
# -- 用户表
# drop table if exists `user_info`;
# create table `user_info` (
#                         `id` bigint auto_increment not null,
#                         `balance` bigint not null default 0 comment '用户余额',
#                         `email` varchar(50) default(NULL) comment '邮箱',
#                         primary key (`id`)
# ) engine=innodb default charset=utf8mb4 comment='用户信息表';

# 敏感词表
drop table if exists `disallow_word`;
create table `disallow_word`
(
    `id`    bigint auto_increment not null,
    `value` varchar(50)           not null comment '敏感字',
    `type`  int default (0) comment '类型',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='敏感词库';

insert into disallow_word(value)
values ('尼玛');
insert into disallow_word(value)
values ('站长');
insert into disallow_word(value)
values ('国家领导人');
insert into disallow_word(value)
values ('操');
insert into disallow_word(value)
values ('lala');

# chatGPT 用户历史查询信息记录 1.0
DROP TABLE IF EXISTS `chat_history`;

CREATE TABLE `chat_history`
(
    `id`         bigint AUTO_INCREMENT NOT NULL,
    `user_id`    bigint                NOT NULL COMMENT '所属用户ID',
    `title`      varchar(50) COMMENT '对话标题',
    `content_id` bigint                NOT NULL COMMENT '对话内容',
    PRIMARY KEY (`id`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 用户历史查询信息记录';

ALTER TABLE chat_history
    ADD total_token bigint;

# 保留按 content_id 字段排序的前 20 条数据，并删除其它数据
DELETE
FROM chat_history
WHERE user_id = 2658810919845888
  and content_id NOT IN (SELECT content_id
                         FROM (SELECT content_id
                               FROM chat_history
                               where user_id = 2658810919845888
                               ORDER BY content_id DESC
                               LIMIT 20) AS subquery);

SELECT *
FROM chat_history
WHERE user_id = 2658810919845888
ORDER BY content_id DESC
LIMIT 20;

# chatGPT 用户历史查询信息记录的 content 字段
DROP TABLE IF EXISTS `chat_history_content`;

CREATE TABLE `chat_history_content`
(
    `id`      bigint NOT NULL,
    `content` mediumtext COMMENT '文档内容',
    PRIMARY KEY (`id`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 用户历史查询信息记录的 content 字段';

# 邮箱激活表
DROP TABLE IF EXISTS `email_active`;

CREATE TABLE `email_active`
(
    `email`  varchar(50) COMMENT '对话标题',
    `active` boolean COMMENT '此邮箱是否被激活',
    PRIMARY KEY (`email`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 用户历史查询信息记录';

update user
set balance = balance - 10
where id = 2658810919845888;

update user
set balance = balance + 6
where id = 2631422762942464;


# chatGPT 信息记录表
DROP TABLE IF EXISTS `chat_record_info`;

create table chat_record_info
(
    id      bigint auto_increment
        primary key,
    date    varchar(50) default (NULL) null,
    ntimes  varchar(50) default (0)    not null,
    ntokens varchar(50) default (0)    not null,
    vtimes  varchar(50) default (0)    not null,
    vtokens varchar(50) default (0)    not null,
    iVtimes varchar(50) default (0)    not null
)
    comment 'chatGPT 用户历史查询信息记录' charset = utf8mb4;

ALTER TABLE chat_record_info
    CHANGE ntimes ntimes varchar(50) default (0);
ALTER TABLE chat_record_info
    CHANGE ntokens ntokens varchar(50) default (0);
ALTER TABLE chat_record_info
    CHANGE vtimes vtimes varchar(50) default (0);
ALTER TABLE chat_record_info
    CHANGE vtokens vtokens varchar(50) default (0);
ALTER TABLE chat_record_info
    CHANGE iVtimes iVtimes varchar(50) default (0);

DESCRIBE chat_record_info;

ALTER TABLE chat_record_info
    ADD CONSTRAINT uq_date UNIQUE (date);


# chatGPT 邀请人总信息表
DROP TABLE IF EXISTS `gpt_inviter`;

CREATE TABLE `gpt_inviter`
(
    `inviter_id`     bigint NOT NULL,
    `invited_count`  int default 0,
    `invite_balance` int default 0,
    `earn_rate`      int default 20,
    `earnings`       int default 0,
    PRIMARY KEY (`inviter_id`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 邀请人总信息表';

ALTER TABLE gpt_inviter
    CHANGE invite_balance invite_balance decimal(20, 2) default 0.00 COMMENT '待提现佣金';
ALTER TABLE gpt_inviter
    CHANGE earnings earnings decimal(20, 2) default 0.00 COMMENT '已提现金额';

UPDATE gpt_inviter
SET earn_rate = 40;
# 将所有人的佣金比例设置为 40%


# chatGPT 邀请表
DROP TABLE IF EXISTS `gpt_invite_code`;

CREATE TABLE `gpt_invite_code`
(
    `id`          bigint AUTO_INCREMENT NOT NULL,
    `invite_code` varchar(10) DEFAULT (NULL),
    `inviter_id`  bigint                NOT NULL,
    `create_time` DATE,
    FOREIGN KEY (`inviter_id`) REFERENCES gpt_inviter (`inviter_id`),
    PRIMARY KEY (`id`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 邀请表';

# chatGPT 邀请对应表
DROP TABLE IF EXISTS `gpt_invitee`;

CREATE TABLE `gpt_invitee`
(
    `id`          bigint AUTO_INCREMENT NOT NULL,
    `invitee_id`  bigint                NOT NULL,
    `inviter_id`  bigint                NOT NULL,
    `kind`        int DEFAULT 0,
    `count`       int DEFAULT 0,
    `create_time` DATE,
    FOREIGN KEY (`inviter_id`) REFERENCES gpt_inviter (`inviter_id`),
    PRIMARY KEY (`id`)
) engine = innodb
  DEFAULT charset = utf8mb4 COMMENT = 'chatGPT 邀请对应表';

ALTER TABLE gpt_invitee
    ADD inviter_name varchar(50) NOT NULL;
ALTER TABLE gpt_invitee
    DROP inviter_name;
ALTER TABLE gpt_invitee
    CHANGE count count varchar(10) default '0' COMMENT '数额';


# GPT 支付信息表
DROP TABLE IF EXISTS `gpt_pay_info`;

CREATE TABLE `gpt_pay_info`
(
    `id`              bigint AUTO_INCREMENT NOT NULL,
    `user_id`         bigint                NOT NULL COMMENT '用户ID',
    `order_no`        bigint                NOT NULL COMMENT '订单号',
    `pay_amount`      decimal(20, 2)        NOT NULL COMMENT '支付金额',
    `platform_number` varchar(200) DEFAULT NULL COMMENT '流水号',
    `platform_status` varchar(20)  DEFAULT NULL COMMENT '支付状态',
    `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uqe_order_no` (`order_no`),
    UNIQUE KEY `uqe_platform_number` (`platform_number`),
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = 'GPT 支付信息表';

SELECT *
FROM gpt_pay_info
WHERE DATE(create_time) BETWEEN '2023-07-01' AND '2023-08-03';

# 题目信息表
DROP TABLE IF EXISTS `question_detail`;

CREATE TABLE `question_detail`
(
    `id`           bigint AUTO_INCREMENT NOT NULL,
    `name`         varchar(20)           NOT NULL COMMENT '题目名称',
    `type`         int                   NOT NULL COMMENT '题目类型',
    `level`        int                            DEFAULT 1 COMMENT '题目难度',
    `category_id1` bigint                NOT NULL DEFAULT 0 COMMENT '分类1',
    `category_id2` bigint COMMENT '分类2',
    `content`      varchar(500)          NOT NULL DEFAULT '{}' COMMENT '题目的内容, 为 JSON 格式',
    `answer`       int                   NOT NULL COMMENT '题目答案',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '题目信息表';


# 用户刷题信息记录表
DROP TABLE IF EXISTS `practice_user`;

CREATE TABLE `practice_user`
(
    `user_id`       bigint NOT NULL,
    `settings_obj`  BLOB   NOT NULL COMMENT '刷题设置',
    `done_id_list`  BLOB   NOT NULL COMMENT '已刷题目',
    `wrong_id_list` BLOB   NOT NULL COMMENT '错误题目',
    `mark_id_list`  BLOB   NOT NULL COMMENT '收藏题目',
    `other`         BLOB COMMENT 'other',
    PRIMARY KEY (`user_id`)
) ENGINE = innodb
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户刷题信息记录表';


EXPLAIN
select *
from question_detail
where id not in (3072529551851520)
limit 4;

SELECT COUNT(*)
FROM user
WHERE balance < 0; # 9.11 - 65 条