-- 회원 더미 데이터 생성
INSERT INTO member (email, nickname, profile_image_url, role, referral_code)
VALUES ('test', '테스트', '', 'USER', '');

-- 메인 카테고리 더미 데이터 생성(전체 제외)
INSERT INTO main_category (name)
VALUES ('고민상담'),
       ('자유수다');

-- 서브 카테고리 더미 데이터 생성
INSERT INTO sub_category (name, main_category_id)
VALUES ('질병/질환', 1),
       ('미용/패션', 1),
       ('교육/훈련', 1),
       ('양육/관리', 1),
       ('반려용품', 1);

-- INSERT INTO pet_talk (title, content, main_category_id, sub_category_id, pet_type, status, view_count, member_id)
-- VALUES ('제목1', '내용1', 1, 1, 'DOG', 'ACTIVE', 0, 1);

-- 데일리 챌린지 데이터 생성
insert into daily_challenge (name, auth_method, payment, payment_method, thumbnail, banner, start_date, end_date,
                             challenge_status, created_at, updated_at)
values ('데일리 간식주기', '반려동물에게 간식주는 사진을 인증해요!', 100, '참여 완료 즉시 지급', 'https://test.url/thumbnail.jpg',
        'https://test.url/banner.jpg', '2023-09-16 00:00:00', '9999-12-31 23:59:59', 'OPENED', now(), now());

insert into reward_challenge (title, sub_title, notice, thumbnail, poster, status, start_date, end_date,
                              kit_start_date, kit_end_date, review_start_date, review_end_date, created_at, updated_at)
values ('천하제일 집사대회', '펫누리 비대면 검진키트 이용후기 올리고 선물세트 받기', '유의사항',
        'https://petnuri-image-bucket.s3.ap-northeast-2.amazonaws.com/test/%ED%98%84%EB%AC%BC%EC%B1%8C%EB%A6%B0%EC%A7%80_%EC%8D%B8%EB%84%A4%EC%9D%BC.png',
        'https://petnuri-image-bucket.s3.ap-northeast-2.amazonaws.com/test/%ED%98%84%EB%AC%BC%EC%B1%8C%EB%A6%B0%EC%A7%80.png',
        'ACTIVE', '2023-10-09 00:00:00', '2023-10-15 23:59:59', '2023-10-17 00:00:00', '2023-10-21 23:59:59',
        '2023-10-23 00:00:00', '2023-10-31 23:59:59', now(), now());

insert into reward_challenger (member_id, reward_challenge_id, process, is_consented_personal_info,
                               created_at, updated_at)
values (1, 1, 'APPLY', 1, now(), now());

INSERT INTO product (name, category, brand, price, quantity, image)
VALUES ('검진키트', 'ETC', '아이톡시', 0, 100, null),
       ('댕댕이 세트', 'SET', '아이톡시', 0, 50, null),
       ('냥냥이 세트', 'SET', '아이톡시', 0, 50, null);

INSERT INTO challenge_product (product_id, reward_challenge_id, category)
VALUES (1, 1, 'KIT'),
       (2, 1, 'REWARD'),
       (3, 1, 'REWARD');