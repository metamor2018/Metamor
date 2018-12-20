-- 投稿お気に入り

# --- !Ups
CREATE TABLE statuses_favorites (
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id VARCHAR(20) NOT NULL,
  status_id BIGINT NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (status_id) REFERENCES statuses(id)
);

# --- !Downs
DROP TABLE statuses_favorites;
