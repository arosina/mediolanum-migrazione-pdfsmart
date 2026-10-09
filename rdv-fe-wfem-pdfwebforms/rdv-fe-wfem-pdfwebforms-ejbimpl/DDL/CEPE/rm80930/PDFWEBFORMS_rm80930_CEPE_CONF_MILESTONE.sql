use CEPE
go

IF OBJECT_ID('PDF_PRODOTTI_MILESTONE') IS NOT NULL
BEGIN
	DROP TABLE PDF_PRODOTTI_MILESTONE
END
go
create table PDF_PRODOTTI_MILESTONE (
	CODICE			int				not null
,	DESCRIZIONE		varchar(150)    null
,	F_VALIDITA		char(1)			null
)
go
alter table PDF_PRODOTTI_MILESTONE add constraint PDF_PRODOTTI_MILESTONE_PK primary key nonclustered (CODICE)
go
grant SELECT on PDF_PRODOTTI_MILESTONE to Consultazione_Database
go
grant all on PDF_PRODOTTI_MILESTONE to Group_CEPE
go

insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 0,'Non specificato', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 1,'Conto Corrente', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 2,'Carte e Bancomat', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 3,'Assegni', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 4,'RID', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 5,'Bonifici Ricorrenti', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 6,'Bonifici altra banca', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 7,'Conto Carta', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 8,'Conto Deposito', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values ( 9,'Prestiti', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (10,'Mutui', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (11,'GPM', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (12,'GPF', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (13,'Investimenti Assicurativi', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (14,'Fondi Comuni', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (15,'Fondi Immobiliari', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (16,'Polizza Vita', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (17,'Fondi pensione', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (18,'Protezione', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (19,'Certificates', 'S')
insert into PDF_PRODOTTI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (20,'GPMULTILINEA', 'S')
go


IF OBJECT_ID('PDF_OPERAZIONI_MILESTONE') IS NOT NULL
BEGIN
	DROP TABLE PDF_OPERAZIONI_MILESTONE
END
go
create table PDF_OPERAZIONI_MILESTONE (
	CODICE			int				not null
,	DESCRIZIONE		varchar(150)    null
,	F_VALIDITA		char(1)			null
)
go
alter table PDF_OPERAZIONI_MILESTONE add constraint PDF_OPERAZIONI_MILESTONE_PK primary key nonclustered (CODICE)
go
grant SELECT on PDF_OPERAZIONI_MILESTONE to Consultazione_Database
go
grant all on PDF_OPERAZIONI_MILESTONE to Group_CEPE
go

insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (1,		'Bonifico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (2,		'Prenotazione Contanti', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (3,		'Bonifico Per Ristrutturazione', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (4,		'Bonifico Energetico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (5,		'Prenotazione Contanti', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (6,		'Pagamento tributi', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (7,		'Pagamento Ri.Ba', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (8,		'Pagamento Bollettini', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (9,		'Ricarica Telefonica e TV Digitale', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (10,	'Richiesta Libretto Assegni', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (11,	'Acquisto Dollari', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (12,	'Vendita Dollari', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (13,	'Richiesta Assegni Circolari', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (14,	'Attivazione RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (15,	'Revoca RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (16,	'Sospensione RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (17,	'Riattivazione RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (18,	'Ricarica Carte Prepagate', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (19,	'Richiesta Bancomat', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (20,	'Richiesta Carta', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (21,	'Richiesta Carta Money Service', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (22,	'Richiesta Nuovo Massimale', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (23,	'Vincolo', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (24,	'Gestione Conti Predefiniti', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (25,	'Richiesta Attivazione Linea Revolving', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (26,	'Richiesta Finanziamento', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (27,	'Attivazione Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (28,	'Revoca Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (29,	'Sospensione Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (30,	'Riattivazione Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (31,	'Addebito Assegno', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (32,	'Addebito RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (33,	'Addebito Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (34,	'Addebito Bancomat', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (35,	'Movimenti Conto Corrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (36,	'Movimenti Gestioni Patrimoniali', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (37,	'Movimento Fondo Comune', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (38,	'Movimento Fondo Pensione', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (39,	'Movimenti Fondi Immobiliari', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (40,	'Movimenti Polizze Unit Linked', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (41,	'Movimenti Altre Polizze', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (42,	'Easy Transfer', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (43,	'Bonifico Estero', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (44,	'Modifica RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (45,	'Modifica Bonifico Ricorrente', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (46,	'Richiesta Conto Carta', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (47,	'Attivazione Bonifico da altra Banca', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (48,	'Modifica Bonifico da altra Banca', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (49,	'Revoca Bonifico da altra Banca', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (50,	'Sottoscrizione', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (51,	'Passaggio', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (52,	'Aggiuntivo', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (53,	'Rimborso', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (54,	'Passaggio', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (55,	'Acquisto GPM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (56,	'Aggiuntivo Polizza', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (57,	'Appuntamento com Family Banker', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (58,	'Evento Banca Mediolanum', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (59,	'Trasferimenti RID', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (60,	'Acquisto GPF', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (61,	'Aggiuntivo', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (62,	'Rimborso', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (63,	'Sottoscrizione', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (64,	'Richiesta Telepass', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (65,	'Ricarica TV', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (66,	'Movimenti Futuri Bonifici Programmati', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (67,	'Movimenti Futuri Bonifici Carte di Credito', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (68,	'Movimenti Futuri Bonifici Easy Transfer', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (69,	'Movimenti Futuri Mutui e Prestiti', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (70,	'Certificate', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (71,	'Sottoscrizione Fondi Comuni - Rifiuto Copernico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (72,	'Aggiuntivo Fondi Comuni - Rifiuto Copernico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (73,	'Svincolo', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (74,	'Eventi ed Appuntamenti', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (75,	'Promemoria - scadenza vincolo', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (76,	'PayPal - Bonifico Immediato', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (80,	'Richiesta premio  Mediolanum Freedom Rewarding', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (81,	'Adesione a Mediolanum Freedom Rewarding', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (82,	'Bollettino e bollettino_poste_ita_dett', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (83,	'Ricarica Paypal', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (84,	'Account Paypal', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (85,	'Abbinamento conto Paypal', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (86,	'Revoca addebito diretto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (87,	'Sospensione addebito diretto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (88,	'Riattivazione addebito diretto', 'S') 
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (89,	'Modifica addebito diretto', 'S') 
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (90,	'NFC', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (91,	'Passaggio polizza', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (92,	'Aggiuntivo polizza mylife', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (93,	'Attivazione 3D Secure Code', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (94,	'Modifica 3D Secure Code', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (95,	'Revoca 3D Secure Code', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (96,	'Prelievo da conto PayPal', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (97,	'Dispositive Fondi Terzi', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (98,	'Sottoscrizione', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (99,	'Rimborso', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (100,	'Aggiuntivi Polizza', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (101,	'Switch Polizza', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (106,	'Rimborso Spot Rifiuto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (107,	'Switch Spot Agg Rifiuto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (108,	'CONVERSIONE PROGRAMMATA', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (109,	'Bollettino CBIL', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (110,	'CONVERSIONE PROGRAMMATA RIFIUTATA', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (111,	'CONVERSIONE SPOT RIFIUTATA', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (112,	'AGGIUNTIVO MEDPAX', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (113,	'SWITCH MEDPAX', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (114,	'RIMBORSO PROGRAMMATO', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (115,	'RIMBORSO PROGR RIFIUTATO', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (116,	'RIMBORSO SPOT MANUALE', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (117,	'SWITCH SPOT SOT', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (118,	'SWITCH', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (119,	'AGGIUNTIVO', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (120,	'PAGAMANTO F24', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (121,	'MIL_STORNO', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (122,	'PAGAMENTO F24', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (123,	'Sottoscrizione Rifiuto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (124,	'Rimborso Rifiuto', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (125,	'Aggiuntivo Fondi Terzi Copernico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (126,	'Milestone Rifiuto Fondi Terzi Copernico', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (127,	'Milestone Trasferimento Jiffy P2P', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (128,	'Conversione Fondi Terzi', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (129,	'Conversione Rifiuto Fondi Terzi', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (131,	'CAMBIO PROFILO MYFREEDOM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (132,	'ATTIVAZIONE DEPOSITO TITOLI', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (164,	'Bonifico P2P', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (165,	'Recupero PIN bancomat', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (166,	'Sottoscrizione GPM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (167,	'Aggiuntivo GPM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (168,	'Rimborso GPM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (169,	'Conversione GPM', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (171,	'Attivazione servizio Apple Pay', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (172,	'Attivazione servizio Samsung Pay', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (173,	'Attivazione servizio Google Pay', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (998,	'SWITCH', 'S')
insert into PDF_OPERAZIONI_MILESTONE (CODICE, DESCRIZIONE, F_VALIDITA) values (999,	'AGGIUNTIVO', 'S')
go

IF OBJECT_ID('PDF_TAG_MILESTONE') IS NOT NULL
BEGIN
	DROP TABLE PDF_TAG_MILESTONE
END
go
create table PDF_TAG_MILESTONE (
	CODICE_OPERAZIONE		int				not null
,	TAG1					varchar(100)    null
,	TAG2					varchar(100)    null
)
go
alter table PDF_TAG_MILESTONE add constraint PDF_TAG_MILESTONE_PK primary key nonclustered (CODICE_OPERAZIONE)
go
grant SELECT on PDF_TAG_MILESTONE to Consultazione_Database
go
grant all on PDF_TAG_MILESTONE to Group_CEPE
go


PRINT "milestone per i pdf configurate"

