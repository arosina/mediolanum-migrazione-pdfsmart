use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'COD_AGE_IMPERSONATO')
BEGIN
 alter table PDF_INSTANCE add COD_AGE_IMPERSONATO char(10) null
END
go

exec sp__comment "Codice agente impersonato dall'agente collegato. Quando valorizzato i clienti saranno quelli di questo FB", PDF_INSTANCE, COD_AGE_IMPERSONATO
go

PRINT "configurazione PDF_INSTANCE effettuata"
go
