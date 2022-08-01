create table if not exists item (
id serial primary key,
name text,
description text,
created date,
done boolean
);

create table if not exists account (
id serial primary key,
name text NOT NULL,
login text NOT NULL unique,
password text NOT NULL
);