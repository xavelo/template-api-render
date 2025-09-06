CREATE TABLE IF NOT EXISTS "authorization_student" (
    id UUID PRIMARY KEY,
    authorization_id UUID NOT NULL REFERENCES "authorization"(id),
    student_id UUID NOT NULL REFERENCES "student"(id)
);
