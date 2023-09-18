create table if not exists advance
(
    id          uuid,
    chat_id     long,
    advance     double,
    data_add timestamptz,
    primary key (id)
    );
create table if not exists salary
(
    id          uuid,
    chat_id     long,
    salary     double,
    data_add timestamptz,
    primary key (id)
    );
create table if not exists income
(
    id          uuid,
    chat_id     long,
    income     double,
    data_add timestamptz,
    primary key (id)
    );
create table if not exists month_Salary
(
    id          uuid,
    chat_id     long,
    month_salary     double,
    month  integer,
    year  integer ,
    primary key (id)
    );