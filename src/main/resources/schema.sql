
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar(20) not null,
    email varchar(100) not null,
    CONSTRAINT pk_user PRIMARY KEY (id),
    UNIQUE (email)
);


CREATE TABLE IF NOT EXISTS requests
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description      varchar(500)                     not null,
    requestor_id   bigint                      not null,
    created   TIMESTAMP WITHOUT TIME ZONE not null,
    CONSTRAINT pk_requests  PRIMARY KEY (id),
    constraint REQUESTS_USERS_ID_FK foreign key (requestor_id) references USERS);

CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar(20) not null,
    description varchar(500) not null,
    available   boolean not null,
    owner_id    bigint  not null,
    request_id bigint,
    CONSTRAINT pk_item PRIMARY KEY (id),
    constraint ITEMS_USERS_ID_FK foreign key (OWNER_ID) references USERS,
    constraint ITEMS_REQUESTS_ID_FK foreign key (request_id) references requests
);
CREATE TYPE  book_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');
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
    text      varchar(500)                     not null,
    item_id   bigint                      not null,
    author_id bigint                      not null,
    created   TIMESTAMP WITHOUT TIME ZONE not null,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    constraint COMMENTS_USERS_ID_FK foreign key (AUTHOR_ID) references USERS,
    constraint COMMENTS_ITEMS_ID_FK foreign key (ITEM_ID) references ITEMS
    );




