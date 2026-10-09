package prgm.pdfwebformspolizzeutil.model;

import java.util.HashMap;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class StrategiaInvestimentoInputModel extends CommandDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1707594087627862463L;
	
	private StringType codProdotto = new StringType();
	private StringType codContratto = new StringType();
	
	private HashMap<StringType, DoubleType> fondiDaDisinvestire = new HashMap<StringType, DoubleType>();
	
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public StringType getCodContratto() {
		return codContratto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public void setCodContratto(StringType codContratto) {
		this.codContratto = codContratto;
	}
	public HashMap<StringType, DoubleType> getFondiDaDisinvestire() {
		return fondiDaDisinvestire;
	}
	public void setFondiDaDisinvestire(HashMap<StringType, DoubleType> fondiDaDisinvestire) {
		this.fondiDaDisinvestire = fondiDaDisinvestire;
	}			
}
