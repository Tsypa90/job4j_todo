create table if not exists categories (
	id serial primary key,
	name varchar(255)
);

create table if not exists item (
id serial primary key,
name varchar(255),
description varchar(255),
created timestamp,
done boolean
);

create table if not exists item_categories (
	categories_id int references categories(id),
	item_id int references item(id)
);

create table if not exists account (
id serial primary key,
name varchar(255),
login varchar(255) unique,
password varchar(255)
);

alter table item
add account_id int references account(id);

insert into categories (name) values('простое');
insert into categories (name) values('обычное');
insert into categories (name) values('сложное');