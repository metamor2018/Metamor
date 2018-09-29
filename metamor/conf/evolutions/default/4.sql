# --- !Ups
CREATE TABLE favorites (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_id VARCHAR(20) NOT NULL,
  content_id INT NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (content_id) REFERENCES contents(id)
);

# --- !Downs
drop table favorites
