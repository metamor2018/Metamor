-- 投稿

# --- !Ups
CREATE TABLE statuses (
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  world_id VARCHAR(20) NOT NULL,
  character_id VARCHAR(20) NOT NULL,
  reply,
  in_reply_to_id,
  FOREIGN KEY (world_id) REFERENCES worlds(id)
  FOREIGN KEY (character_id) REFERENCES charcters(id)
);

# --- !Downs
drop table statuses
