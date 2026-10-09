use CEPE
go

alter table PDF_ANAG add COD_PRODOTTO_MILESTONE varchar(10) null
go

alter table PDF_ANAG add COD_OPERAZIONE_MILESTONE varchar(10) null
go

PRINT "columns on PDF_ANAG added"
