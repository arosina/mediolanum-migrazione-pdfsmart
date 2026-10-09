package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class ContoAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  tipoConto = new StringType();
	private StringType  codAgente = new StringType();
	private StringType  ndgCliente = new StringType();
	private StringType  ruoliAmmessi = new StringType();
	private StringType  divisaConto = new StringType();	
	private ListType	nominativiConto = new ListType(NominativiContoModel.class);
	
	private StringType	whereTipoConto = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String label(){
		String res = "";
		boolean isContoCorrente = false;
		String categoriaConto = propertyToString("categoriaConto");
		if(!categoriaConto.equals("0005") && !categoriaConto.equals("0043") && !categoriaConto.equals("0075") && !categoriaConto.equals("0076"))
			isContoCorrente = true;
		res += "<div style='display:table-row;'>";
		res += "<div style='display:table-cell;white-space:nowrap;'>";
		if(isContoCorrente)
			res += "Conto corrente n.: "+propertyToString("numeroConto");
		else
			res += "Conto n.: "+propertyToString("numeroConto");
		res += "</div>";
		String nominativiContoAsString = nominativiContoAsString();
		if(nominativiContoAsString.length() > 0){
			res += "&nbsp;<div style='display:table-cell;white-space:nowrap;'>intestato a:&nbsp;</div><div style='display:table-cell;'>"+nominativiContoAsString+"</div>";
		}
		return res+"</div>";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String nominativiContoAsString() {
		String nominativiContoAsString = "";
		for(int i=0;i<getNominativiConto().size();i++){
			NominativiContoModel nominativo = (NominativiContoModel)getNominativiConto().get(i);
			if(nominativo.getNdgCliente().equals(getNdgCliente()))
				nominativiContoAsString += "<b>"+Tools.capitalize(nominativo.getCognomeCliente()+" "+nominativo.getNomeCliente())+" ("+nominativo.getRuoloCliente()+")</b>, ";
			else
				nominativiContoAsString += Tools.capitalize(nominativo.getCognomeCliente()+" "+nominativo.getNomeCliente())+" ("+nominativo.getRuoloCliente()+"), ";
		}
		if(nominativiContoAsString.length() > 0)
			nominativiContoAsString = nominativiContoAsString.substring(0, nominativiContoAsString.length()-2);
		return nominativiContoAsString;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsNdgAsString() {
		return new BooleanType((!getNdgCliente().isNull() && getNdgCliente().toString().indexOf("'") >= 0));
	}
	
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getNdgCliente() {
		return ndgCliente;
	}

	public void setNdgCliente(StringType ndgCliente) {
		this.ndgCliente = ndgCliente;
	}

	public StringType getTipoConto() {
		return tipoConto;
	}

	public void setTipoConto(StringType tipoConto) {
		this.tipoConto = tipoConto;
	}

	public StringType getRuoliAmmessi() {
		return ruoliAmmessi;
	}

	public void setRuoliAmmessi(StringType ruoliAmmessi) {
		this.ruoliAmmessi = ruoliAmmessi;
	}

	public ListType getNominativiConto() {
		return nominativiConto;
	}

	public void setNominativiConto(ListType nominativiConto) {
		this.nominativiConto = nominativiConto;
	}

	public StringType getDivisaConto() {
		return divisaConto;
	}

	public void setDivisaConto(StringType divisaConto) {
		this.divisaConto = divisaConto;
	}

	public StringType getWhereTipoConto() {
		return whereTipoConto;
	}

	public void setWhereTipoConto(StringType whereTipoConto) {
		this.whereTipoConto = whereTipoConto;
	}

}
