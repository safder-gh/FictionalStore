CREATE TABLE files (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  file_name TEXT NOT NULL,
  file_path TEXT NOT NULL,
  thumbnail_path TEXT,
  content_type TEXT,
  size BIGINT,
  is_public BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);
