package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class PritInviatiInSedeOutputModel extends CommandDataModel{
	
	private StringType tipoProdotto = new StringType();
	private StringType descProdotto = new StringType();
	private StringType tipoOperazione = new StringType();
	private StringType descOperazione =new StringType();
	private IntegerType gg1 = new IntegerType();
	private IntegerType gg2 = new IntegerType();
	private IntegerType gg3 = new IntegerType();
	private IntegerType gg4 = new IntegerType();
	private IntegerType gg5 = new IntegerType();
	private IntegerType gg6 = new IntegerType();
	private IntegerType gg7 = new IntegerType();
	
	public StringType getTipoProdotto() {
		return tipoProdotto;
	}
	public void setTipoProdotto(StringType tipoProdotto) {
		this.tipoProdotto = tipoProdotto;
	}
	public StringType getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(StringType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public IntegerType getGg1() {
		return gg1;
	}
	public void setGg1(IntegerType gg1) {
		this.gg1 = gg1;
	}
	public IntegerType getGg2() {
		return gg2;
	}
	public void setGg2(IntegerType gg2) {
		this.gg2 = gg2;
	}
	public IntegerType getGg3() {
		return gg3;
	}
	public void setGg3(IntegerType gg3) {
		this.gg3 = gg3;
	}
	public IntegerType getGg4() {
		return gg4;
	}
	public void setGg4(IntegerType gg4) {
		this.gg4 = gg4;
	}
	public IntegerType getGg5() {
		return gg5;
	}
	public void setGg5(IntegerType gg5) {
		this.gg5 = gg5;
	}
	public IntegerType getGg6() {
		return gg6;
	}
	public void setGg6(IntegerType gg6) {
		this.gg6 = gg6;
	}
	public IntegerType getGg7() {
		return gg7;
	}
	public void setGg7(IntegerType gg7) {
		this.gg7 = gg7;
	}
	public StringType getDescProdotto() {
		return descProdotto;
	}
	public void setDescProdotto(StringType descProdotto) {
		this.descProdotto = descProdotto;
	}
	public StringType getDescOperazione() {
		return descOperazione;
	}
	public void setDescOperazione(StringType descOperazione) {
		this.descOperazione = descOperazione;
	}

}
