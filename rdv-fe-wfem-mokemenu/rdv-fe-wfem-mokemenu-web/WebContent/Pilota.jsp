<%
	String alias = request.getHeader("Host");

	String countryCode = "ITA";
	String channelCode = "P";
	String langCode    = "IT";
	String protocol = "http";
	String startUrl = protocol + "://"+ alias;
	
%>
<html>
<body>
<!-- <form id="wfem" name="wfem" action="call.wfem" method="post" target="menuITA">  -->
<form id="wfem" name="wfem" action="call.wfem" method="post" target="_self">
	<input type="hidden" name="wfemCmd"  value="showLogin">
	<input type="hidden" name="country"  value="<%=countryCode%>">
	<input type="hidden" name="channel"  value="<%=channelCode%>">
	<input type="hidden" name="language" value="<%=langCode%>">
	<input type="hidden" name="starturl" value="<%=startUrl%>">
</form>

<script language=javascript>
//	var w = screen.width-(screen.width*0.01);
//	var h = screen.height-(screen.height*0.10);
//	var w = 1024;
//	var h = 768;
//	window.open("about:blank","menuITA","toolbar=no,resizable=yes,status=yes,top=0,left=0,width="+w+"px,height="+h+"px");
	document.wfem.submit();		
</script>
</body>
</html>
