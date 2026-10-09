use CEPE
go

IF OBJECT_ID ('PDF_ACTION_TRACE_LOG') IS NOT NULL BEGIN
    DROP TABLE PDF_ACTION_TRACE_LOG   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_ACTION_TRACE_LOG'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_ACTION_TRACE_LOG' DROPPED"
    END
END
go
create table PDF_ACTION_TRACE_LOG
(
	PDF_INSTANCE_ID				varchar(20)			not null
,	ACTION_CODE 				varchar(10)			not null
,	ACTION_USER 				varchar(20)			not null
,	ACTION_TIME  				datetime			not null
,	COD_RUOLO_IMPERSONATO		varchar(10)			null
)
go
alter table PDF_ACTION_TRACE_LOG add constraint PDF_ACTION_TRACE_LOG_PK PRIMARY KEY NONCLUSTERED (PDF_INSTANCE_ID, ACTION_CODE, ACTION_USER)
go 
grant all on PDF_ACTION_TRACE_LOG to Group_CEPE
go
PRINT "table 'PDF_ACTION_TRACE_LOG' CREATED"
