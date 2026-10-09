<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.agenticlienti.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("ElencoAgentiCliente"); 

	RicercaGlobaleClientiModel model = (RicercaGlobaleClientiModel)template.getPageDataModel();
	int idxCliente = model.getIdxClienteSelezionato().intValue();
	ClienteModel cliente = (ClienteModel)model.getElenco().get(idxCliente);
	String descrCliente = "Elenco degli agenti di:<br>";
	descrCliente += "&nbsp;"+cliente.getCodMediolanum()+" - "+cliente.getCognome()+" "+cliente.getNome()+
					 " Nato/a il "+cliente.getDataNascita();
	descrCliente = descrCliente.replaceAll("\\'","&rsquo;");
%>

<html>

<%=template.getHeader()%>

<body>
<%
	String colonne = "cols='codAgente,nominativoAgente,dataInizioAssegnazione,dataFineAssegnazione' ";
	String dimensioni = "colswidths='12%,*,12%,12%' ";
%>
  <table height="100%" width="100%" cellpadding="0" cellspacing="0">
	 <tr height="100%">
	  <td>
	  	<%=template.grid("elencoAgentiCliente",colonne+dimensioni+
												"title='"+descrCliente+"' "+
												"pdf='true' "+
					  							"decorator='prgm.ita.anagraficaclienti.agenticlienti.RicercaGlobaleClientiModel' "+
												"selection='none' "+
												"height='100%' width='100%'")%>
	  </td>
	 </tr>
	 <tr height="3"><td></td></tr>
	 <tr>
	   <td class="text" align="center" valign="middle">
	    <% if(model.getElencoAgentiCliente().size() > 0){ %>
	    	<table class="text">
	    		<tr><td>L'agente corrente &egrave; evidenziato dal colore 
	    		<span style='border:solid 1px gray;background-color:lightgreen;'>&nbsp;&nbsp;&nbsp;</span></td></tr>
	    		<tr><td>Clicca sull'icona <img src="<%=template.getWfemLayoutWebApp()%>/images/pdf.gif"> per ottenere il formato stampa</b></td></tr>
	    	</table>
	   	<% }else{ %>
		   	&nbsp;
	   	<% } %>
	   </td>
	 </tr>
  </table>

<%=template.getFooter()%>
</body>

<script>
parent.stopRequest();
</script>

</html>