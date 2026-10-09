use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'HAS_LAYER_COLLOC_A_DISTANZA')
BEGIN
 alter table PDF_ANAG add HAS_LAYER_COLLOC_A_DISTANZA char(1) null
END
go

exec sp__comment "Flag per impostare se mostrare o meno il layout di selezione del tipo di collocamento a distanza", PDF_ANAG, HAS_LAYER_COLLOC_A_DISTANZA
go

PRINT "colums added"
go

