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