-- キャラクター通知

# --- !Ups
CREATE TABLE characters_notifications(
  id BIGINT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  from_character_id BIGINT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (from_character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE characters_notifications;
