use CEPE
go

alter table PDF_PUBLICATION add PUBLICATION_NOTES text null
go

alter table PDF_PUBLICATION_WORK add PUBLICATION_NOTES text null
go

PRINT "column on PDF_PUBLICATION added"
