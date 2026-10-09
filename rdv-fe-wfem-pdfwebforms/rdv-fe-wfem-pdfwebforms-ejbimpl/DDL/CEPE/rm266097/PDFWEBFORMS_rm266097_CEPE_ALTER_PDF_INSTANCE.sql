use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'ON_DEAD_END_PROC_QUEUE')
BEGIN
	alter table PDF_INSTANCE add ON_DEAD_END_PROC_QUEUE char(1) null
END
go

exec sp__comment "Flag che indica se vanno gestiti o meno gli stati copernico scaduto/rifiutato, ed eventualmente un domani altri stati fine vita, di una istanza di pdf smart", PDF_INSTANCE, ON_DEAD_END_PROC_QUEUE
go

PRINT "column on PDF_INSTANCE added"
go

