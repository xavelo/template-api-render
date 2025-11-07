ALTER TABLE quote
    ADD COLUMN likes INTEGER NOT NULL DEFAULT 0;

UPDATE quote q
SET likes = COALESCE(ql.likes, 0)
FROM quote_like ql
WHERE q.id = ql.quote_id;

DROP TABLE IF EXISTS quote_like;
