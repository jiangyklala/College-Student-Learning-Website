drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) comment '名称',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `demo` (id, name) values (1, '测试');

# 下载列表
drop table if exists `download_list`;
create table `download_list`(
                                `id`             bigint      not null comment 'id',
                                `name`           varchar(25) not null comment '名称',
                                `category_id1`   bigint      not null comment '分类1',
                                `category_id2`   bigint comment '分类2',
                                `download_count` int  default 0 comment '下载量',
                                `size`           varchar(25) default '' comment '大小',
                                `download_link`  varchar(500)  default '' comment '下载链接',
                                primary key (`id`),
                                index idx_name(`name`),
                                index idx_cid2(`category_id2`)
) engine = innodb
  default charset = utf8mb4 comment ='下载列表';

alter table download_list
    change column download_count
        download_count int  default 0 not null;

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
    `id`     bigint      not null comment 'id',
    `parent` bigint      not null default 0 comment '父id',
    `name`   varchar(50) not null comment '名称',
    `sort`   int comment '顺序',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='分类';

insert into `category` (id, parent, name, sort)
values (100, 000, '前端开发', 100);
insert into `category` (id, parent, name, sort)
values (103, 100, 'Vue', 103);
insert into `category` (id, parent, name, sort)
values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort)
values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort)
values (201, 200, '基础应用', 201);
insert into `category` (id, parent, name, sort)
values (202, 200, '框架应用', 202);
insert into `category` (id, parent, name, sort)
values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort)
values (301, 300, '基础应用', 301);
insert into `category` (id, parent, name, sort)
values (302, 300, '进阶方向应用', 302);
insert into `category` (id, parent, name, sort)
values (400, 000, '数据库', 400);
insert into `category` (id, parent, name, sort)
values (401, 400, 'Mysql', 401);
insert into `category` (id, parent, name, sort)
values (500, 000, '其它', 500);
insert into `category` (id, parent, name, sort)
values (501, 500, '服务器', 501);
insert into `category` (id, parent, name, sort)
values (502, 500, '开发工具', 502);
insert into `category` (id, parent, name, sort)
values (503, 500, '热门服务端语言', 503);

# 课程目录表
drop table if exists `course_list`;
create table `course_list`
(
    `id`           bigint      not null comment 'id',
    `name`         varchar(25) not null comment '课程名称',
    `avatar_link`  varchar(100) comment '封面',
    `category_id1` bigint      not null default 0 comment '分类1',
    `category_id2` bigint comment '分类2',
    `click_count`  int not null default 0 comment '点击量',
    `description`  varchar(100) comment '课程描述',
    primary key (`id`),
    index idx_name(`name`),
    index idx_category(`category_id2`)
) engine = innodb
  default charset = utf8mb4 comment ='课程目录表';

alter table course_list
    change column click_count
        click_count int  default 0 not null;

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
values (5, '视频5', 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png', 000, 0, 15, '人马森是哦阿森');

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
create table `column_list` (
                         `id` bigint not null comment 'id',
                         `name` varchar(50) not null default '' comment '专栏名称',
                         `category_id1` bigint not null default 0 comment '分类1',
                         `category_id2` bigint comment '分类2',
                         `description` varchar(200) default '' comment '描述',
                         `avatar_link` varchar(200) default '' comment '封面',
                         `doc_count` int comment '所含文档数',
                         `view_count` int comment '阅读数',
                         `vote_count` int comment '点赞数',
                         primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='专栏列表';

insert into `column_list` (id, name, description) values (1, 'SpringBoot入门教程', '零基础入门Java开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description) values (2, 'Vue 入门教程', '零基础入门 Vue开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description) values (3, 'Python 入门教程', '零基础入门 Python开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description) values (4, 'Mysql 入门教程', '零基础入门 Mysql开发,企业级应用开发最佳首选框架');
insert into `column_list` (id, name, description) values (5, 'Oracle 入门教程', '零基础入门 Oracle开发,企业级应用开发最佳首选框架');

-- 文档表
drop table if exists `doc`;
create table `doc` (
                       `id` bigint not null comment 'id',
                       `column_id` bigint not null default 0 comment '所属专栏id',
                       `parent` bigint not null default 0 comment '父id',
                       `name` varchar(50) not null comment '名称',
                       `sort` int comment '顺序',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档表';

insert into `doc` (id, column_id, parent, name, sort) values (1, 1, 0, '文档1', 1);
insert into `doc` (id, column_id, parent, name, sort) values (2, 1, 1, '文档1.1', 1);
insert into `doc` (id, column_id, parent, name, sort) values (3, 1, 0, '文档2', 2);
insert into `doc` (id, column_id, parent, name, sort) values (4, 1, 3, '文档2.1', 1);
insert into `doc` (id, column_id, parent, name, sort) values (5, 1, 3, '文档2.2', 2);
insert into `doc` (id, column_id, parent, name, sort) values (6, 1, 5, '文档2.2.1', 1);

# 文档内容表, 大字段分表
drop table if exists `doc_content`;
create table `doc_content` (
                       `id` bigint not null comment 'id',
                       `content` mediumtext not null comment '文档内容',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档内容表';

-- 用户表
drop table if exists `user`;
create table `user` (
                        `id` bigint auto_increment not null,
                        `username` varchar(50) default('新用户') comment '用户名',
                        `useraccount` varchar(50)  comment '用户账号',
                        `password` char(32)  comment '用户密码',
                        `salt` varchar(50)  default('') comment '密码盐值',
                        `github_id` varchar(50) comment 'github_id',
                        `email` varchar(50) default(NULL) comment '邮箱',
                        `balance` bigint default 0 comment '用户余额',
                        `others` varchar(50) comment '其它',
                        primary key (`id`),
                        index idx_username(`username`),
                        index idx_useraccount(`useraccount`),
                        index idx_github_id(`github_id`)
) engine=innodb default charset=utf8mb4 comment='普通用户表';
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

drop table if exists `disallow_word`;
create table `disallow_word` (
                                 `id` bigint auto_increment not null,
                                 `value` varchar(50) not null comment '敏感字',
                                 `type` int default(0) comment '类型',
                                 primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='敏感词库';

insert into disallow_word(value) values ('尼玛');
insert into disallow_word(value) values ('站长');
insert into disallow_word(value) values ('国家领导人');
insert into disallow_word(value) values ('操');
insert into disallow_word(value) values ('lala');


-- 测试表
drop table if exists `test`;
create table `test` (
                        `id` int auto_increment,
                        `A` varchar(20),
                        `B` varchar(20),
                        primary key (`id`),
                        index idx_A_B(`A`, `B`)
) engine=innodb default charset=utf8mb4;

insert into test(A, B) values ('a', 'b');
insert into test(A, B) values ('a', 'bb');
insert into test(A, B) values ('a', 'c');
insert into test(A, B) values ('a', 'cc');
insert into test(A, B) values ('b', 'd');

explain select * from test where A like 'a' and B = 'bb%';

show processlist;
show variables like 'max_connections';