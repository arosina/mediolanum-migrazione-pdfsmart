use CEPE
go

update PDF_ANAG set HAS_DATA_SOTTOSCRIZIONE_OGGI = 'S' where MOM_CODE in (
	'IAA1'
,	'IAA2'
,	'IAA3'
,	'IA99'

,	'FI02'
,	'FI07'
,	'FI12'
,	'FI13'

,	'FE02'
,	'FE03'
,	'FE05'
,	'FE15'
,	'FE23'

,	'FE11'
,	'FE13'

,	'FE27'
,	'FE28'
)
go

PRINT "data sottoscrizione oggi configurata"
