use childlocator;

set time_zone = "+00:00";
set SQL_SAFE_UPDATES = 0;

create table if not exists users
(
    user_id        int 			  not null auto_increment,
    first_name     varchar(32)	  not null,
    last_name      varchar(32)	  not null,
    email          varchar(40)	  not null unique,
    phone_number   varchar(20)	  not null unique,
    password_hash  varchar(50)	  not null,
    
    primary key (user_id)
);

create table if not exists children
(
    child_id       int 		      not null auto_increment,
    first_name     varchar(32)    not null,
    last_name      varchar(32)	  not null,
    phone_number   varchar(20)	  not null unique,
    user_id_fk	   int            not null,
    
    primary key (child_id),
    foreign key (user_id_fk) references users(user_id)
);

create table if not exists location
(
    location_date  timestamp      not null,
    location       varchar(500)   not null,
    user_id_fk	   int            not null,
    child_id_fk	   int            not null,
    
    primary key (location_date),
    foreign key (user_id_fk) references users(user_id),
    foreign key (child_id_fk) references children(child_id)
);
