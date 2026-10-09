use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'COD_AGE_SUPPORTANTE')
BEGIN
 alter table PDF_INSTANCE add COD_AGE_SUPPORTANTE char(10) null
END
go

exec sp__comment "Codice agente che ha supportato nella vendita", PDF_INSTANCE, COD_AGE_SUPPORTANTE
go

PRINT "column on PDF_INSTANCE added"
go

