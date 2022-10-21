create table if not exists "Userinfo"
(
    id    serial
        primary key,
    email varchar(128) not null
        unique,
    name  text,
    age   integer
);

create table if not exists usermeta
(
    id           serial
        primary key,
    email        varchar(128) not null
        unique
        references "Userinfo" (email)
            on delete cascade,
    hobby        text,
    friendsemail text[]
);