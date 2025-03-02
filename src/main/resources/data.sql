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
    (30, 45, (SELECT id FROM vinyl_records WHERE title = 'A Night at the Opera')),
    (20, 30, (SELECT id FROM vinyl_records WHERE title = 'Abbey Road')),
    (15, 25, (SELECT id FROM vinyl_records WHERE title = '1989')),
    (12, 15, (SELECT id FROM vinyl_records WHERE title = 'Lemonade')),
    (18, 20, (SELECT id FROM vinyl_records WHERE title = 'Viva la Vida')),
    (15, 18, (SELECT id FROM vinyl_records WHERE title = '21')),
    (10, 10, (SELECT id FROM vinyl_records WHERE title = 'The Joshua Tree')),
    (15, 18, (SELECT id FROM vinyl_records WHERE title = 'The Marshall Mathers')),
    (20, 25, (SELECT id FROM vinyl_records WHERE title = 'Kind of Blue')),
    (25, 35, (SELECT id FROM vinyl_records WHERE title = 'Rumours')),
    (20, 22, (SELECT id FROM vinyl_records WHERE title = 'Arrival')),
    (15, 20, (SELECT id FROM vinyl_records WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    (20, 22, (SELECT id FROM vinyl_records WHERE title = 'AM')),
    (25, 30, (SELECT id FROM vinyl_records WHERE title = 'What a Wonderful World')),
    (12, 12, (SELECT id FROM vinyl_records WHERE title = 'I Never Loved a Man')),
    (10, 10, (SELECT id FROM vinyl_records WHERE title = 'Soul Revolution')),
    (30, 40, (SELECT id FROM vinyl_records WHERE title = 'News of the World')),
    (20, 25, (SELECT id FROM vinyl_records WHERE title = 'Revolver')),
    (15, 20, (SELECT id FROM vinyl_records WHERE title = 'Bitches Brew')),
    (10, 12, (SELECT id FROM vinyl_records WHERE title = 'Satchmo at Symphony Hall'));


-- Authority roles
INSERT INTO roles (role_type)
VALUES
    ('USER'),
    ('EMPLOYEE'),
    ('ADMIN');


-- Customers
INSERT INTO users (username, email, password, first_name, last_name, date_of_birth, phone, user_type, is_deleted, newsletter_subscribed)
VALUES
    ('vinylfan_01', 'max.stoop@example.com', 'password123', 'Max', 'Stoop', '1987-03-14', '0612345678', 'CUSTOMER', false, true),
    ('groovehunter_02', 'eva.vandongen@example.com', 'password123', 'Eva', 'van Dongen', '1992-07-22', '0623456789', 'CUSTOMER', false, false),
    ('turntableking_03', 'niels.verhoeven@example.com', 'password123', 'Niels', 'Verhoeven', '1990-11-30', '0634567890', 'CUSTOMER', false, true),
    ('recordlover_04', 'sophie.maes@example.com', 'password123', 'Sophie', 'Maes', '1978-05-18', '0645678901', 'CUSTOMER', false, true),
    ('spinningvinyl_05', 'jasper.vanrooij@example.com', 'password123', 'Jasper', 'van Rooij', '1985-09-10', '0656789012', 'CUSTOMER', false, true),
    ('discophile_06', 'lisa.vanacker@example.com', 'password123', 'Lisa', 'Van Acker', '1995-01-25', '0667890123', 'CUSTOMER', false, false),
    ('soundsystemfan_07', 'thomas.dekker@example.com', 'password123', 'Thomas', 'Dekker', '1983-04-12', '0678901234', 'CUSTOMER', false, true),
    ('thevinylman_08', 'anne.vanbeek@example.com', 'password123', 'Anne', 'van Beek', '1999-12-05', '0689012345', 'CUSTOMER', false, false),
    ('oldschoolvinyl_09', 'stefan.vanderlinden@example.com', 'password123', 'Stefan', 'van der Linden', '1991-06-28', '0690123456', 'CUSTOMER', false, true),
    ('newagevinyl_10', 'mila.janssen@example.com', 'password123', 'Mila', 'Janssen', '1974-02-15', '0611234567', 'CUSTOMER', false, true);

-- Employees
INSERT INTO users (username, email, password, first_name, last_name, date_of_birth, phone, user_type, is_deleted, job_title, salary, work_hours)
VALUES
    ('admin_lars', 'lars.vanmeulen@vinylshop.com', 'admin', 'Lars', 'van Meulen', '1983-02-14', '0623344556', 'EMPLOYEE', false, 'Store Manager', 6500.00, 40),
    ('admin_emma', 'emma.verstraeten@vinylshop.com', 'admin', 'Emma', 'Verstraeten', '1992-11-12', '0677889900', 'EMPLOYEE', false, 'Store Manager', 6000.00, 40),
    ('employee_bram', 'bram.vanhof@vinylshop.com', 'employee', 'Bram', 'van Hof', '1990-05-09', '0611223344', 'EMPLOYEE', false, 'Sales Associate', 3600.00, 32),
    ('employee_eline', 'eline.peeters@vinylshop.com', 'employee', 'Eline', 'Peeters', '1992-07-20', '0688997766', 'EMPLOYEE', false, 'Sales Associate', 2900.00, 24),
    ('employee_dean', 'daan.koning@vinylshop.com', 'employee', 'Daan', 'Koning', '1988-03-22', '0622233344', 'EMPLOYEE', false, 'Warehouse Coordinator', 5500.00, 32);


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
    (11, (SELECT id FROM roles WHERE role_type = 'ADMIN')),
    (11, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (12, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (12, (SELECT id FROM roles WHERE role_type = 'ADMIN')),
    (13, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (14, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE')),
    (15, (SELECT id FROM roles WHERE role_type = 'EMPLOYEE'));



--Favorite Vinyl Records
INSERT INTO favorite_vinyl_records (customer_id, vinyl_record_id)
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
    ('Veldstraat', '22', 'Gent', '9000', 'Belgium', NULL, NULL, 14),
    ('Reguliersdwarsstraat', '8B', 'Utrecht', '3511XK', 'Netherlands', NULL, NULL, 15);