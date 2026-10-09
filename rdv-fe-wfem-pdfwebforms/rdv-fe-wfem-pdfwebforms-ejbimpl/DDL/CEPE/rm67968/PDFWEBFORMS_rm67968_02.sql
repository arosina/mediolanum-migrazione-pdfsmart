use master
go

sp_dboption CEPE, 'select into/bulkcopy/pllsort', true
go

use CEPE
go

checkpoint
go 3


USE CEPE
go
alter table PDF_ANAG modify FILENET_CLASSE_DOC_CLIENTE varchar(100) null
go


use master
go

sp_dboption CEPE, 'select into/bulkcopy/pllsort', false
go

use CEPE
go

checkpoint
go 3