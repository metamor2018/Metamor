-- 称号

# --- !Ups
CREATE TABLE emblems(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  icon,
  character_id VARCHAR(20) NOT NULL,
  FOREIGN KEY (character_id) REFERENCES characters(id)
);

# --- !Downs
drop table emblems
