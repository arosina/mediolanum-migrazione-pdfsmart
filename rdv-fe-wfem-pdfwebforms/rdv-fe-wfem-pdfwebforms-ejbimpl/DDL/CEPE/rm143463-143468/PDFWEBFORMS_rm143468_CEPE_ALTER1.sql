use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'IN_BASKET')
BEGIN
 alter table PDF_INSTANCE add IN_BASKET char(1) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'ORDINE_COMPILAZIONE_BASKET')
BEGIN
 alter table PDF_INSTANCE add ORDINE_COMPILAZIONE_BASKET smallint null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'EXT_LINK_AGG_ONSIGN_LABEL')
BEGIN
 alter table PDF_ANAG add EXT_LINK_AGG_ONSIGN_LABEL varchar(250) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'EXT_LINK_AGG_ONSIGN_URL')
BEGIN
 alter table PDF_ANAG add EXT_LINK_AGG_ONSIGN_URL varchar(600) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'COD_LINEA_BUSINESS')
BEGIN
 alter table PDF_ANAG add COD_LINEA_BUSINESS char(2) null
END
go



exec sp__comment "Identifica i pdf fatti in un basket", PDF_INSTANCE, IN_BASKET
go
exec sp__comment "Ordinamento nella compilazione di un basket", PDF_INSTANCE, ORDINE_COMPILAZIONE_BASKET
go
exec sp__comment "Etichetta link aggiuntivo in FD", PDF_ANAG, EXT_LINK_AGG_ONSIGN_LABEL
go
exec sp__comment "Url link aggiuntivo in FD", PDF_ANAG, EXT_LINK_AGG_ONSIGN_URL
go
exec sp__comment "Codice linea di business", PDF_ANAG, COD_LINEA_BUSINESS
go

PRINT "colums added"
go

