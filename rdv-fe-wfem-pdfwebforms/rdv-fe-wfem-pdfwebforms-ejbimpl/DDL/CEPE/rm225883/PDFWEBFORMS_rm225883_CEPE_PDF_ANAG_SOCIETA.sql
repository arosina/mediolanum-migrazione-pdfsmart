use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'SOCIETA')
BEGIN
 alter table PDF_ANAG add SOCIETA varchar(100) null
END
go

exec sp__comment "Societa' di riferimento del modulo", PDF_ANAG, SOCIETA
go

update PDF_ANAG set SOCIETA='Mediolanum Gestione Fondi' 		where MOM_CODE in ('FI02')
update PDF_ANAG set SOCIETA='Mediolanum Gestione Fondi' 		where MOM_CODE in ('FI07')
update PDF_ANAG set SOCIETA='Mediolanum International Funds' 	where MOM_CODE in ('FE03')
update PDF_ANAG set SOCIETA='Mediolanum International Funds' 	where MOM_CODE in ('FE02')
update PDF_ANAG set SOCIETA='Mediolanum International Funds' 	where MOM_CODE in ('FE23')
update PDF_ANAG set SOCIETA='Banca Mediolanum' 					where DRIVER_NAME = 'fonditerziiniziale'
update PDF_ANAG set SOCIETA='Banca Mediolanum' 					where MOM_CODE in ('FT29', 'FT31')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA38', 'IA71', 'IA72', 'IA88')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA41', 'IA73', 'IA77', 'IA92')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IAF5')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IAF8')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA99')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IAA1')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA16')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA35')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA18')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('IA26')
update PDF_ANAG set SOCIETA='Mediolanum International Life' 	where MOM_CODE in ('IAC4')
update PDF_ANAG set SOCIETA='Mediolanum International Life' 	where MOM_CODE in ('IAC7')
update PDF_ANAG set SOCIETA='Mediolanum International Life' 	where DRIVER_NAME = 'polizzemiltranche'
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('PZ07')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('PZ05')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('PZ63')
update PDF_ANAG set SOCIETA='Mediolanum Vita' 					where MOM_CODE in ('PZ64')
update PDF_ANAG set SOCIETA='Banca Mediolanum' 					where MOM_CODE in ('GP06', 'GP10')
update PDF_ANAG set SOCIETA='Banca Mediolanum' 					where MOM_CODE in ('GP07', 'GP11')
update PDF_ANAG set SOCIETA='Mediolanum Gestione Fondi' 		where MOM_CODE in ('FI18')
update PDF_ANAG set SOCIETA='Mediolanum Gestione Fondi' 		where MOM_CODE in ('FI38')
go

PRINT "PDF_ANAG aggiornata"
go


