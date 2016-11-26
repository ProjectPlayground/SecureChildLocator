use childlocator;

set time_zone = "+00:00";
set SQL_SAFE_UPDATES = 0;

create table if not exists users
(
    email               varchar(40)	  not null unique,
    phone_number        varchar(20)	  not null unique,
    password_hash       varchar(50)	  not null,
    
    primary key (email)
);

create table if not exists location
(
    location_id         int            auto_increment not null unique,
    location_date       timestamp      not null,
    location            varchar(500)   not null,
    session_key	        varchar(40)    not null,
    email               varchar(40)    not null,

    primary key (location_id)
);

create table if not exists session_keys
(
    session_key	        varchar(40)	  not null unique,
    email               varchar(40)   not null,
    used                boolean       not null,
    session_timestamp   timestamp	    not null,
    
    primary key (session_key)
);

