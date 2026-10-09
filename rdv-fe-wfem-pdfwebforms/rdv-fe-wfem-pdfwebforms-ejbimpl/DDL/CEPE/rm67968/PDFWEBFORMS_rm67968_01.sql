use CEPE
go

alter table PDF_ANAG add FILENET_CLASSE_DOC_CLIENTE varchar(20) null
go

alter table PDF_INSTANCE add PDFS_FOR_MOM varchar(20) null
go

PRINT "columns added"