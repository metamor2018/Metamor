-- キャラクター

# --- !Ups
CREATE TABLE characters(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  creator_id BIGINT NOT NULL,
  display_id VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(255),
  icon VARCHAR(255),
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (creator_id) REFERENCES creators(id)
);

# --- !Downs
DROP TABLE characters;
