<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");
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
<title><%=template.getProperty(popupName.substring((popupName.lastIndexOf(".")+1))+".titolo")%></title>
</head>
<script>
function encodeStringNoSep(str) {
	var encodedString = "";
	var charCode;
	var skip;
	for(var i=0; i<str.length; i++) {
		skip = false;
		charCode = str.charCodeAt(i);
		if(str.charAt(i) == '\t' || str.charAt(i) == '='){
			encodedString += str.charAt(i);
			continue;
		}
		
		if(charCode > 47 && charCode <58) 		skip = true; // number
		else if(charCode > 64 && charCode <91) 	skip = true; // upper case
		else if(charCode > 96 && charCode <123) skip = true; // lower case
		
		encodedString += skip ? str.charAt(i) : "%"+new Number(charCode).toString(16);
	} 
	return encodedString;
}

var jUrl = "<%=url%>";
var jPopupParams = "<%=popupParams%>";
jPopupParams = encodeStringNoSep(jPopupParams);
var re = new RegExp('\t','g');
jPopupParams = jPopupParams.replace(re,'&');

document.write('<iframe height="100%" width="100%" src="'+jUrl+jPopupParams+'&isPrimaVolta=true">');
document.write('</iframe>');
</script>
</html>