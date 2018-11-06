-- キャラクター通知

# --- !Ups
CREATE TABLE characters_notifications(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id BIGINT NOT NULL,
  from_character_id BIGINT NOT NULL,
  activity_id BIGINT NOT NULL,
  activity_type ENUM('reply', 'favorite', 'world_started', 'world_ended'),
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (from_character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE characters_notifications;
