<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.agenticlienti.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("RicercaGlobaleClienti"); 
	template.setLabelPosition(template.LEFT_LABEL);
	template.setLabelWidth("40%");
	template.setFieldWidth("60%");

	RicercaGlobaleClientiModel model = (RicercaGlobaleClientiModel)template.getPageDataModel();
%>

<html>

<%=template.getHeader()%>

<script src="/ItaAnagraficaClienti/agenticlienti/RicercaGlobaleClienti.js"></script>

<body>

<form name="ricercaClienti" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.agenticlienti.RicercaGlobaleClienti.execute">
<input type="hidden" name="idxClienteSelezionato" value="<%=model.getIdxClienteSelezionato()%>">
<center>
<table width="90%" height="100%">
	<tr>
		<td>
		
		  <fieldset class="fieldsGroup">
		    <legend class="text" style="font-weight: bold;">
		      Visualizzazione degli agenti di un cliente
		      <img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif"
		      onclick="pulisci();" style="cursor: pointer;">
		    </legend>
		    <table width="100%">
		      <tr>
		        <td colspan="3" style="height: 5px;"></td>
		      </tr>
		      <tr>
		        <td><%=template.field("params_codMediolanum","type='num' maxlength='11' size='20'")%></td>
		        <td><%=template.field("params_cognome","maxlength='40' size='40'")%></td>
		        <td><%=template.field("params_nome","maxlength='40' size='40'")%></td>
		      </tr>
		      <tr>
		        <td>
		        </td>
		        <td colspan="2">
			      	<% if(model.isPrimaAttivazione()){ %>
			        <div style="position:relative;height: 100%;width: 100%;" id="help">
					<div style="position:absolute;top:0px;left:0px;z-index:1;">
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
			      	<% if(model.getElenco().size() > 0 && model.getElencoAgentiCliente().size() == 0){ %>
			        <div style="position:relative;height: 100%;width: 100%;" id="help">
					<div style="position:absolute;top:15px;left:-200px;z-index:1;">
					  <table class="text" cellpadding="0" cellspacing="0" width="250">
					   <tr>
					      <td colspan="3" align="center" style="border: solid 1px gray;background-color:yellow;">
					        Selezionare il cliente desiderato per visualizzare nella parte sottostante l'elenco degli agenti
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
		      </tr>
		      <tr>
		        <td colspan="3"><%=template.action("eseguiRicerca")%></td>
		      </tr>
		    </table>
		  </fieldset>
		
		</td>
	</tr>
	
	<tr height="100%">
		<td>	
		<%  if(!model.isPrimaAttivazione()){
		
				String colonne = "cols='codMediolanum,cognome,dataNascita,codFiscale,partitaIva,comuneNascita_comune' ";
				String dimensioni = "colswidths='10%,*,10%,16%,10%,15%' ";
			
				String colonneAgenti = "cols='dataInizioAssegnazione,dataFineAssegnazione,codAgente' ";
				String dimensioniAgenti = "colswidths='*,*,*' ";
		%>
		<table width="100%" height="100%">
			 <tr height="50%">
				  <td>
				  	<%=template.grid("elenco",colonne+dimensioni+
												"title='Elenco dei clienti che soddisfano la ricerca' "+
					  							"decorator='prgm.ita.anagraficaclienti.agenticlienti.RicercaGlobaleClientiModel' "+
				               					"selection='single' "+
				               					"height='100%' width='100%' "+
							                    "onclick='selezionaCliente(this);' ")%>
				  </td>
			 </tr>
			 <tr height="3"><td></td></tr>
			 <tr height="50%">
			  	  <td>
			  	  	<iframe src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" name="elencoAgentiCliente" id="elencoAgentiCliente"
			  	       		  width="100%" height="100%" frameborder="0" scrolling="no"></iframe>
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
document.ricercaClienti.params_codMediolanum.focus();
</script>

<%=template.getFooter()%>
<% model.setPrimaAttivazione(false); %>
</body>
</html>