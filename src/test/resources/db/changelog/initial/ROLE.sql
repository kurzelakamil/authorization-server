CREATE TABLE `role` (
	id bigserial NOT NULL,
	`name` varchar(255) NULL,
	CONSTRAINT role_pk PRIMARY KEY (id),
	CONSTRAINT roles_name_un UNIQUE (name)
);