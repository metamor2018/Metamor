-- キャラクター

# --- !Ups
CREATE TABLE characters(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  creator_id VARCHAR (20) NOT NULL UNIQUE,
  display_id varchar(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(64) NULL,
  icon,
  FOREIGN KEY (creator_id) REFERENCES creators(id)
);

# --- !Downs
drop table characters
