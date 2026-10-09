use CEPE
go

alter table PDF_INSTANCE add NUM_PAGES smallint null
go

PRINT "column 'PDF_INSTANCE.NUM_PAGES' ADDED"
