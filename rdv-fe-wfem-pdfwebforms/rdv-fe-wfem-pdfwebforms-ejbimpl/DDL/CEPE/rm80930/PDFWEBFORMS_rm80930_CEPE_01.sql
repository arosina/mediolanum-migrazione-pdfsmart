use CEPE
go

alter table PDF_INSTANCE add TIPO_COPERNICO char(1) null
go

alter table PDF_INSTANCE add IBAN varchar(27) null
go

PRINT "columns on PDF_INSTANCE added"
