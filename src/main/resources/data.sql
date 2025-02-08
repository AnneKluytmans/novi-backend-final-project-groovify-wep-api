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
    ('I Never Loved a Man ', 'Aretha Franklin’s breakthrough album.', 'SOUL', 'Atlantic Records', 27.99, '1967-03-10', 3300, true, '5678901234568', (SELECT id FROM artists WHERE name = 'Aretha Franklin')),
    ('Soul Revolution', 'An album showcasing Steffon Morrison’s jazz prowess.', 'JAZZ', 'Verve Records', 24.99, '1965-09-15', 3900, false, '6789012345679', (SELECT id FROM artists WHERE name = 'Steffon Morrison')),
    ('News of the World', 'Queen’s album featuring "We Are the Champions".', 'ROCK', 'EMI', 31.99, '1977-10-28', 3600, true, '7890123456790', (SELECT id FROM artists WHERE name = 'Queen')),
    ('Revolver', 'The Beatles’ transformative album with experimental sounds.', 'ROCK', 'Parlophone', 30.99, '1966-08-05', 3300, false, '8901234567901', (SELECT id FROM artists WHERE name = 'The Beatles')),
    ('Bitches Brew', 'A landmark jazz fusion album by Miles Davis.', 'JAZZ', 'Columbia Records', 32.99, '1970-03-30', 4200, true, '9012345678912', (SELECT id FROM artists WHERE name = 'Miles Davis')),
    ('Satchmo at Symphony Hall', 'Live performance album by Louis Armstrong.', 'JAZZ', 'Columbia Records', 17.99, '1947-03-30', 3000, false, '0123456789013', (SELECT id FROM artists WHERE name = 'Louis Armstrong'));


