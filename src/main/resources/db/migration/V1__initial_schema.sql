CREATE TABLE IF NOT EXISTS public."user" (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public."authorization" (
    id UUID PRIMARY KEY,
    title VARCHAR NOT NULL,
    text TEXT NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE,
    sent_by VARCHAR,
    approved_at TIMESTAMP WITH TIME ZONE,
    approved_by VARCHAR,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_authorization_created_by FOREIGN KEY (created_by) REFERENCES public."user"(id)
);

CREATE TABLE IF NOT EXISTS public."guardian" (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS public."student" (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS public."authorization_student" (
    id UUID PRIMARY KEY,
    authorization_id UUID NOT NULL REFERENCES public."authorization"(id),
    student_id UUID NOT NULL REFERENCES public."student"(id)
);

CREATE TABLE IF NOT EXISTS public."student_guardian" (
    student_id UUID NOT NULL,
    guardian_id UUID NOT NULL,
    PRIMARY KEY (student_id, guardian_id),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES public."student"(id),
    CONSTRAINT fk_guardian FOREIGN KEY (guardian_id) REFERENCES public."guardian"(id)
);

CREATE TABLE IF NOT EXISTS public."notification_status" (
    code VARCHAR PRIMARY KEY
);

INSERT INTO public."notification_status" (code) VALUES
    ('PENDING'),
    ('SENT'),
    ('APPROVED'),
    ('REJECT'),
    ('EXPIRED');

CREATE TABLE IF NOT EXISTS public."notification" (
    id UUID PRIMARY KEY,
    authorization_id UUID NOT NULL,
    student_id UUID NOT NULL,
    guardian_id UUID NOT NULL,
    status VARCHAR NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE,
    responded_at TIMESTAMP WITH TIME ZONE,
    responded_by VARCHAR,
    sent_by UUID,
    CONSTRAINT fk_notification_status FOREIGN KEY (status) REFERENCES public."notification_status"(code)
);
