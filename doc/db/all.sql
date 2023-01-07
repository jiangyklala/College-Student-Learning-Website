drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) comment '名称',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `demo` (id, name) values (1, '测试');

drop table if exists `download_list`;
create table `download_list` (
                        `id` bigint not null comment 'id',
                        `name` varchar(25) not null comment '名称',
                        `category_id1` bigint not null comment '分类1',
                        `category_id2` bigint comment '分类2',
                        `description` varchar(100) comment '描述',
                        `download_count` int comment '下载量',
                        `size` varchar(25) comment '大小',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='下载列表';