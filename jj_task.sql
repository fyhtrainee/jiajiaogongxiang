create table task
(
    id      bigint                                 not null
        primary key,
    comment text                                   null,
    ct_id   bigint                                 null,
    take_id bigint                                 null,
    ct_time timestamp    default CURRENT_TIMESTAMP null,
    state   varchar(255) default ''                null,
    ct_role varchar(255)                           null,
    constraint fk_ct_id_task
        foreign key (ct_id) references user (id)
            on delete set null,
    constraint fk_take_id_task
        foreign key (take_id) references user (id)
            on delete set null
);

create index fk_st_id_task
    on task (take_id);

create index fk_th_id_task
    on task (ct_id);

INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649032019043991554, '这是第一个测试test01
', 1646309529614389249, 1646309982137753601, '2023-04-20 20:47:25', '[1646309982137753601,1646309982137753601]', 'teacher');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649032081727864833, '这是第二个测试test02
', 1646309529614389249, 1646309982137753601, '2023-04-20 20:47:40', '[1646309982137753601]', 'teacher');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649032122853015554, '这是第三个测试test03
', 1646309529614389249, 1646309982137753601, '2023-04-20 20:47:50', '[1646309982137753601]', 'teacher');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649033797621805057, '这是一个测试test999', 1646309982137753601, 1646309529614389249, '2023-04-20 20:54:29', '[1646309529614389249]', 'student');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649253387249389570, '这是一个测试test01', 1646309982137753601, null, '2023-04-21 11:27:03', '[1649346153258201089]', 'student');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649368856547635201, 't_test01的订单', 1649346153258201089, null, '2023-04-21 19:05:53', '', 'teacher');
INSERT INTO jj.task (id, comment, ct_id, take_id, ct_time, state, ct_role) VALUES (1649370784086220801, '这是一个学生测试', 1649343011045109762, null, '2023-04-21 19:13:33', '[1649346153258201089]', 'student');