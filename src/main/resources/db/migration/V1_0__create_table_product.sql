create table product (
    id BIGINT not null primary key auto_increment,
    name varchar(255) not null,
    description varchar(255) not null,
    image_url varchar(255) not null,
    price decimal(10, 2) not null
) engine=InnoDB default charset=utf8;