### 网站应该具备的功能：

+ [ ] 首先拥有登陆的功能，这里到时候是采用公众号登陆的哦，然后是扫码登陆还是验证码登陆，这个到时候在说，因为一开始没有登陆功能也是可以滴。
+ [ ] 具备刷题的功能，具体可以参考牛客网这种，但是这个功能留着后面做了，未来半年内应该不会用到。
+ [x] 具备播放视频的功能，资源放在腾讯云等地方
+ [ ] 具体积分系统，不过这个以后再说了。
+ [x] 导航栏
+ [x] 阅读目录
+ [ ] GPT 翻译，copy 等等功能
+ [x] GPT 接口 jar 包重构
+ [ ] GPT 连接线程池
+ [x] 邮箱注册（前期需要有邀请码）
+ [ ] GPT 每日使用次数，需要积分（每日签到弄）
+ [x] 历史记录 30 条（自定义 mapper or pagehelper） 
+ [ ] MySQL，历史记录定时清理
+ [ ] 每次提问花费提问次数
+ [ ] 画图扣十次
+ [x] 用户加上用户类型，比如会员，永久会员，普通用户
+ [ ] 先写个简单的隐藏页面，可以用来充值次数，比如输入用户ID，然后输入充值次数，那么就会充值进去



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

    



## ChatGPT - 3.5

### 用户「对话记录表」设计

![](https://xiaoj-1309630359.cos.ap-nanjing.myqcloud.com/202304072123514.png)



+ 未登录状态下不能使用



设计中遇到的问题：用户提问时需要扣除相应的提问次数，那怎么样设计才能让用户点开右上角的账户详情时，实时查看剩余的提问词数？

+ 扣除完提问次数后，将 userInfo 设为空，这样用户再次刷新页面时，系统会判定此次为第一次登录，会重新查 user 信息存入 userInfo。缺点：需要重新刷新页面
+ 专门写一个函数，在每个可能更改用户余额的操作后，追加一个重新查 userInfo 的操作
+ 只有在用户点击右上角的用户详情时，才会去重新查 user 的信息 ✓ 
  + 进一步优化：可以在前端的 userInfo 中添加一个修改次数（modcount）字段，只有当 modcount 不为 0 时才去重新查。当然，这需要在可能使 userInfo 变化的代码后面添加 ++modcount
  + 增加接口访问次数限制（通用模块）
