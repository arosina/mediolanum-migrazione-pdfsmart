use CEPE
go

alter table PDF_ANAG add HAS_PRIIPS char(1) null
go

alter table PDF_INSTANCE add TIPO_SUPPORTO_MATER_CONTR  varchar(20) null
go

alter table PDF_INSTANCE add ID_REPORT_ADEGUATEZZA varchar(32) null
go

alter table PDF_INSTANCE add NUM_COPIE smallint null
go

alter table PDF_INSTANCE add IS_MULTIPDF char(1) null
go

PRINT "columns added"