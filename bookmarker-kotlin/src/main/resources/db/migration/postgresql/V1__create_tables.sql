create sequence user_id_seq start with 1 increment by 1;
create sequence role_id_seq start with 1 increment by 1;
create sequence bm_id_seq start with 1 increment by 1;
create sequence tag_id_seq start with 1 increment by 1;

create table users (
    id bigint DEFAULT nextval('user_id_seq') not null,
    email varchar(255) not null CONSTRAINT user_email_unique UNIQUE,
    password varchar(255) not null,
    name varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table roles (
    id bigint DEFAULT nextval('role_id_seq') not null,
    name varchar(255) not null CONSTRAINT role_name_unique UNIQUE,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table user_role (
    user_id bigint not null REFERENCES users(id),
    role_id bigint not null REFERENCES roles(id)
);

create table bookmarks (
    id bigint DEFAULT nextval('bm_id_seq') not null,
    url varchar(1024) not null,
    title varchar(1024),
    created_by bigint not null references users(id),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table tags (
    id bigint DEFAULT nextval('tag_id_seq') not null,
    name varchar(255) not null CONSTRAINT tag_name_unique UNIQUE,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table bookmark_tag (
    bookmark_id bigint not null REFERENCES bookmarks(id),
    tag_id bigint not null REFERENCES tags(id)
);