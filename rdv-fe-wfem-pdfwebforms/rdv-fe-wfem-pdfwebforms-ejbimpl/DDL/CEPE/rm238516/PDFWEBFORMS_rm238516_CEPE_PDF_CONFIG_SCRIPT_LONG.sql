use CEPE
go

DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_CONTESTUALI'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE_ESTESO, ATTIVO, DESCRIZIONE)
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
da.val("importoInizialeFondoPic(0)");
', 'S', 'Script recupero importo contestuale origine della provvista')



DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_FUTURI'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE_ESTESO, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_FUTURI',
'if (da.driver("fondiirlandesiiniziale")){
  if (da.equ("tipoSottoscrizione", "Pac"))
   return (da.val("rataUnitariaPacSottoscrizione") * da.val("numeroRatePacSottoscrizione")) - da.val("importoInizialePacSottoscrizione");
  if (da.equ("tipoSottoscrizione", "Pip"))
   return (da.val("importoRataPipSottoscrizione") * da.val("numeroRatePipSottoscrizione")) - da.val("importoInizialeFondo(0)");
  return 0;
}else if (da.driver("fondiirlandesiaggiuntivo")){
  if (da.equ("tipoSottoscrizione", "Pip"))
   return (da.val("importoRataPipSottoscrizione") * da.val("numeroRatePipSottoscrizione")) - da.val("importoFondo(0)");
  return 0;
}else if (da.driver("fondiitaliainiziale") || da.driver("fondiitaliaaggiuntivo")){
  if (da.equ("tipoSottoscrizione", "Pac"))
   return (da.val("importoRataFondoPac(0)") * da.val("numeroRateFondoPac(0)")) - da.val("versamentoInizialeFondoPac(0)");
  if (da.equ("tipoSottoscrizione", "Pip"))
   return (da.val("importoRataFondoPip(0)") * dal.val("numeroRateFondoPip(0)")) - da.val("importoInizialeFondoPip(0)");
  return 0; 
}else if (da.driver("fonditerziiniziale") || (da.driver("fonditerziaggiuntivo") && da.equ("versamentoPAC", "INIZIALE"))){
  if(da.equ("modalitaSottoscrizione", "PAC"))
   return da.val("valoreNominalePacModalitaSottoscrizione0 ") - da.val("versamentoInizialePacModalitaSottoscrizione0");
  return 0; 
}else if (da.driver("mylifeiniziale") || da.driver("intelligentlifeplan")){
  if (da.equ("tipoSottoscrizione", "PAC"))
   return da.val("importoVersamentiSuccessiviPianoPAC");
  return 0; 
}else if (da.driver("lifefunds") || da.driver("newgeneration") || da.driver("premiumplan")){
  if (da.equ("tipoPianoPremio", "Pac"))
   return (da.val("premioUnitarioPac") * da.val("dimensionePianoPac")) - da.val("premioInizialePac");
  return 0; 
}else if (da.driver("polizzapersonalpir")){
  if (da.equ("tipoSottoscrizione", "Pac"))
   return da.val("valoreNominalePac ") - da.val("premioInizialePac"); 
  return 0; 
}else if (da.driver("mediolanumcapitalnew")){
  if (da.equ("tipoPianoPremio", "Pac"))
    return (da.val("premioAnnuoPac") * da.val("durataPremio")) - da.val("premioInizialePac");
  return 0; 
}else if(da.driver("taxbenefitnew")){
  if (da.equ("hasContributiAderente", "S"))
    return (da.val("importoContributoAnnuo") * da.val("durataContratto")) - da.val("importoContributoIniziale");
  return 0; 
}else{
   return 0;
}
', 'S', 'Script recupero importo futuro origine della provvista')
GO



DELETE FROM PDF_CONFIG
WHERE SEZIONE = 'AML' AND PARAMETRO = 'SCRIPT_ORIGINE_IMPORTO_DOUBLE_CHANCE'

INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE_ESTESO, ATTIVO, DESCRIZIONE)
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


