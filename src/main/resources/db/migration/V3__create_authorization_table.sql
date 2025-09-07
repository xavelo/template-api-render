CREATE TABLE IF NOT EXISTS public."authorization" (
    id UUID PRIMARY KEY,
    title VARCHAR NOT NULL,
    text TEXT NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE,
    sent_by VARCHAR,
    approved_at TIMESTAMP WITH TIME ZONE,
    approved_by VARCHAR
);
