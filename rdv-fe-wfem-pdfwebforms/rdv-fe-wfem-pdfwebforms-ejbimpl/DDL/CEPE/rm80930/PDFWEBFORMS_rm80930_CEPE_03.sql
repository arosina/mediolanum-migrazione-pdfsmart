use CEPE
GO

IF OBJECT_ID('PDF_INFO_MILESTONE') IS NOT NULL
BEGIN
	DROP TABLE PDF_INFO_MILESTONE
END
go

create table PDF_INFO_MILESTONE(
	PDF_CODE	        	varchar(50)			not null
,	CHIAVE					varchar(60)			not null
, 	MILESTONE_C_PRODOTTO    int					not null
, 	MILESTONE_C_OPERAZIONE  int					not null
)
go
alter table PDF_INFO_MILESTONE add constraint PDF_INFO_MILESTONE_PK primary key nonclustered (PDF_CODE, CHIAVE)
go
grant SELECT on PDF_INFO_MILESTONE to Consultazione_Database
go
grant all on PDF_INFO_MILESTONE to Group_CEPE
go
