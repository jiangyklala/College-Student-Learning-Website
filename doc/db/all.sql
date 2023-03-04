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
                                `download_count` int comment '下载量',
                                `size`           varchar(25) comment '大小',
                                `download_link`  varchar(500) comment '下载链接',
                                primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='下载列表';

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
    `category_id1` bigint      not null comment '分类1',
    `category_id2` bigint comment '分类2',
    `click_count`  int comment '点击量',
    `description`  varchar(100) comment '课程描述',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='课程目录表';
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
