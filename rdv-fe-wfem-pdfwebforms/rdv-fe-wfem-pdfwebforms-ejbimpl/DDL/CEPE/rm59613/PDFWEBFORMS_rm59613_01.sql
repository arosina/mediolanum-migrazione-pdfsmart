use CEPE
go

alter table PDF_INSTANCE add ID_QUESTIONARIO_IDD varchar(32) null
go

alter table PDF_INSTANCE add ID_RACCOMANDAZIONE_IDD varchar(32) null
go

PRINT "columns added"