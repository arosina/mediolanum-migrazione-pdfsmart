use CEPE
go

alter table PDF_ANAG add ON_SIGNED_PROC_QUEUE char(1) null
go

alter table PDF_ANAG add ON_POSTCOMPL_PROC_QUEUE char(1) null
go

alter table PDF_ANAG add STOP_SIGNED_PROC_QUEUE char(1) null
go

alter table PDF_ANAG add STOP_POSTCOMPL_PROC_QUEUE char(1) null
go

alter table PDF_INSTANCE add ON_SIGNED_PROC_QUEUE  char(1) null
go

alter table PDF_INSTANCE add ON_POSTCOMPL_PROC_QUEUE char(1) null
go

alter table PDF_INSTANCE add IS_SWITCH char(1) null
go

alter table PDF_INSTANCE add ID_CARRELLO numeric(18) null
go

alter table PDF_INSTANCE add ID_DISP_CARRELLO numeric(18) null
go

PRINT "columns added"