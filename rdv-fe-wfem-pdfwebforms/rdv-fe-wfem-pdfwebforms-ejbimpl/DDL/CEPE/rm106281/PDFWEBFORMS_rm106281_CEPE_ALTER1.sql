use CEPE
go

alter table PDF_ANAG add NOTE_DI_CONFIGURAZIONE text null
go

exec sp__comment "Note di configurazione", PDF_ANAG, NOTE_DI_CONFIGURAZIONE
go

PRINT "column on PDF_ANAG added"
go

