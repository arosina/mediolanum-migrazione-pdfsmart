<%@ page import="prgm.ita.anagraficaclienti.questionari.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("Patrimonio");
	template.setJSCombo(false);
	template.setLabelPosition(template.LEFT_LABEL);
	
	PatrimonioModel model = (PatrimonioModel)template.getPageDataModel();
	
	String disabilita="";
	String questionarioVisibility="visible";
	if(!model.getDatiClienteInsufficientiMsg().equals("")){
		disabilita=" disabled='disabled' style=' cursor: default;'";//evolutive tk_1006882
		questionarioVisibility="hidden"; 
	}

%>

<html>

<head>
<%=template.getHeader()%>
<script>
var dataOraCompilazioneMsg = 'Verrà validato l&rsquo;ultimo questionario cronologicamente pervenuto. Il cliente potrebbe profilarsi autonomamente dal sito internet';
var isDichiarazioneANonRispondereMsg = 'Da compilare solo nel caso in cui il cliente non abbia risposto alle domande del questionario, ma abbia invece firmato la dichiarazione nell&rsquo;apposito '+
									   'riquadro. In quel caso il questionario cartaceo NON dovr&agrave; riportere alcuna risposta. '+
									   'Con questa modalit&agrave; verr&agrave; applicata una profilatura d&rsquo;ufficio con criteri prudenziali.';
var etaCliente=<%=model.getCliente().getEta().intValue()%>;

