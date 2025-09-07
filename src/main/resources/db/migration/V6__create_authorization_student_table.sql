CREATE TABLE IF NOT EXISTS public."authorization_student" (
    id UUID PRIMARY KEY,
    authorization_id UUID NOT NULL REFERENCES public."authorization"(id),
    student_id UUID NOT NULL REFERENCES public."student"(id)
);
