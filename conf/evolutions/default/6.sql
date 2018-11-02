-- ワールド（イベント）参加

# --- !Ups
CREATE TABLE worlds_entries(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  character_id VARCHAR(20) NOT NULL,
  world_id INT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id),
  FOREIGN KEY (world_id) REFERENCES worlds(id)
);

# --- !Downs
drop table worlds_entries
