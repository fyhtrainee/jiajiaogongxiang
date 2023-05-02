create table `order`
(
    id      bigint                                  not null
        primary key,
    th_id   bigint                                  null comment '老师id',
    st_id   bigint                                  null comment '学生id',
    ct_time timestamp default CURRENT_TIMESTAMP     not null comment '订单创建时间',
    st_time timestamp default '0000-00-00 00:00:00' not null comment '订单开始时间',
    ed_time timestamp default '0000-00-00 00:00:00' not null comment '订单结束时间',
    captcha char(4)                                 null comment '验证码',
    ct_id   bigint                                  null,
    constraint fk_st_id
        foreign key (st_id) references user (id)
            on delete set null,
    constraint fk_th_id
        foreign key (th_id) references user (id)
            on delete set null
);

INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646472260252164098, 4, 1646309982137753601, '2023-04-13 19:15:51', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '3760', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646475589178908673, 4, 1646309982137753601, '2023-04-13 19:29:05', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '6631', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646475870943862786, 4, 1646309982137753601, '2023-04-13 19:30:12', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '8019', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646491002616975362, 1646309529614389249, 1646309982137753601, '2023-04-13 20:30:20', '2023-04-15 10:04:03', '0000-00-00 00:00:00', '7049', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646491024909701122, 1646309529614389249, 1646309982137753601, '2023-04-13 20:30:25', '2023-04-15 10:37:52', '0000-00-00 00:00:00', '5673', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646491036867661825, 1646309529614389249, 1646309982137753601, '2023-04-13 20:30:28', '2023-04-15 10:39:40', '0000-00-00 00:00:00', '2384', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646491182103826433, 1646309529614389249, 1646309982137753601, '2023-04-13 20:31:03', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '8473', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1646491192568614914, 1646309529614389249, 1646309982137753601, '2023-04-13 20:31:05', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '9815', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1647067819958607873, 2, 1646309529614389249, '2023-04-15 10:42:24', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '6944', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1649035379088363522, 1646309529614389249, 1646309982137753601, '2023-04-20 21:00:47', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '8222', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1649213958430420993, 1646309529614389249, 1646309982137753601, '2023-04-21 08:50:23', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '7967', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1649213975518015490, 1646309529614389249, 1646309982137753601, '2023-04-21 08:50:27', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '8009', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1649213987656331265, 1646309529614389249, 1646309982137753601, '2023-04-21 08:50:30', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '9386', null);
INSERT INTO jj.`order` (id, th_id, st_id, ct_time, st_time, ed_time, captcha, ct_id) VALUES (1649251645665366018, 1646309529614389249, 1646309982137753601, '2023-04-21 11:20:09', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '3868', null);