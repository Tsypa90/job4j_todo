create table if not exists item (
id serial primary key,
name varchar(255),
description varchar(255),
created date,
done boolean
);

create table if not exists account (
id serial primary key,
name varchar(255),
login varchar(255) unique,
password varchar(255)
);