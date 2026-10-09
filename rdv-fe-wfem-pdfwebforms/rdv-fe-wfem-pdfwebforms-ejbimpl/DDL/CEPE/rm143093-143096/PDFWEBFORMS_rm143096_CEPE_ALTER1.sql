use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'ID_SOSTITUZIONE')
BEGIN
 alter table PDF_INSTANCE add ID_SOSTITUZIONE varchar(40) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'ON_STOCK_PROC_QUEUE')
BEGIN
 alter table PDF_INSTANCE add ON_STOCK_PROC_QUEUE char(1) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'HAS_PREFERENZE_IN_PRIIPS')
BEGIN
 alter table PDF_ANAG add HAS_PREFERENZE_IN_PRIIPS char(1) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'ON_STOCK_PROC_QUEUE')
BEGIN
 alter table PDF_ANAG add ON_STOCK_PROC_QUEUE char(1) null
END
go

exec sp__comment "Id sostituzione", PDF_INSTANCE, ID_SOSTITUZIONE
go
exec sp__comment "Flag per stock dati", PDF_INSTANCE, ON_STOCK_PROC_QUEUE
go
exec sp__comment "Configurazione preferenze in priips", PDF_ANAG, HAS_PREFERENZE_IN_PRIIPS
go
exec sp__comment "Configurazione flag per stock dati", PDF_ANAG, ON_STOCK_PROC_QUEUE
go

PRINT "colums added"
go

