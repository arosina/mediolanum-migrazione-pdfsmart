use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'COD_RUOLO_IMPERSONATO')
BEGIN
 alter table PDF_INSTANCE add COD_RUOLO_IMPERSONATO varchar(10) null
END
go

exec sp__comment "Ruolo impersonificato dall'FB collegato", PDF_INSTANCE, COD_RUOLO_IMPERSONATO
go

PRINT "colums added"
go

