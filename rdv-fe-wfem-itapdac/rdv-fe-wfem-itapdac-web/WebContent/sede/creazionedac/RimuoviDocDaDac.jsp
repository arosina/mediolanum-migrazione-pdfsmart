<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.DocumentoModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);

	DocumentoModel doc = (DocumentoModel)template.getPageDataModel();	
%>

<table>
<tr>
	<td>
		<script>
			onRimuoviDocumentoEnd('<%=doc.getBarcode()%>');
		</script>
	</td>
</tr>
</table>