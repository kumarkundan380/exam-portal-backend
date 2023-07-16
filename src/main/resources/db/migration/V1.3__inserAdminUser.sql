INSERT INTO `exam-portal`.`users` (`created_at`, `updated_at`, `email`, `is_verified`, `name`, `password`, `phone_number`, `user_name`, `user_status`) VALUES (now(), now(), 'kundan@youpmail.com', b'1', 'Kundan Kumar', '$2a$10$pwu99pILTEsSEHrSzgCG1.4jDMeModqLRpVDEVrLlD0e5F/LmMLfy', '9661048341', 'kundan@youpmail.com', 'ACTIVE');
INSERT INTO `exam-portal`.`user_role` (`user_id`,`role_id`) VALUES (3, 1);
INSERT INTO `exam-portal`.`user_role` (`user_id`,`role_id`) VALUES (3, 2);
