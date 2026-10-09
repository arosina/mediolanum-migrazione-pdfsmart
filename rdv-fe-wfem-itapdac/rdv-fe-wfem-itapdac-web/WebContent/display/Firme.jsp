<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("Firme");
	template.setJSCombo(false);
	
	FirmeModel model = (FirmeModel)template.getPageDataModel();	
	ContoCorrenteModel contoSelezionato = model.getContoSelezionato();
	
	int numFirme = 0;
	for(int i=0;i<contoSelezionato.getFirmeDelConto().size();i++){
		FirmaModel firma = (FirmaModel)contoSelezionato.getFirmeDelConto().get(i);
	    if(!firma.getUrlFirma().isNull())
	    	numFirme++;
	}
%>
<html>

<head>
<%=template.getHeader()%>
<link rel="stylesheet" type="text/css" href="<%=template.getWfemLayoutWebApp()%>/style.css">
<script>
function ritenta(numConto){
	if(typeof(numConto) != 'undefined')
		document.retryForm.contoSelezionato_numeroConto.value = numConto;
	startRequest();
	document.retryForm.submit();
}
function doStampaFirme(){
	window.open("","stampaFirmeWindow","toolbar=no,resizable=yes");
	document.stampaFirmeForm.submit();		
}
</script>
</head>

<body style="margin:0;">

<form id="retryForm" name="retryForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.display.Firme.execute"></input>
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>"></input>
<input type="hidden" name="codMediolanum" value="<%=model.getCodMediolanum()%>"></input>
<input type="hidden" name="cognome" value="<%=model.getCognome()%>"></input>
<input type="hidden" name="nome" value="<%=model.getNome()%>"></input>
<input type="hidden" name="nominativo" value="<%=model.getNominativo()%>"></input>
<input type="hidden" name="contoSelezionato_numeroConto" value=""></input>
<input type="hidden" name="isReadonly" value="false"></input>
</form>

