use CEPE
go

alter table PDF_INSTANCE add PROD_C_PROD varchar(30) null
go

PRINT "columns added"