export PATH=$PATH:/opt/activator
activator ui


Netoyer la BDD avant de lancer les testes
show tables; drop table StudentLesson; drop table Student; drop table Lesson; show tables;
select * from StudentLesson; select * from Lesson; select * from Student ; delete from StudentLesson; delete from Lesson; delete from Student; select * from StudentLesson; select * from Lesson; select * from Student;