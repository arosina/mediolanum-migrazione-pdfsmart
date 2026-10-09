use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('CEPE_CRLEVE_LOGATTIVITA') AND name = 'PROCESSATA_DA_SEDE')
BEGIN
 alter table CEPE_CRLEVE_LOGATTIVITA add PROCESSATA_DA_SEDE char(1) null
END
go

exec sp__comment "Flag che indica che la deroga e' stata lavorata anche su MOP", CEPE_CRLEVE_LOGATTIVITA, PROCESSATA_DA_SEDE
go

PRINT "colonna aggiunta"
go
