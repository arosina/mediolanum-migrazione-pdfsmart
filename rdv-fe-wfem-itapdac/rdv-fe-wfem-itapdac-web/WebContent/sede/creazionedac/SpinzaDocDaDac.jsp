<%@ page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
%>

<table>
<tr>
	<td>
		<script>
			onSpinzaDocumentoEnd();
		</script>
	</td>
</tr>
</table>