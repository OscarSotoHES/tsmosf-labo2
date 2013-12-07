# --- !Ups
create database if not exists db_tsmosf_labo2;
grant all on db_tsm_osf_labo_2.* to adm_tsmosf_labo2@'localhost' identified by 'tsmosf_labo2';
flush privileges;
# Student schema
 
# --- !Ups
create table if not exists Student
(
	id bigint primary key not null AUTO_INCREMENT,
	name varchar(128) not null
); 
# --- !Downs

# Lesson schema
 
# --- !Ups
create table if not exists Lesson
(
	id bigint primary key not null AUTO_INCREMENT,
	name varchar(128) not null
); 
# --- !Downs

# StudentLesson schema
 
# --- !Ups
create table if not exists StudentLesson
(
	id bigint primary key not null AUTO_INCREMENT,
	student_id bigint references Student(id),
	lesson_id bigint references Lesson(id)
); 
# --- !Downs
