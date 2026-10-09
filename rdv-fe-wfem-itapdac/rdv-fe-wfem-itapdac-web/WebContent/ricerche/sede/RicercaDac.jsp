<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="com.atosorigin.wfem.types.DateType"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.ParamsModel"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.ricerche.sede.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACRICERCHE");
	template.setPageName("RicercaDac");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("100");
	template.setJSCombo(false);
	
	RicercaDacModel model = (RicercaDacModel)template.getPageDataModel();
	
	DateType ieri = Tools.today();
	ieri.addDays(-1);
%>

<html>

<head>
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
<%=template.getHeader()%>
<script>var jsIeri = "<%=ieri%>";</script>
<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
<script src="<%=template.getWebApp()%>/ricerche/sede/RicercaDac.js"></script>
</head>

<body>

<%@ include file="../../resources/include/DacColor.html"%>

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.business.ApriDac.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="tabNum" value="">
<input type="hidden" name="idDac" value="">
<%=model.htmlParams("",ParamsModel.showBack)%>
</form>

<table class="text" width="100%" height="100%">

  <tr>
   <td valign="top" align="center">
		<form name="ricerca" method="post" action="call.wfem" style="margin:0px;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="doSearch" value="">
		<input type="hidden" name="daoAccessName" value="ricercaDacSede">
		<input type="hidden" name="tipoRicerca" value="<%=model.getTipoRicerca()%>">
		<input type="hidden" name="tabNum" value="">
		<% if(model.getFnc().equals(Costanti.FNC_SPUNTA)){ %>
        <table width="95%" height="185" cellspacing="0" cellpadding="0" id="JSTab">
		<% }else{ %>
        <table width="95%" height="230" cellspacing="0" cellpadding="0" id="JSTab">
		<% } %>
		  <tr>
		      <td align="center">
		        <table width="100%" cellspacing="0" cellpadding="0"><tr>
		        <%
		         	String tab1 = "Ricerca Prit / Dac";
	         		String tab2 = "Ricerca Prit / Dac - Ricerca Avanzata";
	         		if(model.getFnc().equals(Costanti.FNC_SPUNTA)){
			         	tab1 = "Spunta Prit";
		         		tab2 = "Spunta Prit - Ricerca Avanzata";
	         		}else if(model.getFnc().equals(Costanti.FNC_AUTORIZZA)){
			         	tab1 = "Autorizzazione Dac";
		         		tab2 = "Autorizzazione Dac - Ricerca Avanzata";	         			
	         		}
		        %>
		          <td nowrap class="htab" valign="bottom" id="JSTab0" onclick="selectJSTab('JSTab',0);">&nbsp;<%=tab1%>&nbsp;</td>
		          <td nowrap class="htab" valign="bottom" id="JSTab1" onclick="selectJSTab('JSTab',1);">&nbsp;<%=tab2%>&nbsp;</td>
		      	  <td nowrap width="100%" style="border-bottom: 1px solid silver;">&nbsp;</td>
				</tr></table>
			  </td>
		  </tr>			
		  <tr>
		  	  <td valign="top" height="100%" class="htabContent">
			    <table width="100%" height="100%" cellspacing="0" cellpadding="0">
			       <tr>
					  <td height="100%" align="center" style="display:none;" id="JSTab0El">
						<%@ include file="./include/RicercaDac.html"%>
					  </td>
					  <td height="100%" align="center" style="display:none;" id="JSTab1El">
						<%@ include file="./include/RicercaDacAvanzata.html"%>
					  </td>
				   </tr>
				   <tr><td colspan="2"><hr></td></tr>
				   <tr>
				      <td colspan="2">
				   		<table width="100%" class="text" style="table-layout:fixed;"><tr>
				   			<td></td>
				   			<td><%=template.action("ricercaAction")%></td>
				   			<td align="right"><span id="pulisciCampiRicercaDacCont" style="visibility:hidden;text-decoration:underline;cursor:pointer;" onclick="pulisciCampiRicercaDac();">Pulisci campi di ricerca</span></td>
				   		</tr></table>
				   	  </td>
				   </tr>
				   <tr>
				      <td colspan="2" class="text">
					   Per consultare
					   documenti ricevuti a partire dal 1/7 è necessario utilizzare
					   l'applicativo MOM.  La consultazione dello storico per la ricerca prit e
					   ricerca documenti sarà disponibile sull'attuale applicazione				      	
				      </td>
				   </tr>
				</table>
			    <script>
			    	selectJSTab("JSTab",<%=model.getTabNum()%>);
			    	try{
				    	document.getElementById("parametri_idDac").focus();
				    	document.getElementById("parametri_idDac").select();
			    	}catch(e){}
			    </script>
		  	  </td>
		  </tr>		
		</table>
		</form>
   </td>
  </tr>

  <tr>
   <td id="toolbarCont">
	     <%if(model.getElencoDac().size() > 0){%>
			<table>
			  <tr>
			     <td>
			       <%=template.action("apriAction","enabled='false' style='width:60'")%>
			     </td>
			     <td>
			       <%=template.action("stampaAction","enabled='false' style='width:60'")%>
			     </td>
			  </tr>
			</table>
	    <%}%>
   </td>
  </tr>
  
  <tr>
   <td id="elencoCont" height="100%" align="center">
		<%if(!model.isPrimaAttivazione()){
			
				String cols = "cols='#descrUffSpunta,"+
								     "tipoDac,idDac,descrStato,descrUbicazione,descrEsito,dataOraCambioStato,descrUffMittente,codUtenteIns,"+
								     "descrUffDestinatario,codUtenteLavorazione,descrUffLavorazione' ";
				String dim = "colswidths='2%,13%,*,8%,*,12%,8%,8%,8%,8%,8%' ";
		%>
			<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			  <tr>
			     <td height="100%">
					<%=template.grid("elencoDac",cols+dim+
												"onclick='selectDac(this);' "+
												"ondblclick='selectDac(this);startRequest();doApriAction();' "+
												"onnewcell='onNewCell(this);' "+
												"selection='single' "+
												"width='100%' height='100%' ")%>
			     </td>
				</tr>
			</table>
		 <%}%>
   </td>
  </tr>

  <tr>
   <td align="center" id="legenda">
  	<% if(model.getElencoDac().size() > 0){ %>
	   	<table class="text"><tr>
	   		<% if(model.getFnc().equals(Costanti.FNC_SPUNTA)){ %>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_STANDARD)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_STANDARD)%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_PERCONTORETE)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_PERCONTORETE)%>;width:15;">&nbsp;</td><td width="25"></td>
	   		<% }else{ %>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_STANDARD)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_STANDARD)%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_PERCONTORETE)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_PERCONTORETE)%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_RACCOMANDATE)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_RACCOMANDATE)%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_CARTOLINE)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_CARTOLINE)%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("tipoDac."+Costanti.TIPO_DAC_SEDE)%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkTipoDac."+Costanti.TIPO_DAC_SEDE)%>;width:15;">&nbsp;</td>
			<% } %>
	   	</tr></table>
	  <% } %>
   </td>
  </tr>
  
</table>

<%=template.getFooter()%>
</body>
</html>
<% model.setPrimaAttivazione(false); %>
