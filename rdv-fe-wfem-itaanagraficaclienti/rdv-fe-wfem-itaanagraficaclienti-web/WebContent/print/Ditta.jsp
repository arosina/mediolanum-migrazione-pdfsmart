<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.flussofatca.AbstractNavigatore"%>
<jsp:useBean id="clienteModel" scope="request" class="prgm.ita.anagraficaclienti.model.ClienteModel"/>

<% 
	boolean includiModuloAllegatiExtraUE =  Boolean.valueOf(clienteModel.getDatiStampa().readProperty("includiModuloAllegatiExtraUE").toString());
%>

<% if( includiModuloAllegatiExtraUE){%>
   <!-- INCLUDEPDF="/print/ModuloAllegatiExtraUE.pdf" -->
<% } %>

<!-- INCLUDEPDF="/print/Ditta.pdf" -->

<% 
	boolean flagCambiato = false;
	boolean includiPrivacy = false;
	if(clienteModel.getIsPotenziale().booleanValue() || clienteModel.getDatiApplicativi().getClienteOriginale() == null){
		includiPrivacy = true;
	}else{
	   DatiPrivacyModel privacyOriginale = clienteModel.getDatiApplicativi().getClienteOriginale().getDatiPrivacy();
	   if(!clienteModel.getDatiPrivacy().getFlagCarte().equals(privacyOriginale.getFlagCarte()) ||
	      !clienteModel.getDatiPrivacy().getFlagLiberatoria().equals(privacyOriginale.getFlagLiberatoria()) ||
	      !clienteModel.getDatiPrivacy().getFlagExtraUE().equals(privacyOriginale.getFlagExtraUE()) ||
	      !clienteModel.getDatiPrivacy().getFlagProfilazione().equals(privacyOriginale.getFlagProfilazione()))
	      flagCambiato = true;
	}
%>

<% if( includiPrivacy || 
		clienteModel.getDatiApplicativi().getTipoStampa() == Costanti.STAMPA_CENSIMENTO ||
	    (clienteModel.getDatiApplicativi().getTipoStampa() == Costanti.STAMPA_VARIAZIONE && flagCambiato)){%>
   <!-- INCLUDEPDF="/print/Privacy.pdf" -->
<% } %>

<% if( clienteModel.getModuloFatca().equals(Costanti.MODULO_W9_FATCA)){%>
   <!-- INCLUDEPDF="/print/fatca/W9.pdf" -->
   <!-- INCLUDEPDF="/print/fatca/W9.pdf" -->
   <!-- INCLUDEPDF="/print/fatca/W9.pdf" -->
   <!-- INCLUDEPDF="/print/fatca/W9.pdf" -->
<% } %>
</body>
</html>
