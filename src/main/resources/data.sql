--Artists
INSERT INTO artists (name, is_group, debut_date, country_of_origin, popularity)
VALUES
    ('Queen', true, '1973-07-13', 'United Kingdom', 85),
    ('The Beatles', true, '1963-03-22', 'United Kingdom', 85),
    ('Taylor Swift', false, '2006-10-24', 'United States', 60),
    ('Beyoncé', false, '2003-06-24', 'United States', 75),
    ('Coldplay', true, '2000-07-10', 'United Kingdom', 80),
    ('Adele', false, '2008-01-28', 'United Kingdom', 70),
    ('U2', true, '1980-10-20', 'Ireland', 80),
    ('Eminem', false, '1999-02-23', 'United States', 70),
    ('Miles Davis', false, '1944-01-01', 'United States', 80),
    ('Fleetwood Mac', true, '1968-07-15', 'United Kingdom', 70),
    ('ABBA', true, '1973-03-23', 'Sweden', 80),
    ('Billie Eilish', false, '2019-03-29', 'United States', 55),
    ('Arctic Monkeys', true, '2006-01-23', 'United Kingdom', 45),
    ('Louis Armstrong', false, '1922-01-01', 'United States', 75),
    ('Aretha Franklin', false, '1956-01-01', 'United States', 85),
    ('Steffon Morrison', false, '1960-10-11', 'United States', 55);


--Vinyl Records
INSERT INTO vinyl_records (title, description, genre, label, price, release_date, play_time_seconds, is_limited_edition, ean, artist_id)
VALUES
    ('A Night at the Opera', 'Classic rock album by Queen.', 'ROCK', 'EMI', 31.99, '1975-11-21', 3600, true, '1234567890123', (SELECT id FROM artists WHERE name = 'Queen')),
    ('Abbey Road', 'Iconic Beatles album, featuring "Come Together".', 'ROCK', 'Apple Records', 29.99, '1969-09-26', 4200, true, '2345678901234', (SELECT id FROM artists WHERE name = 'The Beatles')),
    ('1989', 'Taylor Swift’s hit album featuring "Shake It Off".', 'POP', 'Big Machine Records', 39.89, '2014-10-27', 2700, false, '3456789012345', (SELECT id FROM artists WHERE name = 'Taylor Swift')),
    ('Lemonade', 'Powerful visual album by Beyoncé.', 'RNB', 'Parkwood Entertainment', 19.99, '2016-04-23', 5400, true, '4567890123456', (SELECT id FROM artists WHERE name = 'Beyoncé')),
    ('Viva la Vida', 'Coldplay’s critically acclaimed album.', 'POP', 'Parlophone', 21.99, '2008-06-12', 3900, false, '5678901234567', (SELECT id FROM artists WHERE name = 'Coldplay')),
    ('21', 'Adele’s breakout album with hits like "Rolling in the Deep".', 'POP', 'XL Recordings', 27.99, '2011-01-24', 3300, true, '6789012345678', (SELECT id FROM artists WHERE name = 'Adele')),
    ('The Joshua Tree', 'U2’s legendary album that includes "With or Without You".', 'ROCK', 'Island Records', 49.99, '1987-03-09', 4200, false, '7890123456789', (SELECT id FROM artists WHERE name = 'U2')),
    ('The Marshall Mathers', 'Eminem’s controversial and successful LP.', 'HIP_HOP', 'Interscope Records', 34.99, '2000-05-23', 4200, true, '8901234567890', (SELECT id FROM artists WHERE name = 'Eminem')),
    ('Kind of Blue', 'A jazz masterpiece by Miles Davis.', 'JAZZ', 'Columbia Records', 29.99, '1959-08-17', 4200, false, '9012345678901', (SELECT id FROM artists WHERE name = 'Miles Davis')),
    ('Rumours', 'Fleetwood Mac’s greatest album, with "Go Your Own Way".', 'ROCK', 'Warner Bros. Records', 24.99, '1977-02-04', 3900, true, '0123456789012', (SELECT id FROM artists WHERE name = 'Fleetwood Mac')),
    ('Arrival', 'ABBA’s classic album, featuring "Dancing Queen".', 'POP', 'Polar Music', 29.99, '1976-10-11', 3300, false, '1234567890124', (SELECT id FROM artists WHERE name = 'ABBA')),
    ('When We All Fall Asleep, Where Do We Go?', 'Billie Eilish’s breakout album.', 'POP', 'Darkroom/Interscope', 29.99, '2019-03-29', 3000, true, '2345678901235', (SELECT id FROM artists WHERE name = 'Billie Eilish')),
    ('AM', 'Arctic Monkeys’ album with a more hip hop-influenced sound.', 'ROCK', 'Domino Recording', 24.99, '2013-09-09', 3900, true, '3456789012346', (SELECT id FROM artists WHERE name = 'Arctic Monkeys')),
    ('What a Wonderful World', 'Classic album by jazz legend Louis Armstrong.', 'JAZZ', 'ABC-Paramount', 37.99, '1968-08-01', 2700, false, '4567890123457', (SELECT id FROM artists WHERE name = 'Louis Armstrong')),
    ('I Never Loved a Man', 'Aretha Franklin’s breakthrough album.', 'SOUL', 'Atlantic Records', 27.99, '1967-03-10', 3300, true, '5678901234568', (SELECT id FROM artists WHERE name = 'Aretha Franklin')),
    ('Soul Revolution', 'An album showcasing Steffon Morrison’s jazz prowess.', 'JAZZ', 'Verve Records', 24.99, '1965-09-15', 3900, false, '6789012345679', (SELECT id FROM artists WHERE name = 'Steffon Morrison')),
    ('News of the World', 'Queen’s album featuring "We Are the Champions".', 'ROCK', 'EMI', 31.99, '1977-10-28', 3600, true, '7890123456790', (SELECT id FROM artists WHERE name = 'Queen')),
    ('Revolver', 'The Beatles’ transformative album with experimental sounds.', 'ROCK', 'Parlophone', 30.99, '1966-08-05', 3300, false, '8901234567901', (SELECT id FROM artists WHERE name = 'The Beatles')),
    ('Bitches Brew', 'A landmark jazz fusion album by Miles Davis.', 'JAZZ', 'Columbia Records', 32.99, '1970-03-30', 4200, true, '9012345678912', (SELECT id FROM artists WHERE name = 'Miles Davis')),
    ('Satchmo at Symphony Hall', 'Live performance album by Louis Armstrong.', 'JAZZ', 'Columbia Records', 17.99, '1947-03-30', 3000, false, '0123456789013', (SELECT id FROM artists WHERE name = 'Louis Armstrong'));


