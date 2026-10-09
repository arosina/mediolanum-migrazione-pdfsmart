use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE') AND name = 'IS_MOP')
BEGIN
 alter table PDF_INSTANCE add IS_MOP char(1) null
END
go

exec sp__comment "Flag che identifica se l'istanza e' risultata integrata con MOM tramite il modello MOP", PDF_INSTANCE, IS_MOP
go

PRINT "configurazione PDF_INSTANCE effettuata"
go
