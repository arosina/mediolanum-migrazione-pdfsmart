use CEPE
go

delete from PDF_CONFIG where SEZIONE='PROCESSO_DI_VENDITA' and PARAMETRO IN ('CODICI_MOM_VENDUTI_DA_SA_COME_BMED','CODICI_MOM_VENDUTI_DA_OS_COME_BMED')
go

insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('PROCESSO_DI_VENDITA', 'CODICI_MOM_VENDUTI_DA_SA_COME_BMED', 'PR40,PR46,PR56,PR68', 'S', 'Elenco codici MOM dei pdf che verranno sottoscritti come Bmed se utente Selfy Assistant')
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('PROCESSO_DI_VENDITA', 'CODICI_MOM_VENDUTI_DA_OS_COME_BMED', 'PR40,PR46,PR56,PR68', 'S', 'Elenco codici MOM dei pdf che verranno sottoscritti come Bmed se utente Operatore di Sede')
go

print "Vendita come Bmed configurata"
go


