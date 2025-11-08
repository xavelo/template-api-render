ALTER TABLE quotes
    ADD COLUMN IF NOT EXISTS likes BIGINT NOT NULL DEFAULT 0;

UPDATE quotes q
SET likes = ql.likes
FROM quote_like ql
WHERE q.id = ql.quote_id;

DROP TABLE IF EXISTS quote_like;
