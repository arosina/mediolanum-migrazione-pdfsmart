use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_LINEE_DI_BUSINESS') IS NOT NULL BEGIN
    DROP TABLE PDF_LINEE_DI_BUSINESS   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_LINEE_DI_BUSINESS'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_LINEE_DI_BUSINESS' DROPPED"
    END
END
go
create table PDF_LINEE_DI_BUSINESS
(
	CODICE	    	char(2)         not null
,	DESCRIZIONE		varchar(255)		null 
,	ORDINE			smallint			null
)
go
alter table PDF_LINEE_DI_BUSINESS add constraint PDF_LINEE_DI_BUSINESS_PK PRIMARY KEY NONCLUSTERED ( CODICE )
go 

insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('01','Investimenti Assicurativi',1)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('02','Investimenti Fondi e Gestione',2)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('03','PCT, MedPlus certificate, Obb. B. Med',3)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('04','Previdenza',4)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('05','Protezione',5)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('06','Anagrafica',6)
insert into PDF_LINEE_DI_BUSINESS (CODICE, DESCRIZIONE, ORDINE) values ('07','Altro',7)
go
grant all on PDF_LINEE_DI_BUSINESS to Group_CEPE
go

PRINT "table 'PDF_LINEE_DI_BUSINESS' CREATED"
go

