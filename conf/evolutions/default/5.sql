-- ワールド（イベント）

# --- !Ups
CREATE TABLE worlds(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  creator_id VARCHAR (20) NOT NULL UNIQUE,
  detail VARCHAR(255) NOT NULL,
  started_at,
  ended_at,
  emblem_id,
  FOREIGN KEY (creator_id) REFERENCES creators(id),
  FOREIGN KEY (emblem_id) REFERENCES embelms(id)
);

# --- !Downs
drop table worlds
