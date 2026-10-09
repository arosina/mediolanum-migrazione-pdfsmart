package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class TW00TBO2Model extends CommandDataModel 
{
	private static final long serialVersionUID = 0;
	private IntegerType codIsti = new IntegerType();
	private	StringType  ndgDoss = new StringType();
	private StringType  ndgTemp = new StringType();
	private IntegerType numOrditit = new IntegerType(); 
	private IntegerType totGiortit = new IntegerType(); 
	private IntegerType totOrditit = new IntegerType(); 
	private IntegerType ordTrantit = new IntegerType(); 
	private IntegerType numOrdifon = new IntegerType(); 
	private IntegerType totGiorfon = new IntegerType(); 
	private IntegerType totOrdifon = new IntegerType(); 
	private IntegerType ordTranfon = new IntegerType(); 
	private IntegerType numOrdiass = new IntegerType(); 
	private IntegerType totGiorass = new IntegerType(); 
	private IntegerType totOrdiass = new IntegerType(); 
	private IntegerType ordTranass = new IntegerType();

	public void setZero()
	{
		numOrditit.setStringValue("0"); 
		totGiortit.setStringValue("1"); 
		totOrditit.setStringValue("0");
		ordTrantit.setStringValue("0");
		numOrdifon.setStringValue("0");
		totGiorfon.setStringValue("1");
		totOrdifon.setStringValue("0");
		ordTranfon.setStringValue("0");
		numOrdiass.setStringValue("0");
		totGiorass.setStringValue("1");
		totOrdiass.setStringValue("0");
		ordTranass.setStringValue("0");
	}

	public IntegerType getCodIsti() {
		return codIsti;
	}

	public StringType getNdgDoss() {
		return ndgDoss;
	}

	public StringType getNdgTemp() {
		return ndgTemp;
	}

	public IntegerType getNumOrdiass() {
		return numOrdiass;
	}

	public IntegerType getNumOrdifon() {
		return numOrdifon;
	}

	public IntegerType getNumOrditit() {
		return numOrditit;
	}

	public IntegerType getOrdTranass() {
		return ordTranass;
	}

	public IntegerType getOrdTranfon() {
		return ordTranfon;
	}

	public IntegerType getOrdTrantit() {
		return ordTrantit;
	}

	public IntegerType getTotGiorass() {
		return totGiorass;
	}

	public IntegerType getTotGiorfon() {
		return totGiorfon;
	}

	public IntegerType getTotGiortit() {
		return totGiortit;
	}

	public IntegerType getTotOrdiass() {
		return totOrdiass;
	}

	public IntegerType getTotOrdifon() {
		return totOrdifon;
	}

	public IntegerType getTotOrditit() {
		return totOrditit;
	}

	public void setCodIsti(IntegerType codIsti) {
		this.codIsti = codIsti;
	}

	public void setNdgDoss(StringType ndgDoss) {
		this.ndgDoss = ndgDoss;
	}

	public void setNdgTemp(StringType ndgTemp) {
		this.ndgTemp = ndgTemp;
	}

	public void setNumOrdiass(IntegerType numOrdiass) {
		this.numOrdiass = numOrdiass;
	}

	public void setNumOrdifon(IntegerType numOrdifon) {
		this.numOrdifon = numOrdifon;
	}

	public void setNumOrditit(IntegerType numOrditit) {
		this.numOrditit = numOrditit;
	}

	public void setOrdTranass(IntegerType ordTranass) {
		this.ordTranass = ordTranass;
	}

	public void setOrdTranfon(IntegerType ordTranfon) {
		this.ordTranfon = ordTranfon;
	}

	public void setOrdTrantit(IntegerType ordTrantit) {
		this.ordTrantit = ordTrantit;
	}

	public void setTotGiorass(IntegerType totGiorass) {
		this.totGiorass = totGiorass;
	}

	public void setTotGiorfon(IntegerType totGiorfon) {
		this.totGiorfon = totGiorfon;
	}

	public void setTotGiortit(IntegerType totGiortit) {
		this.totGiortit = totGiortit;
	}

	public void setTotOrdiass(IntegerType totOrdiass) {
		this.totOrdiass = totOrdiass;
	}

	public void setTotOrdifon(IntegerType totOrdifon) {
		this.totOrdifon = totOrdifon;
	}

	public void setTotOrditit(IntegerType totOrditit) {
		this.totOrditit = totOrditit;
	}	
}
