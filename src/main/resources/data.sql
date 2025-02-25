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
    ('vinylfan_01', 'vinylfan01@example.com', 'password123', 'Alice', 'Johnson', '2001-03-14', '612345678', 'CUSTOMER', false, true),
    ('groovehunter_02', 'groovehunter02@example.com', 'password123', 'Bob', 'Smith', '1992-07-22', '623456789', 'CUSTOMER', false, false),
    ('turntableking_03', 'turntableking03@example.com', 'password123', 'Charlie', 'Brown', '1990-11-30', '634567890', 'CUSTOMER', false,true),
    ('recordlover_04', 'recordlover04@example.com', 'password123', 'David', 'Lee', '1965-05-18', '645678901', 'CUSTOMER', false,true),
    ('spinningvinyl_05', 'spinningvinyl05@example.com', 'password123', 'Eve', 'Taylor', '2004-09-10', '656789012', 'CUSTOMER', false,true),
    ('discophile_06', 'discophile06@example.com', 'password123', 'Frank', 'Miller', '1975-01-25', '641769924', 'CUSTOMER', false,false),
    ('soundsystemfan_07', 'soundsystemfan07@example.com', 'password123', 'Grace', 'Davis', '1983-04-12', '612564574', 'CUSTOMER', false,true),
    ('thevinylman_08', 'thevinylman08@example.com', 'password123', 'Hank', 'Garcia', '1999-12-05', '65935968', 'CUSTOMER', false,false),
    ('oldschoolvinyl_09', 'oldschoolvinyl09@example.com', 'password123', 'Irene', 'Martinez', '1991-06-28', '636766940', 'CUSTOMER', false,true),
    ('newagevinyl_10', 'newagevinyl10@example.com', 'password123', 'Jack', 'Wilson', '1974-02-15', '669067739', 'CUSTOMER', false,true);

-- Employees
INSERT INTO users (username, email, password, first_name, last_name, date_of_birth, phone, user_type, is_deleted, job_title, salary, work_hours)
VALUES
    ('admin_lyla', 'lyla.gibson@vinylshop.com', 'admin', 'Lyla', 'Gibson', '1983-02-14', '612345678', 'EMPLOYEE', false, 'Store Manager', 6500.00, 40),
    ('admin_amanda', 'amanda.fowler@vinylshop.com', 'admin', 'Amanda', 'Fowler', '1992-11-12', '656789012', 'EMPLOYEE', false, 'Store Manager', 6000.00, 40),
    ('employee_marcus', 'marcus.hale@vinylshop.com', 'employee', 'Marcus', 'Hale', '1990-05-09', '623456789', 'EMPLOYEE', false,'Sales Associate', 3600.00, 32),
    ('employee_gina', 'gina.parker@vinylshop.com', 'employee', 'Gina', 'Parker', '1992-07-20', '634567890', 'EMPLOYEE', false,'Sales Associate', 2900.00, 24),
    ('employee_oliver', 'oliver.grant@vinylshop.com', 'employee', 'Oliver', 'Grant', '1988-03-22', '645678901', 'EMPLOYEE', false, 'Warehouse Coordinator', 5500.00, 32);


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