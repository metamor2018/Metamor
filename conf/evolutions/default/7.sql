-- 投稿

# --- !Ups
CREATE TABLE statuses(
  id BIGINT NOT NULL PRIMARY KEY,
  world_id BIGINT NOT NULL,
  character_id BIGINT NOT NULL,
  reply BOOLEAN NOT NULL DEFAULT FALSE,
  in_reply_to_id BIGINT,
  text VARCHAR(255) NOT NULL,
  FOREIGN KEY (world_id) REFERENCES worlds(id),
  FOREIGN KEY (character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE statuses;