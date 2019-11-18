-------------------- user --------------------
CREATE TABLE public."user"
(
	uuid uuid NOT NULL,
    username varchar(255) COLLATE pg_catalog."default" NOT NULL,
    password varchar(255) COLLATE pg_catalog."default" NOT NULL,
	date_created timestamp NOT NULL,
	last_login timestamp,
	user_type varchar(255),
    CONSTRAINT user_pkey PRIMARY KEY (uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."user"
    OWNER to postgres;

-------------------- notepad --------------------
CREATE TABLE public."notepad"
(
	uuid uuid NOT NULL,
    name varchar(255) COLLATE pg_catalog."default" NOT NULL,
    body text,
    editor_user_uuid uuid,
    date_edited timestamp,
    creator_user_uuid uuid NOT NULL,
    date_created timestamp NOT NULL,
    CONSTRAINT notepad_pkey PRIMARY KEY (uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."notepad"
    OWNER to postgres;

-------------------- notepad_user_share --------------------
CREATE TABLE public."notepad_user_share"
(
	uuid uuid NOT NULL,
    notepad_uuid uuid NOT NULL,
    user_uuid uuid NOT NULL,
    date_created timestamp NOT NULL,
    CONSTRAINT notepad_user_share_pkey PRIMARY KEY (uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."notepad_user_share"
    OWNER to postgres;