use CEPE
go

alter table PDF_INSTANCE add ID_AGEVOLAZIONE varchar(32) null
go

alter table PDF_INSTANCE add CODICE_AGEVOLAZIONE varchar(10) null
go

alter table PDF_INSTANCE add IMPORTO decimal(18,3) null
go
							
alter table PDF_INSTANCE add NUMERO_CONTRATTO varchar(20) null
go

alter table PDF_INSTANCE add NUMERO_PROPOSTA varchar(25) null
go

create index PDF_INSTANCE_IDX02 on PDF_INSTANCE (MOM_CODE)
go

create index PDF_INSTANCE_IDX03 on PDF_INSTANCE (ID_AGEVOLAZIONE)
go

create index PDF_INSTANCE_IDX04 on PDF_INSTANCE (STATO)
go

create index PDF_INSTANCE_IDX05 on PDF_INSTANCE (STATO, COMPLETION_TIME)
go

PRINT "columns added"
