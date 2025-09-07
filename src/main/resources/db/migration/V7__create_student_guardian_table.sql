CREATE TABLE IF NOT EXISTS public."student_guardian" (
    student_id UUID NOT NULL,
    guardian_id UUID NOT NULL,
    PRIMARY KEY (student_id, guardian_id),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES public."student"(id),
    CONSTRAINT fk_guardian FOREIGN KEY (guardian_id) REFERENCES public."guardian"(id)
);
