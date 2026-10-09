package prgm.ita.anagraficaclienti.questionari.model;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/*******************************************************************/
/*******************************************************************/
public class PatrimonioModel extends CommandDataModel {
	
	private String 	messaggioCentrale = "";
	private boolean salvato = false;
	private String  datiClienteInsufficientiMsg = "";
	
	private IntegerType domandaInErrore = new IntegerType(-1);

	private BooleanType showBack    = new BooleanType();
	private IntegerType scrollPosition = new IntegerType();
	
	private ClienteModel  cliente = new ClienteModel();
	private StringType    numSched = new StringType();    
	private StringType	  codProfiloDiInvestimento = new StringType();
	private StringType    codClusterCedacri 		= new StringType();
	private StringType    descrClusterCedacri 		= new StringType();	
	private IntegerType   dFinValCedacri  			= new IntegerType();
	private TimestampType dataOraCompilazione = new TimestampType();
	private BooleanType	  isPrivacyAllegata = new BooleanType();

	public BooleanType getIsPrivacyAllegata() {
		return isPrivacyAllegata;
	}

	public void setIsPrivacyAllegata(BooleanType isPrivacyAllegata) {
		this.isPrivacyAllegata = isPrivacyAllegata;
	}

	private ElementiPatrimonioModel elementiPatrimonio = new ElementiPatrimonioModel();
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public DateType getDataScadenzaProfiloCedacri(){		
		try{
			String  d = getDFinValCedacri().toString();
			return new DateType(d.substring(6)+"-"+d.substring(4,6)+"-"+d.substring(0,4));
		}catch(Exception e){
			return new DateType();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void appendMessaggioCentrale(String msg){
		messaggioCentrale = messaggioCentrale.concat(msg);
	}
	

	
	/*******************************************************************/
	/*******************************************************************/

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}

	public BooleanType getShowBack() {
		return showBack;
	}

	public void setShowBack(BooleanType showBack) {
		this.showBack = showBack;
	}

	public IntegerType getDomandaInErrore() {
		return domandaInErrore;
	}

	public void setDomandaInErrore(IntegerType domandaInErrore) {
		this.domandaInErrore = domandaInErrore;
	}

	public StringType getCodProfiloDiInvestimento() {
		return codProfiloDiInvestimento;
	}

	public void setCodProfiloDiInvestimento(StringType codProfiloDiInvestimento) {
		this.codProfiloDiInvestimento = codProfiloDiInvestimento;
	}

	public IntegerType getScrollPosition() {
		return scrollPosition;
	}

	public void setScrollPosition(IntegerType scrollPosition) {
		this.scrollPosition = scrollPosition;
	}

	public TimestampType getDataOraCompilazione() {
		return dataOraCompilazione;
	}

	public void setDataOraCompilazione(TimestampType dataOraCompilazione) {
		this.dataOraCompilazione = dataOraCompilazione;
	}

	public boolean isSalvato() {
		return salvato;
	}

	public void setSalvato(boolean salvato) {
		this.salvato = salvato;
	}

	public StringType getNumSched() {
		return numSched;
	}

	public void setNumSched(StringType numSched) {
		this.numSched = numSched;
	}

	public String getDatiClienteInsufficientiMsg() {
		return datiClienteInsufficientiMsg;
	}

	public void setDatiClienteInsufficientiMsg(String datiClienteInsufficientiMsg) {
		this.datiClienteInsufficientiMsg = datiClienteInsufficientiMsg;
	}

	public String getMessaggioCentrale() {
		return messaggioCentrale;
	}

	public void setMessaggioCentrale(String messaggioCentrale) {
		this.messaggioCentrale = messaggioCentrale;
	}

	public StringType getDescrClusterCedacri() {
		return descrClusterCedacri;
	}

	public void setDescrClusterCedacri(StringType descrClusterCedacri) {
		this.descrClusterCedacri = descrClusterCedacri;
	}

	public StringType getCodClusterCedacri() {
		return codClusterCedacri;
	}

	public void setCodClusterCedacri(StringType codClusterCedacri) {
		this.codClusterCedacri = codClusterCedacri;
	}
	public IntegerType getDFinValCedacri() {
		return dFinValCedacri;
	}
	public void setDFinValCedacri(IntegerType finValCedacri) {
		dFinValCedacri = finValCedacri;
	}
	
	public ElementiPatrimonioModel getElementiPatrimonio() {
		return elementiPatrimonio;
	}

	public void setElementiPatrimonio(ElementiPatrimonioModel elementiPatrimonio) {
		this.elementiPatrimonio = elementiPatrimonio;
	}
}
