CREATE UNIQUE INDEX IF NOT EXISTS author_name_unique_idx
    ON author ((LOWER(TRIM(name))));
