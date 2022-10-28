create table if not exists Userinfo
(
    id    serial
        primary key,
    email varchar(128) not null
        unique,
    name  varchar(128) not null,
    age   integer
);

create table if not exists usermeta
(
    id           serial
        primary key,
    email        varchar(128) not null
        unique
        references Userinfo (email)
            on delete cascade,
    hobby        varchar(128),
    friendsemail varchar(256)[]
);