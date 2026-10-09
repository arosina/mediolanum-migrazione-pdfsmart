use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'HAS_CONTROLLO_AML_AVR')
BEGIN
 alter table PDF_ANAG add HAS_CONTROLLO_AML_AVR char(1) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'HAS_CONTROLLO_AML_PEP')
BEGIN
 alter table PDF_ANAG add HAS_CONTROLLO_AML_PEP char(1) null
END
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'HAS_CONTROLLO_SCAI')
BEGIN
 alter table PDF_ANAG add HAS_CONTROLLO_SCAI char(1) null
END
go



exec sp__comment "Indica se va effettuato il controllo AML-AVR", PDF_ANAG, HAS_CONTROLLO_AML_AVR
go
exec sp__comment "Indica se va effettuato il controllo AML-PEP", PDF_ANAG, HAS_CONTROLLO_AML_PEP
go
exec sp__comment "Indica se va effettuato il controllo SCAI", PDF_ANAG, HAS_CONTROLLO_SCAI
go

PRINT "colums added"
go

