CREATE TABLE raw_quotes (
    quote_id UUID PRIMARY KEY REFERENCES quotes(id) ON DELETE CASCADE,
    payload TEXT NOT NULL
);
