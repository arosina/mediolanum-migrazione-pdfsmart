use CEPE
go


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_ANAG') IS NOT NULL BEGIN
    DROP TABLE PDF_ANAG   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_ANAG'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_ANAG' DROPPED"
    END
END
go
create table PDF_ANAG
(
	PDF_ID	            		varchar(32)         not null
,	CODE						varchar(50)			not null
,	DESCR						varchar(255)		null 
,	CREATION_USER 				varchar(20)			null
,	CREATION_TIME 				datetime			null
,	LASTMOD_USER  				varchar(20)			null
,	LASTMOD_TIME  				datetime			null

,	COD_PRODOTTO_PRIT 			smallint 			null
,	COD_OPERAZIONE_PRIT 		smallint 			null
,	MOM_CODE					char(4)				null

,	IS_STAMPA_ENABLED			char(1) 			null
,	IS_CARTA_LIBERA_ENABLED		char(1) 			null
,	IS_CARTA_CHIMICA_ENABLED 	char(1) 			null
,	IS_FIRMA_DIGTALE_ENABLED 	char(1) 			null
,	TESTO_COPIA_MEDIOLANUM 		varchar(100) 		null
,	TESTO_COPIA_CLIENTE 		varchar(100) 		null
,	TESTO_COPIA_AGENTE 			varchar(100) 		null
)
go
alter table PDF_ANAG add constraint PDF_ANAG_PK PRIMARY KEY NONCLUSTERED ( PDF_ID )
go 
grant all on PDF_ANAG to Group_CEPE
go
PRINT "table 'PDF_ANAG' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_PUBLICATION_WORK') IS NOT NULL BEGIN
    DROP TABLE PDF_PUBLICATION_WORK   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_PUBLICATION_WORK'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_PUBLICATION_WORK' DROPPED"
    END
END
go
create table PDF_PUBLICATION_WORK
(
	PDF_ID		        varchar(32)         not null
,	PUBLICATION_ID		int					not null

,	ORIGINAL_PUB_ID		int					null
,	START_DATE			datetime			null
,	EDITION				varchar(10)			null

,	MOM_VERSION			char(3)				null

,	PUBLISH_USER 		varchar(20)			null
,	PUBLISH_TIME		datetime			null

,	FILE_NAME			varchar(250)		null
,	CONTENT				image				null
)
go
alter table PDF_PUBLICATION_WORK add constraint PDF_PUBLICATION_WORK_PK PRIMARY KEY NONCLUSTERED ( PDF_ID, PUBLICATION_ID )
go 
grant all on PDF_PUBLICATION_WORK to Group_CEPE
go
PRINT "table 'PDF_PUBLICATION_WORK' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_PAGE_WORK') IS NOT NULL BEGIN
    DROP TABLE PDF_PAGE_WORK   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_PAGE_WORK'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_PAGE_WORK' DROPPED"
    END
END
go
create table PDF_PAGE_WORK
(
	PDF_ID		        varchar(32)         not null
,	PUBLICATION_ID		int					not null
,	PAGE_NUM			int					not null

,	PAGE_IMG			image				null
)
go
alter table PDF_PAGE_WORK add constraint PDF_PAGE_WORK_PK PRIMARY KEY NONCLUSTERED ( PDF_ID, PUBLICATION_ID, PAGE_NUM )
go 
grant all on PDF_PAGE_WORK to Group_CEPE
go
PRINT "table 'PDF_PAGE_WORK' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_PUBLICATION') IS NOT NULL BEGIN
    DROP TABLE PDF_PUBLICATION   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_PUBLICATION'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_PUBLICATION' DROPPED"
    END
END
go
create table PDF_PUBLICATION
(
	PDF_ID		        varchar(32)         not null
,	PUBLICATION_ID		int					not null
,	IS_ARCHIVED 		char(1) 			null
,	START_DATE			datetime			null
,	EDITION				varchar(10)			null

,	MOM_VERSION			char(3)				null

,	PUBLISH_USER 		varchar(20)			null
,	PUBLISH_TIME		datetime			null

,	FILE_NAME			varchar(250)		null
,	CONTENT				image				null
)
go
alter table PDF_PUBLICATION add constraint PDF_PUBLICATION_PK PRIMARY KEY NONCLUSTERED ( PDF_ID, PUBLICATION_ID )
go 
grant all on PDF_PUBLICATION to Group_CEPE
go
PRINT "table 'PDF_PUBLICATION' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_PAGE') IS NOT NULL BEGIN
    DROP TABLE PDF_PAGE   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_PAGE'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_PAGE' DROPPED"
    END
END
go
create table PDF_PAGE
(
	PDF_ID		        varchar(32)         not null
,	PUBLICATION_ID		int					not null
,	PAGE_NUM			int					not null

,	PAGE_IMG			image				null
)
go
alter table PDF_PAGE add constraint PDF_PAGE_PK PRIMARY KEY NONCLUSTERED ( PDF_ID, PUBLICATION_ID, PAGE_NUM )
go 
grant all on PDF_PAGE to Group_CEPE
go
PRINT "table 'PDF_PAGE' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
delete from PROGRESSIVI_CFG where C_TABELLA = 'PDF_INSTANCE'
go
insert into PROGRESSIVI_CFG
(C_TABELLA,  		C_SERVER,  C_DATABASE, C_OWNER, C_PROCEDURE, N_LUNG_PROGR, C_ONLINE, C_OFFLINE, C_FORMATO) 
values
('PDF_INSTANCE', 	NULL,      NULL,       NULL,    NULL,        9,            'A',      'A',       '4USER+TYPE')
go
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_INSTANCE') IS NOT NULL BEGIN
    DROP TABLE PDF_INSTANCE   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_INSTANCE'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_INSTANCE' DROPPED"
    END
