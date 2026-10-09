package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class FotografiaModel extends CommandDataModel {
	private StringType  			idInforete 	= new StringType();
	private TimestampType  			dataUpload	= new TimestampType();
	private TimestampType  			dataInvioRepository	= new TimestampType();
	private StringType  			nomeFile = new StringType();
	private FileType				immagine = new FileType();
	private StringType  			idRepository = new StringType();
	private BooleanType				isInviataRepository = new BooleanType();
	private StringType  			statoRepository = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FotografiaModel(){
		getImmagine().setFileTypes("jpg,jpeg,png");
	}
	
	public StringType getIdInforete() {
		return idInforete;
	}
	public void setIdInforete(StringType idInforete) {
		this.idInforete = idInforete;
	}
	public FileType getImmagine() {
		return immagine;
	}
	public void setImmagine(FileType immagine) {
		this.immagine = immagine;
	}
	public TimestampType getDataUpload() {
		return dataUpload;
	}
	public void setDataUpload(TimestampType dataUpload) {
		this.dataUpload = dataUpload;
	}

	public TimestampType getDataInvioRepository() {
		return dataInvioRepository;
	}

	public void setDataInvioRepository(TimestampType dataInvioRepository) {
		this.dataInvioRepository = dataInvioRepository;
	}
	public StringType getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(StringType nomeFile) {
		this.nomeFile = nomeFile;
	}

	public StringType getIdRepository() {
		return idRepository;
	}

	public void setIdRepository(StringType idRepository) {
		this.idRepository = idRepository;
	}

	public StringType getStatoRepository() {
		return statoRepository;
	}

	public void setStatoRepository(StringType statoRepository) {
		this.statoRepository = statoRepository;
	}

	public BooleanType getIsInviataRepository() {
		return isInviataRepository;
	}

	public void setIsInviataRepository(BooleanType isInviataRepository) {
		this.isInviataRepository = isInviataRepository;
	}
}
