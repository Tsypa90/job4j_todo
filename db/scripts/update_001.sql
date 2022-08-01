create table if not exists item (
id serial primary key,
name text,
description text,
created date,
done boolean
)