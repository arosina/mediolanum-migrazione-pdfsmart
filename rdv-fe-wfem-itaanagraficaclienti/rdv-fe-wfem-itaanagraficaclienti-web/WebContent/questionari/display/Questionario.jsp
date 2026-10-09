<%@ page import="prgm.ita.anagraficaclienti.questionari.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.questionari.html.adhoc.HtmlRisposteDrawer"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("Questionario");
	template.setJSCombo(false);
	template.setLabelPosition(template.LEFT_LABEL);
	
	QuestionarioModel model = (QuestionarioModel)template.getPageDataModel();
	HtmlRisposteDrawer rispDrawer = new HtmlRisposteDrawer(template, model);
%>

<html>

<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/questionari/display/include/sezioni.css'/>

<script>
var dataOraCompilazioneMsg = 'Verrà validato l&rsquo;ultimo questionario cronologicamente pervenuto. Il cliente potrebbe profilarsi autonomamente dal sito internet';
var etaCliente=<%=model.getCliente().getEta().intValue()%>;
</script>
<script src="/ItaAnagraficaClienti/questionari/display/Questionario.js"></script>

<style>
tableA{
	border-collapse:collapse;
	clear:both;	
}
.tableDato{
	border-width: 0 0 1px 0;
	border-style: solid;
	padding:2px 0 2px 5px;
	vertical-align:center;
	font-family: Arial;
	font-size: 8pt;
	color : #1A458F;	
	font-weight: bold;
}
.tableLabel{
	text-align:left;
	border-width:1px 1px 1px 0px;
	border-style:solid;
	padding:1px 0px 1px 5px;
	font-size: 8pt;
	font-family: Arial;
	color : #1A458F;
	background-color :#F0F0F0  ;
	text-align: center;

}
.tableNascosta{
display:none;
}

</style>
</head>

<body>
<form id="dati" name="dati" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="scrollPosition" value="">
<input type="hidden" name="codProfiloDiInvestimento" value="<%=model.getCodProfiloDiInvestimento() %>">
<input type="hidden" name="statoInviaProfilo" value="0">
<input type="hidden" name="alertAfterCalcoloViewed" value="false">
<input type="hidden" name="codMediolanum" value="<%= model.getCliente().getCodMediolanum()%>">
<input type="hidden" name="codPotenziale" value="<%= model.getCliente().getCodPotenziale()%>">
<input type="hidden" name="partitaIva" value="<%= model.getCliente().getPartitaIva()%>">
<input type="hidden" name="codFiscale" value="<%= model.getCliente().getCodFiscale()%>">
<table width="100%" height="90%" cellpadding="0" cellspacing="0">
<tr>
  <td height="2%">
	<table width="100%" class="text" cellpadding="0" cellspacing="2" style="background:#e1e1e1">
	  <tr>
	    <td><b>&nbsp;&nbsp;Questionario per attribuire il "Profilo dell'Investitore"</b></td>
	    <td align="right">
	      <table>
	        <tr>
	         <% if(model.isSalvato()){ %>
	        <td><%=template.action("stampaPCPAction")%></td>
	         <% } %>
	         <% if(model.getShowBack().booleanValue()){ %>
				 <td><%=template.action("gobackAction")%></td>
	         <% } %>
	         <% if(!model.isSalvato()){ %>
				 <td><%=template.action("calcolaProfiloAction","style='width:140px;'")%></td>
				 <td><%=template.action("salvaQuestionarioAction","style='width:140px;'")%></td><!--aggiunta campo disabilita per controllo tk_1006882  -->
			 <% } %>
	        </tr>
	      </table>
	    </td>
	  </tr>
	</table>
  </td>
</tr>

