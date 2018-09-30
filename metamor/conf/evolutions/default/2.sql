-- フォロー

# --- !Ups
CREATE TABLE relations (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  follow_id VARCHAR(20) NOT NULL,
  follower_id VARCHAR(20) NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (follow_id) REFERENCES users(user_id),
  FOREIGN KEY (follower_id) REFERENCES users(user_id)
);

# --- !Downs
drop table relations