END
go
create table PDF_INSTANCE
(
	PDF_INSTANCE_ID							varchar(20)			not null
,	PDF_ID		        					varchar(32)         not null
,	CODE									varchar(50)			not null
,	PUBLICATION_ID							int					not null

,	ENVIRONMENT								varchar(50)			null
,	DESCR									varchar(100)		null
,	IS_STAMPA_ENABLED						char(1) 			null

,	CREATION_USER 							varchar(20)			null
,	CREATION_TIME 							datetime			null
,	LASTMOD_USER  							varchar(20)			null
,	LASTMOD_TIME  							datetime			null
,	COMPLETION_USER							varchar(20)			null
,	COMPLETION_TIME							datetime			null

,	STATO									char(2)				null
,	MODALITA_DI_SOTTOSCRIZIONE				varchar(20)			null
,	BARCODE									varchar(20)			null
,	COD_AGENTE								char(10)			null

,	COD_PRODOTTO_PRIT 						smallint 			null
,	COD_OPERAZIONE_PRIT 					smallint 			null
,	MOM_CODE								char(4)				null
,	MOM_VERSION								char(3)				null

,	TESTO_COPIA_MEDIOLANUM 					varchar(100) 		null
,	TESTO_COPIA_CLIENTE 					varchar(100) 		null
,	TESTO_COPIA_AGENTE 						varchar(100) 		null

,	CLI1_NDG								char(11)			null
,	CLI1_ID_CENSIMENTO						char(20)			null
,	CLI1_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI1_COGNOME							varchar(40)			null
,	CLI1_NOME								varchar(40)			null

,	CLI2_NDG								char(11)			null
,	CLI2_ID_CENSIMENTO						char(20)			null
,	CLI2_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI2_COGNOME							varchar(40)			null
,	CLI2_NOME								varchar(40)			null

,	CLI3_NDG								char(11)			null
,	CLI3_ID_CENSIMENTO						char(20)			null
,	CLI3_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI3_COGNOME							varchar(40)			null
,	CLI3_NOME								varchar(40)			null

,	CLI4_NDG								char(11)			null
,	CLI4_ID_CENSIMENTO						char(20)			null
,	CLI4_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI4_COGNOME							varchar(40)			null
,	CLI4_NOME								varchar(40)			null

,	CLI5_NDG								char(11)			null
,	CLI5_ID_CENSIMENTO						char(20)			null
,	CLI5_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI5_COGNOME							varchar(40)			null
,	CLI5_NOME								varchar(40)			null

,	CLI6_NDG								char(11)			null
,	CLI6_ID_CENSIMENTO						char(20)			null
,	CLI6_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI6_COGNOME							varchar(40)			null
,	CLI6_NOME								varchar(40)			null

,	CLI7_NDG								char(11)			null
,	CLI7_ID_CENSIMENTO						char(20)			null
,	CLI7_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI7_COGNOME							varchar(40)			null
,	CLI7_NOME								varchar(40)			null

,	CLI8_NDG								char(11)			null
,	CLI8_ID_CENSIMENTO						char(20)			null
,	CLI8_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI8_COGNOME							varchar(40)			null
,	CLI8_NOME								varchar(40)			null

,	CLI9_NDG								char(11)			null
,	CLI9_ID_CENSIMENTO						char(20)			null
,	CLI9_ASSGNCLIAGE_C_POTZLE				char(16)			null
,	CLI9_COGNOME							varchar(40)			null
,	CLI9_NOME								varchar(40)			null

,	XML_DATA								text				null
,	PDF_CONTENT								image				null
)
go
alter table PDF_INSTANCE add constraint PDF_INSTANCE_PK PRIMARY KEY NONCLUSTERED ( PDF_INSTANCE_ID )
go 
grant all on PDF_INSTANCE to Group_CEPE
go
PRINT "table 'PDF_INSTANCE' CREATED"
-- *******************************************************************************************************************************


-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_MODALITA_SOTTOSCRIZIONE') IS NOT NULL BEGIN
    DROP TABLE PDF_MODALITA_SOTTOSCRIZIONE   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_MODALITA_SOTTOSCRIZIONE'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_MODALITA_SOTTOSCRIZIONE' DROPPED"
    END
END
go
create table PDF_MODALITA_SOTTOSCRIZIONE (
	MODALITA_DI_SOTTOSCRIZIONE			varchar(20)		not null
,	X_MODALITA_DI_SOTTOSCRIZIONE		varchar(100)    null
)
go
alter table PDF_MODALITA_SOTTOSCRIZIONE add constraint PDF_MODALITA_SOTTOSCRIZIONE_PK primary key nonclustered (MODALITA_DI_SOTTOSCRIZIONE)
go
grant SELECT on PDF_MODALITA_SOTTOSCRIZIONE to Consultazione_Database
go
grant all on PDF_MODALITA_SOTTOSCRIZIONE to Group_CEPE
go

insert into PDF_MODALITA_SOTTOSCRIZIONE (MODALITA_DI_SOTTOSCRIZIONE, X_MODALITA_DI_SOTTOSCRIZIONE) values ('CARTA_LIBERA','Carta libera')
insert into PDF_MODALITA_SOTTOSCRIZIONE (MODALITA_DI_SOTTOSCRIZIONE, X_MODALITA_DI_SOTTOSCRIZIONE) values ('CARTA_CHIMICA','Carta chimica')
insert into PDF_MODALITA_SOTTOSCRIZIONE (MODALITA_DI_SOTTOSCRIZIONE, X_MODALITA_DI_SOTTOSCRIZIONE) values ('FIRMA_DIGITALE','Firma digitale')
go

PRINT "table 'PDF_MODALITA_SOTTOSCRIZIONE' CREATED"
-- *******************************************************************************************************************************