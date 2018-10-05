-- ユーザー

# --- !Ups
CREATE TABLE users (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id varchar(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp
);

# --- !Downs
drop table users