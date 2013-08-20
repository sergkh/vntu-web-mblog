CREATE TABLE users (
    id bigserial PRIMARY KEY,
    created timestamp NOT NULL DEFAULT NOW(),
    login varchar(128) NOT NULL,
    email varchar(512) NOT NULL,
    passhash varchar(40) NOT NULL,
    blocked timestamp,
    UNIQUE(email),
    UNIQUE(login)
);

CREATE TABLE roles (
    id serial PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE user_roles (
    user_id bigint,
    role_id integer
);

CREATE TABLE posts (
  id bigserial PRIMARY KEY,
  owner_id bigint NOT NULL,
  text varchar(512) NOT NULL,
  stamp timestamp NOT NULL DEFAULT NOW()
);

CREATE TABLE user_followers (
  subscriber_id bigint,
  followed_id bigint,
  PRIMARY KEY(subscriber_id, followed_id)
);

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE posts ADD CONSTRAINT fk_posts_owner FOREIGN KEY (owner_id) REFERENCES users(id);

ALTER TABLE user_followers ADD CONSTRAINT fk_followers_subscriber FOREIGN KEY (subscriber_id) REFERENCES users(id);
ALTER TABLE user_followers ADD CONSTRAINT fk_followers_follower FOREIGN KEY (followed_id) REFERENCES users(id);

CREATE INDEX idx_posts_owner_id ON posts (owner_id);
CREATE INDEX idx_posts_stamp ON posts (stamp);