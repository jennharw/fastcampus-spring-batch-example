create table person (
    id bigint primary key auto_increment,
    name varchar(255),
    age varchar(255),
    address varchar(255)
);

insert into person (`name`, `age`, `address`) values('Lee','32','인천');
insert into person (`name`, `age`, `address`) values('Park','30','서울');
insert into person (`name`, `age`, `address`) values('Harim','25','강원')
