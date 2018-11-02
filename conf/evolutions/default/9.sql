-- 創作者通知

# --- !Ups
CREATE TABLE creators_notifications(
  id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  creator_id VARCHAR(20) NOT NULL,
  from_creator_id VARCHAR(20) NOT NULL,
  FOREIGN KEY (creator_id) REFERENCES creators(id),
  FOREIGN KEY (from_creator_id) REFERENCES creators(id)
);

# --- !Downs
drop table creators_notifications
