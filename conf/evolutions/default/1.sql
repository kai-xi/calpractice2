# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task (
  id                        bigint not null,
  desc                      varchar(255),
  done                      boolean,
  date                      timestamp,
  start_time                integer,
  end_time                  integer,
  owner_email               varchar(255),
  constraint pk_task primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;

create sequence task_seq;

create sequence user_seq;

alter table task add constraint fk_task_owner_1 foreign key (owner_email) references user (email) on delete restrict on update restrict;
create index ix_task_owner_1 on task (owner_email);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists task;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists task_seq;

drop sequence if exists user_seq;

