CREATE TABLE `user_` (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	`password` varchar(1000) NULL,
	role_id bigint NOT NULL,
	uuid bigint,
	status varchar(30),
	verification_token varchar(50),
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT email_un UNIQUE (email),
	CONSTRAINT uuid_un UNIQUE (uuid)
);