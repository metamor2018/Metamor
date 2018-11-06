-- ワールド（イベント）参加

# --- !Ups
CREATE TABLE worlds_entries(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  world_id BIGINT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (world_id) REFERENCES worlds(id)
);

# --- !Downs
DROP TABLE worlds_entries;
