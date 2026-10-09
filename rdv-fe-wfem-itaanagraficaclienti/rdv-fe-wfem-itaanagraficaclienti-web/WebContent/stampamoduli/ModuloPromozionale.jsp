<%@ page import="prgm.ita.anagraficaclienti.stampamoduli.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	ModuloPromozionaleModel model = (ModuloPromozionaleModel)template.getPageDataModel();	 
%>

<!-- PAGEFORMAT="A4" -->
<!-- PAGEORIENTATION="horizontal" -->

<!-- INCLUDEPDF="/stampamoduli/pdf/<%=model.getNomeModulo()%>.pdf" -->
