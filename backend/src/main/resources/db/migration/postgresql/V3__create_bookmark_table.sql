create sequence bm_id_seq start with 1 increment by 1;

create table bookmarks (
    id bigint DEFAULT nextval('bm_id_seq') not null,
    url varchar(1024) not null,
    title varchar(1024),
    description varchar(1024),
    created_by bigint not null references users(id),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
