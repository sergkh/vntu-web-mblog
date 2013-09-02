CREATE TABLE users (
    id bigserial PRIMARY KEY,
    created timestamp NOT NULL DEFAULT NOW(),
    login varchar(128) NOT NULL,
    email varchar(512) NOT NULL,
    passhash varchar(40) NOT NULL,
    blocked timestamp,
    has_avatar boolean DEFAULT false,
    UNIQUE(email),
    UNIQUE(login)
);

CREATE TABLE permissions (
    id smallint PRIMARY KEY,
    name character varying(255)
);

CREATE TABLE user_permissions (
    user_id bigint,
    perm_id smallint
);

CREATE TABLE posts (
  id bigserial PRIMARY KEY,
  owner_id bigint NOT NULL,
  text varchar(512) NOT NULL,
  stamp timestamp NOT NULL DEFAULT NOW(),
  post_validation_date timestamp,
  state smallint
);

CREATE TABLE user_followers (
  subscriber_id bigint,
  followed_id bigint,
  PRIMARY KEY(subscriber_id, followed_id)
);

ALTER TABLE user_permissions ADD CONSTRAINT fk_user_permissions_users FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_permissions ADD CONSTRAINT fk_user_permissions_permissions FOREIGN KEY (perm_id) REFERENCES permissions(id);

ALTER TABLE posts ADD CONSTRAINT fk_posts_owner FOREIGN KEY (owner_id) REFERENCES users(id);

ALTER TABLE user_followers ADD CONSTRAINT fk_followers_subscriber FOREIGN KEY (subscriber_id) REFERENCES users(id);
ALTER TABLE user_followers ADD CONSTRAINT fk_followers_follower FOREIGN KEY (followed_id) REFERENCES users(id);

CREATE INDEX idx_posts_owner_id ON posts (owner_id);
CREATE INDEX idx_posts_stamp ON posts (stamp);

INSERT INTO permissions VALUES (1, 'USER');
INSERT INTO permissions VALUES (2, 'MODERATE_POSTS');
INSERT INTO permissions VALUES (3, 'MANAGE_USERS');

INSERT INTO users (login,email,passhash) VALUES ('admin', 'admin@mail.com', 'd033e22ae348aeb5660fc2140aec35850c4da997');
INSERT INTO users (login,email,passhash) VALUES ('tester', 'tester@mail.com', 'ab4d8d2a5f480a137067da17100271cd176607a1');
INSERT INTO users (login,email,passhash) VALUES ('user', 'user@mail.com', '12dea96fec20593566ab75692c9949596833adc9');
INSERT INTO users (login,email,passhash,blocked) VALUES ('<h1>hacker</h1>', 'hacker@mail.ru', '12dea96fec20593566ab75692c9949596833adc9', NOW());

-- add permissions to users

INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='admin', 1);
INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='admin', 2);
INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='admin', 3);

INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='tester', 1);
INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='user', 1);
INSERT INTO user_permissions VALUES (SELECT id FROM users WHERE login='<h1>hacker</h1>', 1);

-- create some test posts

INSERT INTO posts (owner_id, text) VALUES (SELECT id FROM users WHERE login='admin', 'First post');
INSERT INTO posts (owner_id, text) VALUES (SELECT id FROM users WHERE login='admin', 'Second post');

INSERT INTO posts (owner_id, text) VALUES (SELECT id FROM users WHERE login='tester', 'Testers first post');
INSERT INTO posts (owner_id, text) VALUES (SELECT id FROM users WHERE login='tester', 'Testers second post');

-- subscribe admin to tester 

INSERT INTO user_followers VALUES ( SELECT id FROM users WHERE login='admin' , SELECT id FROM users WHERE login='tester');



