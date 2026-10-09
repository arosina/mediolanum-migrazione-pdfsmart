use CEPE
go

alter table PDF_INSTANCE add ORIGINAL_PDF_INSTANCE_ID varchar(20) null
go

create index PDF_INSTANCE_IDX09 on PDF_INSTANCE (ORIGINAL_PDF_INSTANCE_ID)
go

PRINT "columns added"