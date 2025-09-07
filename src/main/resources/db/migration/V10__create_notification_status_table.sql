CREATE TABLE IF NOT EXISTS public."notification_status" (
    code VARCHAR PRIMARY KEY
);

INSERT INTO public."notification_status" (code) VALUES
    ('PENDING'),
    ('SENT'),
    ('APPROVED'),
    ('REJECT'),
    ('EXPIRED');

ALTER TABLE public."notification"
    ADD CONSTRAINT fk_notification_status
    FOREIGN KEY (status) REFERENCES public."notification_status"(code);
