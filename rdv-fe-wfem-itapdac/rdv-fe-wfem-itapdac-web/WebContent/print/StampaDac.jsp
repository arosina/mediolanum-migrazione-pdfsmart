<%@page import="java.util.*"%>
<%@page import="prgm.ita.p.dac.facade.Costanti"%>
<%@page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<HTML>


<!-- PAGEORIENTATION="Horizontal" -->
<!-- PAGEFONTSIZE="1" -->

<% 	
	template.setApplCode("ITAPDAC");

 	DacModel dac = (DacModel)template.getPageDataModel();
 	String userCode = dac.getUserSessionContext().getClientSessionContext().getUserCode();
 	boolean isPromotore = dac.getUfficio().equals(Costanti.UFFICIO_RETE) ? true : false;
	
	String ufficioMittente =  dac.getDescValue("uffMittente");
	String ufficioDestinatario =  dac.getDescValue("uffDestinatario");
	
	String autore = dac.getCodUtenteIns().toString();
	String operatore = dac.getCodUtenteLavorazione().toString();

	String noteAutore = dac.getNoteAutore().isNull() ? "(nessuna)" : dac.getNoteAutore().toString();
	String noteOperatore = dac.getNoteOperatore().isNull() ? "(nessuna)" : dac.getNoteOperatore().toString();
	
	String stato = dac.getDescValue("stato");;
	String dove = dac.getDescValue("ubicazione");
	String esito = dac.getDescValue("esito");;

	String title = "Logistica documenti "+ufficioMittente+":#CR#Prit da "+ufficioMittente.toUpperCase()+" a "+ufficioDestinatario.toUpperCase();

	String whiteColor = 	"#FFFFFF";
	String grayColor = 		"#E0E0E0";
	String darkGrayColor = 	"#C0C0C0";
	String lightGrayColor = "#E5E5E5";
	String lightSteelBlue = "#87CEEB";
%>

<!-- MARGINTOP="0.8" -->
<!-- HEADERFONTSIZE="5"	-->
<!-- HEADERRIGHT="<%=title%>"	-->
<!-- HEADERLEFT="#IMG#/images/logoBancaMediolanum.jpg"	-->

<!-- MARGINBOTTOM="0.7" -->
<!-- FOOTERDECOR="LINEBOX" -->
<!-- FOOTERFONTSIZE="2"	-->
<!-- FOOTERLEFT="FIRMA MITTENTE ____________________" -->
<!-- FOOTERCENTER=" COPIA PER DIREZIONE #CR# Pagina #CURPAGE# di #TOTPAGE# " -->
<!-- FOOTERRIGHT=" FIRMA RICEVENTE ____________________ #CR# FIRMA DESTINATARIO ____________________ " -->


<body>

<table> <!-- HEADERROWS="4" -->

<%@ include file="./include/TestataDac.html"%>

<%@ include file="./include/Documenti.html"%>

</table>

</body>
</html>