<tr>
  <td height="15%">
      <table width="97%"><tr><td style="padding-left: 8px;">
      <fieldset class="fieldsGroup">
       <legend class="text" style="font-weight: bold;">
		    <img src="<%=template.getWebApp()%>/images/section.gif">
			&nbsp;Cliente&nbsp;
       </legend>
	      <table width="100%">
	      
	        <%  String ditta = "";
	        	if(model.getCliente().getIsDitta().booleanValue())
	        		ditta = model.getCliente().getSecondaIntestazione()+" di ";
	        %>
	        <tr>
	          <td width="55%">
	            <table width="100%">
	              <tr><td>
					<table class="tableA" height="40" width="100%">
					  <tr class="tableLabel">
					    <td align="left">Cognome e nome</td>
					  </tr>	
					  <tr class="tableDato">
					    <td><%=ditta%><%=model.getCliente().getCognome()%> <%=model.getCliente().getNome()%></td>
					  </tr>
					</table>
	              </td></tr>
	              <tr><td>
					<table class="tableA" height="40" width="100%">
					  <tr class="tableLabel">
					    <td align="left"><%=(model.getCliente().getCodMediolanum().isNull()?"Codice cliente potenziale":"Codice cliente")%></td>
					    <td align="left">Data di nascita</td>
					  </tr>	
					  <tr class="tableDato">
					    <td><%=(model.getCliente().getCodMediolanum().isNull()?model.getCliente().getCodPotenziale().toString():model.getCliente().getCodMediolanum().toString())%></td>
					    <td><%=model.getCliente().getDataNascita()%></td>
					  </tr>
					</table>
	              </td></tr>
	           </table>
	          </td>
	          <td align="center">
	            <table width="100%">
		          <% 
		          	String bgcolor="silver";
		          	String color="black";
		          	String text = "NESSUNO";
		          	String textCluster = "NESSUNO";
		          	String textDataScadenza = "NON VALORIZZATA";
		            if(model.getCliente().getCodProfiloDiInvestimento().equals("CON")){
		            	color = "black";
		            	bgcolor = "#72CFF5";
		            	text = "CONSERVATORE";
		            }else if(model.getCliente().getCodProfiloDiInvestimento().equals("EQU")){
		            	color = "black";
		            	bgcolor = "yellow";
		            	text = "EQUILIBRATO";
		            }else if(model.getCliente().getCodProfiloDiInvestimento().equals("INT")){
		            	color = "white";
		            	bgcolor = "red";
		            	text = "INTRAPRENDENTE";
		            }
		            if(!model.getCliente().getCodClusterCedacri().isNull()){
		            	textCluster = model.getCliente().getDescrClusterCedacri().toString();
		            }else{
		            	textCluster = text;
		            }
		            if(!model.getCliente().getDataScadenzaProfiloCedacri().isNull()){
		            	textDataScadenza = model.getCliente().getDataScadenzaProfiloCedacri().toString();
		            }
		            
		            String espfina = "";
		            if(!model.getCliente().getEspfina().isNull()){
		            	if(model.getCliente().getEspfina().equalsIgnoreCase(QuestionarioModel.ESPFINA_ALTA))
		            		espfina = "Alta";
		            	else if(model.getCliente().getEspfina().equalsIgnoreCase(QuestionarioModel.ESPFINA_MEDIA))
		            		espfina = "Media";
		            	else if(model.getCliente().getEspfina().equalsIgnoreCase(QuestionarioModel.ESPFINA_BASSA))
		            		espfina = "Bassa";
		            }
		            if(espfina.length() > 0)
		            	espfina = "<br>- C.E.M.I.: "+espfina;
		          %>
	              <tr>
	              	  <td width="24%" class="text">Profilo assegnato:</td>
			          <td width="38%" class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;color:<%=color%>;background-color:<%=bgcolor%>;">
		                  <%=textCluster%><%=espfina%>
			          </td>
			          <td width="38%" class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;color:<%=color%>;background-color:<%=bgcolor%>;">
		                  <%=textDataScadenza%>
			          </td>			          
	              </tr>
		          <% 
		          	bgcolor="white";
		          	color="white";
		          	textDataScadenza = "NON VALORIZZATA";
		            if(model.getCodProfiloDiInvestimento().equals("CON")){
		            	color = "black";
		            	bgcolor = "#72CFF5";
		            }else if(model.getCodProfiloDiInvestimento().equals("EQU")){
		            	color = "black";
		            	bgcolor = "yellow";
		            }else if(model.getCodProfiloDiInvestimento().equals("INT")){
		            	color = "white";
		            	bgcolor = "red";
		            }
		            textCluster = "NESSUNO";
		            if(!model.getCodClusterCedacri().isNull()){
		            	textCluster = model.getDescrClusterCedacri().toString();
		            }
		            if(!model.getDataScadenzaProfiloCedacri().isNull()){
		            	textDataScadenza = model.getDataScadenzaProfiloCedacri().toString();
		            }	
		            
		            espfina = "";
		            if(!model.getEspfina().isNull()){
		            	if(model.getEspfina().equalsIgnoreCase("ALT"))
		            		espfina = "Alta";
		            	else if(model.getEspfina().equalsIgnoreCase("MED"))
		            		espfina = "Media";
		            	else if(model.getEspfina().equalsIgnoreCase("BAS"))
		            		espfina = "Bassa";
		            }
		            if(espfina.length() > 0)
		            	espfina = "<br>- C.E.M.I.: "+espfina;
		            
		          %>
	              <tr>
	              	  <td width="24%" class="text" <%if(model.getCodProfiloDiInvestimento().isNull()){%>style="visibility:hidden;"<%}%>>Profilo calcolato:</td>
			          <td  width="38%"class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;color:<%=color%>;background-color:<%=bgcolor%>;">
		                  <%=textCluster%><%=espfina%>
			          </td>
			          <td  width="38%"class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;color:<%=color%>;background-color:<%=bgcolor%>;">
		                  <%=textDataScadenza%>
			          </td>			          
	              </tr>
						          
	            </table>
	          </td>
	        </tr>	        
	        
	        <tr>
	        	<td colspan="2" align="center">
	        		<table class="text"><tr>
			        	<td align="right" class="text" style="background-color:lightcyan;">
			        		&nbsp;Selezionare il flag per inserire anche la riga di PRIT della scheda privacy&nbsp;
			        	</td>
			        	<td>
			        	  <%=template.field("isPrivacyAllegata","modality='"+(model.isSalvato()?"read":"insert")+"' labelposition='nolabel'")%>
			        	</td>
	        		</tr></table>
	        	</td>
	        
	        </tr>
	      
	      </table>
      </fieldset>
      </td></tr></table>
    </td>
