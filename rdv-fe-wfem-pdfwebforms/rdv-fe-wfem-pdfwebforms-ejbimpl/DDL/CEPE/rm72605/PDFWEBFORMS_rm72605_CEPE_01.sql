use CEPE
go

alter table PDF_ANAG add HAS_DATA_SOTTOSCRIZIONE_OGGI char(1) null
go

PRINT "column on PDF_ANAG added"
