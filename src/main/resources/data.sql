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


INSERT INTO vinyl_records (title, description, genre, label, price, release_date, play_time_seconds, is_limited_edition, EAN)
VALUES
    ('A Night at the Opera', 'Classic rock album by Queen.', 'ROCK', 'EMI', 19.99, '1975-11-21', 3600, true, '1234567890123'),
    ('Abbey Road', 'Iconic Beatles album, featuring "Come Together".', 'ROCK', 'Apple Records', 22.99, '1969-09-26', 4200, true, '2345678901234'),
    ('1989', 'Taylor Swift’s hit album featuring "Shake It Off".', 'POP', 'Big Machine Records', 16.99, '2014-10-27', 2700, false, '3456789012345'),
    ('Lemonade', 'Powerful visual album by Beyoncé.', 'RNB', 'Parkwood Entertainment', 24.99, '2016-04-23', 5400, true, '4567890123456'),
    ('Viva la Vida', 'Coldplay’s critically acclaimed album.', 'POP', 'Parlophone', 18.99, '2008-06-12', 3900, false, '5678901234567'),
    ('21', 'Adele’s breakout album with hits like "Rolling in the Deep".', 'POP', 'XL Recordings', 19.99, '2011-01-24', 3300, true, '6789012345678'),
    ('The Joshua Tree', 'U2’s legendary album that includes "With or Without You".', 'ROCK', 'Island Records', 21.99, '1987-03-09', 4200, false, '7890123456789'),
    ('The Marshall Mathers LP', 'Eminem’s controversial and successful LP.', 'HIP_HOP', 'Interscope Records', 17.99, '2000-05-23', 4200, true, '8901234567890'),
    ('Kind of Blue', 'A jazz masterpiece by Miles Davis.', 'JAZZ', 'Columbia Records', 23.99, '1959-08-17', 4200, false, '9012345678901'),
    ('Rumours', 'Fleetwood Mac’s greatest album, with "Go Your Own Way".', 'ROCK', 'Warner Bros. Records', 20.99, '1977-02-04', 3900, true, '0123456789012'),
    ('Arrival', 'ABBA’s classic album, featuring "Dancing Queen".', 'POP', 'Polar Music', 18.99, '1976-10-11', 3300, false, '1234567890124'),
    ('When We All Fall Asleep, Where Do We Go?', 'Billie Eilish’s breakout album.', 'POP', 'Darkroom/Interscope', 16.99, '2019-03-29', 3000, true, '2345678901235'),
    ('AM', 'Arctic Monkeys’ album with a more hip hop-influenced sound.', 'ROCK', 'Domino Recording', 19.99, '2013-09-09', 3900, true, '3456789012346'),
    ('What a Wonderful World', 'Classic album by jazz legend Louis Armstrong.', 'JAZZ', 'ABC-Paramount', 14.99, '1968-08-01', 2700, false, '4567890123457'),
    ('I Never Loved a Man the Way I Love You', 'Aretha Franklin’s breakthrough album.', 'SOUL', 'Atlantic Records', 21.99, '1967-03-10', 3300, true, '5678901234568'),
    ('Jazz Impressions', 'An album showcasing Steffon Morrison’s jazz prowess.', 'JAZZ', 'Verve Records', 19.99, '1965-09-15', 3900, false, '6789012345679'),
    ('News of the World', 'Queen’s album featuring "We Are the Champions".', 'ROCK', 'EMI', 18.99, '1977-10-28', 3600, true, '7890123456790'),
    ('Revolver', 'The Beatles’ transformative album with experimental sounds.', 'ROCK', 'Parlophone', 19.99, '1966-08-05', 3300, false, '8901234567901'),
    ('Bitches Brew', 'A landmark jazz fusion album by Miles Davis.', 'JAZZ', 'Columbia Records', 24.99, '1970-03-30', 4200, true, '9012345678912'),
    ('Satchmo at Symphony Hall', 'Live performance album by Louis Armstrong.', 'JAZZ', 'Columbia Records', 17.99, '1947-03-30', 3000, false, '0123456789013');