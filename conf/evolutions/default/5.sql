-- ワールド（イベント）

# --- !Ups
CREATE TABLE worlds(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  creator_id VARCHAR(20) NOT NULL,
  detail VARCHAR(255) NOT NULL,
  started_at DATETIME,
  ended_at DATETIME,
  emblem_id BIGINT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (creator_id) REFERENCES creators(id),
  FOREIGN KEY (emblem_id) REFERENCES emblems(id)
);

# --- !Downs
DROP TABLE worlds;
