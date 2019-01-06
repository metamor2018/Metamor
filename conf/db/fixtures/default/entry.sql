# --- !Ups
insert into worlds_entries(character_id, world_id) values ('hoge', 1);

# --- !Downs
delete from worlds_entries
