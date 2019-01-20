# --- !Ups
insert into worlds_entries(character_id,world_id) values ('testCharacter1',1);
insert into worlds_entries(character_id,world_id) values ('testCharacter2',1);
insert into worlds_entries(character_id,world_id) values ('testCharacter3',1);

insert into worlds_entries(character_id,world_id) values ('testCharacter4',2);


# --- !Downs
delete from worlds_entries;
