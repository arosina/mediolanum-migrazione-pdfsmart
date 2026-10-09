use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'FILENET_GUID')
BEGIN
 alter table PDF_INSTANCE add FILENET_GUID varchar(100) null
END
go

exec sp__comment "Guid filenet del pdf", PDF_INSTANCE, FILENET_GUID
go

PRINT "column on PDF_INSTANCE added"
go

