package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.mom.AzioneMomDataModel;
import prgm.pdfwebforms.mom.MomEventDataModel;
import prgm.pdfwebforms.mom.MomEventParamModel;
import prgm.pdfwebforms.mom.ParametroAzioneMomDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PritMomInfoModel extends CommandDataModel {

	private IntegerType			codProdottoPrit = null;
	private IntegerType			codOperazionePrit = null;
	private ListType			prodottiAttivati = null;
	private AzioneMomDataModel	ultimaAzioneMom = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initUltimaAzioneMom(MomEventDataModel momEventData){
		AzioneMomDataModel ultimaAzioneMom = new AzioneMomDataModel();
		ultimaAzioneMom.setAzione(momEventData.getAzioneMom());
		for(int i=0;i < momEventData.getParametri().size(); i++){
			MomEventParamModel pEvent = (MomEventParamModel)momEventData.getParametri().get(i);
			String[] nomeValore = pEvent.getNomeValore().toString().split("\\|");
			ParametroAzioneMomDataModel pData = new ParametroAzioneMomDataModel();
			if(nomeValore.length > 0){
				pData.setNome(new StringType(nomeValore[0]));
				if(nomeValore.length > 1)
					pData.setValore(new StringType(nomeValore[1]));
				ultimaAzioneMom.getParametri().add(pData);
			}
		}
		setUltimaAzioneMom(ultimaAzioneMom);
	}
	
	public IntegerType getCodProdottoPrit() {
		return codProdottoPrit;
	}
	public void setCodProdottoPrit(IntegerType codProdottoPrit) {
		this.codProdottoPrit = codProdottoPrit;
	}
	public IntegerType getCodOperazionePrit() {
		return codOperazionePrit;
	}
	public void setCodOperazionePrit(IntegerType codOperazionePrit) {
		this.codOperazionePrit = codOperazionePrit;
	}
	public ListType getProdottiAttivati() {
		return prodottiAttivati;
	}
	public void setProdottiAttivati(ListType prodottiAttivati) {
		this.prodottiAttivati = prodottiAttivati;
	}
	public AzioneMomDataModel getUltimaAzioneMom() {
		return ultimaAzioneMom;
	}
	public void setUltimaAzioneMom(AzioneMomDataModel ultimaAzioneMom) {
		this.ultimaAzioneMom = ultimaAzioneMom;
	}

}
