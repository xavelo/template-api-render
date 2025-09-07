ALTER TABLE public."authorization"
    ALTER COLUMN created_by TYPE UUID USING created_by::uuid;

ALTER TABLE public."authorization"
    ADD CONSTRAINT fk_authorization_created_by FOREIGN KEY (created_by) REFERENCES public."user"(id);
