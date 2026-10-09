use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_CONFIG') AND name = 'VALORE_ESTESO')
BEGIN
	alter table PDF_CONFIG add VALORE_ESTESO text null
END
go

exec sp__comment "Valore configurativo esteso", PDF_CONFIG, VALORE_ESTESO
go

PRINT "column on PDF_CONFIG added"
go

