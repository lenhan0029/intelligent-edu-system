CREATE DATABASE auth_db;
CREATE DATABASE user_db;
CREATE DATABASE course_db;
CREATE DATABASE exam_db;
CREATE DATABASE schedule_db;
CREATE DATABASE finance_db;
CREATE DATABASE notification_db;
CREATE DATABASE file_db;
CREATE DATABASE log_db;

-- Wait for DBs to be created and then seed auth_db roles
\c auth_db
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_MODERATOR') ON CONFLICT (name) DO NOTHING;
