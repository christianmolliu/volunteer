create table if not exists Users (
    id identity,
    username varchar(50) not null,
    password varchar(100) not null,
    fullname varchar(50) not null,
    street varchar(50) not null,
    city varchar(50) not null,
    state varchar(2) not null,
    zip varchar(10) not null,
    phone_number varchar(20) not null
);

create table if not exists User_Roles (
    id identity primary key,
    user_id bigint not null,
    role varchar(50) not null,
    foreign key (user_id) references Users(id)
);

create table if not exists Event (
    id identity,
    title varchar(50),
    description varchar(300),
    start_date date,
    end_date date,
    approx_hours float,
    location varchar(100)
);