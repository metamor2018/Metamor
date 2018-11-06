-- 創作者通知

# --- !Ups
CREATE TABLE creators_notifications(
  id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  creator_id BIGINT NOT NULL,
  from_creator_id BIGINT NOT NULL,
  activity_id BIGINT NOT NULL,
  activity_type ENUM('world_created'),
  FOREIGN KEY (creator_id) REFERENCES creators(id),
  FOREIGN KEY (from_creator_id) REFERENCES creators(id)
);

# --- !Downs
DROP TABLE creators_notifications;
