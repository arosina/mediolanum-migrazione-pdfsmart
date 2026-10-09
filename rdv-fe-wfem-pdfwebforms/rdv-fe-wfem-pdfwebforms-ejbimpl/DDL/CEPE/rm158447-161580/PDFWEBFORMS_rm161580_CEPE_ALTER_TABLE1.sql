use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'COD_FASE_COMMERCIALE')
BEGIN
 alter table PDF_ANAG add COD_FASE_COMMERCIALE char(2) null
END
go

exec sp__comment "Codice fase commerciale", PDF_ANAG, COD_FASE_COMMERCIALE
go

PRINT "colums added"
go

