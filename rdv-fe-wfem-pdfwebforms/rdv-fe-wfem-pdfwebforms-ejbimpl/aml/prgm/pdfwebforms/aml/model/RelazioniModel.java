package prgm.pdfwebforms.aml.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class RelazioniModel extends AbstractSezioneAmlModel{
	
	private static final String TE_CAMPO_OBBLIGATORIO = "Campo obbligatorio";

	private Map<String, SoggettoRelazioneModel> mappaSoggetti = new LinkedHashMap<String, SoggettoRelazioneModel>();
	private ListType elencoSoggetti = new ListType(SoggettoRelazioneModel.class);

	private StringType	globalSoggAccordionStatus = new StringType();
	private IntegerType	idxSoggettoSelezionato = new IntegerType();
	private boolean		relazioniContrastanti;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public String drawIcons() {
		int numEl = 0;
		int numErr = 0;
		int numOk = 0;
		for(int i=0;i<getElencoSoggetti().size();i++) {
			
			SoggettoRelazioneModel sogg = (SoggettoRelazioneModel)getElencoSoggetti().get(i);
			if(sogg.iSoggettoVisibileInAML()) {
				String imgName = drawSoggIcons(sogg);
				if(AmlModel.OK_IMG.equals(imgName))
					numOk++;
				else if(AmlModel.ERROR_IMG.equals(imgName))
					numErr++;
				numEl++;
			}

			if(sogg.getIsAssicurato().booleanValue() && sogg.getElencoBeneficiariAssicurato().size() > 0) {
				for(int j=0;j<sogg.getElencoBeneficiariAssicurato().size();j++) {
					SoggettoRelazioneModel beneficiario = (SoggettoRelazioneModel)sogg.getElencoBeneficiariAssicurato().get(j);
					String imgName = drawBeneficiarioIcons(beneficiario);
					if(AmlModel.OK_IMG.equals(imgName))
						numOk++;
					else if(AmlModel.ERROR_IMG.equals(imgName))
						numErr++;
					numEl++;
				}
			}
		}
		if(numErr > 0)
			return AmlModel.ERROR_IMG;
		else if(numOk == numEl)
			return AmlModel.OK_IMG;
		else
			return "";
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawSoggIcons(SoggettoRelazioneModel sogg) {
		ListType beneficiari = sogg.getElencoBeneficiariAssicurato();
		sogg.setElencoBeneficiariAssicurato(new ListType());
		try {
			if(Tools.containsTypeErrors(sogg))
				return AmlModel.ERROR_IMG; 
		}catch(Exception e){
			return "";
		}finally {
			sogg.setElencoBeneficiariAssicurato(beneficiari);
		}

		if(sogg.getCodTipoRelazioneContraente().isNull())
			return "";
		if(sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO) && sogg.getDescrTipoRelazioneContraente().isNull())
			return "";
		if((sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_AZIENDALE) || 
			sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO)) && sogg.getInformazioniRelazioneContraente().isNull())
			return "";
		if(sogg.getIsTerzoPagatore().booleanValue() && sogg.getTipoMotivazionePagamentoTerzoPagatore().isNull())
			return "";
		if(sogg.getIsAssicurato().booleanValue() && sogg.getMotivazioneAssicuratoDiversoDaContraente().isNull())
			return "";
		return AmlModel.OK_IMG;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawBeneficiarioIcons(SoggettoRelazioneModel beneficiario) {
		try {
			if(Tools.containsTypeErrors(beneficiario))
				return AmlModel.ERROR_IMG; 
		}catch(Exception e){
			return "";
		}

		if((beneficiario.getCodTipoRelazioneAssicurando().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_AZIENDALE) || 
			beneficiario.getCodTipoRelazioneAssicurando().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO)) && 
			beneficiario.getInformazioniRelazioneAssicurando().isNull())
			return "";
		return AmlModel.OK_IMG;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean verify() throws Exception {		
		Tools.resetTypesWarningAndErrors(this);
		for(int i=0;i<getElencoSoggetti().size();i++) {
			SoggettoRelazioneModel sogg = (SoggettoRelazioneModel)getElencoSoggetti().get(i);
			if(!sogg.iSoggettoVisibileInAML())
				continue;
			if(sogg.getCodTipoRelazioneContraente().isNull())
				sogg.getCodTipoRelazioneContraente().addTypeError(TE_CAMPO_OBBLIGATORIO);
			
			if(sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO) && sogg.getDescrTipoRelazioneContraente().isNull())
				sogg.getDescrTipoRelazioneContraente().addTypeError(TE_CAMPO_OBBLIGATORIO);
			
			if((sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_AZIENDALE) || 
				sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO)) && sogg.getInformazioniRelazioneContraente().isNull())
				sogg.getInformazioniRelazioneContraente().addTypeError(TE_CAMPO_OBBLIGATORIO);
			
			if(sogg.getIsTerzoPagatore().booleanValue() && sogg.getTipoMotivazionePagamentoTerzoPagatore().isNull())
				sogg.getTipoMotivazionePagamentoTerzoPagatore().addTypeError(TE_CAMPO_OBBLIGATORIO);

			if(sogg.getIsAssicurato().booleanValue() && sogg.getMotivazioneAssicuratoDiversoDaContraente().isNull())
				sogg.getMotivazioneAssicuratoDiversoDaContraente().addTypeError(TE_CAMPO_OBBLIGATORIO);

			// Se il tipo non è "ALTRO" svuoto la descrizione
			if(!sogg.getCodTipoRelazioneContraente().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO))
				sogg.setDescrTipoRelazioneContraente(new StringType());

			// Beneficiari assicurato
			if(sogg.getIsAssicurato().booleanValue() && sogg.getElencoBeneficiariAssicurato().size() > 0) {
				for(int j=0;j<sogg.getElencoBeneficiariAssicurato().size();j++) {
					SoggettoRelazioneModel beneficiario = (SoggettoRelazioneModel)sogg.getElencoBeneficiariAssicurato().get(j);
					if((beneficiario.getCodTipoRelazioneAssicurando().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_AZIENDALE) || 
						beneficiario.getCodTipoRelazioneAssicurando().equals(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO)) && 
						beneficiario.getInformazioniRelazioneAssicurando().isNull())
						beneficiario.getInformazioniRelazioneAssicurando().addTypeError(TE_CAMPO_OBBLIGATORIO);
				}
			}
			
		}
		return !Tools.containsTypeErrors(this);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasSoggettiTerzi() {
		for(int assIdx=0;assIdx<getElencoSoggetti().size();assIdx++){ 
			SoggettoRelazioneModel assicurato = (SoggettoRelazioneModel)getElencoSoggetti().get(assIdx);
			if(assicurato.getIsAssicurato().booleanValue() && assicurato.getElencoBeneficiariAssicurato().size() > 0)
				return true;;
		}
		return false;
	}
	
	/***********************************************************************************************/
	// Usato dal driver del modulo AML adeguataverificainvestimento
	/***********************************************************************************************/
	public SoggettoRelazioneModel findSoggetto(SoggettoRelazioneModel otherSogg) {
		for(int i=0;i<getElencoSoggetti().size();i++) {
			SoggettoRelazioneModel sogg = (SoggettoRelazioneModel)getElencoSoggetti().get(i);
			if(sogg.sameAs(otherSogg))
				return sogg;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isClose(int globalSoggAccordionStatusIdx) {
		return getGlobalSoggAccordionStatus().toString().charAt(globalSoggAccordionStatusIdx) == '0';
	}

	public Map<String, SoggettoRelazioneModel> getMappaSoggetti() {
		return mappaSoggetti;
	}

	public void setMappaSoggetti(Map<String, SoggettoRelazioneModel> mappaSoggetti) {
		this.mappaSoggetti = mappaSoggetti;
	}

	public ListType getElencoSoggetti() {
		return elencoSoggetti;
	}

	public void setElencoSoggetti(ListType elencoSoggetti) {
		this.elencoSoggetti = elencoSoggetti;
	}

	public boolean isRelazioniContrastanti() {
		return relazioniContrastanti;
	}

	public void setRelazioniContrastanti(boolean relazioniContrastanti) {
		this.relazioniContrastanti = relazioniContrastanti;
	}

	public IntegerType getIdxSoggettoSelezionato() {
		return idxSoggettoSelezionato;
	}

	public void setIdxSoggettoSelezionato(IntegerType idxSoggettoSelezionato) {
		this.idxSoggettoSelezionato = idxSoggettoSelezionato;
	}

	public StringType getGlobalSoggAccordionStatus() {
		return globalSoggAccordionStatus;
	}

	public void setGlobalSoggAccordionStatus(StringType globalSoggAccordionStatus) {
		this.globalSoggAccordionStatus = globalSoggAccordionStatus;
	}

}
