# 大学生学习网站

### 具备的功能：

+ 课程资料下载

+ 课程视频在线播放

+ 专栏阅读
+ GPT-3.5 对话

由于目前只暴露了 GPT 的功能：[https://study.playoffer.cn/](https://study.playoffer.cn/)

其它功能地址如下：

+ 课程资料下载：[这里](https://study.playoffer.cn/download/Download)

+ 课程视频在线播放：[这里](https://study.playoffer.cn/course/Course)

+ 专栏阅读：[这里](https://study.playoffer.cn/column/Column)
+ 课程资料下载管理：[这里](https://study.playoffer.cn/admin/AdminDownload)

+ 课程视频在线播放管理：[这里](https://study.playoffer.cn/admin/AdminCourse)

+ 专栏阅读管理：[这里](https://study.playoffer.cn/admin/AdminColumn)
+ 分类标签管理：[这里](https://study.playoffer.cn/admin/AdminCategory)

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

![](https://xiaoj-1309630359.cos.ap-nanjing.myqcloud.com/202303051639281.png)

## 用户登录模块

### 用户表设计

登录模块除了传统的账号密码登录以外，还加入了 GitHub 登录。

登录/注册逻辑：

![](https://xiaoj-1309630359.cos.ap-nanjing.myqcloud.com/202304072100370.png)

#### 注册流程：

首先验证：账号密码是否符合要求。接着进行邮箱验证：向用户注册的邮箱发送激活邮件，用户需要点击激活链接，才能完成注册（插入一条 user 记录）

注意：

  + 发送激活邮件的时间间隔：60s

  + 邮箱格式验证

  + 邮件发送应该创建一个新的线程 ？

  + 邮箱验证的具体逻辑：开始向验证表插入一条记录：「激活码（String）」 和 「激活状态（Boolean）」，然后向用户邮箱发送验证邮件，用户需要点击 “激活”，将本次注册对应的激活状态设置为 true。

  + 注册提交时，后端需检测 “激活码” 的 “激活状态”

