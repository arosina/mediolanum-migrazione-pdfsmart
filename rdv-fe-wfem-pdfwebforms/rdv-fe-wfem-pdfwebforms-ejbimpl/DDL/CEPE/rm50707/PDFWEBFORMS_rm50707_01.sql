use CEPE
go

alter table PDF_INSTANCE add CREATION_USERTYPE varchar(20) null
go

PRINT "columns added"
