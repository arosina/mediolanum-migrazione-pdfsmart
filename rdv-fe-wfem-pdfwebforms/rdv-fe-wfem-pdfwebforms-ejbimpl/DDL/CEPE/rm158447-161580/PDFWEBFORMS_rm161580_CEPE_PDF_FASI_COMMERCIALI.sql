use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_FASI_COMMERCIALI') IS NOT NULL BEGIN
    DROP TABLE PDF_FASI_COMMERCIALI   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_FASI_COMMERCIALI'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_FASI_COMMERCIALI' DROPPED"
    END
END
go
create table PDF_FASI_COMMERCIALI
(
	CODICE	    	char(2)         not null
,	DESCRIZIONE		varchar(255)		null 
,	ORDINE			smallint			null
)
go
alter table PDF_FASI_COMMERCIALI add constraint PDF_FASI_COMMERCIALI_PK PRIMARY KEY NONCLUSTERED ( CODICE )
go 

insert into PDF_FASI_COMMERCIALI (CODICE, DESCRIZIONE, ORDINE) values ('01','Vendita', 1)
insert into PDF_FASI_COMMERCIALI (CODICE, DESCRIZIONE, ORDINE) values ('02','Post-vendita', 2)
insert into PDF_FASI_COMMERCIALI (CODICE, DESCRIZIONE, ORDINE) values ('03','Gestione amministrativa', 3)
go
grant all on PDF_FASI_COMMERCIALI to Group_CEPE
go

PRINT "table 'PDF_FASI_COMMERCIALI' CREATED"
go

