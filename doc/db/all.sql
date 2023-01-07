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

insert into `download_list` (id, name, category_id1, description, download_count, size) values (1, '资料1', 000, '萨法扫了烦啊赛', 0, '1.00MB');
insert into `download_list` (id, name, category_id1, description, download_count, size) values (2, '资料2', 000, '扫地机手动分', 0, '1.00MB');
insert into `download_list` (id, name, category_id1, description, download_count, size) values (3, '资料3', 000, '苏秉承', 0, '1.00MB');
insert into `download_list` (id, name, category_id1, description, download_count, size) values (4, '资料4', 000, '哦词典恩特', 0, '1.00MB');
insert into `download_list` (id, name, category_id1, description, download_count, size) values (5, '资料5', 000, '单故障呢', 0, '1.00MB');
insert into `download_list` (id, name, category_id1, description, download_count, size) values (6, '资料6', 000, '我穿秒拒', 0, '1.00MB');