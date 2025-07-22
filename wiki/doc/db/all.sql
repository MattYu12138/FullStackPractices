drop table if exists `test`;
create table `test` (
  `id` BIGINT NOT NULL comment 'id',
  `name` varchar(50) comment 'name',
  `password` varchar(50) comment 'password',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `test` (id, name, password) values (2, 'test', 'ysz991203');

SHOW GRANTS FOR 'wiki'@'%';


