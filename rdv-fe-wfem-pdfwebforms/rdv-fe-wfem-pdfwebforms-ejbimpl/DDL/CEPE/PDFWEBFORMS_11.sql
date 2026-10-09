use CEPE
go

alter table PDF_INSTANCE add EXTERNAL_ENTITY_APPL varchar(30) null
go

alter table PDF_INSTANCE add EXTERNAL_ENTITY_NAME varchar(30) null
go

alter table PDF_INSTANCE add EXTERNAL_ENTITY_KEY varchar(100) null
go

create index PDF_INSTANCE_IDX01 on PDF_INSTANCE (EXTERNAL_ENTITY_APPL, EXTERNAL_ENTITY_NAME, EXTERNAL_ENTITY_KEY)
go

PRINT "external entity key added"
