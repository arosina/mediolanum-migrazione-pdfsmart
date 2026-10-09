use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_ANAG_RUOLI_UTILIZZATORI') IS NOT NULL BEGIN
    DROP TABLE PDF_ANAG_RUOLI_UTILIZZATORI   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_ANAG_RUOLI_UTILIZZATORI'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_ANAG_RUOLI_UTILIZZATORI' DROPPED"
    END
END
go
create table PDF_ANAG_RUOLI_UTILIZZATORI
(
	PDF_ID			 varchar(32)         not null
,	CODICE_RUOLO     varchar(10)         not null
)
go
alter table PDF_ANAG_RUOLI_UTILIZZATORI add constraint PDF_ANAG_RUOLI_UTILIZZATORI_PK PRIMARY KEY NONCLUSTERED ( PDF_ID, CODICE_RUOLO )
go 
grant all on PDF_ANAG_RUOLI_UTILIZZATORI to Group_CEPE
go

PRINT "table 'PDF_ANAG_RUOLI_UTILIZZATORI' CREATED"
go

