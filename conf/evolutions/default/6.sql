-- ワールド（イベント）参加

# --- !Ups
CREATE TABLE worlds_entries(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  world_id BIGINT NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (world_id) REFERENCES worlds(id)
);

# --- !Downs
DROP TABLE worlds_entries;
