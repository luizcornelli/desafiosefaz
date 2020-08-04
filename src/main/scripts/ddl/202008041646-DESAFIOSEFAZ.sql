-- DESCRIÇÃO: Criando de tabelas
-- AUTOR: Luiz Cornelli  

create table usuario (

	id integer primary key auto_increment
	,nome varchar(30)
	,email varchar(30)
	,senha varchar(30)
	,is_admin boolean
);

create table telefone (
	id integer primary key auto_increment
	,ddd varchar(30)
	,numero varchar(30) 
	,tipo_telefone varchar(30)
	,id_usuario integer
	,foreign key (id_usuario) references usuario (id) on delete cascade
);

