create sequence user_id_seq start with 1 increment by 1;

create table users (
    id bigint DEFAULT nextval('user_id_seq') not null,
    email varchar(255) not null CONSTRAINT user_email_unique UNIQUE,
    password varchar(255) not null,
    name varchar(255) not null,
    role varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
