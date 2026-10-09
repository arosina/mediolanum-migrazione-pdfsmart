<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.ricerche.sede.DacDaGestireModel"%>
<%@ page import="prgm.ita.p.dac.model.ParamsModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACRICERCHE");
	template.setPageName("DacDaGestire");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("100");
	template.setJSCombo(false);
	
	DacDaGestireModel model = (DacDaGestireModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/ricerche/sede/DacDaGestire.js"></script>
</head>

<body onload="javascript:document.getElementById('parametri_idDac').focus();">

<form name="datiRead" style="display:none;">
	<input type="hidden" name="constStatoSpedita" value="<%=Costanti.STATO_SPEDITA%>">
</form>

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.ricezionedac.ApriDacInRicezione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="idDac" value="">
<input type="hidden" name="stato" value="">
<%=model.htmlParams("",ParamsModel.showBack+"_"+ParamsModel.dopoSpunta)%>
</form>

<table class="text" width="100%" height="100%">

<!--	#########################		BARRA SUPERIORE		#########################	-->
<tr>
	<td valign="top">
		<table width="100%" class="text" style="background-color:#e1e1e1;">
		<tr>
    		<td width="100%"><b>&nbsp;Dac da gestire</b></td>
		</tr>
    	<tr>
    		<td align="center">
    			<table width="100%" cellpadding="0" cellspacing="0">
    			<tr>
    				<td width="25%" class="text" align="center"><b>Ufficio corrente: <%=model.getDescValue("ufficio")%></b></td>
    				<td width="70%" class="text"></td>
    			</tr>
    			</table>    			
    		</td>
		</tr>
		</table>
	</td>
</tr>
<!--	#########################		END BARRA SUPERIORE		#########################	-->

<!--  PARAMETRI DI RICERCA -->
  <tr>
   <td align="center">
		<table width="85%">
		 <tr>
		  <td align="center">
			<form name="ricerca" method="post" action="call.wfem" style="margin:0px;">
			<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.ricerche.sede.EseguiRicercaDacDaGestire.execute">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="doSearch" value="">
			<input type="hidden" name="daoAccessName" value="dacDaGestire">

			<fieldset>
				<legend class="text" style="font-weight: bold;">
				    <img src="<%=template.getWebApp()%>/images/section.gif">
					&nbsp;Ricezione Dac&nbsp;
				</legend>

			<table width="95%" height="110px">
			<tr>
				<td width="5%"></td>
				<td width="40%"><%=template.field("parametri_idDac","labelwidth='25%' fieldalign='right' maxlength='20' size='25'")%></td>
				<td width="100%"></td>
			</tr>
			<tr>
				<td colspan="5" style="height: 5"><hr></td>
			</tr>
			<tr>
				<td colspan="5"><%=template.action("ricercaAction","style='width:80px;'")%></td>  
			</tr>
			</table>
			
			</fieldset>
			</form>
		  </td>
		 </tr>
		</table>
	</td>
  </tr>

  <tr>
   <td id="toolbarCont" height="60px">
	     <%if(model.getElencoDac().size() > 0){%>
			<table>
			  <tr>
			     <td>
			       <%=template.action("apriAction","enabled='false' style='width:80'")%>
			     </td>
			  </tr>
			</table>
	    <%}%>
   </td>
  </tr>
  
  <tr>
   <td id="elencoCont" height="100%" align="center">
		<%if(!model.isPrimaAttivazione()){
				String cols = "cols='#stato,idDac,descrStato,codUtenteIns,descrUffMittente,dataOraEmissione' ";
				String dim = "colswidths='20%,*,10%,20%,15%' ";%>	
				
				<table width="95%" height="100%" cellpadding="0" cellspacing="0">
				  <tr height="100%">
				     <td>
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


</table>

<%=template.getFooter()%>
</body>
</html>
<% model.setPrimaAttivazione(false); %>
