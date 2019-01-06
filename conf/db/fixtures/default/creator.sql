# --- !Ups
insert into creators(account_id,id,name) values (1,'hoge','huga');

# --- !Downs
delete from creators;
