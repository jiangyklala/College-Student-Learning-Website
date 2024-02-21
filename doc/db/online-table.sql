#
招聘信息表
DROP TABLE IF EXISTS `recruit_info`;

CREATE TABLE `recruit_info`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT,
    `company`                  VARCHAR(50)  NOT NULL COMMENT '公司名称',
    `recruitment_target`       VARCHAR(50)  NOT NULL COMMENT '招聘对象',
    `start_date`               VARCHAR(50)  NOT NULL COMMENT '开始时间',
    `distance_from_start_date` VARCHAR(50)  NOT NULL COMMENT '距离开始时间',
    `end_date`                 VARCHAR(50) DEFAULT '' COMMENT '结束时间',
    `distance_from_end_date`   VARCHAR(50) DEFAULT '' COMMENT '距离结束时间',
    `city`                     BLOB COMMENT '公司所在城市-list',
    `city_nature`              VARCHAR(50)  NOT NULL COMMENT '公司性质',
    `deliver_address`          VARCHAR(500) NOT NULL COMMENT '投递地址',
    `extrapolation`            VARCHAR(500) NOT NULL COMMENT '内推地址',
    `important_events`         VARCHAR(500) NOT NULL COMMENT '重要事件',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  CHARSET = utf8mb4 COMMENT '招聘信息表';