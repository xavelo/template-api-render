CREATE TABLE IF NOT EXISTS "student_guardian" (
    student_id UUID NOT NULL,
    guardian_id UUID NOT NULL,
    PRIMARY KEY (student_id, guardian_id),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES "student"(id),
    CONSTRAINT fk_guardian FOREIGN KEY (guardian_id) REFERENCES "guardian"(id)
);
