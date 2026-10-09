<%@ page import="prgm.ita.anagraficaclienti.stampamoduli.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	ModuloModel model = (ModuloModel)template.getPageDataModel();	 
%>

<!-- PAGEFORMAT="A4" -->
<!-- PAGEORIENTATION="<%=model.getOrientamentoPagina()%>" -->

<%if(model.getNomeModulo().equals("ClienteIsAgente")){%>
<table>
	<tr><td align="center">Il cliente presentatore non può essere un Family Banker, non è possibile stampare il Coupon.</td></tr>
	</table>
</center>
<%}else{%>
<!-- INCLUDEPDF="/stampamoduli/pdf/<%=model.getNomeModulo()%>.pdf" -->
<%}%>
