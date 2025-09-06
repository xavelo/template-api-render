CREATE TABLE IF NOT EXISTS "notification_status" (
    code VARCHAR PRIMARY KEY
);

INSERT INTO "notification_status" (code) VALUES
    ('SENT'),
    ('APPROVED'),
    ('REJECT'),
    ('EXPIRED');

ALTER TABLE "notification"
    ADD CONSTRAINT fk_notification_status
    FOREIGN KEY (status) REFERENCES "notification_status"(code);
