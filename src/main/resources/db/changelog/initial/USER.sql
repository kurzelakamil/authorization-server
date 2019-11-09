CREATE TABLE "user" (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(1000) NULL,
	role_id bigint NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT email_un UNIQUE (email)
);