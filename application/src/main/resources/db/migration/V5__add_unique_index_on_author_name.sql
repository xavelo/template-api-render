CREATE UNIQUE INDEX IF NOT EXISTS author_name_unique_idx
    ON authors ((LOWER(TRIM(name))));
