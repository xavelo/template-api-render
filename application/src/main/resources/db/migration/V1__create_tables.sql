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
    text TEXT NOT NULL,
    century TEXT,
    likes BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_quote_author FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(24) NOT NULL UNIQUE
);

CREATE TABLE quote_tag (
    quote_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (quote_id, tag_id),
    CONSTRAINT fk_quote FOREIGN KEY (quote_id) REFERENCES quotes(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX author_name_unique_idx
    ON authors ((LOWER(TRIM(name))));

CREATE UNIQUE INDEX quote_text_unique_idx ON quotes(text);
