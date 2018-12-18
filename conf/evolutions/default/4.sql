-- 称号

# --- !Ups
CREATE TABLE emblems(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  icon VARCHAR(255),
  character_id BIGINT NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (character_id) REFERENCES characters(id)
);

# --- !Downs
DROP TABLE emblems;
