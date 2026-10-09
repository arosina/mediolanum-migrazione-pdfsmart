use CEPE
go

delete from PDF_CONFIG where SEZIONE='WHERE_CONDITION_TIPO_CONTO' and PARAMETRO='CONTO_CORRENTE'
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('WHERE_CONDITION_TIPO_CONTO','CONTO_CORRENTE','(CONTR.CONTR_C_CAT not in (''0005'', ''0043'', ''0075'', ''0076'', ''0044''))','S','Where condition per i conti correnti')
go

delete from PDF_CONFIG where SEZIONE='WHERE_CONDITION_TIPO_CONTO' and PARAMETRO='IS_CLIENTE_CORRENTISTA'
insert into PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
values ('WHERE_CONDITION_TIPO_CONTO','IS_CLIENTE_CORRENTISTA','(CONTR.CONTR_C_CAT not in (''0005'', ''0043'', ''0075'', ''0076'', ''0044''))','S','Where condition che identifica i correntisti')
go

PRINT "configurazione effettuata"
go
