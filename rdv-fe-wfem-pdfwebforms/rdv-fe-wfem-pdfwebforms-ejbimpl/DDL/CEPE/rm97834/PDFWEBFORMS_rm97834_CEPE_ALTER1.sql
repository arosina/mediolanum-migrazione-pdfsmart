use CEPE
go

alter table PDF_ANAG add EXIST_IN_CARTA_CHIMICA char(1) null
go

PRINT "column on PDF_ANAG added"