<% if(model.getCodMediolanum().isNull() && model.getCognome().isNull()){ //Cliente non selezionato %>

	<center>
	<table class="text" style="font-size:12;">
	  <tr><td align="center"><b>Cliente non specificato</b></td></tr>
	</table>
	</center>

<% }else if(model.getCodMediolanum().isNull()){ //Cliente potenziale %>

	<center>
	<table class="text" style="font-size:12;">
	  <tr><td align="center"><b>Cliente potenziale</b></td></tr>
	</table>
	</center>

<% }else if(model.getElencoConti().size() == 0){ //Cliente senza conti %>

	<center>
	<table class="text" style="font-size:12;">
	  <tr><td align="center"><b>Il cliente non ha conti correnti in portafoglio</b></td></tr>
	</table>
	</center>
	
<% }else if(contoSelezionato.getNumeroConto().hasTypeErrors()){ //ERRORE %>

	<center>
	<table class="text" style="font-size:12;" cellpadding="0" cellspacing="0">
	  <tr>
	    <td>
	      <table class="text" width="100%">
			<tr>
			  <td style="color:red;"><b>&nbsp;Errore nel richiamo al sistema remoto per il reperimento delle firme</b></td>
			  <td align="center">&#9658;&nbsp;<span onclick="ritenta();" style="text-decoration:underline;cursor:pointer;"><b>Clicca qui per ritentare il caricamento firme</b></span>&nbsp;&#9668;</td>
		    </tr>
	      </table>
	    </td>
	  </tr>
	  <tr><td style="height:5;"></td></tr>
	  <tr>
	    <td>
	      <table class="text" width="100%" border="1">
			  <tr><td colspan="2" style="background-color:#F0F0F0;"><b>Dati relativi alla chiamata</b></td></tr>
			  <tr><td>Codice errore</td><td><%=contoSelezionato.getSrvCode()%></td></tr>
			  <tr><td>Descrizione errore</td><td><%=contoSelezionato.getSrvMessage()%></td></tr>
			  <% if(model.isServizioFirmePerContoCorrente()){ %>
				  <tr><td>Conti Correnti sui quali &egrave; stato effettuato il tentativo</td><td> 
				  <%
	 			  		    for(int i=0;i<model.getElencoConti().size();i++){
	 			  		    ContoCorrenteModel conto = (ContoCorrenteModel)model.getElencoConti().get(i);
	 			  %>
					  <%=conto.getNumeroConto()%>
					  <%if(i < (model.getElencoConti().size()-1)){%>
					   ,&nbsp;
					  <%}%>
				  <%}%>
				  </td></tr>
			  <% } %>
	      </table>
	    </td>
	  </tr>
	</table>
	</center>
	
<% }else{ // OK %>

    <table height="100%" width="100%">
      <tr>
        <td>
      	   <table width="100%" cellpadding="0" cellspacing="0" class="text"><tr>
      	   	  <td>
      	   	  <% if(model.isServizioFirmePerContoCorrente()){ %>
      	   	  
				  <%	boolean almenoUno = false;
				  		for(int i=0;i<model.getElencoConti().size();i++){
				  		    ContoCorrenteModel conto = (ContoCorrenteModel)model.getElencoConti().get(i);
				  		    String color = "";
				  		    String chiuso = "";
				  		    if(conto.getIsChiuso().intValue() > 0){
				  		    	color = "color:red;";
				  		    	chiuso = " (Conto chiuso)";
				  		    }
				  		    String numConto = conto.getNumeroConto().toString();
				  		    String htmlNumConto = numConto;
				  		    if(contoSelezionato.getNumeroConto().equals(numConto)){
				  		    	htmlNumConto = "<span style='font-weight:bold;"+color+"'"+
				  		    		  "title='Conto selezionato"+chiuso+"' "+
				  		    		  ">&#9658;&nbsp;"+numConto+"&nbsp;&#9668;</span>";		    		
				  		    	almenoUno = true;
				  		    }else{
				  		    	htmlNumConto = "<span onclick='ritenta(\""+numConto+"\");' "+
				  		    		 "title='Clicca qui per caricare le firme di questo conto"+chiuso+"' "+
				  		    		 "style='text-decoration:underline;cursor:pointer;"+color+"'>"+numConto+"</span>";
				  			}%>
				  			
								  <% if(i == 0){ %>
								   <b><%if(model.getElencoConti().size() == 1){%>Conto corrente<%}else{%>Conti correnti<%}%>:</b>&nbsp;
								  <%}%>
								     <%=htmlNumConto%>
								  <%if(i < (model.getElencoConti().size()-1)){%>,&nbsp;<%}%>
								  
				  	<%	}%>
					<%	if(!almenoUno){%>
						  	<%if(model.getElencoConti().size() == 1){%>
							  	<b>&nbsp;&nbsp;&#9668;&nbsp;Seleziona il conto corrente per vedere le firme</b>
						  	<%}else{%>
							  	<b>&nbsp;&nbsp;&#9668;&nbsp;Seleziona uno dei conti correnti per vedere le firme</b>
						  	<%}%>
					<%	}%>
					
				<% }else{ %>
				
					&nbsp;&nbsp;
					<span onclick='ritenta("0000000000");'
				  		  title='Clicca qui per <%=(contoSelezionato.getNumeroConto().isNull() ? "caricare" : "ricaricare")%> le firme'
				  		  style='text-decoration:underline;cursor:pointer;'>
				  		  <%=(contoSelezionato.getNumeroConto().isNull() ? "Carica firme" : "Ricarica firme")%>
					</span>
					
				<% } %>
      	   	  </td>
		      <%if(numFirme > 0){%>
			  	<td align="right"><table><tr><td><%=template.action("stampaFirme","text='Stampa firme'")%></td></tr></table></td>
		      <%}%>
      	   </tr></table>
      	</td>
      </tr>
      <tr>
        <td height="100%" align="center">
        
        	<% if(!contoSelezionato.getNumeroConto().isNull() && contoSelezionato.getFirmeDelConto().size() == 0){ //Nessun documento{ %>
        	
				<table class="text" style="font-size:12;">
				  <tr><td align="center"><b>Nessun documento trovato</b></td></tr>
				</table>
				
        	<% }else{ %>
        	
			    <div style="height:100%;width:100%;overflow-y:auto;">
				  <table class="text" width="100%">
				  <%
		  		    for(int i=0;i<contoSelezionato.getFirmeDelConto().size();i++){
			  		    FirmaModel firma = (FirmaModel)contoSelezionato.getFirmeDelConto().get(i);
			  		    String urlDocumento = firma.getUrlDocumento().toString();
			  		    String urlFirma = firma.getUrlFirma().toString();
				  %>
				    <tr>
				    	<td nowrap="nowrap" align="center">
				        <%if(urlFirma.length() == 0){%>
				        	Immagine Firma non disponibile. Aprire il PDF (icona a destra)
				        <%}else{%>
				          <table width="100%" style="table-layout:fixed;"><tr><td>
							<img src="<%=urlFirma%>"  alt="Immagine in caricamento..." style="cursor:pointer;" width="100%" 
								 onclick='window.open(this.src);'>
						  </td></tr></table>
				        <%}%>
				    	</td>
				    	<td valign="top" style="padding-top:10;">
				    		<img src="<%=template.getWebApp()%>/images/acrobatLink.gif" 
				    			 style="cursor: pointer" onclick='window.open("<%=urlDocumento%>");'>
				    	</td>
				    </tr>
				    <tr><td colspan="2"><hr></td></tr>
				  <%}%>
				  </table>
				</div>
				
			<% } %>
        </td>
      </tr>
    </table>

	<form id="stampaFirmeForm" name="stampaFirmeForm" method="post" action="<%=model.getCodMediolanum()%>.wfem" style="display:none;"  target="stampaFirmeWindow">
	  <input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.display.FirmePrint.execute"></input>
	  <input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>"></input>
	  <input type="hidden" name="codMediolanum" value="<%=model.getCodMediolanum()%>"></input>
	  <input type="hidden" name="cognome" value="<%=model.getCognome()%>"></input>
	  <input type="hidden" name="nome" value="<%=model.getNome()%>"></input>
	  <input type="hidden" name="nominativo" value="<%=model.getNominativo()%>"></input>
	  <%
  	  	   int count=0;
  	  	   for(int i=0;i<contoSelezionato.getFirmeDelConto().size();i++){
  		      FirmaModel firma = (FirmaModel)contoSelezionato.getFirmeDelConto().get(i);
  		      String urlFirma = firma.getUrlFirma().toString();
  		      if(urlFirma.length() == 0)
  		    	continue;
	  %>
		  <input type="hidden" name="firmeDelConto<%=count%>_urlFirma" value="<%=urlFirma%>"></input>
	  <%      count++;
	       } %>
	</form>
	
<% } %>

<%=template.getFooter()%>
</body>
</html>
