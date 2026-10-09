use CEPE
go

alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI1 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI2 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI3 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI4 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI5 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI6 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI7 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI8 char(1) null
go
alter table PDF_ANAG add IS_PROSPECT_ENABLED_ON_CLI9 char(1) null
go

PRINT "column on PDF_ANAG added"
