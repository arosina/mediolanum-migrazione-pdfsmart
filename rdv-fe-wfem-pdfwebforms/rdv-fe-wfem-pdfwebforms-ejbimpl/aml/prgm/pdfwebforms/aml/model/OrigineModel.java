package prgm.pdfwebforms.aml.model;

import java.math.BigDecimal;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrigineModel extends AbstractSezioneAmlModel{
	
	private static final String COD_TIPO_IMPORTO_FIELD_NAME = "codTipoImporto";
	
	public static String COD_TIPO_IMPORTO_DISINVESTIMENTO_GRUPPO = "1";
	public static String COD_TIPO_IMPORTO_ORIGINE_ALTRO = "0";
	
	private StringType 		codTipoImportoPerTendina = new StringType();
	
	private DoubleType		importoTotaleDigitatoContestuali = new DoubleType();
	private DoubleType		importoTotaleDigitatoFuturi = new DoubleType();
	
	private DoubleType		importoContestualeTotale = new DoubleType();
	private DoubleType		importoFuturoTotale = new DoubleType();
	private DoubleType		importoRimborsiSwitchFondi = new DoubleType();
	private DoubleType		importoDoubleChence = new DoubleType();
	
	private ListType		elencoImportiContestuali = new ListType(TipoImportoOrigineModel.class);
	private StringType		descrVoceAltroImportiContestuali = new StringType();
	
	private ListType		elencoImportiFuturi = new ListType(TipoImportoOrigineModel.class);
	private StringType		descrVoceAltroImportiFuturi = new StringType();
	
	private IntegerType		idxImportoSelezionato = new IntegerType();
	private StringType		sezioneImporti = new StringType();
	private int				numTipiImporto;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public String drawIcons() {
		String res = super.drawIcons();
		if(!res.isEmpty())
			return res;

		if(!showOkImg(getImportoContestualeTotale(), getElencoImportiContestuali(), getDescrVoceAltroImportiContestuali()) || 
		   !showOkImg(getImportoFuturoTotale(), getElencoImportiFuturi(), getDescrVoceAltroImportiFuturi()))
			return "";
		
		return AmlModel.OK_IMG;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean showOkImg(DoubleType importoTotale, ListType elenco, StringType descrVoceAltro) {
		if(importoTotale.doubleValue() == 0)
			return true;
		boolean tuttiVuoti = true;
		for(int i=0;i<elenco.size();i++) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
			if(ti.getCodTipoImporto().isNull() && ti.getImporto().isNull())
				continue;
			tuttiVuoti = false;
			if((ti.getCodTipoImporto().isNull() && !ti.getImporto().isNull()) || 
			   (ti.getImporto().isNull() && !ti.getCodTipoImporto().isNull()))
				return false;
			if(ti.getCodTipoImporto().equals(COD_TIPO_IMPORTO_ORIGINE_ALTRO) && descrVoceAltro.isNull())
				return false;
			if(ti.getCodTipoImporto().isNull() && ti.getImporto().isNull())
				return false;
		}
		if(tuttiVuoti)
			return false;
		return true;
	}
	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void aggiungiTipoImporto(ListType elenco) {		
		TipoImportoOrigineModel ti = new TipoImportoOrigineModel();
		ti.addCodDescField(COD_TIPO_IMPORTO_FIELD_NAME, creaTendinaTipiImporto(elenco, ti));
		elenco.add(ti);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void aggiungiTipoImportoSwitchFondi(ListType elenco, double importoRimborsiSwitchFondi) {		
		TipoImportoOrigineModel ti = new TipoImportoOrigineModel();
		ti.setCodTipoImporto(new StringType(COD_TIPO_IMPORTO_DISINVESTIMENTO_GRUPPO));
		ti.setImporto(new DoubleType(BigDecimal.valueOf(importoRimborsiSwitchFondi).setScale(2, BigDecimal.ROUND_HALF_UP)));
		ti.setImportoOriginale(new DoubleType(BigDecimal.valueOf(importoRimborsiSwitchFondi).setScale(2, BigDecimal.ROUND_HALF_UP)));
		ti.setCodTipoImportoPreselezionatoSwitchFondi(true);
		ti.addCodDescField(COD_TIPO_IMPORTO_FIELD_NAME, creaTendinaTipiImporto(elenco, ti));
		elenco.add(ti);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void aggiornaTendineTipiImporto() {
		ListType elenco = getElencoImportiContestuali();
		StringType descrAltro = getDescrVoceAltroImportiContestuali();
		if(getSezioneImporti().equals("Futuri")) {
			elenco = getElencoImportiFuturi();
			descrAltro = getDescrVoceAltroImportiFuturi();
		}
		descrAltro.resetTypeErrors();
		for(int i=0;i<elenco.size();i++) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
			ti.addCodDescField(COD_TIPO_IMPORTO_FIELD_NAME, creaTendinaTipiImporto(elenco, ti));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private CodDescDataList creaTendinaTipiImporto(ListType elenco, TipoImportoOrigineModel importoSelezionato) {
		CodDescDataList baseDl = getCodDescDataList("codTipoImportoPerTendina");
		CodDescDataList dl = new CodDescDataList();
		for(int i=0;i<baseDl.getCodDescCount();i++) {
			CodDescData d = baseDl.getCodDesc(i);
			if(importoSelezionato.getCodTipoImporto().equals(d.getCod()) || !isTipoImportoGiaSelezionato(d.getCod(), elenco))
				dl.addCodDescData(d);
		}		
		return dl;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isTipoImportoGiaSelezionato(String codTipoImporto, ListType elenco) {
		for(int i=0;i<elenco.size();i++) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
			if(ti.getCodTipoImporto().equals(codTipoImporto))
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eliminaTipoImporto() {
		int idx = getIdxImportoSelezionato().intValue();		
		ListType elenco = getElencoImportiContestuali();
		if(getSezioneImporti().equals("Futuri"))
			elenco = getElencoImportiFuturi();
		if(idx >= 0 && idx < elenco.size())
			elenco.remove(idx);
		aggiornaTendineTipiImporto();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean verify() throws Exception {		
		Tools.resetTypesWarningAndErrors(this);		
		verifyElencoImporti(getElencoImportiContestuali(), getImportoContestualeTotale(), getImportoTotaleDigitatoContestuali(), getDescrVoceAltroImportiContestuali());
		verifyElencoImporti(getElencoImportiFuturi(), getImportoFuturoTotale(), getImportoTotaleDigitatoFuturi(), getDescrVoceAltroImportiFuturi());
		return !Tools.containsTypeErrors(this);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verifyElencoImporti(ListType elenco, DoubleType importoTotale, DoubleType importoTotaleDigitato, StringType descrVoceAltro) {
		double tot = 0;
		boolean tuttiVuoti = true;
		for(int i=0;i<elenco.size();i++) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
			if(ti.getCodTipoImporto().isNull() && ti.getImporto().isNull())
				continue;
			tuttiVuoti = false;
			if(ti.getCodTipoImporto().isNull())
				ti.getCodTipoImporto().addTypeError("Selezionare un valore");
			if(ti.getImporto().isNull())
				ti.getImporto().addTypeError("Inserire l'importo in euro");			
			else
				tot += ti.getImporto().doubleValue();
			
			if(ti.isCodTipoImportoPreselezionatoSwitchFondi() && ti.getImporto().doubleValue() < ti.getImportoOriginale().doubleValue())
				ti.getImporto().addTypeError("L'importo minimo previsto per questa tipologia è di euro "+ti.getImportoOriginale()+" in quanto coincide con l'importo derivante da operazioni di switch");
		}
		if(tuttiVuoti && elenco.size() > 0) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(0);
			ti.getCodTipoImporto().addTypeError("Indicare almeno una tipologia e un importo");
		}
		
		if(tot >= 0 && tot != importoTotale.doubleValue())
			importoTotaleDigitato.addTypeError("La somma degli importi indicati non coincide con l'importo totale");
		
		if(isSelezionatoAltro(elenco)) {
			if(descrVoceAltro.isNull())
				descrVoceAltro.addTypeError("Campo obbligatorio");
		}else {
			 descrVoceAltro.setStringValue("");
		}
		
		// Elimino le righe vuote, a meno che non lo siano tutte
		if(!tuttiVuoti) { 
			for(int i=elenco.size()-1;i>=0;i--) {
				TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
				if(ti.getCodTipoImporto().isNull() && ti.getImporto().isNull())
					elenco.remove(i);
			}			
		}
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isSelezionatoAltro(ListType elenco) {
		for(int i=0;i<elenco.size();i++) {
			TipoImportoOrigineModel ti = (TipoImportoOrigineModel)elenco.get(i);
			if(ti.getCodTipoImporto().equals(COD_TIPO_IMPORTO_ORIGINE_ALTRO))
				return true;
		}
		return false;
	}
	
	public DoubleType getImportoContestualeTotale() {
		return importoContestualeTotale;
	}
	public void setImportoContestualeTotale(DoubleType importoContestualeTotale) {
		this.importoContestualeTotale = importoContestualeTotale;
	}
	public DoubleType getImportoFuturoTotale() {
		return importoFuturoTotale;
	}
	public void setImportoFuturoTotale(DoubleType importoFuturoTotale) {
		this.importoFuturoTotale = importoFuturoTotale;
	}
	public ListType getElencoImportiContestuali() {
		return elencoImportiContestuali;
	}
	public void setElencoImportiContestuali(ListType elencoImportiContestuali) {
		this.elencoImportiContestuali = elencoImportiContestuali;
	}
	public ListType getElencoImportiFuturi() {
		return elencoImportiFuturi;
	}
	public void setElencoImportiFuturi(ListType elencoImportiFuturi) {
		this.elencoImportiFuturi = elencoImportiFuturi;
	}
	public int getNumTipiImporto() {
		return numTipiImporto;
	}
	public void setNumTipiImporto(int numTipiImporto) {
		this.numTipiImporto = numTipiImporto;
	}
	public IntegerType getIdxImportoSelezionato() {
		return idxImportoSelezionato;
	}
	public void setIdxImportoSelezionato(IntegerType idxImportoSelezionato) {
		this.idxImportoSelezionato = idxImportoSelezionato;
	}
	public StringType getCodTipoImportoPerTendina() {
		return codTipoImportoPerTendina;
	}
	public void setCodTipoImportoPerTendina(StringType codTipoImportoPerTendina) {
		this.codTipoImportoPerTendina = codTipoImportoPerTendina;
	}

	public StringType getDescrVoceAltroImportiContestuali() {
		return descrVoceAltroImportiContestuali;
	}

	public void setDescrVoceAltroImportiContestuali(StringType descrVoceAltroImportiContestuali) {
		this.descrVoceAltroImportiContestuali = descrVoceAltroImportiContestuali;
	}

	public StringType getDescrVoceAltroImportiFuturi() {
		return descrVoceAltroImportiFuturi;
	}

	public void setDescrVoceAltroImportiFuturi(StringType descrVoceAltroImportiFuturi) {
		this.descrVoceAltroImportiFuturi = descrVoceAltroImportiFuturi;
	}

	public StringType getSezioneImporti() {
		return sezioneImporti;
	}

	public void setSezioneImporti(StringType sezioneImporti) {
		this.sezioneImporti = sezioneImporti;
	}

	public DoubleType getImportoTotaleDigitatoContestuali() {
		return importoTotaleDigitatoContestuali;
	}

	public void setImportoTotaleDigitatoContestuali(DoubleType importoTotaleDigitatoContestuali) {
		this.importoTotaleDigitatoContestuali = importoTotaleDigitatoContestuali;
	}

	public DoubleType getImportoTotaleDigitatoFuturi() {
		return importoTotaleDigitatoFuturi;
	}

	public void setImportoTotaleDigitatoFuturi(DoubleType importoTotaleDigitatoFuturi) {
		this.importoTotaleDigitatoFuturi = importoTotaleDigitatoFuturi;
	}

	public DoubleType getImportoRimborsiSwitchFondi() {
		return importoRimborsiSwitchFondi;
	}

	public void setImportoRimborsiSwitchFondi(DoubleType importoRimborsiSwitchFondi) {
		this.importoRimborsiSwitchFondi = importoRimborsiSwitchFondi;
	}

	public DoubleType getImportoDoubleChence() {
		return importoDoubleChence;
	}

	public void setImportoDoubleChence(DoubleType importoDoubleChence) {
		this.importoDoubleChence = importoDoubleChence;
	}

}
