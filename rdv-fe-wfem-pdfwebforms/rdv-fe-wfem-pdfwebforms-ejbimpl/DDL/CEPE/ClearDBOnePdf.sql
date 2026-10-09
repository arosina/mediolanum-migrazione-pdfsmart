use CEPE
go

declare @pdfcode varchar(50)
select @pdfcode = '4HOME'
declare @pdfid varchar(32)
select @pdfid=PDF_ID from PDF_ANAG where CODE = @pdfcode  
delete from PDF_PUBLICATION_WORK where PDF_ID = @pdfid
delete from PDF_PAGE_WORK where PDF_ID = @pdfid
delete from PDF_PUBLICATION where PDF_ID = @pdfid
delete from PDF_PAGE where PDF_ID = @pdfid
delete from PDF_INSTANCE where PDF_ID = @pdfid
delete from PDF_ANAG where PDF_ID = @pdfid

declare @modcode varchar(50)
select @modcode = '4HOME'
declare @modid varchar(32)
select @modid=ID_MODULO from CEPE_CATM_MODULO where CODICE_MODULO = @modcode  
delete from CEPE_CATM_MODULO_ALLEGATO where ID_MODULO = @modid
delete from CEPE_CATM_MODULO_COMP_EXMP where ID_MODULO = @modid
delete from CEPE_CATM_MODULO_PRODOTTO where ID_MODULO = @modid
delete from CEPE_CATM_CONTR_ELETTR where ID_MODULO = @modid
delete from CEPE_CATM_MODULO where ID_MODULO = @modid


