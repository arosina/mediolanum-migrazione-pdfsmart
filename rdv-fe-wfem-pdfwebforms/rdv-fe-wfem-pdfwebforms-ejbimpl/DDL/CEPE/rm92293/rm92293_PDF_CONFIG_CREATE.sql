use CEPE
go

IF OBJECT_ID ('PDF_CONFIG') IS NULL 
BEGIN
 EXECUTE("create table PDF_CONFIG
	(
		SEZIONE		varchar(100)	not null
	,	PARAMETRO	varchar(100)	not null
	,	VALORE		varchar(2000)	null
	,	ATTIVO		char(1)			null
	,	DESCRIZIONE	varchar(300)	null
	)
	alter table PDF_CONFIG add constraint PDF_CONFIG_PK PRIMARY KEY NONCLUSTERED ( SEZIONE, PARAMETRO )
	grant all on PDF_CONFIG to Group_CEPE")
END
go

--delete from PDF_CONFIG where SEZIONE='PRATICHE_DIGITALI' and PARAMETRO='DISABILITA_CALL_SRV_DISPOSITIVA'
--insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
--values ('PRATICHE_DIGITALI','DISABILITA_CALL_SRV_DISPOSITIVA','S','S','Disattiva l''integrazione con il processa dispositiva (default false)')
--go

print "Configurazione effettuata"
go