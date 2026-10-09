<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>

<%
	template.setApplCode("MOKEMENU2");

	String country  = (String)session.getAttribute("country"); if(country == null)country = new String("");
	String channel  = (String)session.getAttribute("channel"); if(channel == null)channel = new String("");
	String language = (String)session.getAttribute("language");if(language == null)language = new String("");
	String starturl = (String)session.getAttribute("starturl");if(starturl == null)starturl = new String("");
	
	String totUrl = "&country="+country+"&channel="+channel+"&language="+language+"&starturl="+starturl;
%>

<html>
<head>
<%@ include file="../../url.jsi"%>
<title><%=titolo%></title>
</head>

<frameset id="applFrameSet" name="applFrameSet" framespacing="0" rows="115,*" frameborder="no" border="0">
  <frame name="menu" src="call.wfem?wfemCmd=moke.menu2.display.Menu.execute<%=totUrl%>" scrolling="no" noresize="noresize">
  <frameset id="funzFrameSet" name="funzFrameSet" framespacing="0" cols="176,*" frameborder="no" border="0">
    <frame scrolling="no" name="menuFunzioni" src="/<%=webApp%>/menu2/display/MenuFunzioni.html" scrolling="no" noresize="noresize">
    <frame scrolling="no" name="clientarea" src="/StartCmd.jsp">
  </frameset>
</frameset>
</html>

