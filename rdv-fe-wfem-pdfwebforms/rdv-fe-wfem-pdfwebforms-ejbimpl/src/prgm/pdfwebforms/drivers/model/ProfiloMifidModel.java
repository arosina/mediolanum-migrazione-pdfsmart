package prgm.pdfwebforms.drivers.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ProfiloMifidModel extends CommandDataModel{

	private boolean		notFound = false;
	
	private DateType	dataScadenza = new DateType();
	private ListType 	indicatoriPCP = new ListType(IndicatoreProfiloPcpClienteModel.class);
	
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
	private DoubleType findIndicatoreAsDouble(String indicatore) {
		DoubleType res = new DoubleType();
		StringType ind = findIndicatore(indicatore);
		if(!ind.isNull()) {
			try {
				Double perc = new Double(ind.toString());
				perc = perc * 100;
				res = new DoubleType(BigDecimal.valueOf(perc).setScale(2, BigDecimal.ROUND_HALF_UP));
			}catch(NumberFormatException nge) {}
		}
		return res;
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
	public DoubleType getEsigenzaMoltoBreve() {
		return findIndicatoreAsDouble("esliqmbrev");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DoubleType getEsigenzaBreve() {
		return findIndicatoreAsDouble("esliqbreve");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DoubleType getEsigenzaMedio() {
		return findIndicatoreAsDouble("esliqmedio");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DoubleType getEsigenzaLungo() {
		return findIndicatoreAsDouble("esliqlungo");
	}
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getEsg() {
		return findIndicatore("esgindicat");
	}

	public boolean isNotFound() {
		return notFound;
	}
	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}
	public DateType getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(DateType dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public ListType getIndicatoriPCP() {
		return indicatoriPCP;
	}
	public void setIndicatoriPCP(ListType indicatoriPCP) {
		this.indicatoriPCP = indicatoriPCP;
	}
	
	/***********************************************************************************************/
	/* Mappatura vecchi dati cedacri. 
	 * DA NON USARE PIU' CON L'INTRODUZIONE DEL NUOVO PCP (rfc #200558) */
	/***********************************************************************************************/
	private static Map<String, String> codesClusterCedacri = new HashMap<String, String>();
	static{
		codesClusterCedacri.put("intraprendente lungo", 	"CLUSTER1");
		codesClusterCedacri.put("intraprendente medio", 	"CLUSTER2");
		codesClusterCedacri.put("intraprendente breve",		"CLUSTER3");
		codesClusterCedacri.put("equilibrato lungo", 		"CLUSTER4");
		codesClusterCedacri.put("equilibrato medio", 		"CLUSTER5");
		codesClusterCedacri.put("equilibrato breve", 		"CLUSTER6");
		codesClusterCedacri.put("conservatore lungo",		"CLUSTER7");
		codesClusterCedacri.put("conservatore medio", 		"CLUSTER8");
		codesClusterCedacri.put("conservatore breve",	 	"CLUSTER9");
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCodProfiloDiInvestimento() {
		String profilo = getProfilo().toString();
		if(profilo.equalsIgnoreCase("CONSERVATORE"))
			return new StringType("CON");
		else if(profilo.equalsIgnoreCase("EQUILIBRATO"))
			return new StringType("EQU");
		else if(profilo.equalsIgnoreCase("INTRAPRENDENTE"))
			return new StringType("INT");
		return new StringType();
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCodClusterCedacri() {
		String desClusterCedacri = getProfilo()+" "+getOrizzonteTemporale();
		return new StringType(codesClusterCedacri.get(desClusterCedacri.toLowerCase()));
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCemi() {
		String espfina = getEsposizioneFinanziaria().toString();
		if(espfina.equalsIgnoreCase("ALTA"))
			return new StringType("ALT");
		else if(espfina.equalsIgnoreCase("MEDIA"))
			return new StringType("MED");
		else if(espfina.equalsIgnoreCase("BASSA"))
			return new StringType("BAS");
		return new StringType();
	}		
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setCodProfiloDiInvestimento(StringType codProfiloDiInvestimento) {
		// do nothing
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setCodClusterCedacri(StringType codClusterCedacri) {
		// do nothing
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setCemi(StringType cemi) {
		// do nothing
	}
}
