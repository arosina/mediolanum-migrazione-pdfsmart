<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setApplCode("ITAPDACTITOLIPOPUP");
	String popupName = (String)request.getAttribute("popupName");
	String popupParams = (String)request.getAttribute("popupParams");
	if(popupParams == null)
		popupParams = "";
	String tmpPopupParams = popupParams.toString();
	popupParams = "";
	for(int i=0;i<tmpPopupParams.length();i++){
	   if(tmpPopupParams.charAt(i) == '"')
	      popupParams += "\\";
       popupParams += String.valueOf(tmpPopupParams.charAt(i));
    }
	String url = "call.wfem?wfemCmd="+popupName+".executeOnPopup&";
%>
<html>
<head>
<%=template.getHeader()%>
<title><%=template.getProperty(popupName.substring((popupName.lastIndexOf(".")+1))+".titolo")%></title>
</head>
<script>
var jUrl = "<%=url%>";
var jPopupParams = "<%=popupParams%>";
document.write('<iframe height="100%" width="100%" src="'+__retrieveGatewayUrl()+jUrl+jPopupParams+'">');
document.write('</iframe>');
</script>
</html>