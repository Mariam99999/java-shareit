
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
CREATE TYPE book_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');
CREATE TABLE IF NOT EXISTS bookings
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE not null ,
    end_date TIMESTAMP WITHOUT TIME ZONE not null ,
    item_id bigint  not null,
    booker_id bigint  not null,
    status book_status not null ,
    CONSTRAINT pk_book PRIMARY KEY (id),
    constraint BOOKINGS_USERS_ID_FK foreign key (BOOKER_ID) references USERS,
    constraint BOOKINGS_ITEMS_ID_FK foreign key (ITEM_ID) references ITEMS
    );

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      varchar                     not null,
    item_id   bigint                      not null,
    author_id bigint                      not null,
    created   TIMESTAMP WITHOUT TIME ZONE not null,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    constraint COMMENTS_USERS_ID_FK foreign key (AUTHOR_ID) references USERS,
    constraint COMMENTS_ITEMS_ID_FK foreign key (ITEM_ID) references ITEMS
    );


