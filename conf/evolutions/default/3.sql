-- キャラクター

# --- !Ups
CREATE TABLE characters(
  id VARCHAR(20) PRIMARY KEY,
  creator_id VARCHAR(20) NOT NULL,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(255),
  icon VARCHAR(255),
  deleted_at timestamp null,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (creator_id) REFERENCES creators(id)
);

# --- !Downs
DROP TABLE characters;
