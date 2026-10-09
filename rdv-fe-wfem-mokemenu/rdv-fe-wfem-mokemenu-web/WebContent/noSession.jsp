<html>
<body>
<%@ include file="./url.jsi"%>
<table><tr></td>
	<script>
	if(typeof(top.window.doUnload) != 'undefined')
		top.window.doUnload = false;
	top.document.location.href = "/<%=webApp%>/noSessionMessage.html";
	</script>
</td></tr></table>
</body>
</html>
