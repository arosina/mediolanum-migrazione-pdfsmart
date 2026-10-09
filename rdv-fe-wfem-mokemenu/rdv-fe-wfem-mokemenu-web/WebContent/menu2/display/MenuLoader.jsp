<HTML>
<HEAD>
<%@ include file="../../url.jsi"%>
<script>
function gotoMenu(){
	var menuUrl = "/<%=webApp%>/call.wfem?wfemCmd=moke.menu2.display.Menu.execute&BrowserInstance=0";
	top.document.location.href = menuUrl;
}
</script>
</HEAD>
<BODY>
</BODY>
<script>
gotoMenu();
</script>
</HTML>
