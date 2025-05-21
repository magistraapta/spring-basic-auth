-- Insert admin user with password 'admin123' (BCrypt hashed)
INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi')
ON CONFLICT (username) DO NOTHING;

-- Insert regular user with password 'user123' (BCrypt hashed)
INSERT INTO users (username, password)
VALUES ('user', '$2a$10$8K1p/a0dR1LXMIgoEDFrwOeAQGQ4Qx1I8mRkDBWnOBd.fP7T3WYyS')
ON CONFLICT (username) DO NOTHING; 