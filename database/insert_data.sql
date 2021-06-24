INSERT INTO announcement(code, content, short_description, title) VALUES
("announcement1", "Announcement 1 Content", "Announcement 1 Short description", "Announcement 1 Title"),
("announcement2", "Announcement 2 Content", "Announcement 2 Short description", "Announcement 2 Title"),
("announcement3", "Announcement 3 Content", "Announcement 3 Short description", "Announcement 3 Title");

INSERT INTO role(code, name) VALUES
("ADMIN", "Administrator"),
("USER", "User");

INSERT INTO message(content) VALUES
("content1"),
("content2");

INSERT INTO mission(name, description, short_description, type, num_of_coin_gift_box, num_of_star, num_of_time_gift_box) VALUES
("Điểm danh", "Thực hiện điểm danh mỗi ngày để nhận được những phần thưởng tương ứng.", "Thực hiện điểm danh mỗi ngày.", "DIEMDANH", 0, 3, 0),
("Capcha", "Mission 2 description", "Short description" , "CAPCHA", 0, 0, 4);

INSERT INTO momo(phone_number, name) VALUES
("0975543975", "Cao Đinh Sỹ Vỹ"),
("0987654321", "Đinh Thị Thùy Linh");

INSERT INTO user(username, password, account_balance, address, commission, email, facebook_link, fullname, num_of_coin_gift_box, 
num_of_star, num_of_time_gift_box, phone_number, token_code, momo_phone_number, num_of_default_time, num_of_travelled_time) VALUES
("admin", "123456", 500, "Quảng Bình", 40, "caovy2001@gmail.com", "https://facebook.com/abc", "Cao Đinh Sỹ Vỹ", 5, 
40, 8, "0975543975", "akjsfhaskldjfhaslkdfjhalkfjaashdgfkasjdf", "0975543975", 60, 0),
("user", "123456", 1000, "Quảng Bình", 60, "abc@gmail.com", "https://facebook.com/cba", "Đinh Thị Thùy Linh", 10, 
80, 14, "0987654321", "eitoyegskdlfgsldkghsdkfhgsldkgsldkfgjhd", "0987654321", 60, 0),
("user1", "123456", 1000, null, 0, null, null, null, 150, 80, 200, null, "eitoyegskdlfgsldkghsdkfhgsldkgsldkfgjcvnbvdasfdshda", null, 60, 0),
("user2", "123456", 1000, null, 0, null, null, null, 150, 80, 200, null, "eitoyegskdlfgsldkghsdkfhgsldkgsldkfgjcvnbvdasfdshdb", null, 60, 0),
("user3", "123456", 1000, null, 0, null, null, null, 150, 80, 200, null, "eitoyegskdlfgsldkghsdkfhgsldkgsldkfgjcvnbvdasfdshdc", null, 60, 0),
("user4", "123456", 1000, null, 0, null, null, null, 150, 80, 200, null, "eitoyegskdlfgsldkghsdkfhgsldkgsldkfgjcvnbvdasfdshdd", null, 60, 0);


INSERT INTO user_role(username, role_code) VALUES
("admin", "ADMIN"),
("user", "USER"),
("user1", "USER"),
("user2", "USER"),
("user3", "USER"),
("user4", "USER");

INSERT INTO withdraw_request(amount_of_money, payment_date, status, type, username) VALUES
(500, null, "UNPAID", "MOMO", "admin"),
(800, "2021-04-20 07:00:00", "PAID", "MOMO", "user");