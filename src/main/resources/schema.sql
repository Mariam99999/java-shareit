
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar not null,
    email varchar not null,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

create unique index USERS_EMAIL_UINDEX
    on users (email);
CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar not null,
    description varchar not null,
    available   boolean not null,
    owner_id    bigint  not null,
    CONSTRAINT pk_item PRIMARY KEY (id),
    constraint ITEMS_USERS_ID_FK foreign key (OWNER_ID) references USERS
);


