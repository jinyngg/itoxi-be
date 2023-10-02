-- create_auth
insert into daily_auth(member_id, daily_challenge_id, image_url, created_at, updated_at)
values (1, 1, 'https://www.test.url/auth.jpg', CURRENT_DATE - 1, CURRENT_DATE - 1);
