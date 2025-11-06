ALTER TABLE quote
    DROP COLUMN IF EXISTS translator,
    DROP COLUMN IF EXISTS language,
    DROP COLUMN IF EXISTS reference_system,
    DROP COLUMN IF EXISTS work_part,
    DROP COLUMN IF EXISTS locator,
    DROP COLUMN IF EXISTS source_url,
    DROP COLUMN IF EXISTS source_institution,
    DROP COLUMN IF EXISTS license;
