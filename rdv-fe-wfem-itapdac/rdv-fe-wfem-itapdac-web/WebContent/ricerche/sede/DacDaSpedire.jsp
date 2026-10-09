<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.ricerche.sede.DacDaSpedireModel"%>
<%@ page import="prgm.ita.p.dac.model.ParamsModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACRICERCHE");
	template.setPageName("DacDaSpedire");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("100");
	template.setJSCombo(false);
	
	DacDaSpedireModel model = (DacDaSpedireModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/ricerche/sede/DacDaSpedire.js"></script>
</head>

<body>

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.creazionedac.ApriDacInCreazione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="idDac" value="">
<input type="hidden" name="stato" value="<%=Costanti.STATO_INCORSO%>">
<%=model.htmlParams("",ParamsModel.showBack+"_"+ParamsModel.dopoSpunta)%>
</form>

<table class="text" width="100%" height="100%">

<!--	#########################		BARRA SUPERIORE		#########################	-->
<tr>
	<td valign="top">
		<table width="100%" class="text" style="background-color:#e1e1e1;">
		<tr>
    		<td width="100%"><b>&nbsp;Dac da spedire</b></td>
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


  <tr><td height="20px">&nbsp;</td></tr>
  <tr>
   <td id="toolbarCont">
	     <%if(model.getElencoDac().size() > 0){%>
			<table>
			  <tr>
			     <td>
			       <%=template.action("apriAction","enabled='false' style='width:60'")%>
			     </td>
			  </tr>
			</table>
	    <%}%>
   </td>
  </tr>
  
  <tr>
   <td id="elencoCont" height="100%" align="center">
		<%if(!model.isPrimaAttivazione()){
				String cols = "";
				String dim = "";

				if (model.isSmistatore()) {
					cols = "cols='idDac,codUtenteIns,descrBox,descrUffDestinatario,dataOraIns' ";
					dim = "colswidths='20%,20%,20%,20%,*' ";
				}else{
					cols = "cols='idDac,codUtenteIns,descrUffDestinatario,dataOraIns,reso' ";
					dim = "colswidths='20%,20%,25%,*,8%' ";
				} %>
				
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
