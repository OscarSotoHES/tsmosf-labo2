# Student schema
 
# --- !Ups
create table Student
(
	id bigint primary key not null AUTO_INCREMENT,
	name varchar(128) not null
); 
# --- !Downs

# Lesson schema
 
# --- !Ups
create table Lesson
(
	id bigint primary key not null AUTO_INCREMENT,
	name varchar(128) not null
); 
# --- !Downs

# StudentLesson schema
 
# --- !Ups
create table StudentLesson
(
	id bigint primary key not null AUTO_INCREMENT,
	student_id bigint references Student(id),
	lesson_id bigint references Lesson(id)
); 
# --- !Downs
