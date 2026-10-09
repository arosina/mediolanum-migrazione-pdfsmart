<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("RicercaClienti");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
%>

<html>

<script src="/ItaAnagraficaClienti/popup/popup.js"></script>

<script>
function doPopupQuestionario(){
	if(document.datiInput.codAgente.value == ""){
		alert("Inserire il codice agente");
		return;
	}
	if(document.datiInput.codPotenziale.value == "" &&
	   document.datiInput.codMediolanum.value == ""){
		alert("Inserire il codice potenziale o Mediolanum del cliente");
		return;
	}
	showPopupQuestinario(document.datiInput.codAgente.value,
						 document.datiInput.codPotenziale.value,
						 document.datiInput.codMediolanum.value);
}
function doPopupAgenti(){
	var age = showPopupAgenti('P', document.datiInput.codAgente.value, 
								   document.datiInput.cognomeAgente.value);
	if(age != null)
		setAge(age);
}
function doPopupAgentiGBL(){
	var age = showPopupGlobalSpecialist('P', document.datiInput.codAgente.value, 
								   		document.datiInput.cognomeAgente.value);
	if(age != null)
		setAge(age);
}
function doPopupClienti(){
	var cli = showPopupClienti('P', document.datiInput.codAgente.value, 
									document.datiInput.codMediolanum.value,
									document.datiInput.cognome.value);
	if(cli != null)
		setCli(cli);
}
function doPopupPrincipali(){
	if(document.datiInput.codAgente.value == ""){
		alert("Inserire il codice agente");
		return;
	}
	var cli = showPopupClientiPrimari('P', document.datiInput.codAgente.value,
										     document.datiInput.codMediolanum.value,
										     document.datiInput.cognome.value,
										     document.datiInput.tipoElementi.value);
	if(cli != null)
		setCli(cli);
}
function doPopupSecondari(){
	if(document.datiInput.codAgente.value == ""){
		alert("Inserire il codice agente");
		return;
	}
	var cli = showPopupClientiSecondari('P',document.datiInput.codAgente.value,
									       		document.datiInput.codMediolanum.value,
									       		document.datiInput.cognome.value,
									       		document.datiInput.tipoElementi.value);
	if(cli != null)
		setCli(cli);
}
function doPopupCliente(){
	if(document.datiInput.codAgente.value == ""){
		alert("Inserire il codice agente");
		return;
	}
	if(document.datiInput.codPotenziale.value == "" &&
	   document.datiInput.codMediolanum.value == ""){
		alert("Inserire il codice potenziale o Mediolanum del cliente");
		return;
	}
	showPopupAnagraficaCliente(document.datiInput.codAgente.value,
							   document.datiInput.codPotenziale.value,
							   document.datiInput.codMediolanum.value);
}

function setAge(age){
	clearAge();
	var o = document.datiOutputAge;
	o.codAgente.value = age.codAgente;
	o.cognomeAgente.value = age.cognomeAgente;
	o.nomeAgente.value = age.nomeAgente;
	o.areaAgente.value = age.areaAgente;
	o.serverReplica.value = age.serverReplica;
	o.codAgenzia.value = age.codAgenzia;
	o.descrAgenzia.value = age.descrAgenzia;
	o.codProvincia.value = age.codProvincia;
	o.codiceContrattoAgente.value = age.codiceContrattoAgente;
	o.cicloVitaAgente.value = age.cicloVitaAgente;
}
function clearAge(){
	var o = document.datiOutputAge;
	o.codAgente.value = "";
	o.cognomeAgente.value = "";
	o.nomeAgente.value = "";
	o.areaAgente.value = "";
	o.serverReplica.value = "";
	o.codAgenzia.value = "";
	o.descrAgenzia.value = "";
	o.codProvincia.value = "";
	o.codiceContrattoAgente.value = "";
	o.cicloVitaAgente.value = "";
}

