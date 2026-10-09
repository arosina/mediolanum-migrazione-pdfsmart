use CEPE
go

delete from PDF_CONFIG where SEZIONE='SOSTITUZIONI' and PARAMETRO='CONTROLLO_NON_BLOCCANTE'

insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('SOSTITUZIONI', 'CONTROLLO_NON_BLOCCANTE', 'S', 'S', 'Imposta come non bloccante il controllo sostituzioni (default false)')
go

delete from PDF_CONFIG where SEZIONE='SOSTITUZIONI' and PARAMETRO='DISABILITA_PREFERENZE_IN_SWITCH'

insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('SOSTITUZIONI', 'DISABILITA_PREFERENZE_IN_SWITCH', 'N', 'S', 'Disabilita le preferenze sullo switch fondi (default false)')
go


print 'Configurazione sostituzioni effettuata'
go

