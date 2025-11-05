CREATE TABLE tag (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(24) NOT NULL UNIQUE
);

CREATE TABLE quote_tag (
    quote_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (quote_id, tag_id),
    CONSTRAINT fk_quote FOREIGN KEY (quote_id) REFERENCES quote(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

INSERT INTO tag (name)
SELECT DISTINCT tag_value
FROM (
    SELECT UNNEST(theme_tags) AS tag_value FROM quote WHERE theme_tags IS NOT NULL
) AS tags
WHERE tag_value IS NOT NULL;

INSERT INTO quote_tag (quote_id, tag_id)
SELECT q.id, t.id
FROM quote q
         JOIN LATERAL UNNEST(q.theme_tags) AS tag_value(tag_name) ON TRUE
         JOIN tag t ON t.name = tag_value.tag_name;

ALTER TABLE quote DROP COLUMN theme_tags;
