use CEPE
go

alter table PDF_ANAG add CALL_SRV_DISPOSITIVA_BMED char(1) null
go

alter table PDF_INSTANCE add ID_FILE_NAS varchar(20) null
go

alter table PDF_INSTANCE add COD_DISPOSITIVA_BMED varchar(20) null
go

PRINT "columns added"