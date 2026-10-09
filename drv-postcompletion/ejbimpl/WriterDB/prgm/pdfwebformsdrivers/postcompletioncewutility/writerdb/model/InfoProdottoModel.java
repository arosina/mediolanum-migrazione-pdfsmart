package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class InfoProdottoModel extends CommandDataModel {

	private StringType codProdotto = new StringType();	
	private StringType versioneProdotto = new StringType();
	private StringType nomeDisposizione = new StringType();
	private StringType descrProdotto = new StringType();
	private StringType codRamoProdotto = new StringType();
	private StringType codProdottoRegole = new StringType();
	private StringType tipoDisposizione = new StringType();
	private StringType tipoDisposizioneOnline = new StringType();
	private StringType tipoDisposizioneSwitch = new StringType();
	private StringType tipoProdotto = new StringType();
	private StringType pritCodProdotto = new StringType();
	private StringType pritCodProdottoDigitale = new StringType();
	private StringType pritCodOperazione = new StringType();
	private StringType pritCodOperazioneRid = new StringType();
	private StringType pritCodOperazioneAdeguatezza = new StringType();
	private StringType codSocietaProdotto = new StringType();
	private StringType srunServiceName = new StringType();
	//Servono per inserire il prit
	private StringType codiceProdottoDispo 	= new StringType();
	private StringType chiave 				= new StringType();
	private StringType nomeProprietaImporto	= new StringType();
	
	public StringType getCodProdotto() {
		return codProdotto;
	}

	public StringType getCodSocietaProdotto() {
		return codSocietaProdotto;
	}

	public StringType getPritCodOperazione() {
		return pritCodOperazione;
	}

	public StringType getPritCodProdotto() {
		return pritCodProdotto;
	}

	public StringType getSrunServiceName() {
		return srunServiceName;
	}

	public StringType getTipoDisposizione() {
		return tipoDisposizione;
	}

	public StringType getTipoProdotto() {
		return tipoProdotto;
	}

	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}

	public void setCodSocietaProdotto(StringType codSocietaProdotto) {
		this.codSocietaProdotto = codSocietaProdotto;
	}

	public void setPritCodOperazione(StringType pritCodOperazione) {
		this.pritCodOperazione = pritCodOperazione;
	}

	public void setPritCodProdotto(StringType pritCodProdotto) {
		this.pritCodProdotto = pritCodProdotto;
	}

	public void setSrunServiceName(StringType srunServiceName) {
		this.srunServiceName = srunServiceName;
	}

	public void setTipoDisposizione(StringType tipoDisposizione) {
		this.tipoDisposizione = tipoDisposizione;
	}

	public void setTipoProdotto(StringType tipoProdotto) {
		this.tipoProdotto = tipoProdotto;
	}

	public StringType getPritCodOperazioneRid() {
		return pritCodOperazioneRid;
	}

	public void setPritCodOperazioneRid(StringType pritCodOperazioneRid) {
		this.pritCodOperazioneRid = pritCodOperazioneRid;
	}

	public StringType getCodRamoProdotto() {
		return codRamoProdotto;
	}

	public StringType getDescrProdotto() {
		return descrProdotto;
	}

	public void setCodRamoProdotto(StringType codRamoProdotto) {
		this.codRamoProdotto = codRamoProdotto;
	}

	public void setDescrProdotto(StringType descrProdotto) {
		this.descrProdotto = descrProdotto;
	}

	public StringType getTipoDisposizioneOnline() {
		return tipoDisposizioneOnline;
	}

	public void setTipoDisposizioneOnline(StringType tipoDisposizioneOnline) {
		this.tipoDisposizioneOnline = tipoDisposizioneOnline;
	}

	public StringType getTipoDisposizioneSwitch() {
		return tipoDisposizioneSwitch;
	}

	public void setTipoDisposizioneSwitch(StringType tipoDisposizioneSwitch) {
		this.tipoDisposizioneSwitch = tipoDisposizioneSwitch;
	}

	public StringType getCodProdottoRegole() {
		return codProdottoRegole;
	}

	public void setCodProdottoRegole(StringType codProdottoRegole) {
		this.codProdottoRegole = codProdottoRegole;
	}

	public StringType getNomeDisposizione() {
		return nomeDisposizione;
	}

	public void setNomeDisposizione(StringType nomeDisposizione) {
		this.nomeDisposizione = nomeDisposizione;
	}

	public StringType getVersioneProdotto() {
		return versioneProdotto;
	}

	public void setVersioneProdotto(StringType versioneProdotto) {
		this.versioneProdotto = versioneProdotto;
	}

	public StringType getPritCodOperazioneAdeguatezza() {
		return pritCodOperazioneAdeguatezza;
	}

	public void setPritCodOperazioneAdeguatezza(
			StringType pritCodOperazioneAdeguatezza) {
		this.pritCodOperazioneAdeguatezza = pritCodOperazioneAdeguatezza;
	}

	public StringType getPritCodProdottoDigitale() {
		return pritCodProdottoDigitale;
	}

	public void setPritCodProdottoDigitale(StringType pritCodProdottoDigitale) {
		this.pritCodProdottoDigitale = pritCodProdottoDigitale;
	}

	public StringType getCodiceProdottoDispo() {
		return codiceProdottoDispo;
	}

	public void setCodiceProdottoDispo(StringType codiceProdottoDispo) {
		this.codiceProdottoDispo = codiceProdottoDispo;
	}

	public StringType getChiave() {
		return chiave;
	}

	public void setChiave(StringType chiave) {
		this.chiave = chiave;
	}

	public StringType getNomeProprietaImporto() {
		return nomeProprietaImporto;
	}

	public void setNomeProprietaImporto(StringType nomeProprietaImporto) {
		this.nomeProprietaImporto = nomeProprietaImporto;
	}

}
