create table if not exists "User"
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
        references "User" (email)
            on delete cascade,
    hobby        text,
    friendsemail text[]
);