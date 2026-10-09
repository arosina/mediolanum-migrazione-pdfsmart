use CEPE
go

alter table PDF_INSTANCE add NUM_ORDINE_REPORT_ADEGUATEZZA varchar(32) null
go

PRINT "columns added"