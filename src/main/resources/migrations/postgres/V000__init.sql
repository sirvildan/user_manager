CREATE TABLE "User"
(
    id    serial not null,
    email text,
    name  text,
    age   int
);

CREATE TABLE UserMeta
(
    id           serial not null,
    hobby        text,
    friendsEmail text
);