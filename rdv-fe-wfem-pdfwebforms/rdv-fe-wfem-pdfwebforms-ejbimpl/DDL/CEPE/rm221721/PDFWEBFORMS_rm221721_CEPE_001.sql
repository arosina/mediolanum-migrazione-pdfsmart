use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_PUBLICATION') AND name = 'DATA_FINE_ACCET_PUBBL_PREC')
BEGIN
 alter table PDF_PUBLICATION add DATA_FINE_ACCET_PUBBL_PREC datetime null
END
go
exec sp__comment "Data fine accettazione pubblicazione precedente su MOM", PDF_PUBLICATION, DATA_FINE_ACCET_PUBBL_PREC
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_PUBLICATION_WORK') AND name = 'DATA_FINE_ACCET_PUBBL_PREC')
BEGIN
 alter table PDF_PUBLICATION_WORK add DATA_FINE_ACCET_PUBBL_PREC datetime null
END
go
exec sp__comment "Data fine accettazione pubblicazione precedente sui sistemi di sede (MOM)", PDF_PUBLICATION_WORK, DATA_FINE_ACCET_PUBBL_PREC
go

PRINT "configurazione PDF_PUBLICATION effettuata"
go
