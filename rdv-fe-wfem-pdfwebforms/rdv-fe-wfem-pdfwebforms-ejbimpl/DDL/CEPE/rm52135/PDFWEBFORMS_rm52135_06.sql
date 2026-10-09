use CEPE
go

create index PDF_ANAG_IDX01 on PDF_ANAG (CODE)
go

create index PDF_ANAG_IDX02 on PDF_ANAG (MOM_CODE)
go

create index PDF_ANAG_IDX03 on PDF_ANAG (AREA)
go

create index PDF_ANAG_IDX04 on PDF_ANAG (AREA, MOM_CODE)
go

PRINT "indici su anag aggiunti"
