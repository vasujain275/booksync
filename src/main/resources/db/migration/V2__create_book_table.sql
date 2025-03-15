CREATE TABLE IF NOT EXISTS "book" (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    authors TEXT, -- Comma-separated list of author names
    description TEXT,
    publisher TEXT,
    published_date TEXT,
    category TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
