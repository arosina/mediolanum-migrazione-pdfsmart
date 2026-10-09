<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AllegaFotoClienti");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%>
<script src="/ItaAnagraficaClienti/allegafoto/AllegaFotoClienti.js"></script>
<style>
.mtory{
	display: none;
}
.mtoryscoring{
	display: none;
}
</style>
</head>

<body>

<form name="dati" method="post" action="call.wfem" target="cliente" style="display: none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="clienteSelezionato_codFiscale" value="">
<input type="hidden" name="clienteSelezionato_codAgente" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="ricercaClienti" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.allegafoto.AllegaFotoClienti.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="params_maxRows" value="50">
<input type="hidden" name="isPrimaVolta" value="false">
<input type='hidden' name='index' id='index'>
<input type='hidden' name='listPropertyName' id='listPropertyName'>
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<center>
<table width="95%" height="100%">
	<tr>
		<td>
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/images/section.gif">
				&nbsp;Invio in sede delle fotografie dei clienti&nbsp;
				<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
				     onclick="pulisci();" style="cursor: pointer;">
			</legend>
			<table width="100%"> 
			  <tr>
			     <td colspan="3" style="height: 5"></td>
			  </tr>
			  <tr>
			     <td><%=template.field("params_cognome","labelwidth='40%' size='35'  maxlength='40' ")%></td>
			     <td><%=template.field("params_nome","labelwidth='40%' size='35'  maxlength='40' ")%></td>
			     <td><%=template.field("params_codMediolanum","maxlength='16' labelwidth='40%' size='25'")%></td>
			  </tr>
			  <tr>
			     <td colspan="3" style="height: 5"></td>
			  </tr>   
			  <tr>
			    <td colspan="3">
					<%=template.action("ricercaAction","style='width:60px;'")%>  
			    </td>
			  </tr>
			  <tr>
			     <td colspan="3" style="height: 5"></td>
			  </tr>   
			</table>
		</fieldset>
		</td>
	</tr>
	<tr height="100%">
		<td>
			<%
				String colonne =  "cols='#codPotenziale,codMediolanum,cognome,nome,codFiscale,dataNascita,";
				       colonne += "#datiApplicativi_nomeTabella,#isCancellabile,";
				       colonne += "#codAgente,#stato,#statoProposta,#statoConfermato,#partitaIva,";
				       colonne += "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,";
				       colonne += "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#secondaIntestazione' ";
				String dimensioni = "colswidths='12%,*,*,15%,10%' ";
			    if(!model.getIsPrimaVolta().booleanValue()){ %>
					<table width="100%" height="100%">
					   <tr height="200">	     
					     <td>
							<%=template.grid("elenco", 
							   				   colonne+dimensioni+
							   				   "decorator='prgm.ita.anagraficaclienti.popup.model.PopupClientiModel' "+
							                   "selection='single' pageformname='ricercaClienti' height='100%' width='100%' "+
							                   "onclick='selCli(this);'")%>
						</td>
					   </tr>
					    <%if(model.getElenco().size() > 0){%>
							<tr><td align="center" class="text"><b>Seleziona il cliente desiderato</b></td></tr>
				        <%}%>
					   <tr height="100%">	     
						<td>
						    <iframe src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" name="cliente" id="cliente"
				  	       		    width="100%" height="100%" frameborder="0" scrolling="no">
				  	        </iframe>			
						</td>
					   </tr>
					</table>
			  <%}%>
		</td>
	</tr>
</table>
</form>
</center>

<script>
document.ricercaClienti.params_cognome.focus();
</script>

<%=template.getFooter()%>
</body>
</html>
