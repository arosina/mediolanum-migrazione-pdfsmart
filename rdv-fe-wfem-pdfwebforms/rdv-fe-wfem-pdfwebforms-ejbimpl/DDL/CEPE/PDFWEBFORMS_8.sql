use CEPE
go

alter table PDF_INSTANCE add FLAG_WAYOUT char(1) null
go

PRINT "column on PDF_INSTANCE added"
