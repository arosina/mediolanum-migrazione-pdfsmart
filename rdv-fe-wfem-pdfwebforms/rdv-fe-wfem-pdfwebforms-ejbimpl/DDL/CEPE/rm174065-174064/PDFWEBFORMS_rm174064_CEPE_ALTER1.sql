use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_INSTANCE_DETT') AND name = 'COD_DISPOSITIVA_BMED')
BEGIN
	alter table PDF_INSTANCE_DETT add COD_DISPOSITIVA_BMED varchar(20) null
END
go

IF EXISTS (SELECT 1 FROM sysindexes i, sysobjects o, sysusers u WHERE  o.id = i.id AND o.uid = u.uid AND i.name = 'PDF_INSTANCE_DETT_IDX03' AND u.name = 'dbo' AND o.name = 'PDF_INSTANCE_DETT' AND i.indid > 0)
BEGIN
setuser 'dbo'
drop index PDF_INSTANCE_DETT.PDF_INSTANCE_DETT_IDX03
END
go

IF (@@error != 0)
BEGIN
PRINT 'Error dropping Index PDF_INSTANCE_DETT_IDX03'
SELECT syb_quit()
END
go

create nonclustered index PDF_INSTANCE_DETT_IDX03
on CEPE.dbo.PDF_INSTANCE_DETT(COD_DISPOSITIVA_BMED)
go

-- *******************************************************************************************************************************
-- *******************************************************************************************************************************
IF OBJECT_ID ('PDF_MESSAGE_CODES') IS NOT NULL BEGIN
    DROP TABLE PDF_MESSAGE_CODES   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_MESSAGE_CODES'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_MESSAGE_CODES' DROPPED"
    END
END
go
create table PDF_MESSAGE_CODES
(
	CONF_ITEM_NAME    		varchar(255)    not null
,	CONF_ITEM_PROPERTIES	text			null 
)
go
alter table PDF_MESSAGE_CODES add constraint PDF_MESSAGE_CODES_PK PRIMARY KEY NONCLUSTERED ( CONF_ITEM_NAME )
go
grant all on PDF_MESSAGE_CODES to Group_CEPE
go
grant SELECT on PDF_MESSAGE_CODES to Consultazione_Database
go

PRINT "table ALTERED"
