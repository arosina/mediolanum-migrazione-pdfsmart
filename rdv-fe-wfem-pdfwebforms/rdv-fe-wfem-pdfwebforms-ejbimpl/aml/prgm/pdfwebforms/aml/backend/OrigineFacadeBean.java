package prgm.pdfwebforms.aml.backend;

import java.math.BigDecimal;

import javax.ejb.EJBException;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;

import prgm.pdfwebforms.aml.model.DispositivaAmlContainer;
import prgm.pdfwebforms.aml.model.OrigineModel;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrigineFacadeBean extends FacadeObject implements OrigineFacade{
	
	public static final String DAO_XML_AML_NAME = "PdfWebForms.PdfAml";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static class CodiciTipoImportoPF{
		private static CodDescDataList codTipoImportoPFDl = new CodDescDataList();
		static {
			CodDescData d = new CodDescData(); d.setCod(OrigineModel.COD_TIPO_IMPORTO_DISINVESTIMENTO_GRUPPO); d.setDescr("Disinvestimento prodotti del Gruppo"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("2"); d.setDescr("Disinvestimento da altri intermediari"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("3"); d.setDescr("Reddito da lavoro dipendente/pensione"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("4"); d.setDescr("Reddito da lavoro autonomo"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("5"); d.setDescr("Utile societario/Reddito d'impresa"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("6"); d.setDescr("Vendita immobile/azienda"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("7"); d.setDescr("Redditi fondiari"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("8"); d.setDescr("Redditi finanziari"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("9"); d.setDescr("Lascito/eredità/donazione"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("10"); d.setDescr("Premi/lotterie"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("11"); d.setDescr("Scudo fiscale/voluntary disclosure"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("12"); d.setDescr("Prestito"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("13"); d.setDescr("Investimento di fondi da liquidazione sinistro danni/vita"); codTipoImportoPFDl.addCodDescData(d);
						d = new CodDescData(); d.setCod(OrigineModel.COD_TIPO_IMPORTO_ORIGINE_ALTRO); d.setDescr("Altro"); codTipoImportoPFDl.addCodDescData(d);
		}
		private static CodDescDataList getCodTipoImportoPFDl() {
			return codTipoImportoPFDl;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static class CodiciTipoImportoPG{
		private static CodDescDataList codTipoImportoPGDl = new CodDescDataList();
		static {
			CodDescData d = new CodDescData(); d.setCod(OrigineModel.COD_TIPO_IMPORTO_DISINVESTIMENTO_GRUPPO); d.setDescr("Disinvestimento prodotti del Gruppo"); codTipoImportoPGDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("14"); d.setDescr("Disponibilità liquide"); codTipoImportoPGDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("15"); d.setDescr("Operazioni straordinarie"); codTipoImportoPGDl.addCodDescData(d);
						d = new CodDescData(); d.setCod(OrigineModel.COD_TIPO_IMPORTO_ORIGINE_ALTRO); d.setDescr("Altro"); codTipoImportoPGDl.addCodDescData(d);
		}
		private static CodDescDataList getCodTipoImportoPGDl() {
			return codTipoImportoPGDl;
		}
	}

	/***********************************************************************************************/
	// Usato dal driver del modulo AML adeguataverificainvestimento
	/***********************************************************************************************/
	public OrigineModel importiOrigineDispositiva(ClientSessionContext csc, PdfModel pdf) throws EJBException {		
		try {
			OrigineModel origine = new OrigineModel();
			origine.getElencoDispositive().add(new DispositivaAmlContainer(pdf));
			initImporti(csc, origine);
			return origine;
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void init(ClientSessionContext csc, PdfModel pdf, OrigineModel origine) throws EJBException {		
		try {
			
			initImporti(csc, origine);
	
			PdfPersonModel sottoscrittore = pdf.mainPdfData().getPerson(1);	
			BooleanType isPersonaFisica = new BooleanType(isPersonaConsiderataFisica(csc, sottoscrittore));
			
			CodDescDataList dl = CodiciTipoImportoPF.getCodTipoImportoPFDl();
			if(!isPersonaFisica.booleanValue())
				dl = CodiciTipoImportoPG.getCodTipoImportoPGDl();
			origine.setNumTipiImporto(dl.getCodDescCount());
			origine.addCodDescField("codTipoImportoPerTendina", dl);
			
			if(origine.getImportoContestualeTotale().doubleValue() > 0) {
				if(origine.getImportoRimborsiSwitchFondi().doubleValue() > 0)
					origine.aggiungiTipoImportoSwitchFondi(origine.getElencoImportiContestuali(), origine.getImportoRimborsiSwitchFondi().doubleValue());
				else
					origine.aggiungiTipoImporto(origine.getElencoImportiContestuali());
			}
			if(origine.getImportoFuturoTotale().doubleValue() > 0)
				origine.aggiungiTipoImporto(origine.getElencoImportiFuturi());
			
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}

	/***********************************************************************************************/
	private static final String SEZIONE_AML = "AML";
	/***********************************************************************************************/
	private static void initImporti(ClientSessionContext csc, OrigineModel origine) throws Exception {		
		
		String scriptImportoVersamentiContestuali = PdfConfig.getExtendedValue(csc, SEZIONE_AML, "SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_CONTESTUALI").toString();
		String scriptImportoVersamentiFuturi = PdfConfig.getExtendedValue(csc, SEZIONE_AML, "SCRIPT_ORIGINE_IMPORTO_VERSAMENTI_FUTURI").toString();
		String scriptImportoDoubleChance = PdfConfig.getExtendedValue(csc, SEZIONE_AML, "SCRIPT_ORIGINE_IMPORTO_DOUBLE_CHANCE").toString();
		
		JexlEngine jexlEngine = new JexlEngine();		
		double importoContestualeTotale = 0;
		double importoFuturoTotale = 0;
		double importoDoubleChance = 0;
		double importoRimborsiSwitchFondi = 0;
		for(int i=0;i<origine.getElencoDispositive().size();i++) {
			
			DispositivaAmlContainer dc = origine.getElencoDispositive().get(i);
			PdfAnagModel pdfAnag = AmlFacadeBean.pdfAnagContratto(dc.getPdf());
			PdfDataModel pdfData = AmlFacadeBean.pdfDataContratto(dc.getPdf());

			importoContestualeTotale += executeJexlScript(jexlEngine, scriptImportoVersamentiContestuali, pdfAnag, pdfData);
			importoFuturoTotale += executeJexlScript(jexlEngine, scriptImportoVersamentiFuturi, pdfAnag, pdfData);
			importoDoubleChance += executeJexlScript(jexlEngine, scriptImportoDoubleChance, pdfAnag, pdfData);
			
			// Gli switch predeterminano la creazione della prima tendina con il loro importo del rimborso
			if(dc.getPdf().mainPdfData().getIsSwitch().booleanValue()) { 
				DoubleType importoRimborso = (DoubleType)dc.getPdf().mainPdfData().read(PdfPredefinedFields.IMPORTO);
				if(importoRimborso != null)
					importoRimborsiSwitchFondi += importoRimborso.doubleValue();
			}
		}		
		
		if(importoContestualeTotale > 0)
			origine.setImportoContestualeTotale(new DoubleType(BigDecimal.valueOf(importoContestualeTotale).setScale(2, BigDecimal.ROUND_HALF_UP)));
		if(importoFuturoTotale > 0)
			origine.setImportoFuturoTotale(new DoubleType(BigDecimal.valueOf(importoFuturoTotale).setScale(2, BigDecimal.ROUND_HALF_UP)));
		if(importoDoubleChance > 0)
			origine.setImportoDoubleChence(new DoubleType(BigDecimal.valueOf(importoDoubleChance).setScale(2, BigDecimal.ROUND_HALF_UP)));
		if(importoRimborsiSwitchFondi > 0)
			origine.setImportoRimborsiSwitchFondi(new DoubleType(BigDecimal.valueOf(importoRimborsiSwitchFondi).setScale(2, BigDecimal.ROUND_HALF_UP)));
			
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static double executeJexlScript(JexlEngine jexlEngine, String scriptCode, 
											PdfAnagModel pdfAnag, PdfDataModel pdfData) throws Exception{
		try {
			Expression e = jexlEngine.createExpression(scriptCode);
		    JexlContext jc = new MapContext();
		    jc.set("da", new OrigineDataAccessors(pdfAnag, pdfData));
		    Double propValue = (Double)e.evaluate(jc);
			if(propValue == null || propValue < 0)
				return 0;
			return propValue;
		}catch(Throwable t){
			throw new Exception("Jexl error on code ["+scriptCode+"]: "+t.toString());
		}		
	}
	
	/***********************************************************************************************/
	// Usato anche dal driver del modulo AML adeguataverificainvestimento
	/***********************************************************************************************/
	public boolean isPersonaConsiderataFisica(ClientSessionContext csc, PdfPersonModel cli) throws EJBException{
		BooleanType isPersonaFisica = (BooleanType)cli.readProperty("isPersonaFisica");
		if(isPersonaFisica == null || isPersonaFisica.booleanValue())
			return true;
		if(cli.getNdg().isNull())
			return true;
		try {
			BooleanType isClienteFiduciaria = (BooleanType)new DAOObject(csc, DAO_XML_AML_NAME).executeQueryAccess("isClienteFiduciaria", cli).getSingleResult();
			return isClienteFiduciaria.booleanValue();
		}catch(DAOException daoe) {
			throw new EJBException(daoe.toString());
		}
	}
}
