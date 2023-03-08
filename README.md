### 网站应该具备的功能：

1. 首先拥有登陆的功能，这里到时候是采用公众号登陆的哦，然后是扫码登陆还是验证码登陆，这个到时候在说，因为一开始没有登陆功能也是可以滴。
2. 具备刷题的功能，具体可以参考牛客网这种，但是这个功能留着后面做了，未来半年内应该不会用到。
3. 具备播放视频的功能，资源放在腾讯云等地方
4. 具体积分系统，不过这个以后再说了。
5. 导航栏
6. 阅读目录



## 分类标签模块

### 分类表

```mysql
create table `category`
(
    `id`     bigint      not null comment 'id',
    `parent` bigint      not null default 0 comment '父id',
    `name`   varchar(50) not null comment '名称',
    `sort`   int comment '顺序',
    primary key (`id`),
  	index idx_name(`name`)
) engine = innodb
  default charset = utf8mb4 comment ='分类';
```

|  字段  |            解释            |
| :----: | :------------------------: |
| parent |      所属父分类的 id       |
|  sort  | 所有分类依据 sort 进行排序 |
|   id   |      默认和 sort 相等      |

注意：一级分类的 `parent` 默认为 0。

### 下载列表

```mysql
create table `download_list`(
                                `id`             bigint      not null comment 'id',
                                `name`           varchar(25) not null comment '名称',
                                `category_id1`   bigint      not null comment '分类1',
                                `category_id2`   bigint comment '分类2',
                                `download_count` int comment '下载量',
                                `size`           varchar(25) comment '大小',
                                `download_link`  varchar(500) comment '下载链接',
                                primary key (`id`),
                                index idx_name(`name`),
                                index idx_cid2(`category_id2`)
) engine = innodb
  default charset = utf8mb4 comment ='下载列表';
```

| 字段         | 解释           |
| ------------ | -------------- |
| category_id1 | 所属的一级分类 |
| category_id2 | 所属的二级分类 |

## 课程目录表 and 课程视频表

```mysql
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
```

```mysql
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
```

值得一提的是，其中 course_item 表中的 sort 逻辑上由四位整数组成：前两位代表 “第几章”，后两位代表 “第几节”。例如：`0103`：第一章第三节，其中第一个零可省略；`158`：第一章第五十八节。

两者的联系：

课程目录中的每一个课程，都对应着多个视频，两张表可通过 `course_list.name = course_item.course` 连接

<img src="https://xiaoj-1309630359.cos.ap-nanjing.myqcloud.com/202303051639281.png" style="zoom:50%;" />