--Vinyl Records Copies
INSERT INTO vinyl_record_copy (serial_number, is_sold, vinyl_record_id)
VALUES
    -- Copies 'A Night at the Opera'
    ('VYN-0001-0001', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0002', true, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0003', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0004', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0005', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0006', true, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0007', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),
    ('VYN-0001-0008', false, (SELECT id FROM vinyl_record WHERE title = 'A Night at the Opera')),

    -- Copies 'Abbey Road'
    ('VYN-0002-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0002', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0004', true, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0006', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0008', true, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0009', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0010', true, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0011', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),
    ('VYN-0002-0012', false, (SELECT id FROM vinyl_record WHERE title = 'Abbey Road')),

    -- Copies '1989'
    ('VYN-0003-0001', false, (SELECT id FROM vinyl_record WHERE title = '1989')),
    ('VYN-0003-0002', true, (SELECT id FROM vinyl_record WHERE title = '1989')),
    ('VYN-0003-0003', false, (SELECT id FROM vinyl_record WHERE title = '1989')),
    ('VYN-0003-0004', false, (SELECT id FROM vinyl_record WHERE title = '1989')),
    ('VYN-0003-0005', false, (SELECT id FROM vinyl_record WHERE title = '1989')),
    ('VYN-0003-0006', true, (SELECT id FROM vinyl_record WHERE title = '1989')),

    -- Copies 'Lemonade'
    ('VYN-0004-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0004', true, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0006', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0008', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),
    ('VYN-0004-0009', false, (SELECT id FROM vinyl_record WHERE title = 'Lemonade')),

    -- Copies 'Viva la Vida'
    ('VYN-0005-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),
    ('VYN-0005-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),
    ('VYN-0005-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),                                                                        ('VYN-0005-0004', true, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0005-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),
    ('VYN-0005-0006', true, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),
    ('VYN-0005-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),
    ('VYN-0005-0008', true, (SELECT id FROM vinyl_record WHERE title = 'Viva la Vida')),

    -- Copies '21'
    ('VYN-0006-0001', false, (SELECT id FROM vinyl_record WHERE title = '21')),
    ('VYN-0006-0002', true, (SELECT id FROM vinyl_record WHERE title = '21')),
    ('VYN-0006-0003', false, (SELECT id FROM vinyl_record WHERE title = '21')),
    ('VYN-0006-0004', true, (SELECT id FROM vinyl_record WHERE title = '21')),
    ('VYN-0006-0005', false, (SELECT id FROM vinyl_record WHERE title = '21')),

    -- Copies 'The Joshua Tree'
    ('VYN-0007-0001', true, (SELECT id FROM vinyl_record WHERE title = 'The Joshua Tree')),
    ('VYN-0007-0002', false, (SELECT id FROM vinyl_record WHERE title = 'The Joshua Tree')),
    ('VYN-0007-0003', false, (SELECT id FROM vinyl_record WHERE title = 'The Joshua Tree')),
    ('VYN-0007-0004', false, (SELECT id FROM vinyl_record WHERE title = 'The Joshua Tree')),

    -- Copies 'The Marshall Mathers'
    ('VYN-0008-0001', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0002', true, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0003', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0004', true, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0005', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0006', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0007', true, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0008', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0009', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),
    ('VYN-0008-0010', false, (SELECT id FROM vinyl_record WHERE title = 'The Marshall Mathers')),

    -- Copies 'Kind of Blue'
    ('VYN-0009-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),                                                                        ('VYN-0005-0004', true, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0006', true, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0008', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0009', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0010', true, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),
    ('VYN-0009-0011', false, (SELECT id FROM vinyl_record WHERE title = 'Kind of Blue')),

    -- Copies 'Rumours'
    ('VYN-0010-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0004', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0006', true, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0008', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0009', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0010', true, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0011', false, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),
    ('VYN-0010-0012', true, (SELECT id FROM vinyl_record WHERE title = 'Rumours')),

    -- Copies 'Arrival'
    ('VYN-0011-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),
    ('VYN-0011-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),
    ('VYN-0011-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),
    ('VYN-0011-0004', true, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),
    ('VYN-0011-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),
    ('VYN-0011-0006', false, (SELECT id FROM vinyl_record WHERE title = 'Arrival')),

    -- Copies 'When We All Fall Asleep, Where Do We Go?'
    ('VYN-0012-0001', false, (SELECT id FROM vinyl_record WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    ('VYN-0012-0002', true, (SELECT id FROM vinyl_record WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    ('VYN-0012-0003', false, (SELECT id FROM vinyl_record WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    ('VYN-0012-0004', true, (SELECT id FROM vinyl_record WHERE title = 'When We All Fall Asleep, Where Do We Go?')),
    ('VYN-0012-0005', false, (SELECT id FROM vinyl_record WHERE title = 'When We All Fall Asleep, Where Do We Go?')),

    -- Copies 'AM'
    ('VYN-0013-0001', false, (SELECT id FROM vinyl_record WHERE title = 'AM')),
    ('VYN-0013-0002', true, (SELECT id FROM vinyl_record WHERE title = 'AM')),
    ('VYN-0013-0003', false, (SELECT id FROM vinyl_record WHERE title = 'AM')),
    ('VYN-0013-0004', false, (SELECT id FROM vinyl_record WHERE title = 'AM')),
    ('VYN-0013-0005', false, (SELECT id FROM vinyl_record WHERE title = 'AM')),

    -- Copies 'What a Wonderful World'
    ('VYN-0014-0001', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0002', true, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0003', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0004', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0005', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0006', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0007', true, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0008', true, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),
    ('VYN-0014-0009', false, (SELECT id FROM vinyl_record WHERE title = 'What a Wonderful World')),

    -- Copies 'I Never Loved a Man'
    ('VYN-0015-0001', false, (SELECT id FROM vinyl_record WHERE title = 'I Never Loved a Man')),
    ('VYN-0015-0002', true, (SELECT id FROM vinyl_record WHERE title = 'I Never Loved a Man')),
    ('VYN-0015-0003', false, (SELECT id FROM vinyl_record WHERE title = 'I Never Loved a Man')),
    ('VYN-0015-0004', false, (SELECT id FROM vinyl_record WHERE title = 'I Never Loved a Man')),

    -- Copies 'Soul Revolution'
    ('VYN-0016-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),
    ('VYN-0016-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),
    ('VYN-0016-0003', true, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),
    ('VYN-0016-0004', false, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),
    ('VYN-0016-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),
    ('VYN-0016-0006', false, (SELECT id FROM vinyl_record WHERE title = 'Soul Revolution')),

    -- Copies 'News of the World'
    ('VYN-0017-0001', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0002', true, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0003', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0004', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0005', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0006', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0007', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0008', true, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0009', true, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0010', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0011', true, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),
    ('VYN-0017-0012', false, (SELECT id FROM vinyl_record WHERE title = 'News of the World')),

    -- Copies 'Revolver'
    ('VYN-0018-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Revolver')),
    ('VYN-0018-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Revolver')),
    ('VYN-0018-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Revolver')),
    ('VYN-0018-0004', false, (SELECT id FROM vinyl_record WHERE title = 'Revolver')),
    ('VYN-0018-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Revolver')),

    -- Copies 'Bitches Brew'
    ('VYN-0019-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0004', false, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0006', true, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),
    ('VYN-0019-0007', false, (SELECT id FROM vinyl_record WHERE title = 'Bitches Brew')),

    -- Copies 'Satchmo at Symphony Hall'
    ('VYN-0020-0001', false, (SELECT id FROM vinyl_record WHERE title = 'Satchmo at Symphony Hall')),
    ('VYN-0020-0002', true, (SELECT id FROM vinyl_record WHERE title = 'Satchmo at Symphony Hall')),
    ('VYN-0020-0003', false, (SELECT id FROM vinyl_record WHERE title = 'Satchmo at Symphony Hall')),
    ('VYN-0020-0004', false, (SELECT id FROM vinyl_record WHERE title = 'Satchmo at Symphony Hall')),
    ('VYN-0020-0005', false, (SELECT id FROM vinyl_record WHERE title = 'Satchmo at Symphony Hall'));