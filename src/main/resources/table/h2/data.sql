-- 회원 더미 데이터 생성
INSERT INTO member (email, password, nickname, phone, provider, role, referral_code)
VALUES ('test', '$2a$10$co2T64NBAT5yAPcNKnlgmemUBxm2.Gk3TnsYM7SFX3Dci7/lhmt5a', '테스트',
        '010-1234-5678', 'GOOGLE', 'USER', '');

-- 메인 카테고리 더미 데이터 생성(전체 제외)
INSERT INTO main_category (name)
VALUES ('고민상담'),
       ('자유수다'),
       ('챌린지');

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
insert into daily_challenge (name, auth_method, payment, start_date, end_date, challenge_status, created_at, updated_at)
values ('데일리 간식주기', '반려동물에게 간식주는 사진을 인증해요!', 100, '2023-09-16 00:00:00', '9999-12-31 23:59:59', 'OPENED', now(), now());
