-- 創作者

# --- !Ups
CREATE TABLE creators(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  display_id varchar(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL,
  profile VARCHAR(64) NULL,
  icon,
  official,
  deleted_at,
--   created_at timestamp not null default current_timestamp,
--   updated_at timestamp not null default current_timestamp on update current_timestamp
);

# --- !Downs
drop table creators