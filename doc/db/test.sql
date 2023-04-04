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

# 测试 varchar(n) 的最大字段
DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
    `name` varchar(65533) NOT NULL) ENGINE = innodb DEFAULT CHARACTER SET = ascii ROW_FORMAT = compact;
# 255 + 1 + 65277 + 2 = 65535

