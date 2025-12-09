-- ============================================
-- INSERT USERS
-- ============================================

-- ADMIN (odmah aktivan, nije potrebno aktiviranje mejlom)
INSERT INTO users (
    username, password, full_name, email, date_of_birth, role, status
) VALUES (
             'admin',
             '$2a$10$0pnsNrPC9/hUV73rnMUP3.vBsOMcorzyWokTzJag8OdSVZk5oMF7.', -- 123
             'System Administrator',
             'admin@example.com',
             '1990-01-01',
             'ADMIN',
             'ACTIVE'       -- odmah aktivan

         );

-- GAMER #1 (nije aktiviran → INITIALIZED)
INSERT INTO users (
    username, password, full_name, email, date_of_birth, role
) VALUES (
             'gamer_one',
             '$2a$10$0pnsNrPC9/hUV73rnMUP3.vBsOMcorzyWokTzJag8OdSVZk5oMF7.',
             'John Gamer',
             'gamer1@example.com',
             '2000-05-10',
             'GAMER'
         );
-- trigger automatski kreira gamer_stats

-- GAMER #2
INSERT INTO users (
    username, password, full_name, email, date_of_birth, role
) VALUES (
             'gamer_two',
             '$2a$10$0pnsNrPC9/hUV73rnMUP3.vBsOMcorzyWokTzJag8OdSVZk5oMF7.',
             'Marko Player',
             'gamer2@example.com',
             '1999-09-05',
             'GAMER'
         );
-- trigger automatski kreira gamer_stats

