DROP TABLE IF EXISTS exhibitions CASCADE;
DROP TABLE IF EXISTS halls CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS expositions CASCADE;

CREATE TABLE users (
    id bigint not null unique,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null,
    primary key (id)
);

CREATE TABLE exhibitions (
    id bigint not null unique,
    theme varchar(255) not null unique,
    description varchar(255) not null,
    start_date date not null,
    end_date date not null,
    price double precision not null,
    primary key (id)
);

CREATE TABLE halls (
    id bigint not null unique,
    name varchar(255) not null unique,
    description varchar(255) not null,
    primary key (id)
);

CREATE TABLE expositions (
    hall_id bigint not null references halls(id),
    exhibition_id bigint not null references exhibitions(id)
);

CREATE TABLE orders(
    exhibition_id bigint not null references exhibitions(id),
    user_id bigint not null references users(id)
);
