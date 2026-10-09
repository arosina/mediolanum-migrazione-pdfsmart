-----------------------------------------------------------------------------
-- DDL for Table 'CEPE.dbo.PDF_SRV_DISPOSITIVA_BMED_DATA'
-----------------------------------------------------------------------------
print '<<<<< CREATING Table - "CEPE.dbo.PDF_SRV_DISPOSITIVA_BMED_DATA" >>>>>'
go

use CEPE
go 

setuser 'dbo'
go 

IF EXISTS (SELECT 1 FROM sysobjects o, sysusers u WHERE o.uid=u.uid AND o.name = 'PDF_SRV_DISPOSITIVA_BMED_DATA' AND u.name = 'dbo' AND o.type = 'U')
	drop table PDF_SRV_DISPOSITIVA_BMED_DATA

IF (@@error != 0)
BEGIN
	PRINT 'Error CREATING table "CEPE.dbo.PDF_SRV_DISPOSITIVA_BMED_DATA"'
	SELECT syb_quit()
END
go

create table PDF_SRV_DISPOSITIVA_BMED_DATA (
	PDF_INSTANCE_ID  			VARCHAR (20) NOT NULL,
	COD_DISPOSITIVA_BMED 		VARCHAR (20) NOT NULL,
	TIPO_DISPOSITIVA_BMED 		VARCHAR(30)  NOT NULL, --CENSIMENTO_ANAGRAFICO
	BARCODE                    	VARCHAR (20) NULL,
	PDF_INSTANCE_ID_MOM  		VARCHAR (30) NULL,
	CONSTRAINT PDF_SRV_DISPO_BMED_DATA_PK PRIMARY KEY NONCLUSTERED (PDF_INSTANCE_ID,COD_DISPOSITIVA_BMED)  on 'default' 
)
lock datarows
with dml_logging = full
 on 'default'
go 

Grant Select on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Consultazione_Database Granted by dbo
go
Grant Delete Statistics on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Truncate Table on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Update Statistics on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Select on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Insert on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Delete on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go
Grant Update on dbo.PDF_SRV_DISPOSITIVA_BMED_DATA to Group_CEPE Granted by dbo
go

setuser
go 


-- DDLGen Completed
-- at 06/28/19 4:17:48 CEST

if object_id('dbo.PDF_SRV_DISPOSITIVA_BMED_DATA') is not null
	print "<<< dbo.PDF_SRV_DISPOSITIVA_BMED_DATA created >>>"
else
	print "<<< dbo.PDF_SRV_DISPOSITIVA_BMED_DATA NOT created >>>"
go
