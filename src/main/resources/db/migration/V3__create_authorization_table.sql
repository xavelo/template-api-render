CREATE TABLE IF NOT EXISTS "authorization" (
    id UUID PRIMARY KEY,
    title VARCHAR NOT NULL,
    text TEXT NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR NOT NULL,
    sent_at TIMESTAMPTZ,
    sent_by VARCHAR,
    approved_at TIMESTAMPTZ,
    approved_by VARCHAR
);
