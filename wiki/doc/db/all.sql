drop table if exists `test`;
create table `test` (
  `id` BIGINT NOT NULL comment 'id',
  `name` varchar(50) comment 'name',
  `password` varchar(50) comment 'password',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into `test` (id, name, password) values (2, 'test', 'ysz991203');


DROP TABLE IF EXISTS `demo`;
-- 创建优化后的表
CREATE TABLE `demo` (
                        `id` BIGINT NOT NULL COMMENT 'Unique identifier for the record',
                        `name` VARCHAR(50) NOT NULL COMMENT 'Name for the entity',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Table to store demo information';

-- 插入数据测试
INSERT INTO `demo` (id, name) VALUES (1, 'test');

drop table if exists `ebook`;

create table `ebook` (
                         `id` bigint not null comment 'id',
                         `name` varchar(50) comment '名称',
                         `category1_id` bigint comment '分类1',
                         `category2_id` bigint comment '分类2',
                         `description` varchar(200) comment '描述',
                         `cover` varchar(200) comment '封面',
                         `doc_count` int comment '文档数',
                         `view_count` int comment '阅读数',
                         `vote_count` int comment '点赞数',
                         primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='电子书';

insert into `ebook` (id, name, description) values
                                                (1, 'Spring Boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架'),
                                                (2, 'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳前端框架'),
                                                (3, 'MySQL 数据库教程', '系统学习 MySQL 数据库基础与进阶操作'),
                                                (4, 'Python 编程入门', '面向初学者的 Python 编程实战教程'),
                                                (5, 'JavaScript 高级编程', '深入理解 JavaScript 核心机制与高级应用'),
                                                (6, 'HTML + CSS 快速入门', '前端页面开发必备技能组合教程'),
                                                (7, 'Linux 运维基础教程', '掌握 Linux 系统管理与常用命令'),
                                                (8, 'Docker 容器化部署', '学习使用 Docker 构建和部署应用'),
                                                (9, 'Redis 核心技术解析', '深入理解 Redis 高性能缓存机制与应用场景'),
                                                (10, 'Spring Cloud 微服务实战', '构建企业级微服务架构的完整解决方案'),
                                                (11, 'Kubernetes 容器编排教程', '使用 Kubernetes 实现容器集群的管理与部署'),
                                                (12, 'TypeScript 全面指南', '从 JavaScript 升级到 TypeScript，提升代码可维护性');
