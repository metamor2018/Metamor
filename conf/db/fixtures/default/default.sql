# --- !Ups

insert into accounts (auth_id) values ('bw1u196jSQMmlE7bd3U3Qj56jOK9MskJ@clients');

insert into creators(account_id,id,name) values (1,'hoge','huga');

insert into characters(creator_id,id,name) values ('hoge','hoge','huga');

insert into worlds(name,creator_Id,detail,started_at) values ('name','hoge','hoge','2018-12-04 06:45:55');
insert into worlds(name,creator_Id,detail,started_at) values ('name','hoge','hoge','2018-12-05 06:45:55');

# --- !Downs