</script>
<script src="/ItaAnagraficaClienti/questionari/display/Patrimonio.js"></script>

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
<form name="dati" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="scrollPosition" value="">
<input type="hidden" name="CodProfiloDiInvestimento" value="<%=model.getCodProfiloDiInvestimento() %>">
<input type="hidden" name="statoInviaProfilo" value="0">
<table width="100%" height="90%" cellpadding="0" cellspacing="0">
<tr>
  <td height="2%">
	<table width="100%" class="text" cellpadding="0" cellspacing="2" style="background:#e1e1e1">
	  <tr>
	    <td><b>&nbsp;&nbsp;Questionario per valorizzare il patrimonio detenuto presso terzi</b></td>
	    <td align="right">
	      <table>
	        <tr>
	         <% if(model.getShowBack().booleanValue()){ %>
				 <td><%=template.action("gobackAction")%></td>
	         <% } %>
	         <% if(!model.isSalvato()){ %>
				 <td><%=template.action("salvaPatrimonioAction",disabilita+"style='width:140px;'")%></td>
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
	          <td width="60%">
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
	            <table width="80%">
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
		          %>
	              <tr>
	              	  <td width="30%" class="text">Profilo assegnato:</td>
			          <td width="35%" class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;background-color:<%=bgcolor%>;">
		                  <font color="<%=color%>"><%=textCluster%></font>
			          </td>
			          <td width="35%" class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;background-color:<%=bgcolor%>;">
		                  <font color="<%=color%>"><%=textDataScadenza%></font>
			          </td>			          
	              </tr>
		          <% 
		          	bgcolor="white";
		          	color="white";
		          	//text = "NESSUNO";
		          	textDataScadenza = "NON VALORIZZATA";
		            if(model.getCodProfiloDiInvestimento().equals("CON")){
		            	color = "black";
		            	bgcolor = "#72CFF5";
		            	//text = "CONSERVATORE";
		            }else if(model.getCodProfiloDiInvestimento().equals("EQU")){
		            	color = "black";
		            	bgcolor = "yellow";
		            	//text = "EQUILIBRATO";
		            }else if(model.getCodProfiloDiInvestimento().equals("INT")){
		            	color = "white";
		            	bgcolor = "red";
		            	//text = "INTRAPRENDENTE";
		            }
		            textCluster = "NESSUNO";
		            if(!model.getCodClusterCedacri().isNull()){
		            	textCluster = model.getDescrClusterCedacri().toString();
		            }
		            if(!model.getDataScadenzaProfiloCedacri().isNull()){
		            	textDataScadenza = model.getDataScadenzaProfiloCedacri().toString();
		            }	
		          %>
	              <tr>
	              	  <td width="30%" class="text" <%if(model.getCodProfiloDiInvestimento().isNull()){%>style="visibility:hidden;"<%}%> >Profilo calcolato:</td>
			          <td  width="35%"class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;background-color:<%=bgcolor%>;">
		                  <font color="<%=color%>"><%=textCluster%></font>
			          </td>
			          <td  width="35%"class="text" align="center" valign="middle" style="font-weight: bold;padding:10px;background-color:<%=bgcolor%>;">
		                  <font color="<%=color%>"><%=textDataScadenza%></font>
			          </td>			          
	              </tr>
	            </table>
	          </td>
	        </tr>
	        <% if(model.getDatiClienteInsufficientiMsg().equals("")){%>
	        <tr>
	          <td colspan="2" align="center">
		        <%
		        	String checkVisibility = "visible";
		        %>
	            <table>
	               <tr>
	                 <td>
	                 	<table><tr>
	                 		<td>
	                 			<%=template.field("dataOraCompilazione","noseconds='true' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
	                 		</td>
	                 		<td>
			                 <% if(!model.isSalvato()){ %>
			                 	<img title="Aiuto" id="dataOraCompilazioneHlpmsg"  align="middle" 
										src="<%=template.getWfemLayoutWebApp()%>/images/typeMessage.png" 
									    onclick="showMessageHelper('dataOraCompilazioneHlpmsg',dataOraCompilazioneMsg);" style="cursor:pointer">
			                 <% } %>
	                 		</td>
	                 	</tr></table>
	                 </td>
	                 <td width="20%">
	                      <%=template.field("numSched","onchange='padLeft(this,\"0\");' onfocus='showHelpNumSched();' onblur='hideHelpNumSched();' style='width:62;' labelwidth='50%' type='num' maxlength='9' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
	                 </td>
	                 <td>
	                 <% if(!model.isSalvato()){ %>
				        <div style="position:relative;height: 100%;width: 100%;visibility:hidden;" id="helpNumSched">
						<div style="position:absolute;top:-37px;left:-240px;z-index:1;">
						  <table class="text" cellpadding="0" cellspacing="0" width="175">
							    <tr>
								    <td style="padding:3pt;background-color:yellow;border: solid 1px gray;">
								    	Attenzione ! Non digitare il trattino
								    </td>
								</tr>
							    <tr>
								    <td style="font-size:12pt;" valign="top" align="right">&#9660;</td>
								</tr>
						  </table>
						</div>
				        </div>
	                 <% } %>
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
	      <%} %>
	      </table>
	      
      </fieldset>
      </td></tr></table>
    </td>
</tr>
<% if(!model.getDatiClienteInsufficientiMsg().equals("")){%>
<tr>
   <td height="75%" style="padding-top: 10; padding-bottom: 10;">
	 <div id="datiCliInsuff" style="width:100%;height:100%;overflow:auto;">
		<table width="100%">
			<tr>
				<td>
					<table width='100%' class='text'>
						<tr>
							<td  class='text' style='font-size:12px;' align="center">
								<%=template.getProperty(model.getDatiClienteInsufficientiMsg().toString())%>
						    </td>
						</tr>		
					</table>
				</td>
			</tr>
		</table>
	</div>
  </td>	
 </tr> 	
<%} %>
<tr>
   <td height="75%" style="padding-top: 10; padding-bottom: 10;">
	 <div id="patrimonio" style="width:100%;height:100%;overflow:auto;visibility:<%=questionarioVisibility%>;">
		<table width="100%">
		  <tr>
		    <td>
		    	<table width='100%' class='text' id='sezionePatrimonio'>
		    		<tr>
		    			<td>
		    				<fieldset class='fieldsGroup'>
		    					<table width='100%' class='text'>
		    						<tr>
		    							<td>
		    							   	<table width='100%' class='text'>
		    							   		<tr>
		    							   			<td>
			    										<table class='text' width='100%'>
			    											<tr><td colspan="3">Per ciascuna classe di rischio (BASSO MEDIO, ALTO) riportato nella tabella sottostante, Le chiediamo di indicare l'importo del patrimonio mobiliare prontamente liquidabile da Lei detenuto presso altri intermediari, suddividendo per orizzonte temporale dell'investimento (Breve, Medio, Lungo). Sottolineiamo che negli importi indicati non devono essere considerati ne' il patrimonio immobiliare ne' gli strumenti finanziari "illiquidi" (ovvero quegli investimenti che non possono essere liquidati prontamente e ad un prezzo significativo).</td></tr>
			    											<tr><td></td></tr>
			    											<tr>
																<td width='60%' style='padding-left:10px;'><b> Grado di rischio</b></td> <td width='20%'><b>Orizzonte temporale</b></td> <td width='20%'><b>Importo investito</b></td>
															</tr>
														</table>				
														<table width='100%'><tr><td style='background-color:silver;line-height:1px;'></td></tr></table>
		
														<!-- Prima domanda di tre -->
														<table width='100%' class='text' style='table-layout:fixed;' id='domanda11'>
															<tr>  
																<td width='60%' valign='top'>    
																	<table class='text' width='100%' cellpadding='10'>
																		<tr>      
																			<td>BASSO) Ad esempio: Liquidita' in euro, Fondi monetari in euro, titoli obbligazionari in euro, Fondi obbligazionari in euro, Polizze Unit Linked, Fondi interni obbligazionari.
																			</td>    
																		</tr>
																	</table>  
																</td>  
																<td width='40%' valign='top'>    
																	<table class='text' width='100%'  >
																		<tr>
																			<td>      
																				<table width='100%' class='text' cellspacing='0' cellpadding='0' id='domanda11'>
																					<tr>
																						<td>
																							<table width='100%' class='text' cellspacing='0' cellpadding='0'>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Breve (fino a 3 anni)</td>
																												<td width='30%'><div id='risposte17_rispostaModel_numSeleField' name='risposte17_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																															<tr><td id='risposte17_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo1","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																															</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Medio (3 - 10 anni)</td>
																												<td width='30%'><div id='risposte18_rispostaModel_numSeleField' name='risposte18_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte18_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo2","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>    
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Lungo (oltre 10 anni)</td>
																												<td width='30%'><div id='risposte19_rispostaModel_numSeleField' name='risposte19_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte19_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo3","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								    
																							</table>
																						</td>    					
																					</tr>
																				</table>
																			</td>
																		</tr>		
																	</table>
																</td>	
															</tr>
														</table>		
														<!-- FINE Prima domanda di tre -->
														<br>
														<!-- Seconda domanda di tre -->
														<table width='100%' class='text' style='table-layout:fixed;' id='domanda12'>
															<tr>  
																<td width='60%' valign='top'>    
																	<table class='text' width='100%' cellpadding='10'>
																		<tr>      
																			<td>MEDIO) Ad esempio: Titoli obbligazionari in valuta, Titoli strutturati e certificates protetti, Fondi obbligazionari in valuta, Fondi bilanciati, Polizze Unit Linked, Fondi interni bilanciati o obbligazionari in valuta. Polizze Index Linked protette.
																			</td>    
																		</tr>
																	</table>  
																</td>  
																<td width='40%' valign='top'>    
																	<table class='text' width='100%'  >
																		<tr>
																			<td>      
																				<table width='100%' class='text' cellspacing='0' cellpadding='0' id='domanda11'>
																					<tr>
																						<td>
																							<table width='100%' class='text' cellspacing='0' cellpadding='0'>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Breve (fino a 3 anni)</td>
																												<td width='30%'><div id='risposte20_rispostaModel_numSeleField' name='risposte20_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																															<tr><td id='risposte20_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo4","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																															</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Medio (3 - 10 anni)</td>
																												<td width='30%'><div id='risposte21_rispostaModel_numSeleField' name='risposte21_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte21_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo5","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>    
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Lungo (oltre 10 anni)</td>
																												<td width='30%'><div id='risposte22_rispostaModel_numSeleField' name='risposte22_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte22_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo6","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								    
																							</table>
																						</td>    					
																					</tr>
																				</table>
																			</td>
																		</tr>		
																	</table>
																</td>	
															</tr>
														</table>		
														<!-- FINE seconda domanda di tre -->														
														<br>
														<!-- Terza domanda di tre -->
														<table width='100%' class='text' style='table-layout:fixed;' id='domanda13'>
															<tr>  
																<td width='60%' valign='top'>    
																	<table class='text' width='100%' cellpadding='10'>
																		<tr>      
																			<td>ALTO) Ad esempio: Liquidita' (conti correnti e depositi) in valuta, Operazione a termine in valuta, Titoli azionari, Fondi azionari, flessibili o hedge, Titoli strutturati e certificates non protetti, Polizze Index Linked non protette. Fondi immobiliari.
																			</td>    
																		</tr>
																	</table>  
																</td>  
																<td width='40%' valign='top'>    
																	<table class='text' width='100%'  >
																		<tr>
																			<td>      
																				<table width='100%' class='text' cellspacing='0' cellpadding='0' id='domanda11'>
																					<tr>
																						<td>
																							<table width='100%' class='text' cellspacing='0' cellpadding='0'>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Breve (fino a 3 anni)</td>
																												<td width='30%'><div id='risposte23_rispostaModel_numSeleField' name='risposte23_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																															<tr><td id='risposte23_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo7","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																															</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Medio (3 - 10 anni)</td>
																												<td width='30%'><div id='risposte24_rispostaModel_numSeleField' name='risposte24_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte24_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo8","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>    
																								<tr>        
																									<td>
																										<table class='text' width='100%' cellspacing='0' cellpadding='0' >
																											<tr>
																												<td width='50%'>Lungo (oltre 10 anni)</td>
																												<td width='30%'><div id='risposte25_rispostaModel_numSeleField' name='risposte25_rispostaModel_numSeleField' style='margin:0;width:100%;'>
																													<center>
																														<table width='100%' border='0' cellpadding='0' cellspacing='0'>
																														<tr><td id='risposte25_rispostaModel_numSeleLabel' style='display:none;'></td>
																															<td ><%=template.field("elementiPatrimonio_importo9","type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+(model.isSalvato()?"read":"insert")+"'")%>
																															</td>
																														</tr>
																														</table>
																													</center></div>
																												</td>
																												<td width='20%'>,00 &euro; </td>
																											</tr>
																										</table>
																									</td>        
																								</tr>        
																								    
																							</table>
																						</td>    					
																					</tr>
																				</table>
																			</td>
																		</tr>		
																	</table>
																</td>	
															</tr>
														</table>		
														<!-- FINE terza domanda di tre -->																	
																												
		    										</td>
		    									</tr>
		    								</table>
		    							</td>
		    						</tr>
		    					</table>
							</fieldset>		
						</td>
					</tr>
				</table>	
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
			    </td></tr></tale>
			</td></tr>
			<tr><td align='right' style='height:20px;'>&nbsp;</td></tr>
			</table>
	   </td>
	 </tr>
	</table>
	</center>
	</div>
	<% 	model.setMessaggioCentrale("");
	} %>

</body>
</html>
