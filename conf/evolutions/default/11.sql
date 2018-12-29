-- キャラクター通知

# --- !Ups
CREATE TABLE characters_notifications(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id VARCHAR(20) NOT NULL,
  from_character_id VARCHAR(20) NOT NULL,
  activity_id BIGINT NOT NULL,
  activity_type ENUM('reply', 'favorite', 'world_started', 'world_ended'),
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (from_character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE characters_notifications;
