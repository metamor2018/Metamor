-- キャラクター通知

# --- !Ups
CREATE TABLE characters_notifications(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id VARCHAR(20) NOT NULL,
  from_character_id VARCHAR(20) NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (from_character_id) REFERENCES characters(id)
);

# --- !Downs
drop table characters_notifications
