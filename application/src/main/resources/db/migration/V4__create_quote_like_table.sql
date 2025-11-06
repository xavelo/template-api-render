CREATE TABLE IF NOT EXISTS quote_like (
    quote_id UUID PRIMARY KEY,
    likes BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_quote_like_quote FOREIGN KEY (quote_id) REFERENCES quote(id) ON DELETE CASCADE
);
