<%@page import="java.util.ArrayList"%>
<%@ page import="prgm.ita.anagraficaclienti.questionari.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("Questionario");
	template.setJSCombo(false);
	template.setLabelPosition(template.LEFT_LABEL);
	
	QuestionarioModel model = (QuestionarioModel)template.getPageDataModel();
	String wfemCmdAfterCalcolo = "prgm.ita.anagraficaclienti.questionari.business.CalcoloProfilo.execute";
	if("SalvaQuestionario".equals(model.getCmdAfterCalcolo()))
		wfemCmdAfterCalcolo = "prgm.ita.anagraficaclienti.questionari.business.SalvaQuestionario.execute";
%>

<html>

<head>
<%=template.getHeader()%>
<script>
function doAnnulla(){
	closeModalPopup(null);
}
function doConferma(){
	var ret = new Object();
	ret.cmdAfterCalcolo = "<%=wfemCmdAfterCalcolo%>";
	closeModalPopup(ret);
}
</script>
</head>

<body>

<table width='100%' height='100%' cellpadding='10' cellspacing='0'>
 <tr>
   <td align='center'>
		<table width='100%' height='100%' cellpadding='2' cellspacing='0' style='background-color:white;'>
			<tr>
				<td>
					<ul class='text' style='font-size:12px;'>
					<% for(int i=0;i<model.getAlertAfterCalcolo().size();i++){%>
						<li><%=model.getAlertAfterCalcolo().get(i).toString()%></li>
					<% } %>
					</ul>
	    		</td>
			</tr>
			<tr>
				<td align="center">
					<table>
						<tr>
							<% if("SalvaQuestionario".equals(model.getCmdAfterCalcolo())){ %>
								<td><%=template.action("conferma","text='Ok' style='width:80;'")%></td>
							<% }else{ %>
								<td><%=template.action("annulla","text='Ok' style='width:80;'")%></td>
							<% } %>
						</tr>
					</table>
				</td>
			</tr>
		</table>
   </td>
 </tr>
</table>

<%=template.getFooter()%>
<% 
	model.setCmdAfterCalcolo("");
	model.setAlertAfterCalcolo(new ArrayList()); 
%>
</body>
</html>
