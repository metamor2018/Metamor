-- ワールド（イベント）お気に入り

# --- !Ups
CREATE TABLE worlds_favorites(
  id BIGINT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  world_id BIGINT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (world_id) REFERENCES worlds(id)
);

# --- !Downs
DROP TABLE worlds_favorites;
