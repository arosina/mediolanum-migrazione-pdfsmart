package prgm.ita.anagraficaclienti.pcp;

import java.math.BigDecimal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class ProfiloPcpClienteModel extends CommandDataModel {

	private static String REDSTYLE = " style='color:orangered;'";
	private static String ENDSUFF = " - ";
	private static String NA = "NON ATTIVO";
	
	private DateType 		dataInizioValiditaPCP = new DateType();
	private DateType 		dataFineValiditaPCP = new DateType();
	private ListType 		indicatoriPCP = new ListType(IndicatoreProfiloPcpClienteModel.class);
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public String getHtmlProfiloPcp() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHtmlIndicatorePcp("Profilo dell'investitore", getProfilo(), getProfilo().isNull() ? "" : ENDSUFF));
		if(!getProfilo().isNull()) {
			sb.append(getHtmlIndicatorePcp("Orizzonte temporale", getOrizzonteTemporale(), ENDSUFF));
			sb.append(getHtmlIndicatorePcp("Cemi", getEsposizioneFinanziaria(), ENDSUFF));
			sb.append(getHtmlIndicatorePcp("Data scadenza PCP", getDataFineValiditaPCP(), "<br>"));
			if(!getCapacitaDiSospendereLePerdite().isNull())
				sb.append(getHtmlIndicatorePcp("Capacità di sostenere le perdite", getCapacitaDiSospendereLePerdite(), ENDSUFF));	
			if(!getEsigenzaMoltoBreve().isNull() || !getEsigenzaBreve().isNull() || !getEsigenzaMedio().isNull() || !getEsigenzaLungo().isNull())
				sb.append(getHtmlIndicatorePcpMultiplo("Esigenze temporali di liquidità", 
															new String[]{"Molto breve", "Breve", "Medio", "Lungo"},
															new StringType[] {getEsigenzaMoltoBreve(), getEsigenzaBreve(), getEsigenzaMedio(), getEsigenzaLungo()}, ENDSUFF));	
			if(!getEsg().isNull())
				sb.append(getHtmlIndicatorePcp("Rilevanza della sostenibilità degli investimenti - ESG", getEsg(), ""));
		}
		return sb.toString();
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private String getHtmlIndicatorePcp(String nome, AbstractType valore, String endSuff) {
		if(valore.isNull())
			valore = new StringType(NA);
		else
			valore = new StringType(Tools.capitalize(valore.toString()));
		return nome+": <span "+REDSTYLE+">"+valore+"</span>"+endSuff;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private String getHtmlIndicatorePcpMultiplo(String nome, String[] labels, StringType[] valori, String endSuff) {
		StringBuilder vals = new StringBuilder();
		for(int i=0; i<valori.length;i++){
			if(!valori[i].isNull())
				vals.append(labels[i]+": "+valori[i]+", ");
		}
		String val = NA;
		if(vals.length() > 0)
			val = vals.substring(0, vals.length()-2);
		return nome+": <span "+REDSTYLE+">"+Tools.capitalize(val)+"</span>"+endSuff;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getProfilo() {
		return findIndicatore("tolrischio");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getOrizzonteTemporale() {
		return findIndicatore("orizztempo");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsposizioneFinanziaria() {
		return findIndicatore("espconfina");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getCapacitaDiSospendereLePerdite() {
		return findIndicatore("capsostper");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsigenzaMoltoBreve() {
		return findIndicatorePercento("esliqmbrev");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsigenzaBreve() {
		return findIndicatorePercento("esliqbreve");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsigenzaMedio() {
		return findIndicatorePercento("esliqmedio");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsigenzaLungo() {
		return findIndicatorePercento("esliqlungo");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsg() {
		return findIndicatore("esgindicat");
	}
			
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formattaData(String value) {
		if(value == null) 
			return "";

		if(value.length() >= 8)
			value = value.substring(0,8);

		String aa = value.substring(0,4);
		String mm = value.substring(4,6);
		String gg = value.substring(6,8);
		return gg + "-" + mm + "-" + aa;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private StringType findIndicatorePercento(String indicatore) {
		StringType ind = findIndicatore(indicatore);
		if(!ind.isNull()) {
			try {
				Double perc = new Double(ind.toString());
				if(perc == 0)
					return new StringType();
				perc = perc * 100;
				ind = new StringType(new DoubleType(BigDecimal.valueOf(perc).setScale(2, BigDecimal.ROUND_HALF_UP)).toString()+"%");
			}catch(NumberFormatException nge) {}
		}
		return ind;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private StringType findIndicatore(String indicatore) {
		for(int i=0;i<getIndicatoriPCP().size();i++) {
			IndicatoreProfiloPcpClienteModel ind = (IndicatoreProfiloPcpClienteModel)getIndicatoriPCP().get(i);
			if(ind.getCodiceIndicatore().equals(indicatore))
				return ind.getValoreIndicatore();
		}
		return new StringType();
	}

	public DateType getDataInizioValiditaPCP() {
		return dataInizioValiditaPCP;
	}

	public void setDataInizioValiditaPCP(DateType dataInizioValiditaPCP) {
		this.dataInizioValiditaPCP = dataInizioValiditaPCP;
	}

	public DateType getDataFineValiditaPCP() {
		return dataFineValiditaPCP;
	}

	public void setDataFineValiditaPCP(DateType dataFineValiditaPCP) {
		this.dataFineValiditaPCP = dataFineValiditaPCP;
	}

	public ListType getIndicatoriPCP() {
		return indicatoriPCP;
	}

	public void setIndicatoriPCP(ListType indicatoriPCP) {
		this.indicatoriPCP = indicatoriPCP;
	}

}
