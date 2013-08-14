CREATE TABLE users (
    id serial PRIMARY KEY,
    created timestamp without time zone NOT NULL,
    email character varying(512),
    passhash character varying(40) NOT NULL,
    deleted timestamp without time zone,
    UNIQUE(email)
);

CREATE TABLE roles (
    id serial PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE user_roles (
    user_id integer,
    role_id integer
);

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id);