</tr>

<tr>
	<% 
		String questionarioVisibility="visible";
	%>
   <td height="75%" style="padding-top: 10; padding-bottom: 10;">
	 <div id="questionario" style="width:100%;height:100%;overflow:auto;visibility:<%=questionarioVisibility%>;">
		<table width="100%">
		  <% model.htmlQuestionario(template); // Costruisce la struttura dei campi e volendo torna l'html standard, utile per la composizione di quello ad-hoc %> 
		  <tr>
		    <td>
			   <%@ include file="./include/SezioneA.html"%>
			   <br>
			   <%@ include file="./include/SezioneB.html"%>
			   <br>
			   <%@ include file="./include/SezioneC.html"%>
			   <br>
			   <%@ include file="./include/SezioneD.html"%>
		    </td>
		  </tr>
		</table>
	 </div>		 		 
   </td>
</tr>

<tr>
  <td height="7%">
	<%=template.getMessagesAndErrors("background-color:red;color:white;font-size:12;font-weight:bold;",null,null)%>
  </td>
</tr>

</table>
</form>

<!--------  GOTO PAGE FORM --------------->
<form name="goback" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>
<!------------------------------------------->

<%=template.getFooter()%>

<script>
setScrollPosition(<%=model.getScrollPosition()%>);
</script>

<% if(model.getMessaggioCentrale().length() > 0){ %>
		<iframe  name='divMessaggiIF' id='divMessaggiIF' src='<%=template.getWfemLayoutWebApp()%>/blankPage.html' scrolling='no' frameborder='0' style='position:absolute;left:100px;top:150px;height:200px;width:600px;'></iframe>
		<div  name='divMessaggi' id='divMessaggi' style='z-index:100;position:absolute;left:100px;top:150px;height:200px;width:600px;background-color:white;'>
		<center>
		<table width='100%' height='100%' cellpadding='10' cellspacing='0' style='background-color:#587bc4;'>
		 <tr>
		   <td align='center'>
				<table width='100%' height='100%' cellpadding='2' cellspacing='0' style='background-color:white;'>
				<tr><td align='right' style='height:20px;'><table><tr><td class='text' style='font-size:12px;cursor:pointer;text-decoration:underline;' title='Chiudi' onclick='closeMessages();'>Chiudi</td></tr></table></td></tr>
				<tr><td align='center' class='text' style='height:20px;'><b>Operazione conclusa correttamente. Attenzione ai seguenti punti:</b></td></tr>
				<tr><td align='center'>
					<table><tr><td  class='text' style='font-size:12px;'>
					<%=template.getProperty(model.getMessaggioCentrale().toString())%>
				    </td></tr></table>
				</td></tr>
				<tr><td align='right' style='height:20px;'>&nbsp;</td></tr>
				</table>
		   </td>
		 </tr>
		</table>
		</center>
		</div>
<% 		model.setMessaggioCentrale("");
	} %>
	
<% if(model.getAlertAfterCalcolo().size() > 0){ %>
<script>
openModalPopup("prgm.ita.anagraficaclienti.questionari.display.AlertAfterCalcolo",
				"BrowserInstance=<%=request.getAttribute("BrowserInstance")%>",null,alertAfterCalcoloCallback,"Avviso",300,500,true);
$(".ui-dialog-titlebar-close").hide();
</script>
<% } %>

</body>
</html>
