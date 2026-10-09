package prgm.pdfwebforms.aml.backend;

import javax.ejb.EJBException;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.aml.model.DispositivaAmlContainer;
import prgm.pdfwebforms.aml.model.NaturaModel;
import prgm.pdfwebforms.aml.model.ScopoRapportoModel;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class NaturaFacadeBean extends FacadeObject implements NaturaFacade{

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static class CodiciScopoRapporto{
		private static CodDescDataList codScopoRapportoDl = new CodDescDataList();
		static {
			CodDescData d = new CodDescData(); d.setCod("1"); d.setDescr("Investimento e risparmio (con possibili disinvestimenti/riscatti FREQUENTI)"); codScopoRapportoDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("2"); d.setDescr("Investimento e risparmio (con possibili disinvestimenti/riscatti OCCASIONALI)"); codScopoRapportoDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("3"); d.setDescr("Investimento e risparmio con intervento di un terzo pagatore"); codScopoRapportoDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("4"); d.setDescr("Investimento e risparmio per conto terzi"); codScopoRapportoDl.addCodDescData(d);
		}
		private static CodDescDataList getCodScopoRapportoDl() {
			return codScopoRapportoDl;
		}
	}

	/***********************************************************************************************/
	// Usato dal driver del modulo AML adeguataverificainvestimento
	/***********************************************************************************************/
	public NaturaModel scopoRapportoDispositiva(ClientSessionContext csc, PdfModel pdf) throws EJBException {		
		try {
			NaturaModel natura = new NaturaModel();
			natura.getElencoDispositive().add(new DispositivaAmlContainer(pdf));
			natura.getElencoScopiRapporto().add(new ScopoRapportoModel());
			init(csc, pdf, natura);
			return natura;
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void init(ClientSessionContext csc, PdfModel pdf, NaturaModel natura) throws EJBException {
		try {
			StringType nomiCampoNdgTerzoPagatore = PdfConfig.getParamAsString(csc, "AML", "NOMI_CAMPO_NDG_TERZO_PAGATORE");
			for(int i=0;i<natura.getElencoScopiRapporto().size();i++) {
				ScopoRapportoModel scopoRapporto = (ScopoRapportoModel)natura.getElencoScopiRapporto().get(i);
				scopoRapporto.setCodScopoRapportoDataList(CodiciScopoRapporto.getCodScopoRapportoDl());
				
				DispositivaAmlContainer dc = natura.getElencoDispositive().get(i);
				preimpostaCodScopoRapporto(AmlFacadeBean.pdfAnagContratto(dc.getPdf()), AmlFacadeBean.pdfDataContratto(dc.getPdf()), 
										   scopoRapporto, nomiCampoNdgTerzoPagatore.toString());
			}		
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void preimpostaCodScopoRapporto(PdfAnagModel pdfAnag, PdfDataModel pdfData, 
												   ScopoRapportoModel scopoRapporto, String nomiCampoNdgTerzoPagatore) throws Exception{
		String valorePreimpostato = "";		
		String ndgTerzoPagatore = AmlFacadeBean.findFieldValue(pdfData, nomiCampoNdgTerzoPagatore);
		if(!ndgTerzoPagatore.isEmpty())
			valorePreimpostato = "3";

		if(pdfAnag.getPdfDriverName().equals("polizzemiltranche") || pdfAnag.getPdfDriverName().equals("mediolanumcapitalnew")){
			StringType contraente = AmlFacadeBean.stringField(pdfData, "codiceFiscalePartitaIvaCliente1");
			StringType beneficiario1 = AmlFacadeBean.stringField(pdfData, "codiceFiscaleBeneficiarioVita1");
			StringType beneficiario2 = AmlFacadeBean.stringField(pdfData, "codiceFiscalePartitaIvaBeneficiarioVita2");
			if(sonoDiversi(beneficiario1, contraente) || sonoDiversi(beneficiario2, contraente))
				valorePreimpostato = "4";
		}
		
		if(!valorePreimpostato.isEmpty()) {
			scopoRapporto.setCodScopoRapporto(new StringType(valorePreimpostato));
			scopoRapporto.setCodScopoRapportoPreselezionato(true);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean sonoDiversi(StringType src, StringType dst) {
		if(src.isNull() || dst.isNull())
			return false;
		return !src.equals(dst);
	}
	
}
