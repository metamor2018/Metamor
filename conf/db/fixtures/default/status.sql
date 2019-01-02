# --- !Ups
insert into statuses(world_id, character_id, reply, in_reply_to_id, text) values (1, 'hoge', false, null, 'てきすと1');
insert into statuses(world_id, character_id, reply, in_reply_to_id, text) values (1, 'hoge', false, null, 'てきすと2');
insert into statuses(world_id, character_id, reply, in_reply_to_id, text) values (1, 'hoge', false, null, 'てきすと3');
insert into statuses(world_id, character_id, reply, in_reply_to_id, text) values (1, 'hoge', false, null, 'てきすと4');
insert into statuses(world_id, character_id, reply, in_reply_to_id, text) values (1, 'hoge', false, null, 'てきすと5');

# --- !Downs
delete from statuses;
