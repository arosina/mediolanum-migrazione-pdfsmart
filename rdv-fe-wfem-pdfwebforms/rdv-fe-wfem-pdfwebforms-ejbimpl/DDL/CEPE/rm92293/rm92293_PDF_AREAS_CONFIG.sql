use CEPE
go

delete from PDF_AREAS where AREA='ACCORDI_INTEGRATIVI'
insert into PDF_AREAS (AREA, DESCR) values ('ACCORDI_INTEGRATIVI', 'Accordi integrativi')

delete from PDF_AREAS where AREA='DEROGHE'
insert into PDF_AREAS (AREA, DESCR) values ('DEROGHE', 'Deroghe')

delete from PDF_AREAS where AREA='MKT_EVENTI'
insert into PDF_AREAS (AREA, DESCR) values ('MKT_EVENTI', 'Marketing eventi')

delete from PDF_AREAS where AREA='MODULI_ASSOCIATI_AI_CONTRATTI'
insert into PDF_AREAS (AREA, DESCR) values ('MODULI_ASSOCIATI_AI_CONTRATTI', 'Moduli associati ai contratti')

delete from PDF_AREAS where AREA='WEALTH_MANAGEMENT'
insert into PDF_AREAS (AREA, DESCR) values ('WEALTH_MANAGEMENT', 'Wealth Management')

go

print "Aree configurate"
go

