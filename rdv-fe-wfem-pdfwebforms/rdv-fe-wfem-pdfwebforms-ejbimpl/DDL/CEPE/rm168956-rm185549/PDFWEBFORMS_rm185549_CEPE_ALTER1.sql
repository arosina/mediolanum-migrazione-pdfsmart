use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'F_SOTT_FD_DISTANZA')
BEGIN
 alter table PDF_INSTANCE add F_SOTT_FD_DISTANZA char(1) null
END
go

exec sp__comment "Flag che indica se la sottoscrizione in FD è stata effettuata con i sottoscrittori a distanza (S) o in presenza (N, null per il pregresso)", PDF_INSTANCE, F_SOTT_FD_DISTANZA
go

PRINT "column on PDF_INSTANCE added"
go

