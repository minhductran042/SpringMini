-- Add github_id column to users table
ALTER TABLE users ADD COLUMN github_id VARCHAR(255);

-- Create index for better performance
CREATE INDEX idx_users_github_id ON users(github_id); 