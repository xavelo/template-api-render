CREATE TABLE IF NOT EXISTS "notification" (
    id UUID PRIMARY KEY,
    authorization_id UUID NOT NULL,
    student_id UUID NOT NULL,
    guardian_id UUID NOT NULL,
    status VARCHAR NOT NULL,
    sent_at TIMESTAMPTZ,
    responded_at TIMESTAMPTZ,
    responded_by VARCHAR
);
