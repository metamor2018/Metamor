# --- !Ups
CREATE TABLE contents (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id VARCHAR(20) NOT NULL,
  content VARCHAR(255) NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

# --- !Downs
drop table contents
