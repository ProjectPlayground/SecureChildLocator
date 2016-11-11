use childlocator;

set time_zone = "+00:00";

create table if not exists users
(
    user_id        int            not null auto_increment,
    first_name     varchar(32)    not null,
    last_name      varchar(32)    not null,
    e_mail         varchar(40)    not null,
    password_hash  varchar(50)    not null,

    primary key (user_id)
);

create table if not exists children
(
    child_id       int            not null auto_increment,
    first_name     varchar(32)    not null,
    last_name      varchar(32)    not null,
    user_id_fk     int            not null,

    primary key (child_id),
    foreign key (user_id_fk) references users(user_id)
);

create table if not exists location
(
    location_date  datetime       not null,
    location       varchar(500)   not null,
    user_id_fk     int            not null,
    child_id_fk    int            not null,

    primary key (location_date),
    foreign key (user_id_fk) references users(user_id),
    foreign key (child_id_fk) references children(child_id)
);
