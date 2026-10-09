use CEPE
go

alter table PDF_APPL_REFERENCED_CODE add APPL_DESCR varchar(1000) null
go

alter table PDF_ANAG add IS_CONTROLLI_COMPLETI char(1) null
go

PRINT "columns added"
