create table user (
    id BIGINT not null primary key auto_increment,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(20) not null
)