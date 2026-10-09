package prgm.pdfwebformspolizzeutil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebformspolizzeutil.model.RecuperaStrategiaInvestimentoModel;
import prgm.pdfwebformspolizzeutil.model.RipartizioneStrategia;
import prgm.pdfwebformspolizzeutil.model.StrategiaInvestimentoDettaglioModel;
import prgm.pdfwebformspolizzeutil.model.StrategiaInvestimentoInputModel;
import prgm.pdfwebformspolizzeutil.model.StrategiaInvestimentoOutputModel;

public class StrategiaInvestimentoUtil {
	private StrategiaInvestimentoUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static final String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.StrategiaInvestimento";
	public static final String ACCESS_NAME = "infoCompletamento";

	public static Map<StringType, RipartizioneStrategia> infoCompletamento(ClientSessionContext csc, RecuperaStrategiaInvestimentoModel recuperaStrategiaInvestimentoModel) throws Exception, DAOException {

		HashMap<StringType, RipartizioneStrategia> strategiaMap = new HashMap<StringType, RipartizioneStrategia>();
		DAOObject dao = new DAOObject(csc, DAO_FILE_NAME);
		DAOOSBResultModel osbRes = dao.executeOSBAccess(ACCESS_NAME, recuperaStrategiaInvestimentoModel);
		if(osbRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_OK){

			for(int i=0;i<recuperaStrategiaInvestimentoModel.getInfoCompletamentoResponse().size();i++) {

				RipartizioneStrategia ripartizionestrategia;
				StrategiaInvestimentoInputModel infoCompletamentoInput = (StrategiaInvestimentoInputModel)recuperaStrategiaInvestimentoModel.getInfoCompletamentoInput().get(0);
				StrategiaInvestimentoOutputModel infoCompletamentoResponse = (StrategiaInvestimentoOutputModel)recuperaStrategiaInvestimentoModel.getInfoCompletamentoResponse().get(i);
				if(infoCompletamentoResponse!=null) {

					double importoResiduoDaProiettare = importoResiduoDaProiettare(infoCompletamentoInput, infoCompletamentoResponse);
					
					for(int j=0;j<infoCompletamentoResponse.getDettaglio().size();j++) {
						StringBuilder codProdottoLineaServizio = new  StringBuilder();
						codProdottoLineaServizio.append(infoCompletamentoResponse.getCodProdotto().toString());
						StrategiaInvestimentoDettaglioModel dettaglio = (StrategiaInvestimentoDettaglioModel)infoCompletamentoResponse.getDettaglio().get(j);
						codProdottoLineaServizio.append("_"+dettaglio.getCodNaturaFondo().toString());	
						codProdottoLineaServizio.append("_"+infoCompletamentoResponse.getTipoServz().toString());	
						
						if(strategiaMap.get(new StringType(codProdottoLineaServizio.toString()))==null) {							
							ripartizionestrategia = new RipartizioneStrategia();
							ripartizionestrategia.setCodProdotto(infoCompletamentoResponse.getCodProdotto());
							ripartizionestrategia.setTipoServz(infoCompletamentoResponse.getTipoServz());
							ripartizionestrategia.setLinea(dettaglio.getCodNaturaFondo());
							strategiaMap.put(new StringType(codProdottoLineaServizio.toString()), ripartizionestrategia);
						}else {
							ripartizionestrategia = strategiaMap.get(new StringType(codProdottoLineaServizio.toString()));
						}
						
						DoubleType importoDestinazione ;
						if(dettaglio.getRuoloFondo().equals("DESTINAZIONE") && 
								(strategiaMap.get(new StringType(codProdottoLineaServizio.toString())))!=null &&
								!strategiaMap.get(new StringType(codProdottoLineaServizio.toString())).getImportoDestinazione().isNull()) {
							
							if (infoCompletamentoResponse.getTipoServz().equalsIgnoreCase("DC")) {
								importoDestinazione = ripartizionestrategia.getImportoDestinazione();
								importoDestinazione = new DoubleType(importoDestinazione.doubleValue()+ dettaglio.getImpPianoResid().doubleValue());
							
							} else {
								double importoPianoResiduo = 0;
								if (importoResiduoDaProiettare > -1) {
										importoPianoResiduo = importoResiduoDaProiettare * dettaglio.getPrcRipart().doubleValue() / 100;
								}
								importoDestinazione = new DoubleType(ripartizionestrategia.getImportoDestinazione().doubleValue()+ importoPianoResiduo);
							}
							ripartizionestrategia.setImportoDestinazione(importoDestinazione);
							strategiaMap.put(new StringType(codProdottoLineaServizio.toString()), ripartizionestrategia);
						}

						DoubleType importoPartenza;						
						if(dettaglio.getRuoloFondo().equals("PARTENZA") &&
								(strategiaMap.get(new StringType(codProdottoLineaServizio.toString())))!=null &&
								!((RipartizioneStrategia)strategiaMap.get(new StringType(codProdottoLineaServizio.toString()))).getImportoPartenza().isNull()) {

							importoPartenza = strategiaMap.get(new StringType(codProdottoLineaServizio.toString())).getImportoPartenza();
							if (importoResiduoDaProiettare > -1) {
								importoPartenza = new DoubleType(importoPartenza.doubleValue()+ importoResiduoDaProiettare);
							}
							ripartizionestrategia.setImportoPartenza(importoPartenza);
							strategiaMap.put(new StringType(codProdottoLineaServizio.toString()), ripartizionestrategia);
						}													
					}
				}
			}
		}else {
			throw new DAOException();
		}

		return strategiaMap;

	}
	
	private static double importoResiduoDaProiettare(StrategiaInvestimentoInputModel infoCompletamentoInput, StrategiaInvestimentoOutputModel infoCompletamentoResponse) {
		
		HashMap<StringType, DoubleType> fondiDaDisinvestire = infoCompletamentoInput.getFondiDaDisinvestire();
		if (fondiDaDisinvestire == null)
			return -1;
		
		for(int j=0;j<infoCompletamentoResponse.getDettaglio().size();j++) {
			
			StrategiaInvestimentoDettaglioModel dettaglio = (StrategiaInvestimentoDettaglioModel)infoCompletamentoResponse.getDettaglio().get(j);
			
			if(dettaglio.getRuoloFondo().equals("PARTENZA")) {				
				StringType codIsinServ = dettaglio.getCodIsin();

				if (fondiDaDisinvestire.get(codIsinServ)!=null) {
					return dettaglio.getImpPianoResid().doubleValue() - fondiDaDisinvestire.get(codIsinServ).doubleValue();
				}
			}
		}
		
		return -1;
	}
	
	public static BigDecimal importoFondiStrategia(BigDecimal importoFondi, StringType codProdotto,
			Map<StringType, RipartizioneStrategia> resultMap, String linea, String tipoServ) {
		
		StringBuilder key = new StringBuilder();
		key.append(codProdotto.toString()+"_"+ linea + "_"+tipoServ);		
		if(resultMap!=null && resultMap.get(new StringType(key.toString()))!=null) {
			StringType tipoServizio = resultMap.get(new StringType(key.toString())).getTipoServz();
			if(tipoServizio.equalsIgnoreCase("BC") ||
					tipoServizio.equalsIgnoreCase("IIS")) {		
				importoFondi = BigDecimal.valueOf(importoFondi.doubleValue()-resultMap.get(new StringType(key.toString())).getImportoPartenza().doubleValue());		
			}
			importoFondi = BigDecimal.valueOf(importoFondi.doubleValue()+resultMap.get(new StringType(key.toString())).getImportoDestinazione().doubleValue());
		}
		return importoFondi;
	}

}