function setCli(cli){
	clearCli();
	var o = document.datiOutputCli;
	o.codAgente.value = cli.codAgente;
	o.codInforete.value = cli.codInforete;
	o.codPotenziale.value = cli.codPotenziale;
	o.codMediolanum.value = cli.codMediolanum;
	o.cognome.value = cli.cognome;
	o.nome.value = cli.nome;
	o.dataNascita.value = cli.dataNascita;
	o.sesso.value = cli.sesso;
	o.codFiscale.value = cli.codFiscale;
	o.partitaIva.value = cli.partitaIva;
	o.codCluster.value = cli.codCluster;
	o.descrCluster.value = cli.descrCluster;
	o.secondaIntestazione.value = cli.secondaIntestazione;
	o.isTopBusiness.value = cli.isTopBusiness;
	o.stato.value = cli.stato;
	o.statoProposta.value = cli.statoProposta;
	o.statoConfermato.value = cli.statoConfermato;
	o.naturaGiuridica.value = cli.naturaGiuridica;
	o.isDitta.value = cli.isDitta;

	if(cli.agente)
		setAge(cli.agente);	
}
function clearCli(){
	var o = document.datiOutputCli;
	o.codAgente.value = "";
	o.codInforete.value = "";
	o.codPotenziale.value = "";
	o.codMediolanum.value = "";
	o.cognome.value = "";
	o.nome.value = "";
	o.dataNascita.value = "";
	o.sesso.value = "";
	o.codFiscale.value = "";
	o.partitaIva.value = "";
	o.codCluster.value = "";
	o.descrCluster.value = "";
	o.secondaIntestazione.value = "";
	o.isTopBusiness.value = "";
	o.stato.value = "";
	o.statoProposta.value = "";
	o.statoConfermato.value = "";
	o.naturaGiuridica.value = "";
	o.isDitta.value = "";

	clearAge();	
}
function doPopupIndirCliente(){
	if(document.datiInput.codAgente.value == ""){
		alert("Inserire il codice agente");
		return;
	}
	if(document.datiInput.codPotenziale.value == "" &&
	   document.datiInput.codMediolanum.value == ""){
		alert("Inserire il codice potenziale o Mediolanum del cliente");
		return;
	}
	var ind = showPopupIndirizziCliente(document.datiInput.codAgente.value,
										document.datiInput.codPotenziale.value,
										document.datiInput.codMediolanum.value);
}
</script>

<body>

<form name="datiInput">

<table width="100%">

  <tr>
    <td align="center">
    	<fieldset><legend>Agente</legend>
    	<table width="100%">
    	  <tr>
			<td>Codice:&nbsp;<input type="text" name="codAgente" value=""></td>
			<td>Cognome:&nbsp;<input type="text" name="cognomeAgente" value=""></td>
		  </tr>
		</table>
		</fieldset>
  </tr>
  <tr>
    <td align="center">
    	<fieldset><legend>Cliente</legend>
    	<table width="100%">
    	  <tr>
			<td>Tipo Elementi:&nbsp;<input type="text" name="tipoElementi" value=""></td>
			<td></td>
			<td></td>
    	  </tr>
    	  <tr>
			<td>Cod. potenziale:&nbsp;<input type="text" name="codPotenziale" value=""></td>
			<td>Cod. Mediolanum:&nbsp;<input type="text" name="codMediolanum" value=""></td>
			<td>Cognome:&nbsp;<input type="text" name="cognome" value=""></td>
		  </tr>
		</table>
		</fieldset>
  </tr>
  <tr>
    <td align="center">
    	<table>
    	  <tr>
		  	<td><input type="button" value="Questionario" onclick="doPopupQuestionario();"></td>
		  	<td><input type="button" value="Agenti" onclick="doPopupAgenti();"></td>
		  	<td><input type="button" value="Clienti principali Agente" onclick="doPopupPrincipali();"></td>
		  	<td><input type="button" value="Clienti secondari Agente" onclick="doPopupSecondari();"></td>
		 </tr>
		 <tr>
		  	<td><input type="button" value="Clienti mediolanum" onclick="doPopupClienti();"></td>
		  	<td><input type="button" value="Anag. Cliente" onclick="doPopupCliente();"></td>
		  	<td><input type="button" value="Indir Cliente" onclick="doPopupIndirCliente();"></td>
		  	<td><input type="button" value="Agenti GBL" onclick="doPopupAgentiGBL();"></td>
    	  </tr>
	    </table>
    </td>
  </tr>
