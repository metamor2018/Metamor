-- 投稿

# --- !Ups
CREATE TABLE statuses(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  world_id BIGINT NOT NULL,
  character_id VARCHAR(20) NOT NULL,
  reply BOOLEAN NOT NULL DEFAULT FALSE,
  in_reply_to_id BIGINT,
  text VARCHAR(255) NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (world_id) REFERENCES worlds(id),
  FOREIGN KEY (character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE statuses;
