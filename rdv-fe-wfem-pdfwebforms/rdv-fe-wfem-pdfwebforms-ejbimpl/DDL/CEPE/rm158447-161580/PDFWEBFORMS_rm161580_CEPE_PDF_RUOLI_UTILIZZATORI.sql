use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_RUOLI_UTILIZZATORI') IS NOT NULL BEGIN
    DROP TABLE PDF_RUOLI_UTILIZZATORI   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_RUOLI_UTILIZZATORI'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_RUOLI_UTILIZZATORI' DROPPED"
    END
END
go
create table PDF_RUOLI_UTILIZZATORI
(
	CODICE	    	varchar(10)         not null
,	DESCRIZIONE		varchar(255)		null 
,	ORDINE			smallint			null
)
go
alter table PDF_RUOLI_UTILIZZATORI add constraint PDF_RUOLI_UTILIZZATORI_PK PRIMARY KEY NONCLUSTERED ( CODICE )
go 

insert into PDF_RUOLI_UTILIZZATORI (CODICE, DESCRIZIONE, ORDINE) values ('FB', 'Family Banker', 1)
insert into PDF_RUOLI_UTILIZZATORI (CODICE, DESCRIZIONE, ORDINE) values ('FPS','Family Protection Specialist', 2)
insert into PDF_RUOLI_UTILIZZATORI (CODICE, DESCRIZIONE, ORDINE) values ('BC', 'Banker Consultant', 3)
go
grant all on PDF_RUOLI_UTILIZZATORI to Group_CEPE
go

PRINT "table 'PDF_RUOLI_UTILIZZATORI' CREATED"
go

