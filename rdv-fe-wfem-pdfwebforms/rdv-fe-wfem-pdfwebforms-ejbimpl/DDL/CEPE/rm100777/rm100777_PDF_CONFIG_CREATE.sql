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

delete from PDF_CONFIG where SEZIONE='PUBLISHER' and PARAMETRO='BLOB_SU_DB'
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('PUBLISHER','BLOB_SU_DB','S','S','Attiva il salvataggio dei pdf pubblicati e delle immagini su DB e non sulla NAS (default false)')
go

delete from PDF_CONFIG where SEZIONE='PDF_INSTANCE' and PARAMETRO='BLOB_SU_DB'
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('PDF_INSTANCE','BLOB_SU_DB','S','S','Attiva il salvataggio dei pdf compilati su DB e non sulla NAS (default false)')
go

delete from PDF_CONFIG where SEZIONE='PILOTA_SCRITTURA_NAS' and PARAMETRO='CODICI_AGENTE'
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('PILOTA_SCRITTURA_NAS','CODICI_AGENTE','NESSUNO','S','Elenco dei codici agente, separati da virgola, per il pilota della scrittura dei pdf compilati su NAS (default tutti)')
go

--insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
--values ('PUBLISHER','AREE_CRAFTER','CATALOGO_MODULI,CATALOGO_OPERAZIONI','S','Aree che popolano crafter (default CATALOGO_MODULI,CATALOGO_OPERAZIONI)')
--go

--insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
--values ('SIGN_PROCESS','GG_BOZZE_MYFREEDOM','30','S','Giorni di validità delle bozze MyFreedom (default 30)')
--go

--insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
--values ('SIGN_PROCESS','DISABILITA_WAYOUT_CARTACEO','N','S','Disbilita la sottoscrizione cartacea in caso di errore FD (default false)')
--go

--insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
--values ('REPORT_ADEGUATEZZA','MAX_RECUPERO_RETRY_COUNT','10','S','Numero massimo di retry nel recupero RDA (default 10)')
--go


