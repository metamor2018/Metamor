-- キャラクター

# --- !Ups
CREATE TABLE characters(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  creator_id BIGINT NOT NULL,
  display_id VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(255),
  icon VARCHAR(255),
  FOREIGN KEY (creator_id) REFERENCES creators(id)
);

# --- !Downs
DROP TABLE characters;
