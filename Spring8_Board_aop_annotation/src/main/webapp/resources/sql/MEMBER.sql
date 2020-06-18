drop table member;

create table member(
	id			varchar2(15),
	password	varchar2(60),
	name		varchar2(15),
	age			Number,
	gender		varchar2(5),
	email		varchar2(30),
	PRIMARY KEY(id)
);

select* from member;
update member set password=1 where id='admin';

insert into member
values('admin',1,'admin',30,'¿©','admin@naver.com');

insert into member
values('jsp',1,'jsp',21,'³²','jsp@naver.com');

insert into member
values('java',1,'java',25,'¿©','java@naver.com');
select* from member;