use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
insert into PROGRESSIVI_CFG (C_TABELLA,  N_LUNG_PROGR, C_ONLINE, C_OFFLINE, C_FORMATO) 
values ("PDFWEBFORM_ANAG_COUNTER", 20, "", "", "4RESONLYNU") 
go 

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_APPL_REFERENCED_CODE') IS NOT NULL BEGIN
    DROP TABLE PDF_APPL_REFERENCED_CODE   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_APPL_REFERENCED_CODE'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_APPL_REFERENCED_CODE' DROPPED"
    END
END
go
create table PDF_APPL_REFERENCED_CODE
(
	APPL_REFERENCE		varchar(255)	not null
,	PDF_ENVIRONMENT		varchar(50)		null
,	PDF_CODE			varchar(50)		null
,	ACROFORM_VERSION 	int 			null
)
go
alter table PDF_APPL_REFERENCED_CODE add constraint PDF_APPL_REFERENCED_CODE_PK PRIMARY KEY NONCLUSTERED ( APPL_REFERENCE )
go 
grant all on PDF_APPL_REFERENCED_CODE to Group_CEPE
go
PRINT "table 'PDF_APPL_REFERENCED_CODE' CREATED"
go

insert into PDF_APPL_REFERENCED_CODE (APPL_REFERENCE,PDF_ENVIRONMENT,PDF_CODE,ACROFORM_VERSION)
values ('PET-4PROTECTIONHOME','PET','PR16',null)
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_AREAS') IS NOT NULL BEGIN
    DROP TABLE PDF_AREAS   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_AREAS'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_AREAS' DROPPED"
    END
END
go
create table PDF_AREAS
(
	AREA						varchar(50)			not null
,	DESCR						varchar(255)		null 
)
go
alter table PDF_AREAS add constraint PDF_AREAS_PK PRIMARY KEY NONCLUSTERED ( AREA )
go 
grant all on PDF_AREAS to Group_CEPE
go
PRINT "table 'PDF_AREAS' CREATED"
go

insert into PDF_AREAS (AREA, DESCR) values ('CATALOGO_MODULI', 'Catalogo moduli')
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
alter table PDF_PUBLICATION_WORK add ACROFORM_VERSION int null
go
alter table PDF_PUBLICATION add ACROFORM_VERSION int null
go
update PDF_PUBLICATION_WORK set ACROFORM_VERSION = 1
go
update PDF_PUBLICATION set ACROFORM_VERSION = 1
go
alter table PDF_PUBLICATION_WORK add DRIVER_VERSION varchar(50) null
go
alter table PDF_PUBLICATION add DRIVER_VERSION varchar(50) null
go
alter table PDF_PUBLICATION_WORK add VALIDATION_WARNINGS_MSG text null
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
alter table PDF_ANAG add SIGN_ALL char(1) null
go
alter table PDF_ANAG add AREA varchar(50) null
go
alter table PDF_ANAG add START_DATE datetime null
go
alter table PDF_ANAG add END_DATE datetime null
go
alter table PDF_ANAG add DRIVER_NAME varchar(50) null
go
alter table PDF_ANAG add SEND_FB_MAIL_ONSIGN char(1) null
go
alter table PDF_ANAG add SEND_CLI_SMS_ONSIGN char(1) null
go
alter table PDF_ANAG add CLI_SMS_ONSIGN_TEXT varchar(255) null
go
alter table PDF_ANAG add EXT_LINK_ONSIGN_LABEL varchar(250) null
go
alter table PDF_ANAG add EXT_LINK_ONSIGN_URL varchar(600) null
go
alter table PDF_ANAG add NUM_COPIE smallint null
go
alter table PDF_ANAG add TESTO_COPIA_1 varchar(100) null
go
alter table PDF_ANAG add TESTO_COPIA_2 varchar(100) null
go
alter table PDF_ANAG add TESTO_COPIA_3 varchar(100) null
go
alter table PDF_ANAG add TESTO_COPIA_4 varchar(100) null
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
update PDF_ANAG set AREA = 'CATALOGO_MODULI'
where PDF_ID not in (select A.PDF_ID
				 from PDF_ANAG A where not exists (select 1 from CEPE_CATM_MODULO C where A.PDF_ID = C.ID_MODULO))
go

PRINT "Columns ADDED"
