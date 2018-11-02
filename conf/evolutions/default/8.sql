-- 投稿お気に入り

# --- !Ups
CREATE TABLE statuses_favorites (
  id BIGINT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  status_id BIGINT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (status_id) REFERENCES statuses(id)
);

# --- !Downs
DROP TABLE statuses_favorites;
