CREATE TABLE IF NOT EXISTS tag (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(24) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS quote_tag (
    quote_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (quote_id, tag_id),
    CONSTRAINT fk_quote FOREIGN KEY (quote_id) REFERENCES quote(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

ALTER TABLE quote DROP COLUMN IF EXISTS theme_tags;
