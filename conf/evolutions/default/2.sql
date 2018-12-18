-- 創作者

# --- !Ups
CREATE TABLE creators(
  id VARCHAR(20) PRIMARY KEY,
  account_id BIGINT NOT NULL,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(64) NULL,
  icon VARCHAR(255) NULL,
  official BOOLEAN NOT NULL DEFAULT FALSE,
  deleted_at timestamp null,
  created_at timestamp not null default current_timestamp,
  updated_at timestamp not null default current_timestamp on update current_timestamp,
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

# --- !Downs
DROP TABLE creators;
