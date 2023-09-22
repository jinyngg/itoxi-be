DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS main_category;
DROP TABLE IF EXISTS sub_category;
DROP TABLE IF EXISTS daily_challenge;
DROP TABLE IF EXISTS reward_challenge;
DROP TABLE IF EXISTS reward_challenger;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS challenge_product;
DROP TABLE IF EXISTS delivery_address;
DROP TABLE IF EXISTS challenge_delivery;

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

create table reward_challenge
(
    reward_challenge_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    sub_title           VARCHAR(255) NOT NULL,
    notice              VARCHAR(255) ,
    thumbnail           VARCHAR(255) NOT NULL,
    poster              VARCHAR(255) NOT NULL,
    status              VARCHAR(255) NOT NULL,
    start_date          TIMESTAMP    NOT NULL,
    end_date            TIMESTAMP    NOT NULL,
    kit_start_date      TIMESTAMP    NOT NULL,
    kit_end_date        TIMESTAMP    NOT NULL,
    review_start_date   TIMESTAMP    NOT NULL,
    review_end_date     TIMESTAMP    NOT NULL,
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL
);

create table reward_challenger
(
    reward_challenger_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id            BIGINT       NOT NULL,
    reward_challenge_id  BIGINT       NOT NULL,
    process              VARCHAR(255) NOT NULL,
    created_at           TIMESTAMP    NOT NULL,
    updated_at           TIMESTAMP    NOT NULL
    FOREIGN KEY (member_id) REFERENCES member (member_id)
    FOREIGN KEY (reward_challenge_id) REFERENCES reward_challenge (reward_challenge_id)
);

CREATE TABLE product
(
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    category    VARCHAR(255) NOT NULL,
    brand       VARCHAR(255) NOT NULL,
    price       BIGINT       NOT NULL,
    quantity    BIGINT       NOT NULL,
    image       VARCHAR(255) ,
);

CREATE TABLE challenge_product
(
    challenge_product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id           BIGINT NOT NULL,
    reward_challenge_id  BIGINT NOT NULL,
    category             VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (product_id),
    FOREIGN KEY (reward_challenge_id) REFERENCES reward_challenge (reward_challenge_id)
);

CREATE TABLE delivery_address
(
    delivery_address_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id            BIGINT       NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    phone                VARCHAR(255) NOT NULL,
    roadAddress          VARCHAR(255) NOT NULL,
    address              VARCHAR(255) NOT NULL,
    zipcode              VARCHAR(255) NOT NULL,
    is_based             INT          NOT NULL,  -- JPA Boolean은 tinyint(1)로 매핑 됨(false는 0, true는 1)
    FOREIGN KEY (member_id) REFERENCES member (member_id),
);

CREATE TABLE challenge_delivery
(
    challenge_delivery_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id              BIGINT       NOT NULL,
    challenge_product_id   BIGINT       NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    phone                  VARCHAR(255) NOT NULL,
    roadAddress            VARCHAR(255) NOT NULL,
    address                VARCHAR(255) NOT NULL,
    zipcode                VARCHAR(255) NOT NULL,
    message                VARCHAR(255) NOT NULL,
    process                VARCHAR(255) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (challenge_product_id) REFERENCES challenge_product (challenge_product_id)
);