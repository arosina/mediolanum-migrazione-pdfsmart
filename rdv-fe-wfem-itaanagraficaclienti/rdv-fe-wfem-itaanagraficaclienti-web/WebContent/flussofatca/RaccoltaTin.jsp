<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="com.atosorigin.wfem.types.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.LEFT_LABEL);
	
	ClienteModel model = (ClienteModel)template.getPageDataModel();
%>

<html>
<head>
<%=template.getHeader()%>
<style>
fieldset{
 text-align: left;
}
</style>
<script>
function pulisci(){
	document.dati.datiFatca_tin.value = "";
	enableField("datiFatca_tin",true);
}
function doContinua(){
	if(document.dati.datiFatca_tin.value == ""){
		alert("Specificare il TIN");
		return false;
	}
	if(document.dati.datiFatca_tin.value.length != 9){
		alert("Specificare un valore TIN valido");
		return false;
	}
	document.dati.submit();
	return true;
}
</script>
</head>

<body>

<form name="dati" method="post" action="call.wfem" style="margin:0px;">  
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.flussofatca.GoOn.execute">
<input type="hidden" name="resetErrors" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="datiFatca_isAppliedFor" value="false"> <!-- RM 37816 - Eliminazione possibilità di selezionare "Applied for" -->
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<table width="100%">
  <tr>
	  <td align="center">
		<fieldset class="fieldsGroup">
		<legend class="text" style="font-weight: bold;">
		    <img src="<%=template.getWebApp()%>/images/section.gif">
			&nbsp;Verifica FATCA&nbsp;
			<img title="Pulisci il TIN" src="<%=template.getWebApp()%>/images/clear.gif" 
			     onclick="pulisci();" style="cursor: pointer;">
		</legend>
		<center>
		<table width="80%" class="text">
			<tr>
				<td align="center">
					<table class="text">
						<tr>
						  <td><%=template.field("datiFatca_tin","labelcode='Inserire il TIN' maxlength='9' type='num'")%></td>
					  	</tr>
						<tr>
							<td align="center"><%=template.action("continua","text='Salva e Continua'")%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</center>
		</fieldset> 
	  </td>
  </tr>
</table>
</form>

<%=template.getFooter()%>

</body>
</html>
