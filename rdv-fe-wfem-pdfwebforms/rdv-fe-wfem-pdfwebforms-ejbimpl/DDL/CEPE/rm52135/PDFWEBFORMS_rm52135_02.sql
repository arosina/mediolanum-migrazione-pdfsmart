use CEPE
go

IF OBJECT_ID ('PDF_INSTANCE_DETT') IS NOT NULL BEGIN
    DROP TABLE PDF_INSTANCE_DETT   
    IF (@@error != 0)
    BEGIN
        PRINT "Error DROPPING table 'PDF_INSTANCE_DETT'"
    END
    ELSE
    BEGIN
        PRINT "table 'PDF_INSTANCE_DETT' DROPPED"
    END
END
go

create table PDF_INSTANCE_DETT
(
	PDF_INSTANCE_ID							varchar(20)			not null
,	PDF_INSTANCE_DETT_NUM					int					not null

,	PDF_ID		        					varchar(32)         null
,	CODE									varchar(50)			null
,	PUBLICATION_ID							int					null

,	DESCR									varchar(100)		null
,	BARCODE									varchar(20)			null

,	COD_PRODOTTO_PRIT 						smallint 			null
,	COD_OPERAZIONE_PRIT 					smallint 			null
,	MOM_CODE								char(4)				null
,	MOM_VERSION								char(3)				null

,	NUM_PAGES 								smallint 			null
,	NUM_COPIE 								smallint 			null
)
go
alter table PDF_INSTANCE_DETT add constraint PDF_INSTANCE_DETT_PK PRIMARY KEY NONCLUSTERED ( PDF_INSTANCE_ID, PDF_INSTANCE_DETT_NUM )
go 
grant all on PDF_INSTANCE_DETT to Group_CEPE
go
create index PDF_INSTANCE_DETT_IDX01 on PDF_INSTANCE_DETT (CODE)
go
create index PDF_INSTANCE_DETT_IDX02 on PDF_INSTANCE_DETT (MOM_CODE)
go

PRINT "table 'PDF_INSTANCE_DETT' CREATED"
