use CEPE
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_MOM_INSTANCE') IS NOT NULL BEGIN
    DROP TABLE PDF_MOM_INSTANCE   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_MOM_INSTANCE'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_MOM_INSTANCE' DROPPED"
    END
END
go
create table PDF_MOM_INSTANCE
(
	PDF_INSTANCE_ID							varchar(20)			not null

,	STATO									char(2)				null

,	CREATION_USER 							varchar(20)			null
,	CREATION_TIME 							datetime			null
,	LASTMOD_USER  							varchar(20)			null
,	LASTMOD_TIME  							datetime			null
,	COMPLETION_USER							varchar(20)			null
,	COMPLETION_TIME							datetime			null

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
)
go
alter table PDF_MOM_INSTANCE add constraint PDF_MOM_INSTANCE_PK PRIMARY KEY NONCLUSTERED ( PDF_INSTANCE_ID )
go 
grant all on PDF_MOM_INSTANCE to Group_CEPE
go
PRINT "table 'PDF_MOM_INSTANCE' CREATED"
-- *******************************************************************************************************************************

