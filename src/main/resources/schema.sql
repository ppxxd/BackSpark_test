create table if not exists socks(
    id bigint generated by default as identity primary key,
    color varchar(50) not null,
    cotton_part bigint not null,
    quantity bigint not null
);