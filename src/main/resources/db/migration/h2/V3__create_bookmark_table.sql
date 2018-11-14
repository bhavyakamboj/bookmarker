create sequence bm_id_seq start with 1 increment by 1;

create table bookmarks (
    id bigint default bm_id_seq.nextval,
    url varchar(1024) not null,
    title varchar(1024),
    description varchar(1024),
    created_by bigint not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    foreign key (created_by) references users(id)
);
