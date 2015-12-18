# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        bigint not null,
  body                      varchar(255),
  user_id                   bigint,
  tool_id                   bigint,
  constraint pk_comment primary key (id))
;

create table tool (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  img_url                   varchar(255),
  owner_id                  bigint,
  tool_type_id              bigint,
  constraint pk_tool primary key (id))
;

create table tool_type (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_tool_type primary key (id))
;

create table transaction (
  id                        bigint not null,
  renter_id                 bigint,
  to_borrow_id              bigint,
  available                 boolean,
  constraint pk_transaction primary key (id))
;

create table users (
  id                        bigint not null,
  username                  varchar(255),
  email                     varchar(255),
  password_hash             varchar(255),
  constraint uq_users_username unique (username),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;

create sequence comment_seq;

create sequence tool_seq;

create sequence tool_type_seq;

create sequence transaction_seq;

create sequence users_seq;

alter table comment add constraint fk_comment_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_comment_user_1 on comment (user_id);
alter table comment add constraint fk_comment_tool_2 foreign key (tool_id) references tool (id) on delete restrict on update restrict;
create index ix_comment_tool_2 on comment (tool_id);
alter table tool add constraint fk_tool_owner_3 foreign key (owner_id) references users (id) on delete restrict on update restrict;
create index ix_tool_owner_3 on tool (owner_id);
alter table tool add constraint fk_tool_toolType_4 foreign key (tool_type_id) references tool_type (id) on delete restrict on update restrict;
create index ix_tool_toolType_4 on tool (tool_type_id);
alter table transaction add constraint fk_transaction_renter_5 foreign key (renter_id) references users (id) on delete restrict on update restrict;
create index ix_transaction_renter_5 on transaction (renter_id);
alter table transaction add constraint fk_transaction_to_borrow_6 foreign key (to_borrow_id) references tool (id) on delete restrict on update restrict;
create index ix_transaction_to_borrow_6 on transaction (to_borrow_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists tool;

drop table if exists tool_type;

drop table if exists transaction;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists comment_seq;

drop sequence if exists tool_seq;

drop sequence if exists tool_type_seq;

drop sequence if exists transaction_seq;

drop sequence if exists users_seq;

