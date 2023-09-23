DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS main_category;
DROP TABLE IF EXISTS sub_category;

CREATE TABLE member
(
    member_id     SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    nickname      VARCHAR(255) UNIQUE NOT NULL,
    profile_image_url VARCHAR(255),
    role          VARCHAR(20)         NOT NULL,
    referral_code VARCHAR(255)
);

CREATE TABLE main_category
(
    main_category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(60) NOT NULL
);

CREATE TABLE sub_category
(
    sub_category_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(60) NOT NULL,
    main_category_id BIGINT      NOT NULL,
    FOREIGN KEY (main_category_id) REFERENCES main_category (main_category_id)
);

create table daily_challenge
(
    daily_challenge_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               varchar(255) not null,
    auth_method        varchar(255) not null,
    payment            bigint       not null,
    payment_method     varchar(255) not null,
    thumbnail          varchar(255) not null,
    banner             varchar(255) not null,
    start_date         timestamp    not null,
    end_date           timestamp    not null,
    challenge_status   varchar(255) not null,
    created_at         timestamp    NOT NULL,
    updated_at         timestamp    NOT NULL
);
