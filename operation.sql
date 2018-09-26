create table user
(
  user_id int auto_increment
    primary key,
  user_name varchar(50) default 'NULL' null comment '用户名',
  role tinyint default 'NULL' null comment '角色',
  password varchar(40) default '''NULL''' null comment '密码',
  created_at timestamp default current_timestamp() null,
  updated_at timestamp default current_timestamp() null on update current_timestamp(),
  constraint user_name
  unique (user_name)
)comment '用户表';
