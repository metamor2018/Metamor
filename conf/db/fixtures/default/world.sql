# --- !Ups
insert into worlds(name,creator_Id,detail,started_at) values ('name','hoge','hoge','2018-12-04 06:45:55');
insert into worlds(name,creator_Id,detail,started_at) values ('name','hoge','hoge','2018-12-05 06:45:55');

# --- !Downs
delete from statuses;
delete from worlds_entries;
delete from worlds;
