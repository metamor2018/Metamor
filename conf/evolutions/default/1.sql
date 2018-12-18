-- キャラクター通知

# --- !Ups
CREATE TABLE accounts(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  auth_id VARCHAR(255) NOT NULL,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp
);

# --- !Downs
DROP TABLE accounts;
