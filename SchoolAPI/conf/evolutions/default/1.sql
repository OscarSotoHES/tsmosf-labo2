# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table lesson (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_lesson primary key (id))
;

create table student (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_student primary key (id))
;


create table lesson_student (
  lesson_id                      bigint not null,
  student_id                     bigint not null,
  constraint pk_lesson_student primary key (lesson_id, student_id))
;



alter table lesson_student add constraint fk_lesson_student_lesson_01 foreign key (lesson_id) references lesson (id) on delete restrict on update restrict;

alter table lesson_student add constraint fk_lesson_student_student_02 foreign key (student_id) references student (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table lesson;

drop table lesson_student;

drop table student;

SET FOREIGN_KEY_CHECKS=1;

