use CEPE
go

alter table PDF_ANAG add IS_TIPO_SOGG_PERS_FISICA char(1) null
go
alter table PDF_ANAG add IS_TIPO_SOGG_PROFESSIONAL char(1) null
go
alter table PDF_ANAG add IS_TIPO_SOGG_PERS_GIURIDICA char(1) null
go
alter table PDF_ANAG add IS_TIPO_SOGG_FAMILYBANKER char(1) null
go
alter table PDF_ANAG add IS_USABLE_IN_CRAFTER_WAYOUT char(1) null
go

PRINT "column on PDF_ANAG added"

