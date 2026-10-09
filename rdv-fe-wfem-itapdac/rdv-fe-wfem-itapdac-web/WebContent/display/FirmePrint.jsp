<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("FirmePrint");
	template.setJSCombo(false);
	
	ContoCorrenteModel model = (ContoCorrenteModel)template.getPageDataModel();	
	String nominativo = "";
	if(!model.getCodMediolanum().isNull())
		nominativo = model.getCodMediolanum().toString();
	if(!model.getNominativo().isNull()){
		nominativo += " - "+model.getNominativo();
	}else{
		if(!model.getNome().isNull() || !model.getCognome().isNull()){
			nominativo += " - ";
			if(!model.getCognome().isNull())
				nominativo += model.getCognome();
			if(!model.getNome().isNull())
				nominativo += " "+model.getNome();
		}
	}
%>
<html>
<head>
<title><%=model.getCodMediolanum()%></title>
<script>
var numFirme = <%=model.getFirmeDelConto().size()%>;
function doPrint(){
	if(numFirme == 0)
		return;
	window.print();
	document.getElementById("ristampa").style.visibility='visible';
}
</script>
</head>
<body onload="doPrint();">
<table width="100%" style="table-layout:fixed;">
  <tr>
     <td>
  		<span id="ristampa" style="visibility:hidden;font-family:Arial;font-size:10pt;cursor:pointer;text-decoration:underline;" onclick="doPrint();">Ristampa</span>
  	 </td>
  </tr>
  <tr>
     <td align="center" style="font-family:Arial;">
  		<b><%=nominativo%></b>
  	 </td>
  </tr>
  <tr><td><hr></td></tr>
  <% if(model.getFirmeDelConto().size() == 0){ %>
	  <tr><td>Non ci sono firme per il cliente</td></tr>
  <% }else{ %>
	  <%
	  		  for(int i=0;i<model.getFirmeDelConto().size();i++){
	  		  FirmaModel firma = (FirmaModel)model.getFirmeDelConto().get(i);
	  %>
	  	<tr><td><img src="<%=firma.getUrlFirma()%>" width="100%"></td></tr>
		<tr><td><hr></td></tr>
	  <% } %>
  <% } %>
</table>
</body>
</html>
