create table hibernate_sequence (
       next_val bigint
);

insert into hibernate_sequence values ( 1 );

create table role (
    role_id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    description varchar(255) not null,
    role_name ENUM('USER', 'ADMIN') not null,
    primary key (role_id)
);

create table users (
    user_id bigint not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    email varchar(255) not null,
    is_verified bit not null,
    name varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(255) not null,
    user_image varchar(255),
    user_name varchar(255) not null,
    user_status ENUM('ACTIVE', 'DELETED') not null,
    primary key (user_id)
 );

create table user_role (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);


