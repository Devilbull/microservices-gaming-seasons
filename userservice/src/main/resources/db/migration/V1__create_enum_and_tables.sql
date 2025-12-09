-- ============================================
-- Create ENUM types
-- ============================================

DROP TYPE IF EXISTS user_status CASCADE;
DROP TYPE IF EXISTS organizer_title CASCADE;

CREATE TYPE user_status AS ENUM ('INITIALIZED', 'ACTIVE', 'BLOCKED');
CREATE TYPE  user_role AS ENUM ('ADMIN', 'GAMER');



CREATE TYPE organizer_title AS ENUM
    (
    'BRONZE',
    'SILVER',
    'GOLD',
    'PLATINUM',
    'DIAMOND'
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- Create tables
-- ============================================

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

                       username        VARCHAR(50) UNIQUE NOT NULL,
                       password        VARCHAR(255)       NOT NULL,
                       full_name       VARCHAR(120)       NOT NULL,
                       email           VARCHAR(120) UNIQUE NOT NULL,
                       date_of_birth   DATE               NOT NULL,

                       role   VARCHAR(20) NOT NULL DEFAULT 'GAMER'
                           CHECK (role IN ('ADMIN', 'GAMER')),

                       status VARCHAR(20) NOT NULL DEFAULT 'INITIALIZED'
                           CHECK (status IN ('INITIALIZED', 'ACTIVE', 'BLOCKED'))


);

CREATE TABLE gamer_stats (
                             user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,

                             total_sessions              INTEGER NOT NULL DEFAULT 0,
                             attended_sessions           INTEGER NOT NULL DEFAULT 0,
                             missed_sessions             INTEGER NOT NULL DEFAULT 0,
                             attendance_rate             NUMERIC(5,2) NOT NULL DEFAULT 0.00,

                             successful_hosted_sessions  INTEGER NOT NULL DEFAULT 0,
                             organizer_title             organizer_title NOT NULL DEFAULT 'BRONZE'
);

-- ============================================
-- Trigger function: create stats for GAMER users
-- ============================================

CREATE OR REPLACE FUNCTION create_default_gamer_stats()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.role = 'GAMER' THEN
        INSERT INTO gamer_stats (user_id)
        VALUES (NEW.id);
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- Trigger
-- ============================================

CREATE TRIGGER trg_create_gamer_stats
    AFTER INSERT ON users
    FOR EACH ROW
    EXECUTE FUNCTION create_default_gamer_stats();

-- ============================================