use CEPE
go

alter table PDF_ANAG add IS_COPERNICO_ENABLED char(1) null
go

alter table PDF_INSTANCE add NOTE_FB_COPERNICO text null
go

alter table PDF_INSTANCE add COPERNICO_RESPONSE_TIME datetime null
go

PRINT "column on PDF_INSTANCE added"
