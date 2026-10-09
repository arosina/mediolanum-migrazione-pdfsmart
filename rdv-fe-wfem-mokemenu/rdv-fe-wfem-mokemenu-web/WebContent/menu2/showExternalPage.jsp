<% 
   String url = (String)request.getParameter("url");
%>
<html>
<% if(url != null && !url.equals("")){
      url = url.replace(',','&');
      if(url.startsWith("'"))
      	url = url.substring(1);
      if(url.endsWith("'"))
	url = url.substring(0,url.length()-1);
%>
	<script>
	document.location.href = "<%=url%>";
	</script>
<%}%>
</html>