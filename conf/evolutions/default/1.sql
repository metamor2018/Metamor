-- 創作者

# --- !Ups
CREATE TABLE creators(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  display_id VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(64) NULL,
  icon VARCHAR(255) NULL,
  official BOOLEAN NOT NULL DEFAULT FALSE,
  deleted_at DATETIME
);

# --- !Downs
DROP TABLE creators;
