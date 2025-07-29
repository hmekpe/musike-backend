-- Initialize Musike Database
-- This script runs when the PostgreSQL container starts for the first time

-- Create database if it doesn't exist
SELECT 'CREATE DATABASE Musike'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Musike')\gexec

-- Connect to the Musike database
\c Musike;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create custom functions for audit trails (optional)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create indexes for better performance
-- These will be created automatically by JPA, but you can add custom ones here

-- Grant necessary permissions
GRANT ALL PRIVILEGES ON DATABASE Musike TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- Set timezone
SET timezone = 'UTC';

-- Log the initialization
DO $$
BEGIN
    RAISE NOTICE 'Musike database initialized successfully at %', CURRENT_TIMESTAMP;
END $$;