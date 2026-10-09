use CEPE
go

alter table PDF_ANAG add FILENET_CODICE_DOC_CLIENTE varchar(20) null
go

PRINT "columns added"