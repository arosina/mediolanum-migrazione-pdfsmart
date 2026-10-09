use CEPE
go

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = object_id('PDF_ANAG') AND name = 'NOTE_FB_COPERNICO')
BEGIN
 alter table PDF_ANAG add NOTE_FB_COPERNICO text null
END
go

exec sp__comment "Testo fisso della nota copernico. Se valorizzato il FB in fase di sottoscrizione non lo può modificare", PDF_ANAG, NOTE_FB_COPERNICO
go

update PDF_ANAG set NOTE_FB_COPERNICO=null where NOTE_FB_COPERNICO is not null
go

update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA44'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA43'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA76'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA75'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA80'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA79'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA93'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA94'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA29'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA30'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA28'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IA27'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di riscatto.' where MOM_CODE='IAA2'

update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di rimborso.' where MOM_CODE='GP09'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di rimborso.' where MOM_CODE='FT17'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di rimborso.' where MOM_CODE='FT33'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di rimborso.' where MOM_CODE='FI13'
update PDF_ANAG set NOTE_FB_COPERNICO='Come da intese, sono ad inviarti il seguente modulo, in formato elettronico, per perfezionare la tua disposizione di rimborso.' where MOM_CODE='FE15'
go

PRINT "configurazione PDF_ANAG effettuata"
go