--Vinyl Records Stock
INSERT INTO vinyl_records_stock (amount_in_stock, amount_sold, vinyl_record_id)
VALUES
    (30, 0, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera')),
    (20, 2, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road')),
    (15, 0, (SELECT id FROM vinyl_records WHERE title = '1989')),
    (12, 5, (SELECT id FROM vinyl_records WHERE title = 'Lemonade')),
    (18, 4, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida')),
    (15, 2, (SELECT id FROM vinyl_records WHERE title = '21')),
    (10, 6, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree')),
    (15, 3, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers')),
    (20, 3, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue')),
    (25, 1, (SELECT id FROM vinyl_records WHERE title = 'Rumours')),
    (20, 0, (SELECT id FROM vinyl_records WHERE title = 'Arrival')),
    (15, 1, (SELECT id FROM vinyl_records WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    (20, 7, (SELECT id FROM vinyl_records WHERE title = 'AM')),
    (25, 1, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World')),
    (12, 2, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man')),
    (10, 3, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution')),
    (20, 0, (SELECT id FROM vinyl_records WHERE title = 'News of the World')),
    (20, 1, (SELECT id FROM vinyl_records WHERE title = 'Revolver')),
    (15, 1, (SELECT id FROM vinyl_records WHERE title = 'Bitches Brew')),
    (10, 1, (SELECT id FROM vinyl_records WHERE title = 'Satchmo at Symphony Hall'));


-- Authority roles
INSERT INTO roles (role_type)
VALUES
    ('USER'),
    ('EMPLOYEE'),
    ('ADMIN');


-- Customers
INSERT INTO users (username, email, password, first_name, last_name, date_of_birth, phone, user_type, is_deleted, newsletter_subscribed)
VALUES
    ('vinylfan_01', 'max.stoop@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Max', 'Stoop', '1987-03-14', '0612345678', 'CUSTOMER', false, true),
    ('groovehunter_02', 'eva.vandongen@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Eva', 'van Dongen', '1992-07-22', '0623456789', 'CUSTOMER', false, false),
    ('turntableking_03', 'niels.verhoeven@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Niels', 'Verhoeven', '1990-11-30', '0634567890', 'CUSTOMER', false, true),
    ('recordlover_04', 'sophie.maes@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Sophie', 'Maes', '1978-05-18', '0645678901', 'CUSTOMER', false, true),
    ('spinningvinyl_05', 'jasper.vanrooij@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Jasper', 'van Rooij', '1985-09-10', '0656789012', 'CUSTOMER', false, true),
    ('discophile_06', 'lisa.vanacker@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Lisa', 'Van Acker', '1995-01-25', '0667890123', 'CUSTOMER', false, false),
    ('soundsystemfan_07', 'thomas.dekker@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Thomas', 'Dekker', '1983-04-12', '0678901234', 'CUSTOMER', false, true),
    ('thevinylman_08', 'anne.vanbeek@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Anne', 'van Beek', '1999-12-05', '0689012345', 'CUSTOMER', false, false),
    ('oldschoolvinyl_09', 'stefan.vanderlinden@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Stefan', 'van der Linden', '1991-06-28', '0690123456', 'CUSTOMER', false, true),
    ('newagevinyl_10', 'mila.janssen@example.com', '$2a$10$jq3a65A2weHaOIv/2vxIdu6zp.XaFOTAs7v1kqNVq7xaT6gLYZOBG', 'Mila', 'Janssen', '1974-02-15', '0611234567', 'CUSTOMER', false, true);

-- Employees
INSERT INTO users (username, email, password, first_name, last_name, date_of_birth, phone, user_type, is_deleted, job_title, salary, work_hours)
VALUES
    ('admin_lars', 'lars.vanmeulen@vinylshop.com', '$2a$10$Y1fp2IloggWibbS04Q4fRO1RyTDo2EWdjbi.xSkbnth1l0Ksp1z4e', 'Lars', 'van Meulen', '1983-02-14', '0623344556', 'EMPLOYEE', false, 'Store Manager', 6500.00, 40),
    ('admin_emma', 'emma.verstraeten@vinylshop.com', '$2a$10$Y1fp2IloggWibbS04Q4fRO1RyTDo2EWdjbi.xSkbnth1l0Ksp1z4e', 'Emma', 'Verstraeten', '1992-11-12', '0677889900', 'EMPLOYEE', false, 'Store Manager', 6000.00, 40),
    ('employee_bram', 'bram.vanhof@vinylshop.com', '$2a$10$ZVPLU04qTxu1T5DRHcCFDu4CFbXcRqaThbDwU7PH4qAZapWq6XUta', 'Bram', 'van Hof', '1990-05-09', '0611223344', 'EMPLOYEE', false, 'Sales Associate', 3600.00, 32),
    ('employee_eline', 'eline.peeters@vinylshop.com', '$2a$10$ZVPLU04qTxu1T5DRHcCFDu4CFbXcRqaThbDwU7PH4qAZapWq6XUta', 'Eline', 'Peeters', '1992-07-20', '0688997766', 'EMPLOYEE', false, 'Sales Associate', 2900.00, 24),
    ('employee_dean', 'daan.koning@vinylshop.com', '$2a$10$ZVPLU04qTxu1T5DRHcCFDu4CFbXcRqaThbDwU7PH4qAZapWq6XUta', 'Daan', 'Koning', '1988-03-22', '0622233344', 'EMPLOYEE', false, 'Warehouse Coordinator', 5500.00, 32);


-- Customer roles
INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, (SELECT id FROM roles WHERE role_type = 'USER')),
    (2, (SELECT id FROM roles WHERE role_type = 'USER')),
    (3, (SELECT id FROM roles WHERE role_type = 'USER')),
    (4, (SELECT id FROM roles WHERE role_type = 'USER')),
    (5, (SELECT id FROM roles WHERE role_type = 'USER')),
    (6, (SELECT id FROM roles WHERE role_type = 'USER')),
    (7, (SELECT id FROM roles WHERE role_type = 'USER')),
    (8, (SELECT id FROM roles WHERE role_type = 'USER')),
    (9, (SELECT id FROM roles WHERE role_type = 'USER')),
    (10, (SELECT id FROM roles WHERE role_type = 'USER'));

-- Employee roles
INSERT INTO user_roles (user_id, role_id)
VALUES
    (11, (SELECT id FROM roles WHERE role_type = 'USER')),
    (11, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (11, (SELECT id FROM roles WHERE role_type = 'ADMIN')),
    (12, (SELECT id FROM roles WHERE role_type = 'USER')),
    (12, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (12, (SELECT id FROM roles WHERE role_type = 'ADMIN')),
    (13, (SELECT id FROM roles WHERE role_type = 'USER')),
    (13, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (14, (SELECT id FROM roles WHERE role_type = 'USER')),
    (14, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (15, (SELECT id FROM roles WHERE role_type = 'USER')),
    (15, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE'));



--Favorite Vinyl Records
INSERT INTO customer_favorite_vinyl_records (customer_id, vinyl_record_id)
VALUES
    (1, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera')),
    (1, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road')),
    (1, (SELECT id FROM vinyl_records WHERE title = '1989')),

    (2, (SELECT id FROM vinyl_records WHERE title = 'Lemonade')),
    (2, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida')),
    (2, (SELECT id FROM vinyl_records WHERE title = '21')),

    (4, (SELECT id FROM vinyl_records WHERE title = 'Rumours')),
    (4, (SELECT id FROM vinyl_records WHERE title = 'Arrival')),
    (4, (SELECT id FROM vinyl_records WHERE title = 'When We All Fall Asleep, Where Do We Go?')),

    (5, (SELECT id FROM vinyl_records WHERE title = 'AM')),
    (5, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World')),
    (5, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man')),

    (6, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution')),
    (6, (SELECT id FROM vinyl_records WHERE title = 'News of the World')),
    (6, (SELECT id FROM vinyl_records WHERE title = 'Revolver')),

    (8, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera')),
    (8, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road')),

    (9, (SELECT id FROM vinyl_records WHERE title = '21')),
    (9, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers'));


--Addresses

-- Addresses for Customers
INSERT INTO addresses (street, house_number, city, postal_code, country, is_shipping_address, is_billing_address, customer_id)
VALUES
    ('Kalverstraat', '101', 'Amsterdam', '1012NX', 'Netherlands', true, true, 1),
    ('Nieuwendijk', '87', 'Amsterdam', '1012MB', 'Netherlands', false, false, 1),
    ('Damrak', '33B', 'Amsterdam', '1012LK', 'Netherlands', false, false, 1),

    ('Korte Lijnbaan', '5', 'Rotterdam', '3012ED', 'Netherlands', true, false, 2),
    ('Coolsingel', '123A', 'Rotterdam', '3012AG', 'Netherlands', false, true, 2),

    ('Steenstraat', '12', 'Brugge', '8000', 'Belgium', true, true, 3),
    ('Zuidzandstraat', '45C', 'Brugge', '8000', 'Belgium', false, false, 3),

    ('Hoogpoort', '88', 'Gent', '9000', 'Belgium', true, false, 4),
    ('Veldstraat', '67', 'Gent', '9000', 'Belgium', false, true, 4),
    ('Kouter', '9A', 'Gent', '9000', 'Belgium', false, false, 4),
    ('Langemunt', '34', 'Gent', '9000', 'Belgium', false, false, 4),

    ('Vrijthof', '23', 'Maastricht', '6211LC', 'Netherlands', true, false, 5),
    ('Markt', '18', 'Maastricht', '6211CJ', 'Netherlands', false, true, 5),
    ('Kleine Staat', '9B', 'Maastricht', '6211ED', 'Netherlands', false, false, 5),
    ('Boschstraat', '67', 'Maastricht', '6211AX', 'Netherlands', false, false, 5),

    ('Kapellestraat', '11', 'Oostende', '8400', 'Belgium', true, false, 6),
    ('Alfons Pieterslaan', '23', 'Oostende', '8400', 'Belgium', false, true, 6),
    ('Van Iseghemlaan', '45D', 'Oostende', '8400', 'Belgium', false, false, 6),

    ('Haarlemmerstraat', '67', 'Leiden', '2312DM', 'Netherlands', true, false, 7),
    ('Breestraat', '23', 'Leiden', '2311CG', 'Netherlands', false, true, 7),
    ('Nieuwstraat', '19A', 'Leiden', '2312KL', 'Netherlands', false, false, 7),

    ('Mariastraat', '6', 'Utrecht', '3511LP', 'Netherlands', true, false, 8),
    ('Oudegracht', '100B', 'Utrecht', '3511AH', 'Netherlands', false, true, 8),
    ('Neude', '12', 'Utrecht', '3512AD', 'Netherlands', false, false, 8),

    ('Steenhouwersvest', '3A', 'Antwerpen', '2000', 'Belgium', true, true, 9),
    ('Lombardenvest', '22', 'Antwerpen', '2000', 'Belgium', false, false, 9),

    ('Leidsestraat', '48', 'Amsterdam', '1017PH', 'Netherlands', true, false, 10),
    ('Spuistraat', '33', 'Amsterdam', '1012SR', 'Netherlands', false, true, 10);

-- Addresses for Employees
INSERT INTO addresses (street, house_number, city, postal_code, country, is_shipping_address, is_billing_address, employee_id)
VALUES
    ('Lange Leidsedwarsstraat', '45', 'Amsterdam', '1017NG', 'Netherlands', NULL, NULL, 11),
    ('Meir', '78', 'Antwerpen', '2000', 'Belgium', NULL, NULL, 12),
    ('Hoogstraat', '15A', 'Rotterdam', '3011PN', 'Netherlands', NULL, NULL, 13),
    ('Veldstraat', '22', 'Gent', '9000', 'Belgium', NULL, NULL, 14);

-- Addresses for orders (stand alone shipping addresses)
INSERT INTO addresses (street, house_number, city, postal_code, country, is_shipping_address, is_billing_address)
VALUES
    ('Coolsingel', '20', 'Rotterdam', '3011AD', 'Netherlands', NULL, NULL),
    ('Zuiderzijde', '39', 'Den Haag', '2543CV', 'Netherlands', NULL, NULL);


-- Carts and items

-- Carts
INSERT INTO carts (customer_id, created_at, updated_at)
VALUES
    (1, '2025-01-12 17:10:00', '2025-03-19 19:20:00'),
    (2, '2025-01-18 16:20:00', '2025-03-22 17:40:00'),
    (3, '2025-01-20 15:30:00', '2025-02-28 18:00:00'),
    (4, '2025-01-24 14:00:00', '2025-03-19 14:30:00'),
    (5, '2025-02-12 12:45:00', '2025-03-22 14:25:00'),
    (6, '2025-02-14 11:00:00', '2025-03-21 15:30:00'),
    (7, '2025-03-05 19:50:00', '2025-03-20 21:45:00'),
    (8, '2025-03-11 09:30:00', '2025-03-23 10:30:00'),
    (9, '2025-03-19 18:40:00', '2025-03-22 20:30:00'),
    (10, '2025-03-22 13:15:00', '2025-03-22 16:45:00');

-- Cart Items
INSERT INTO cart_items (cart_id, vinyl_record_id, quantity)
VALUES
    (1, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road'), 1),
    (1, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera'), 1),
    (1, (SELECT id FROM vinyl_records WHERE title = '1989'), 1),
    (1, (SELECT id FROM vinyl_records WHERE title = '21'), 2),

    (4, (SELECT id FROM vinyl_records WHERE title = 'Lemonade'), 1),
    (4, (SELECT id FROM vinyl_records WHERE title = 'Arrival'), 3),

    (5, (SELECT id FROM vinyl_records WHERE title = 'AM'), 2),
    (5, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World'), 1),
    (5, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man'), 1),
    (5, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue'), 1),

    (6, (SELECT id FROM vinyl_records WHERE title = 'Rumours'), 1),
    (6, (SELECT id FROM vinyl_records WHERE title = 'Bitches Brew'), 1),
    (6, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1),

    (8, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera'), 2),
    (8, (SELECT id FROM vinyl_records WHERE title = 'Revolver'), 2),
    (8, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree'), 2),

    (10, (SELECT id FROM vinyl_records WHERE title = 'When We All Fall Asleep, Where Do We Go?'), 1),
    (10, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1),
    (10, (SELECT id FROM vinyl_records WHERE title = 'News of the World'), 1);



-- Orders

-- Orders
INSERT INTO orders (order_date, expected_delivery_date, recipient_name, sub_total_price, shipping_cost, note, payment_method, confirmation_status, payment_status, shipping_status, shipping_address_id, billing_address_id, customer_id, is_deleted)
VALUES
    ('2025-01-20 19:00:00', '2025-01-23 14:00:00', 'Max Stoop', 152.96, 0.00, NULL, 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 1, 1, 1, FALSE),
    ('2025-02-08 10:00:00', '2025-02-12 11:00:00', 'Max Stoop', 67.97, 4.99, NULL, 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 2, 2, 1, FALSE),

    ('2025-01-23 13:20:00', '2025-01-26 15:00:00', 'Eva van Dongen', 115.96, 0.00, 'Urgent delivery', 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 4, 5, 2, FALSE),
    ('2025-02-18 12:00:00', '2025-02-21 10:30:00', 'Eva van Dongen', 72.97, 2.99, NULL, 'GIFT_CARD', 'CONFIRMED', 'PAID', 'DELIVERED', 33, 33, 2, FALSE),
    ('2025-03-22 17:40:00', '2025-03-26 14:30:00', 'Eva van Dongen', 24.99, 2.99, NULL, 'IDEAL', 'PENDING', 'NOT_APPLICABLE', 'NOT_APPLICABLE', 33, 33, 2, FALSE),

    ('2025-02-28 18:00:00', '2025-03-04 16:00:00', 'Niels Verhoeven', 113.97, 0.00, NULL, 'IDEAL', 'CONFIRMED', 'REFUNDED', 'RETURNED', 6, 6, 3, FALSE),

    ('2025-01-26 18:00:00', '2025-01-29 12:00:00', 'Sophie Maes', 74.97, 4.99, 'First time ordering — So excited!', 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 8, 9, 4, FALSE),
    ('2025-02-13 14:00:00', '2025-02-16 11:30:00', 'Sophie Maes', 84.97, 0.00, NULL, 'AFTER_PAY', 'CONFIRMED', 'PAID', 'DELIVERED', 8, 9, 4, FALSE),
    ('2025-03-20 14:00:00', '2025-03-25 10:00:00', 'Sophie Maes', 96.96, 0.00, NULL, 'AFTER_PAY', 'CONFIRMED', 'AWAITING_PAYMENT', 'PROCESSING', 10, 10, 4, FALSE),

    ('2025-02-15 10:35:00', '2025-02-19 14:00:00', 'Jasper van Rooij', 154.96, 0.00, NULL, 'CREDIT_CARD', 'CONFIRMED', 'PAID', 'DELIVERED', 12, 13, 5, FALSE),

    ('2025-02-17 21:00:00', '2025-02-21 16:00:00', 'Lisa van Acker', 92.97, 0.00, 'No invoice needed.', 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 16, 17, 6, FALSE),
    ('2025-03-12 19:10:00', '2025-03-15 15:30:00', 'Lisa van Acker', 74.97, 4.99, NULL, 'APPLE_PAY', 'CONFIRMED', 'PAID', 'DELIVERED', 18, 17, 6, FALSE),

    ('2025-03-06 09:10:00', '2025-03-10 12:00:00', 'Arne Smit', 59.98, 4.99, 'Gift wrap, please!', 'IDEAL', 'CONFIRMED', 'PAID', 'DELIVERED', 34, 20, 7, FALSE),
    ('2025-03-20 21:45:00', '2025-03-24 10:30:00', 'Thomas Dekker', 107.96, 0.00, NULL, 'IDEAL', 'CONFIRMED', 'PAID', 'SHIPPED', 21, 20, 7, FALSE),

    ('2025-03-12 09:30:00', '2025-03-15 13:00:00', 'Anne van Beek', 62.98, 4.99, NULL, 'IDEAL', 'CONFIRMED', 'REFUNDED', 'LOST', 22, 23, 8, FALSE),
    ('2025-03-21 09:30:00', '2025-03-26 11:00:00', 'Anne van Beek', 121.97, 0.00, NULL, 'IDEAL', 'CONFIRMED', 'PAID', 'PROCESSING', 24, 23, 8, FALSE),

    ('2025-03-22 20:30:00', '2025-03-26 15:00:00', 'Stefan van der Linden', 87.97, 0.00, NULL, 'IDEAL', 'PENDING', 'NOT_APPLICABLE', 'NOT_APPLICABLE', 25, 25, 9, FALSE);

-- Order Items
INSERT INTO order_items (order_id, vinyl_record_id, quantity, price_at_purchase)
VALUES
    (1, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree'), 2, 49.99),
    (1, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida'), 1, 21.99),
    (1, (SELECT id FROM vinyl_records WHERE title = 'Revolver'), 1, 30.99),

    (2, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1, 24.99),
    (2, (SELECT id FROM vinyl_records WHERE title = 'Satchmo at Symphony Hall'), 1, 17.99),
    (2, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99),

    (3, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road'), 1, 29.99),
    (3, (SELECT id FROM vinyl_records WHERE title = '21'), 2, 27.99),
    (3, (SELECT id FROM vinyl_records WHERE title = 'When We All Fall Asleep, Where Do We Go?'), 1, 29.99),

    (4, (SELECT id FROM vinyl_records WHERE title = 'Lemonade'), 1, 19.99),
    (4, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99),
    (4, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man'), 1, 27.99),

    (5, (SELECT id FROM vinyl_records WHERE title = 'Rumours'), 1, 24.99),

    (6, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World'), 3, 37.99),

    (7, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers'), 1, 34.99),
    (7, (SELECT id FROM vinyl_records WHERE title = 'Lemonade'), 2, 19.99),

    (8, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue'), 2, 29.99),
    (8, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99),

    (9, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida'), 2, 21.99),
    (9, (SELECT id FROM vinyl_records WHERE title = 'Bitches Brew'), 1, 32.99),
    (9, (SELECT id FROM vinyl_records WHERE title = 'Lemonade'), 1, 19.99),

    (10, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree'), 2, 49.99),
    (10, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1, 24.99),
    (10, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road'), 1, 29.99),

    (11, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1, 24.99),
    (11, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue'), 1, 29.99),
    (11, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World'), 1, 37.99),

    (12, (SELECT id FROM vinyl_records WHERE title = 'Rumours'), 1, 24.99),
    (12, (SELECT id FROM vinyl_records WHERE title = 'AM'), 2, 24.99),

    (13, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99),
    (13, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers'), 1, 34.99),

    (14, (SELECT id FROM vinyl_records WHERE title = 'Lemonade'), 1, 19.99),
    (14, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man'), 1, 27.99),
    (14, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers'), 1, 34.99),
    (14, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99),

    (15, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue'), 1, 29.99),
    (15, (SELECT id FROM vinyl_records WHERE title = 'Bitches Brew'), 1, 32.99),

    (16, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida'), 1, 21.99),
    (16, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree'), 2, 49.99),

    (17, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution'), 1, 24.99),
    (17, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World'), 1, 37.99),
    (17, (SELECT id FROM vinyl_records WHERE title = 'AM'), 1, 24.99);


-- Invoices
INSERT INTO invoices (invoice_date, order_id)
VALUES
    ('2025-01-20 19:03:00', 1),
    ('2025-02-08 10:05:00', 2),

    ('2025-01-23 13:24:00', 3),
    ('2025-02-18 12:02:00', 4),

    ('2025-02-28 18:04:00', 6),

    ('2025-01-26 18:05:00', 7),
    ('2025-02-18 11:30:00', 8),

    ('2025-02-15 10:40:00', 10),

    ('2025-02-17 21:06:00', 11),
    ('2025-03-12 19:14:00', 12),

    ('2025-03-06 09:15:00', 13),
    ('2025-03-20 21:49:00', 14),

    ('2025-03-12 9:32:00', 15),
    ('2025-03-21 9:35:00', 16);