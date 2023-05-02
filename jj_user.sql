create table user
(
    name        varchar(255) default ' admin' not null comment '昵称',
    pwd         varchar(255) default '123456' not null comment '密码',
    phone_nb    varchar(20)                   null comment '电话号码',
    permissions varchar(255)                  null comment '权限',
    locked      int          default 0        not null comment '用户是否被锁',
    id          bigint                        not null
        primary key,
    user_id     varchar(32)                   null comment '用户名',
    gender      char                          null comment '性别',
    role        varchar(255)                  null comment '角色',
    order_list  varchar(255) default ''       null,
    constraint uq_user_id
        unique (user_id)
);

INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('fyh', '123456', '123456', '', 0, 2, 'user2', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('张三', '789', '123456', 'student', 0, 3, 'user3', '女', 'student', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('张四', '789', '123456', 'student', 0, 4, 'user4', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('王五', '123456', '123456', '1', 0, 5, 'user5', '男', 'student', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('符宇恒', '2e0e1a59e15334d8ef2fdf729fd361f3', '18702631058', null, 0, 1646137128513273857, 'test', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('fyhfyh', 'e23d587f148b59211c91a7eccf77a301', '1234', null, 0, 1646304124825767938, 'test02', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('1234', 'bd63c144e0d77d5646c794d0cb858e77', '1234', null, 0, 1646305537421217793, 'test03', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('yh f', '5b386ad528df83858b56d4ed18289a9c', '1234', null, 0, 1646308672260927489, 'test04', '男', 'teacher', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('1234', '24992f6d7e904b82f666d941b332acf7', '1234', null, 0, 1646309529614389249, 'test05', '男', 'teacher', '[]');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('1234', 'e20e6ecab562c9f0284b3de7218ae3ed', '1234', null, 0, 1646309982137753601, 'test06', '男', 'student', '[]');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('学生01', 'e5ccdaf35a0b0d2f76de12ead682f2c1', null, null, 0, 1649343011045109762, 's_test01', '男', 'student', '');
INSERT INTO jj.user (name, pwd, phone_nb, permissions, locked, id, user_id, gender, role, order_list) VALUES ('老师02', '68dae0d903eabfa2b7f86f0b4b080f10', null, null, 0, 1649346153258201089, 't_test01', '女', 'teacher', '[1649253387249389570,1649370784086220801]');