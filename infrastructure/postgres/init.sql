-- Create user: app_user
CREATE USER app_user WITH PASSWORD 'app_password';
GRANT ALL PRIVILEGES ON DATABASE app_db TO app_user;
GRANT USAGE, CREATE ON SCHEMA public TO app_user;