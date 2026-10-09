use CEPE
go

alter table PDF_PUBLICATION add VALIDATION_WARNINGS_MSG text null
go

PRINT "campo aggiunto"