<%@ page import="prgm.ita.anagraficaclienti.stampamoduli.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("STAMPAMODULI");
	template.setPageName("StampaModuli"); 
	template.setLabelPosition(template.LEFT_LABEL);
	template.setLabelWidth("40%");
	template.setFieldWidth("60%");

	StampaModuliModel model = (StampaModuliModel)template.getPageDataModel();	 
%>

<html>

<%=template.getHeader()%>
<style>
.gridBodyCellDisabled{
	border-color: darkgray;
	border-width: 1px;
	border-bottom-style: dotted;
	border-right-style: solid;
	padding-left: 2px;
	padding-right: 2px;
	overflow:hidden;
	color: gray;
}
</style>

<script src="/ItaAnagraficaClienti/stampamoduli/StampaModuli.js"></script>

<body>

<form name="moduloForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.stampamoduli.Modulo.execute">
<input type="hidden" name="nomeModulo" value="<%=model.getNomeModulo()%>">
<input type="hidden" name="formatoPagina" value="<%=model.getFormatoPagina()%>">
<input type="hidden" name="codMediolanum" value="">
<input type="hidden" name="codPotenziale" value="">
<input type="hidden" name="partitaIva" value="">
<input type="hidden" name="codFiscale" value="">
<input type="hidden" name="codAgente" value="">
</form>

<form name="ricercaClienti" method="post" action="call.wfem" style="margin: 0;">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.stampamoduli.StampaModuli.execute">
<input type="hidden" name="popupClientiModel_params_maxRows" value="50">
<input type="hidden" name="popupClientiModel_isPrimaVolta" value="false">
<input type="hidden" name="tipoRicerca"  value="<%=model.getTipoRicerca()%>">
<center>
<table height="100%" width="90%">
	<tr>
		<td>
		
		  <fieldset class="fieldsGroup">
		    <legend class="text" style="font-weight: bold;">
		      Stampa modulo <%=template.getProperty("nomeModulo."+model.getNomeModulo().toString())%>
		      <img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif"
		      onclick="pulisci();" style="cursor:pointer;">
		    </legend>
		
		    <center>
		
		    <table width="100%">
		      <tr>
		        <td><%=template.field("popupClientiModel_params_cognome","maxlength='40' size='35'")%></td>
		        <td><%=template.field("popupClientiModel_params_nome","maxlength='40' size='35'")%></td>
		        <td width="230px" id="tdCodMediolanum">		        							 
		        	<%=template.field("popupClientiModel_params_codMediolanum","maxlength='11' size='25'")%>
		        </td>
		      </tr>
		      <tr>
		        <td>
			      	<% if(model.getPopupClientiModel().getIsPrimaVolta().booleanValue()){ %>
			        <div style="position:relative;height: 100%;width: 100%;" id="help">
					<div style="position:absolute;top:0px;left:150px;">
					  <table class="text" cellpadding="0" cellspacing="0" width="160">
						    <tr>
							    <td rowspan="2" style="padding:3pt;background-color:yellow;border: solid 1px gray;">
							    	Selezionare i criteri di ricerca desiderati e cliccare sul pulsante
							    </td>
							    <td style="font-size:12pt;" valign="top">&#9658</td>
							</tr>
						    <tr>
							    <td>&nbsp;</td>
							</tr>
					  </table>
					</div>
			        </div>
					<% } %>
			      	<% if(model.getPopupClientiModel().getElenco().size() > 0){ %>
			        <div style="position:relative;height: 100%;width: 100%;" id="help">
					<div style="position:absolute;top:0px;left:0px;z-index:1;">
					  <table class="text" cellpadding="0" cellspacing="0" width="250">
					   <tr>
					      <td colspan="3" align="center" style="border: solid 1px gray;background-color:yellow;">
					      Selezionare il cliente desiderato   
					  per stampare il modulo <%=template.getProperty("nomeModulo."+model.getNomeModulo().toString())%>
					     
					      </td>
					   </tr>
					   <tr>
					      <td width="50%">&nbsp;</td>
					      <td width="10%" align="center" valign="top" style="font-size:12pt;">&#9660</td>
					      <td>&nbsp;</td>
					   </tr>
					 </table>
					</div>
			        </div>
					<% } %>
		        </td>
		        <td></td>
		        <td></td>
		      </tr>
		      <tr>
		        <td colspan="3"><%=template.action("ricercaAction")%></td>
		      </tr>
		    </table>
		
		    </center>
		  </fieldset>
		
		</td>
	</tr>
	<tr>
		<td height="100%" style="padding-top: 10;">
			<%  if(!model.getPopupClientiModel().getIsPrimaVolta().booleanValue()){
				
				String colonne =  "cols='";
				String dimensioni = "colswidths='";
				
				colonne += "codInforete,codMediolanum,cognome,nome,dataNascita,naturaGiuridica,";
		    	colonne += "#codPotenziale,#datiApplicativi_nomeTabella,#isCancellabile,";
		        colonne += "#codAgente,#stato,#statoProposta,#statoConfermato,#codFiscale,#partitaIva,";
		        colonne += "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,";
	           	colonne += "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#secondaIntestazione";
				
	           	dimensioni += "15%,12%,*,*,10%,10%";

	           	colonne += "' ";
			    dimensioni +="' ";
			%>
			<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			  <tr height="30%">
				  <td align="center"><%=template.grid("popupClientiModel_elenco",colonne+dimensioni+
				 		   				    			"decorator='prgm.ita.anagraficaclienti.popup.model.PopupClientiModel' "+
				 		   				    			"onnewcell='newCell(this);' "+
				 		   				    			"selection='single' "+
					                   					"height='100%' width='100%' "+
									                    "onclick='selezionaCliente(this);' ")%>
				  </td>
			  </tr>
			  <tr>
				   <td class="text" style="height: 15px;">
				   </td>
			  </tr>
			  <tr height="70%">
			  	  <td align="center" id="moduloPrintObject">
				  </td>
			  </tr>
			</table>
			<%  }  %>
		</td>
	</tr>
</table>
</center>
</form>

<script>
resetIdxClienteSelezionato();
document.ricercaClienti.popupClientiModel_params_cognome.focus();
</script>

<%=template.getFooter()%>
</body>
</html>