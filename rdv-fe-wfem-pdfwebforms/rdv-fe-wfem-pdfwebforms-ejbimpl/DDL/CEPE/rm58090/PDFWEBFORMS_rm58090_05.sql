use CEPE
GO

IF OBJECT_ID ('PDF_INFO_PRIT') IS NULL 
	BEGIN 
		exec ("create table PDF_INFO_PRIT(
			PDF_CODE	        	varchar(50)			not null
		,	CHIAVE					varchar(60)			not null
		, 	PRIT_C_PRODOTTO         int					not null
		, 	PRIT_C_OPERAZIONE       int					not null)
		alter table PDF_INFO_PRIT add constraint PDF_INFO_PRIT_PK PRIMARY KEY NONCLUSTERED ( PDF_CODE, CHIAVE )
		PRINT 'table PDF_INFO_PRIT CREATED'")
	END 
ELSE
	BEGIN
		PRINT "OK"
	END
GO
grant all on PDF_INFO_PRIT to Group_CEPE
GO

