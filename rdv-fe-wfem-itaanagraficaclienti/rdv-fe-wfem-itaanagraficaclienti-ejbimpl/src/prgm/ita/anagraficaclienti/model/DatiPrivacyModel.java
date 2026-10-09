package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiPrivacyModel extends AbstractSectionModel{

	private StringType flagLiberatoria = new StringType();
	private DateType   dataLiberatoria = new DateType();
	private StringType flagCarte = new StringType();
	private DateType   dataCarte = new DateType();
	private StringType flagExtraUE = new StringType();
	private DateType   dataExtraUE = new DateType();
	private StringType flagProfilazione = new StringType();	// dato agg 6023  primo carattere
	private DateType   dataProfilazione = new DateType();	// dato agg 6023  da secondo al nono carattere
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DatiPrivacyModel(){
		
		frontendPropName.add("datiPrivacy_flagCarte");
		frontendPropName.add("datiPrivacy_dataCarte");
		frontendPropName.add("datiPrivacy_flagLiberatoria");
		frontendPropName.add("datiPrivacy_dataLiberatoria");
		frontendPropName.add("datiPrivacy_flagExtraUE");
		frontendPropName.add("datiPrivacy_dataExtraUE");
		frontendPropName.add("datiPrivacy_flagProfilazione");
		frontendPropName.add("datiPrivacy_dataProfilazione");
		
		CodDescDataList sinoList = new CodDescDataList();
		CodDescData sino = null;
		sino = new CodDescData(); sino.setCod("S"); sino.setDescr("Si");
		sinoList.addCodDescData(sino);
		sino = new CodDescData(); sino.setCod("N"); sino.setDescr("No");
		sinoList.addCodDescData(sino);
		
		addCodDescField("flagLiberatoria",sinoList);
		addCodDescField("flagCarte",sinoList);
		addCodDescField("flagExtraUE",sinoList);
		addCodDescField("flagProfilazione",sinoList);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setFlagLiberatoria(StringType flagLiberatoria) {
		if(!flagLiberatoria.equals("S") && !flagLiberatoria.equals("N"))
			flagLiberatoria.setStringValue("");
		this.flagLiberatoria = flagLiberatoria;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setFlagCarte(StringType flagCarte) {
		if(!flagCarte.equals("S") && !flagCarte.equals("N"))
			flagCarte.setStringValue("");
		this.flagCarte = flagCarte;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setFlagExtraUE(StringType flagExtraUE) {
		if(!flagExtraUE.equals("S") && !flagExtraUE.equals("N"))
			flagExtraUE.setStringValue("");
		this.flagExtraUE = flagExtraUE;
	}

	
	public DateType getDataExtraUE() {
		return dataExtraUE;
	}

	public DateType getDataLiberatoria() {
		return dataLiberatoria;
	}

	public StringType getFlagExtraUE() {
		return flagExtraUE;
	}

	public StringType getFlagLiberatoria() {
		return flagLiberatoria;
	}

	public void setDataExtraUE(DateType dataExtraUE) {
		this.dataExtraUE = dataExtraUE;
	}

	public void setDataLiberatoria(DateType dataLiberatoria) {
		this.dataLiberatoria = dataLiberatoria;
	}

	public DateType getDataCarte() {
		return dataCarte;
	}

	public StringType getFlagCarte() {
		return flagCarte;
	}

	public void setDataCarte(DateType dataCarte) {
		this.dataCarte = dataCarte;
	}

	public StringType getFlagProfilazione() {
		return flagProfilazione;
	}

	public void setFlagProfilazione(StringType flagProfilazione) {
		this.flagProfilazione = flagProfilazione;
	}

	public DateType getDataProfilazione() {
		return dataProfilazione;
	}

	public void setDataProfilazione(DateType dataProfilazione) {
		this.dataProfilazione = dataProfilazione;
	}
}
