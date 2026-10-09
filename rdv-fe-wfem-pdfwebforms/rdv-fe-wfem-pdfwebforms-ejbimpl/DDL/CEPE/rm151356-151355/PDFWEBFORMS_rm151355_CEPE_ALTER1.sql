use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'SISTEMA_CLIENT')
BEGIN
 alter table PDF_INSTANCE add SISTEMA_CLIENT char(1) null
END
go

exec sp__comment "Codice del sistema client", PDF_INSTANCE, SISTEMA_CLIENT
go

PRINT "column on PDF_INSTANCE added"
go

