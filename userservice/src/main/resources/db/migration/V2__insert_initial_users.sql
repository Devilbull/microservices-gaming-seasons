-- ============================================
-- INSERT USERS
-- ============================================

-- ADMIN (odmah aktivan, nije potrebno aktiviranje mejlom)
INSERT INTO users (
    username, password, full_name, email, date_of_birth, role, status, is_activated
) VALUES (
             'admin',
             'admin123',
             'System Administrator',
             'admin@example.com',
             '1990-01-01',
             'ADMIN',
             'ACTIVE',       -- odmah aktivan
             true            -- već aktiviran
         );

-- GAMER #1 (nije aktiviran → INITIALIZED)
INSERT INTO users (
    username, password, full_name, email, date_of_birth, role
) VALUES (
             'gamer_one',
             'pass123',
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
             'pass456',
             'Marko Player',
             'gamer2@example.com',
             '1999-09-05',
             'GAMER'
         );
-- trigger automatski kreira gamer_stats

