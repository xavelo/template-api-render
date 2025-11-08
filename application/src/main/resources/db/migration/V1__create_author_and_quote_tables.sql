CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    wikipedia_url TEXT
);

CREATE TABLE quotes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id UUID NOT NULL,
    work TEXT,
    year INTEGER,
    translator TEXT,
    language TEXT,
    text TEXT NOT NULL,
    reference_system TEXT,
    work_part TEXT,
    locator TEXT,
    theme_tags TEXT[],
    century TEXT,
    source_url TEXT,
    source_institution TEXT,
    license TEXT,
    CONSTRAINT fk_quote_author FOREIGN KEY (author_id) REFERENCES authors(id)
);
