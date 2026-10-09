use CEPE
go

DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_CONTESTUALI'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_CONTESTUALI',
'da.val("importoFondoPic(0)") +
da.val("importoInizialeFondoPip(0)") +
da.val("versamentoInizialeFondoPac(0)") +
da.isnull("importoTotalePicDc","importoInizialeFondo(0)") +
da.val("importoFondo(0)") +
da.val("importoCompartoClassePicModalitaSottoscrizione(0)") +
da.val("versamentoInizialePacModalitaSottoscrizione0") +
da.val("importoPacVersAggModalitaSottoscrizione0") +
da.val("importoPremioUnicoPIC") +
da.val("importoDoubleChanceDC") +
da.val("importoPremioUnicoPICDC") + da.val("importoDoubleChancePICDC") +
da.val("importoInizialePianoPAC") +
da.val("premioUnicoPic") +
da.val("premioInizialePac") +
da.val("importoPremioAggiuntivo") +
da.val("importoPremioUnico") +
da.val("importoUNICOConferimento") +
da.val("importoDBCHANCEConferimento") +
da.val("importoUnicoUNICODBCHANCEConferimento") + da.val("importoDbChanceUNICODBCHANCEConferimento") +
da.val("importoInizialeFondoPic(0)");', 
'S', 'Script recupero importo contestuale origine della provvista')



DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_FUTURI'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_FUTURI',
'(da.val("importoRataFondoPip(0)") * da.val("numeroRateFondoPip(0)")) +
((da.val("importoRataFondoPac(0)") * da.val("numeroRateFondoPac(0)")) - da.val("versamentoInizialeFondoPac(0)")) +
((da.val("importoRataPipSottoscrizione") * da.val("numeroRatePipSottoscrizione")) - da.val("importoInizialeFondo(0)")) +
((da.val("rataUnitariaPacSottoscrizione") * da.val("numeroRatePacSottoscrizione")) - da.val("importoInizialePacSottoscrizione")) +
((da.val("importoRataPipSottoscrizione") * da.val("numeroRatePipSottoscrizione")) - da.val("importoFondo(0)")) +
(da.val("valoreNominalePacModalitaSottoscrizione0") - da.val("versamentoInizialePacModalitaSottoscrizione0")) +
da.val("importoVersamentiSuccessiviPianoPAC") +
(da.val("valoreNominalePac") - da.val("premioInizialePac")) +
((da.val("premioUnitarioPac") * da.val("dimensionePianoPac")) - da.val("premioInizialePac")) +
((da.val("premioAnnuoPac") * da.val("durataPremio")) - da.val("premioInizialePac")) +
((da.val("premioUnitarioPac") * da.val("dimensionePianoPac"))  - da.val("premioInizialePac"));', 
'S', 'Script recupero importo futuro origine della provvista')
GO



DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_DOUBLE_CHANCE'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'SCRIPT_ORIGINE_IMPORTO_DOUBLE_CHANCE',
'da.val("importoTotalePicDc") +
da.val("importoDoubleChanceDC") +
da.val("importoDBCHANCEConferimento") +
da.val("importoDbChanceUNICODBCHANCEConferimento");', 
'S', 'Script recupero importo DC')
GO



DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'NOMI_CAMPO_NDG_TERZO_PAGATORE'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'NOMI_CAMPO_NDG_TERZO_PAGATORE',
'ndgALTROCLIENTESDDBMEDAlimentazione,ndgALTROCLIENTESDDBMEDAlimentazionePAC', 
'S', 'Nomi campo terzo pagatore')
GO

PRINT "Script AML configurati"
go