</table>
</form>
<hr>
<table width="100%" style="font-size: 8pt;">
<tr><td>Agente</td><td>Cliente</td></tr>
<tr>
 <td>
	<form name="datiOutputAge">
	<table border="1" style="font-size: 8pt;">
	  <tr><td>codAgente</td><td><input type"text" readonly name="codAgente"></td></tr>
	  <tr><td>cognomeAgente</td><td><input type"text" readonly name="cognomeAgente"></td></tr>
	  <tr><td>nomeAgente</td><td><input type"text" readonly name="nomeAgente"></td></tr>
	  <tr><td>areaAgente</td><td><input type"text" readonly name="areaAgente"></td></tr>
	  <tr><td>serverReplica</td><td><input type"text" readonly name="serverReplica"></td></tr>
	  <tr><td>codAgenzia</td><td><input type"text" readonly name="codAgenzia"></td></tr>
	  <tr><td>descrAgenzia</td><td><input type"text" readonly name="descrAgenzia"></td></tr>
	  <tr><td>codProvincia</td><td><input type"text" readonly name="codProvincia"></td></tr>
	  <tr><td>codiceContrattoAgente</td><td><input type"text" readonly name="codiceContrattoAgente"></td></tr>
	  <tr><td>cicloVitaAgente</td><td><input type"text" readonly name="cicloVitaAgente"></td></tr>
	</table>
	</form>
 </td>
 <td>
	<form name="datiOutputCli">
	<table border="1"><tr><td><table style="font-size: 8pt;">
	  <tr><td>naturaGiuridica</td><td><input type"text" readonly name="naturaGiuridica"></td></tr>
	  <tr><td>codAgente</td><td><input type"text" readonly name="codAgente"></td></tr>
	  <tr><td>codInforete</td><td><input type"text" readonly name="codInforete"></td></tr>
	  <tr><td>codPotenziale</td><td><input type"text" readonly name="codPotenziale"></td></tr>
	  <tr><td>codMediolanum</td><td><input type"text" readonly name="codMediolanum"></td></tr>
	  <tr><td>cognome</td><td><input type"text" readonly name="cognome"></td></tr>
	  <tr><td>nome</td><td><input type"text" readonly name="nome"></td></tr>
	  <tr><td>dataNascita</td><td><input type"text" readonly name="dataNascita"></td></tr>
	  <tr><td>sesso</td><td><input type"text" readonly name="sesso"></td></tr>
	  <tr><td>codFiscale</td><td><input type"text" readonly name="codFiscale"></td></tr>
	</table></td><td><table style="font-size: 8pt;">
	  <tr><td>isDitta</td><td><input type"text" readonly name="isDitta"></td></tr>
	  <tr><td>partitaIva</td><td><input type"text" readonly name="partitaIva"></td></tr>
	  <tr><td>codCluster</td><td><input type"text" readonly name="codCluster"></td></tr>
	  <tr><td>descrCluster</td><td><input type"text" readonly name="descrCluster"></td></tr>
	  <tr><td>secondaIntestazione</td><td><input type"text" readonly name="secondaIntestazione"></td></tr>
	  <tr><td>isTopBusiness</td><td><input type"text" readonly name="isTopBusiness"></td></tr>
	  <tr><td>stato</td><td><input type"text" readonly name="stato"></td></tr>
	  <tr><td>statoProposta</td><td><input type"text" readonly name="statoProposta"></td></tr>
	  <tr><td>statoConfermato</td><td><input type"text" readonly name="statoConfermato"></td></tr>
	</table></td></tr>
	</table>
	</form>
 </td>
</tr>
</table>

</body>
</html>
