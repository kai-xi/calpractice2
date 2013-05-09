# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table date_helper (
  id                        bigint not null,
  date                      timestamp,
  task_id                   bigint,
  constraint pk_date_helper primary key (id))
;

create table task (
  id                        bigint not null,
  title                     varchar(255),
  task_type                 varchar(255),
  date                      timestamp,
  date_displayed            varchar(255),
  repeat_until              timestamp,
  repeats_mon               boolean,
  repeats_tues              boolean,
  repeats_wed               boolean,
  repeats_thurs             boolean,
  repeats_fri               boolean,
  repeats_sat               boolean,
  repeats_sun               boolean,
  start                     varchar(255),
  start_time                integer,
  end                       varchar(255),
  end_time                  integer,
  place                     varchar(255),
  notes                     varchar(255),
  owner_email               varchar(255),
  constraint pk_task primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;

create sequence date_helper_seq;

create sequence task_seq;

create sequence user_seq;

alter table date_helper add constraint fk_date_helper_task_1 foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_date_helper_task_1 on date_helper (task_id);
alter table task add constraint fk_task_owner_2 foreign key (owner_email) references user (email) on delete restrict on update restrict;
create index ix_task_owner_2 on task (owner_email);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists date_helper;

drop table if exists task;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists date_helper_seq;

drop sequence if exists task_seq;

drop sequence if exists user_seq;

