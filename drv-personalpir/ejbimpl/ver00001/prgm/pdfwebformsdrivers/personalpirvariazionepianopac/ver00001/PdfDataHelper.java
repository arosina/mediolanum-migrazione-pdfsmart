package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;
import prgm.pdfwebforms.drivers.PdfBaseDataHelper;
import prgm.pdfwebforms.model.PdfDataModel;

public class PdfDataHelper extends PdfBaseDataHelper{
	public PdfDataHelper(PdfDataModel pdfData){
		super(pdfData);
	}
	public StringType getBarcode() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.BARCODE);
	}
	public void setBarcode(StringType value){
		write(FieldNames.BARCODE,value);
	}
	public StringType getIdReportAdeguatezza() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.IDREPORTADEGUATEZZA);
	}
	public void setIdReportAdeguatezza(StringType value){
		write(FieldNames.IDREPORTADEGUATEZZA,value);
	}
	public StringType getLuogo() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LUOGO);
	}
	public void setLuogo(StringType value){
		write(FieldNames.LUOGO,value);
	}
	public DateType getDataSottoscrizione() throws PdfAcroFieldNotFoundException{
		return (DateType)read(FieldNames.DATASOTTOSCRIZIONE);
	}
	public void setDataSottoscrizione(DateType value){
		write(FieldNames.DATASOTTOSCRIZIONE,value);
	}
	public StringType getCognomeCliente1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.COGNOMECLIENTE1);
	}
	public void setCognomeCliente1(StringType value){
		write(FieldNames.COGNOMECLIENTE1,value);
	}
	public StringType getNomeCliente1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NOMECLIENTE1);
	}
	public void setNomeCliente1(StringType value){
		write(FieldNames.NOMECLIENTE1,value);
	}
	public StringType getCodiceFiscalePartitaIvaCliente1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFISCALEPARTITAIVACLIENTE1);
	}
	public void setCodiceFiscalePartitaIvaCliente1(StringType value){
		write(FieldNames.CODICEFISCALEPARTITAIVACLIENTE1,value);
	}
	public StringType getNdgCliente1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NDGCLIENTE1);
	}
	public void setNdgCliente1(StringType value){
		write(FieldNames.NDGCLIENTE1,value);
	}
	public StringType getNumeroPolizza() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NUMEROPOLIZZA);
	}
	public void setNumeroPolizza(StringType value){
		write(FieldNames.NUMEROPOLIZZA,value);
	}
	public DoubleType getImportoPiano() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOPIANO);
	}
	public void setImportoPiano(DoubleType value){
		write(FieldNames.IMPORTOPIANO,value);
	}
	public DoubleType getFrequenzaPiano() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.FREQUENZAPIANO);
	}
	public void setFrequenzaPiano(DoubleType value){
		write(FieldNames.FREQUENZAPIANO,value);
	}
	public StringType getNumeroContratto() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NUMEROCONTRATTO);
	}
	public void setNumeroContratto(StringType value){
		write(FieldNames.NUMEROCONTRATTO,value);
	}
	public StringType getCodProdottoPolizza() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODPRODOTTOPOLIZZA);
	}
	public void setCodProdottoPolizza(StringType value){
		write(FieldNames.CODPRODOTTOPOLIZZA,value);
	}
	public StringType getFlagTrasformatoPic() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.FLAGTRASFORMATOPIC);
	}
	public void setFlagTrasformatoPic(StringType value){
		write(FieldNames.FLAGTRASFORMATOPIC,value);
	}
	public StringType getFormaContrattuale() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.FORMACONTRATTUALE);
	}
	public void setFormaContrattuale(StringType value){
		write(FieldNames.FORMACONTRATTUALE,value);
	}
	public StringType getIsVariazioneDisposizioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEDISPOSIZIONESDD);
	}
	public void setIsVariazioneDisposizioneSDD(StringType value){
		write(FieldNames.ISVARIAZIONEDISPOSIZIONESDD,value);
	}
	public StringType getIsVariazioneImportoSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEIMPORTOSDD);
	}
	public void setIsVariazioneImportoSDD(StringType value){
		write(FieldNames.ISVARIAZIONEIMPORTOSDD,value);
	}
	public DoubleType getImportoVariazioneSDD() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOVARIAZIONESDD);
	}
	public void setImportoVariazioneSDD(DoubleType value){
		write(FieldNames.IMPORTOVARIAZIONESDD,value);
	}
	public StringType getIsVariazioneFrequenzaSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEFREQUENZASDD);
	}
	public void setIsVariazioneFrequenzaSDD(StringType value){
		write(FieldNames.ISVARIAZIONEFREQUENZASDD,value);
	}
	public StringType getFrequenzaVariazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.FREQUENZAVARIAZIONESDD);
	}
	public void setFrequenzaVariazioneSDD(StringType value){
		write(FieldNames.FREQUENZAVARIAZIONESDD,value);
	}
	public StringType getIsVariazioneDataPremioSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEDATAPREMIOSDD);
	}
	public void setIsVariazioneDataPremioSDD(StringType value){
		write(FieldNames.ISVARIAZIONEDATAPREMIOSDD,value);
	}
	public StringType getGiornoValutaVariazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.GIORNOVALUTAVARIAZIONESDD);
	}
	public void setGiornoValutaVariazioneSDD(StringType value){
		write(FieldNames.GIORNOVALUTAVARIAZIONESDD,value);
	}
	public StringType getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD);
	}
	public void setTipoSOSPENSIONEREVOCARIATTIVAZIONESDD(StringType value){
		write(FieldNames.TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD,value);
	}
	public StringType getIsVariazioneImportoRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEIMPORTORIATTIVAZIONESDD);
	}
	public void setIsVariazioneImportoRiattivazioneSDD(StringType value){
		write(FieldNames.ISVARIAZIONEIMPORTORIATTIVAZIONESDD,value);
	}
	public DoubleType getImportoRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTORIATTIVAZIONESDD);
	}
	public void setImportoRiattivazioneSDD(DoubleType value){
		write(FieldNames.IMPORTORIATTIVAZIONESDD,value);
	}
	public StringType getIsVariazioneFrazionamentoRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEFRAZIONAMENTORIATTIVAZIONESDD);
	}
	public void setIsVariazioneFrazionamentoRiattivazioneSDD(StringType value){
		write(FieldNames.ISVARIAZIONEFRAZIONAMENTORIATTIVAZIONESDD,value);
	}
	public StringType getFrequenzaRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.FREQUENZARIATTIVAZIONESDD);
	}
	public void setFrequenzaRiattivazioneSDD(StringType value){
		write(FieldNames.FREQUENZARIATTIVAZIONESDD,value);
	}
	public StringType getIsVariazioneDataPremioRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONEDATAPREMIORIATTIVAZIONESDD);
	}
	public void setIsVariazioneDataPremioRiattivazioneSDD(StringType value){
		write(FieldNames.ISVARIAZIONEDATAPREMIORIATTIVAZIONESDD,value);
	}
	public StringType getGiornoValutaRiattivazioneSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.GIORNOVALUTARIATTIVAZIONESDD);
	}
	public void setGiornoValutaRiattivazioneSDD(StringType value){
		write(FieldNames.GIORNOVALUTARIATTIVAZIONESDD,value);
	}
	public StringType getMeseAnnoSospensioneDa() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.MESEANNOSOSPENSIONEDA);
	}
	public void setMeseAnnoSospensioneDa(StringType value){
		write(FieldNames.MESEANNOSOSPENSIONEDA,value);
	}
	public StringType getMeseAnnoSospensioneA() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.MESEANNOSOSPENSIONEA);
	}
	public void setMeseAnnoSospensioneA(StringType value){
		write(FieldNames.MESEANNOSOSPENSIONEA,value);
	}
	public StringType getIsVariazioneRipartizione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONERIPARTIZIONE);
	}
	public void setIsVariazioneRipartizione(StringType value){
		write(FieldNames.ISVARIAZIONERIPARTIZIONE,value);
	}
	public StringType getLineaFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO0);
	}
	public void setLineaFondoPremio0(StringType value){
		write(FieldNames.LINEAFONDOPREMIO0,value);
	}
	public StringType getCodiceFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO0);
	}
	public void setCodiceFondoPremio0(StringType value){
		write(FieldNames.CODICEFONDOPREMIO0,value);
	}
	public StringType getSocietaFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO0);
	}
	public void setSocietaFondoPremio0(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO0,value);
	}
	public StringType getIsinFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO0);
	}
	public void setIsinFondoPremio0(StringType value){
		write(FieldNames.ISINFONDOPREMIO0,value);
	}
	public StringType getDescrizioneFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO0);
	}
	public void setDescrizioneFondoPremio0(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO0,value);
	}
	public DoubleType getPercentualeFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO0);
	}
	public void setPercentualeFondoPremio0(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO0,value);
	}
	public DoubleType getImportoFondoPremio0() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO0);
	}
	public void setImportoFondoPremio0(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO0,value);
	}
	public StringType getLineaFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO1);
	}
	public void setLineaFondoPremio1(StringType value){
		write(FieldNames.LINEAFONDOPREMIO1,value);
	}
	public StringType getCodiceFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO1);
	}
	public void setCodiceFondoPremio1(StringType value){
		write(FieldNames.CODICEFONDOPREMIO1,value);
	}
	public StringType getSocietaFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO1);
	}
	public void setSocietaFondoPremio1(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO1,value);
	}
	public StringType getIsinFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO1);
	}
	public void setIsinFondoPremio1(StringType value){
		write(FieldNames.ISINFONDOPREMIO1,value);
	}
	public StringType getDescrizioneFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO1);
	}
	public void setDescrizioneFondoPremio1(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO1,value);
	}
	public DoubleType getPercentualeFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO1);
	}
	public void setPercentualeFondoPremio1(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO1,value);
	}
	public DoubleType getImportoFondoPremio1() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO1);
	}
	public void setImportoFondoPremio1(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO1,value);
	}
	public StringType getLineaFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO2);
	}
	public void setLineaFondoPremio2(StringType value){
		write(FieldNames.LINEAFONDOPREMIO2,value);
	}
	public StringType getCodiceFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO2);
	}
	public void setCodiceFondoPremio2(StringType value){
		write(FieldNames.CODICEFONDOPREMIO2,value);
	}
	public StringType getSocietaFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO2);
	}
	public void setSocietaFondoPremio2(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO2,value);
	}
	public StringType getIsinFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO2);
	}
	public void setIsinFondoPremio2(StringType value){
		write(FieldNames.ISINFONDOPREMIO2,value);
	}
	public StringType getDescrizioneFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO2);
	}
	public void setDescrizioneFondoPremio2(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO2,value);
	}
	public DoubleType getPercentualeFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO2);
	}
	public void setPercentualeFondoPremio2(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO2,value);
	}
	public DoubleType getImportoFondoPremio2() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO2);
	}
	public void setImportoFondoPremio2(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO2,value);
	}
	public StringType getLineaFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO3);
	}
	public void setLineaFondoPremio3(StringType value){
		write(FieldNames.LINEAFONDOPREMIO3,value);
	}
	public StringType getCodiceFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO3);
	}
	public void setCodiceFondoPremio3(StringType value){
		write(FieldNames.CODICEFONDOPREMIO3,value);
	}
	public StringType getSocietaFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO3);
	}
	public void setSocietaFondoPremio3(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO3,value);
	}
	public StringType getIsinFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO3);
	}
	public void setIsinFondoPremio3(StringType value){
		write(FieldNames.ISINFONDOPREMIO3,value);
	}
	public StringType getDescrizioneFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO3);
	}
	public void setDescrizioneFondoPremio3(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO3,value);
	}
	public DoubleType getPercentualeFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO3);
	}
	public void setPercentualeFondoPremio3(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO3,value);
	}
	public DoubleType getImportoFondoPremio3() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO3);
	}
	public void setImportoFondoPremio3(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO3,value);
	}
	public StringType getLineaFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO4);
	}
	public void setLineaFondoPremio4(StringType value){
		write(FieldNames.LINEAFONDOPREMIO4,value);
	}
	public StringType getCodiceFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO4);
	}
	public void setCodiceFondoPremio4(StringType value){
		write(FieldNames.CODICEFONDOPREMIO4,value);
	}
	public StringType getSocietaFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO4);
	}
	public void setSocietaFondoPremio4(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO4,value);
	}
	public StringType getIsinFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO4);
	}
	public void setIsinFondoPremio4(StringType value){
		write(FieldNames.ISINFONDOPREMIO4,value);
	}
	public StringType getDescrizioneFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO4);
	}
	public void setDescrizioneFondoPremio4(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO4,value);
	}
	public DoubleType getPercentualeFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO4);
	}
	public void setPercentualeFondoPremio4(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO4,value);
	}
	public DoubleType getImportoFondoPremio4() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO4);
	}
	public void setImportoFondoPremio4(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO4,value);
	}
	public StringType getLineaFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO5);
	}
	public void setLineaFondoPremio5(StringType value){
		write(FieldNames.LINEAFONDOPREMIO5,value);
	}
	public StringType getCodiceFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO5);
	}
	public void setCodiceFondoPremio5(StringType value){
		write(FieldNames.CODICEFONDOPREMIO5,value);
	}
	public StringType getSocietaFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO5);
	}
	public void setSocietaFondoPremio5(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO5,value);
	}
	public StringType getIsinFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO5);
	}
	public void setIsinFondoPremio5(StringType value){
		write(FieldNames.ISINFONDOPREMIO5,value);
	}
	public StringType getDescrizioneFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO5);
	}
	public void setDescrizioneFondoPremio5(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO5,value);
	}
	public DoubleType getPercentualeFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO5);
	}
	public void setPercentualeFondoPremio5(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO5,value);
	}
	public DoubleType getImportoFondoPremio5() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO5);
	}
	public void setImportoFondoPremio5(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO5,value);
	}
	public DoubleType getImportoFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO6);
	}
	public void setImportoFondoPremio6(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO6,value);
	}
	public StringType getLineaFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO6);
	}
	public void setLineaFondoPremio6(StringType value){
		write(FieldNames.LINEAFONDOPREMIO6,value);
	}
	public StringType getCodiceFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO6);
	}
	public void setCodiceFondoPremio6(StringType value){
		write(FieldNames.CODICEFONDOPREMIO6,value);
	}
	public StringType getSocietaFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO6);
	}
	public void setSocietaFondoPremio6(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO6,value);
	}
	public StringType getIsinFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO6);
	}
	public void setIsinFondoPremio6(StringType value){
		write(FieldNames.ISINFONDOPREMIO6,value);
	}
	public StringType getDescrizioneFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO6);
	}
	public void setDescrizioneFondoPremio6(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO6,value);
	}
	public DoubleType getPercentualeFondoPremio6() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO6);
	}
	public void setPercentualeFondoPremio6(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO6,value);
	}
	public DoubleType getImportoFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO7);
	}
	public void setImportoFondoPremio7(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO7,value);
	}
	public StringType getLineaFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO7);
	}
	public void setLineaFondoPremio7(StringType value){
		write(FieldNames.LINEAFONDOPREMIO7,value);
	}
	public StringType getCodiceFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO7);
	}
	public void setCodiceFondoPremio7(StringType value){
		write(FieldNames.CODICEFONDOPREMIO7,value);
	}
	public StringType getSocietaFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO7);
	}
	public void setSocietaFondoPremio7(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO7,value);
	}
	public StringType getIsinFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO7);
	}
	public void setIsinFondoPremio7(StringType value){
		write(FieldNames.ISINFONDOPREMIO7,value);
	}
	public StringType getDescrizioneFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO7);
	}
	public void setDescrizioneFondoPremio7(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO7,value);
	}
	public DoubleType getPercentualeFondoPremio7() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO7);
	}
	public void setPercentualeFondoPremio7(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO7,value);
	}
	public DoubleType getImportoFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO8);
	}
	public void setImportoFondoPremio8(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO8,value);
	}
	public DoubleType getImportoFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO9);
	}
	public void setImportoFondoPremio9(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO9,value);
	}
	public StringType getLineaFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO8);
	}
	public void setLineaFondoPremio8(StringType value){
		write(FieldNames.LINEAFONDOPREMIO8,value);
	}
	public StringType getCodiceFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO8);
	}
	public void setCodiceFondoPremio8(StringType value){
		write(FieldNames.CODICEFONDOPREMIO8,value);
	}
	public StringType getSocietaFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO8);
	}
	public void setSocietaFondoPremio8(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO8,value);
	}
	public StringType getIsinFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO8);
	}
	public void setIsinFondoPremio8(StringType value){
		write(FieldNames.ISINFONDOPREMIO8,value);
	}
	public StringType getDescrizioneFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO8);
	}
	public void setDescrizioneFondoPremio8(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO8,value);
	}
	public DoubleType getPercentualeFondoPremio8() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO8);
	}
	public void setPercentualeFondoPremio8(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO8,value);
	}
	public DoubleType getImportoFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO10);
	}
	public void setImportoFondoPremio10(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO10,value);
	}
	public StringType getLineaFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO9);
	}
	public void setLineaFondoPremio9(StringType value){
		write(FieldNames.LINEAFONDOPREMIO9,value);
	}
	public StringType getCodiceFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO9);
	}
	public void setCodiceFondoPremio9(StringType value){
		write(FieldNames.CODICEFONDOPREMIO9,value);
	}
	public StringType getSocietaFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO9);
	}
	public void setSocietaFondoPremio9(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO9,value);
	}
	public StringType getIsinFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO9);
	}
	public void setIsinFondoPremio9(StringType value){
		write(FieldNames.ISINFONDOPREMIO9,value);
	}
	public StringType getDescrizioneFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO9);
	}
	public void setDescrizioneFondoPremio9(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO9,value);
	}
	public DoubleType getPercentualeFondoPremio9() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO9);
	}
	public void setPercentualeFondoPremio9(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO9,value);
	}
	public DoubleType getImportoFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO11);
	}
	public void setImportoFondoPremio11(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO11,value);
	}
	public StringType getLineaFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO10);
	}
	public void setLineaFondoPremio10(StringType value){
		write(FieldNames.LINEAFONDOPREMIO10,value);
	}
	public StringType getCodiceFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO10);
	}
	public void setCodiceFondoPremio10(StringType value){
		write(FieldNames.CODICEFONDOPREMIO10,value);
	}
	public StringType getSocietaFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO10);
	}
	public void setSocietaFondoPremio10(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO10,value);
	}
	public StringType getIsinFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO10);
	}
	public void setIsinFondoPremio10(StringType value){
		write(FieldNames.ISINFONDOPREMIO10,value);
	}
	public StringType getDescrizioneFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO10);
	}
	public void setDescrizioneFondoPremio10(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO10,value);
	}
	public DoubleType getPercentualeFondoPremio10() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO10);
	}
	public void setPercentualeFondoPremio10(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO10,value);
	}
	public DoubleType getImportoFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO12);
	}
	public void setImportoFondoPremio12(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO12,value);
	}
	public DoubleType getImportoFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO13);
	}
	public void setImportoFondoPremio13(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO13,value);
	}
	public StringType getLineaFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO11);
	}
	public void setLineaFondoPremio11(StringType value){
		write(FieldNames.LINEAFONDOPREMIO11,value);
	}
	public StringType getCodiceFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO11);
	}
	public void setCodiceFondoPremio11(StringType value){
		write(FieldNames.CODICEFONDOPREMIO11,value);
	}
	public StringType getSocietaFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO11);
	}
	public void setSocietaFondoPremio11(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO11,value);
	}
	public StringType getIsinFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO11);
	}
	public void setIsinFondoPremio11(StringType value){
		write(FieldNames.ISINFONDOPREMIO11,value);
	}
	public StringType getDescrizioneFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO11);
	}
	public void setDescrizioneFondoPremio11(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO11,value);
	}
	public DoubleType getPercentualeFondoPremio11() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO11);
	}
	public void setPercentualeFondoPremio11(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO11,value);
	}
	public DoubleType getImportoFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO14);
	}
	public void setImportoFondoPremio14(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO14,value);
	}
	public StringType getLineaFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO12);
	}
	public void setLineaFondoPremio12(StringType value){
		write(FieldNames.LINEAFONDOPREMIO12,value);
	}
	public StringType getCodiceFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO12);
	}
	public void setCodiceFondoPremio12(StringType value){
		write(FieldNames.CODICEFONDOPREMIO12,value);
	}
	public StringType getSocietaFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO12);
	}
	public void setSocietaFondoPremio12(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO12,value);
	}
	public StringType getIsinFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO12);
	}
	public void setIsinFondoPremio12(StringType value){
		write(FieldNames.ISINFONDOPREMIO12,value);
	}
	public StringType getDescrizioneFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO12);
	}
	public void setDescrizioneFondoPremio12(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO12,value);
	}
	public DoubleType getPercentualeFondoPremio12() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO12);
	}
	public void setPercentualeFondoPremio12(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO12,value);
	}
	public DoubleType getImportoFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO15);
	}
	public void setImportoFondoPremio15(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO15,value);
	}
	public DoubleType getImportoFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.IMPORTOFONDOPREMIO16);
	}
	public void setImportoFondoPremio16(DoubleType value){
		write(FieldNames.IMPORTOFONDOPREMIO16,value);
	}
	public StringType getLineaFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO13);
	}
	public void setLineaFondoPremio13(StringType value){
		write(FieldNames.LINEAFONDOPREMIO13,value);
	}
	public StringType getCodiceFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO13);
	}
	public void setCodiceFondoPremio13(StringType value){
		write(FieldNames.CODICEFONDOPREMIO13,value);
	}
	public StringType getSocietaFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO13);
	}
	public void setSocietaFondoPremio13(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO13,value);
	}
	public StringType getIsinFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO13);
	}
	public void setIsinFondoPremio13(StringType value){
		write(FieldNames.ISINFONDOPREMIO13,value);
	}
	public StringType getDescrizioneFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO13);
	}
	public void setDescrizioneFondoPremio13(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO13,value);
	}
	public DoubleType getPercentualeFondoPremio13() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO13);
	}
	public void setPercentualeFondoPremio13(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO13,value);
	}
	public StringType getLineaFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO14);
	}
	public void setLineaFondoPremio14(StringType value){
		write(FieldNames.LINEAFONDOPREMIO14,value);
	}
	public StringType getCodiceFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO14);
	}
	public void setCodiceFondoPremio14(StringType value){
		write(FieldNames.CODICEFONDOPREMIO14,value);
	}
	public StringType getSocietaFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO14);
	}
	public void setSocietaFondoPremio14(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO14,value);
	}
	public StringType getIsinFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO14);
	}
	public void setIsinFondoPremio14(StringType value){
		write(FieldNames.ISINFONDOPREMIO14,value);
	}
	public StringType getDescrizioneFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO14);
	}
	public void setDescrizioneFondoPremio14(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO14,value);
	}
	public DoubleType getPercentualeFondoPremio14() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO14);
	}
	public void setPercentualeFondoPremio14(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO14,value);
	}
	public StringType getLineaFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO15);
	}
	public void setLineaFondoPremio15(StringType value){
		write(FieldNames.LINEAFONDOPREMIO15,value);
	}
	public StringType getCodiceFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO15);
	}
	public void setCodiceFondoPremio15(StringType value){
		write(FieldNames.CODICEFONDOPREMIO15,value);
	}
	public StringType getSocietaFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO15);
	}
	public void setSocietaFondoPremio15(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO15,value);
	}
	public StringType getIsinFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO15);
	}
	public void setIsinFondoPremio15(StringType value){
		write(FieldNames.ISINFONDOPREMIO15,value);
	}
	public StringType getDescrizioneFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO15);
	}
	public void setDescrizioneFondoPremio15(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO15,value);
	}
	public DoubleType getPercentualeFondoPremio15() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO15);
	}
	public void setPercentualeFondoPremio15(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO15,value);
	}
	public StringType getLineaFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.LINEAFONDOPREMIO16);
	}
	public void setLineaFondoPremio16(StringType value){
		write(FieldNames.LINEAFONDOPREMIO16,value);
	}
	public StringType getCodiceFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEFONDOPREMIO16);
	}
	public void setCodiceFondoPremio16(StringType value){
		write(FieldNames.CODICEFONDOPREMIO16,value);
	}
	public StringType getSocietaFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.SOCIETAFONDOPREMIO16);
	}
	public void setSocietaFondoPremio16(StringType value){
		write(FieldNames.SOCIETAFONDOPREMIO16,value);
	}
	public StringType getIsinFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISINFONDOPREMIO16);
	}
	public void setIsinFondoPremio16(StringType value){
		write(FieldNames.ISINFONDOPREMIO16,value);
	}
	public StringType getDescrizioneFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONEFONDOPREMIO16);
	}
	public void setDescrizioneFondoPremio16(StringType value){
		write(FieldNames.DESCRIZIONEFONDOPREMIO16,value);
	}
	public DoubleType getPercentualeFondoPremio16() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.PERCENTUALEFONDOPREMIO16);
	}
	public void setPercentualeFondoPremio16(DoubleType value){
		write(FieldNames.PERCENTUALEFONDOPREMIO16,value);
	}
	public DoubleType getTotalePercentuale() throws PdfAcroFieldNotFoundException{
		return (DoubleType)read(FieldNames.TOTALEPERCENTUALE);
	}
	public void setTotalePercentuale(DoubleType value){
		write(FieldNames.TOTALEPERCENTUALE,value);
	}
	public StringType getTestofirma3Cliente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TESTOFIRMA3CLIENTE);
	}
	public void setTestofirma3Cliente(StringType value){
		write(FieldNames.TESTOFIRMA3CLIENTE,value);
	}
	public StringType getTestofirma4Cliente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TESTOFIRMA4CLIENTE);
	}
	public void setTestofirma4Cliente(StringType value){
		write(FieldNames.TESTOFIRMA4CLIENTE,value);
	}
	public StringType getIsVariazioneContoSDD() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.ISVARIAZIONECONTOSDD);
	}
	public void setIsVariazioneContoSDD(StringType value){
		write(FieldNames.ISVARIAZIONECONTOSDD,value);
	}
	public StringType getTipoIntestazioneContoSDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE);
	}
	public void setTipoIntestazioneContoSDDBMEDVariazione(StringType value){
		write(FieldNames.TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE,value);
	}
	public StringType getTipoContoINTESTATARIOSDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE);
	}
	public void setTipoContoINTESTATARIOSDDBMEDVariazione(StringType value){
		write(FieldNames.TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE,value);
	}
	public StringType getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.IBANCONTOCORRENTECCINTESTATARIOSDDBMEDVARIAZIONE);
	}
	public void setIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione(StringType value){
		write(FieldNames.IBANCONTOCORRENTECCINTESTATARIOSDDBMEDVARIAZIONE,value);
	}
	public StringType getNumeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NUMEROPROPOSTACCINAPERTURAINTESTATARIOSDDBMEDVARIAZIONE);
	}
	public void setNumeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione(StringType value){
		write(FieldNames.NUMEROPROPOSTACCINAPERTURAINTESTATARIOSDDBMEDVARIAZIONE,value);
	}
	public StringType getIbanContoCorrenteCCINTESTATARIOSDDEsternaVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.IBANCONTOCORRENTECCINTESTATARIOSDDESTERNAVARIAZIONE);
	}
	public void setIbanContoCorrenteCCINTESTATARIOSDDEsternaVariazione(StringType value){
		write(FieldNames.IBANCONTOCORRENTECCINTESTATARIOSDDESTERNAVARIAZIONE,value);
	}
	public StringType getNdgALTROCLIENTESDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.NDGALTROCLIENTESDDBMEDVARIAZIONE);
	}
	public void setNdgALTROCLIENTESDDBMEDVariazione(StringType value){
		write(FieldNames.NDGALTROCLIENTESDDBMEDVARIAZIONE,value);
	}
	public StringType getCognomeNomeALTROCLIENTESDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.COGNOMENOMEALTROCLIENTESDDBMEDVARIAZIONE);
	}
	public void setCognomeNomeALTROCLIENTESDDBMEDVariazione(StringType value){
		write(FieldNames.COGNOMENOMEALTROCLIENTESDDBMEDVARIAZIONE,value);
	}
	public StringType getTipoContoALTROCLIENTESDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE);
	}
	public void setTipoContoALTROCLIENTESDDBMEDVariazione(StringType value){
		write(FieldNames.TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE,value);
	}
	public StringType getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.IBANCONTOCORRENTEALTROCLIENTESDDBMEDVARIAZIONE);
	}
	public void setIbanContoCorrenteALTROCLIENTESDDBMEDVariazione(StringType value){
		write(FieldNames.IBANCONTOCORRENTEALTROCLIENTESDDBMEDVARIAZIONE,value);
	}
	public StringType getIbanContoCorrenteALTROCLIENTESDDEsternaVariazione() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.IBANCONTOCORRENTEALTROCLIENTESDDESTERNAVARIAZIONE);
	}
	public void setIbanContoCorrenteALTROCLIENTESDDEsternaVariazione(StringType value){
		write(FieldNames.IBANCONTOCORRENTEALTROCLIENTESDDESTERNAVARIAZIONE,value);
	}
	public StringType getTipoRelazioneContraenteTerzoPagatore() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TIPORELAZIONECONTRAENTETERZOPAGATORE);
	}
	public void setTipoRelazioneContraenteTerzoPagatore(StringType value){
		write(FieldNames.TIPORELAZIONECONTRAENTETERZOPAGATORE,value);
	}
	public StringType getDescrizioneTipoRelazioneContraenteTerzoPagatore() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.DESCRIZIONETIPORELAZIONECONTRAENTETERZOPAGATORE);
	}
	public void setDescrizioneTipoRelazioneContraenteTerzoPagatore(StringType value){
		write(FieldNames.DESCRIZIONETIPORELAZIONECONTRAENTETERZOPAGATORE,value);
	}
	public StringType getTestofirma1Cliente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TESTOFIRMA1CLIENTE);
	}
	public void setTestofirma1Cliente(StringType value){
		write(FieldNames.TESTOFIRMA1CLIENTE,value);
	}
	public BooleanType getRevocaVersamentoPremioAggiuntivo() throws PdfAcroFieldNotFoundException{
		return (BooleanType)read(FieldNames.REVOCAVERSAMENTOPREMIOAGGIUNTIVO);
	}
	public void setRevocaVersamentoPremioAggiuntivo(BooleanType value){
		write(FieldNames.REVOCAVERSAMENTOPREMIOAGGIUNTIVO,value);
	}
	public StringType getTestofirma2Cliente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TESTOFIRMA2CLIENTE);
	}
	public void setTestofirma2Cliente(StringType value){
		write(FieldNames.TESTOFIRMA2CLIENTE,value);
	}
	public StringType getCognomeNomeAgente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.COGNOMENOMEAGENTE);
	}
	public void setCognomeNomeAgente(StringType value){
		write(FieldNames.COGNOMENOMEAGENTE,value);
	}
	public StringType getCodiceAgente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.CODICEAGENTE);
	}
	public void setCodiceAgente(StringType value){
		write(FieldNames.CODICEAGENTE,value);
	}
	public StringType getTestofirma1Agente() throws PdfAcroFieldNotFoundException{
		return (StringType)read(FieldNames.TESTOFIRMA1AGENTE);
	}
	public void setTestofirma1Agente(StringType value){
		write(FieldNames.TESTOFIRMA1AGENTE,value);
	}

	public class FieldNames {
		public static final String BARCODE = "barcode";
		public static final String IDREPORTADEGUATEZZA = "idReportAdeguatezza";
		public static final String LUOGO = "luogo";
		public static final String DATASOTTOSCRIZIONE = "dataSottoscrizione";
		public static final String COGNOMECLIENTE1 = "cognomeCliente1";
		public static final String COGNOMECLIENTE = "cognomeCliente";
		public static final String NOMECLIENTE1 = "nomeCliente1";
		public static final String NOMECLIENTE = "nomeCliente";
		public static final String CODICEFISCALEPARTITAIVACLIENTE1 = "codiceFiscalePartitaIvaCliente1";
		public static final String CODICEFISCALEPARTITAIVACLIENTE = "codiceFiscalePartitaIvaCliente";
		public static final String NDGCLIENTE1 = "ndgCliente1";
		public static final String NDGCLIENTE = "ndgCliente";
		public static final String NUMEROPOLIZZA = "numeroPolizza";
		public static final String IMPORTOPIANO = "importoPiano";
		public static final String FREQUENZAPIANO = "frequenzaPiano";
		public static final String NUMEROCONTRATTO = "numeroContratto";
		public static final String CODPRODOTTOPOLIZZA = "codProdottoPolizza";
		public static final String FLAGTRASFORMATOPIC = "flagTrasformatoPic";
		public static final String FORMACONTRATTUALE = "formaContrattuale";
		public static final String ISVARIAZIONEDISPOSIZIONESDD = "isVariazioneDisposizioneSDD";
		public static final String ISVARIAZIONEIMPORTOSDD = "isVariazioneImportoSDD";
		public static final String IMPORTOVARIAZIONESDD = "importoVariazioneSDD";
		public static final String ISVARIAZIONEFREQUENZASDD = "isVariazioneFrequenzaSDD";
		public static final String FREQUENZAVARIAZIONESDD = "frequenzaVariazioneSDD";
		public static final String ISVARIAZIONEDATAPREMIOSDD = "isVariazioneDataPremioSDD";
		public static final String GIORNOVALUTAVARIAZIONESDD = "giornoValutaVariazioneSDD";
		public static final String TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD = "tipoSOSPENSIONEREVOCARIATTIVAZIONESDD";
		public static final String ISVARIAZIONEIMPORTORIATTIVAZIONESDD = "isVariazioneImportoRiattivazioneSDD";
		public static final String IMPORTORIATTIVAZIONESDD = "importoRiattivazioneSDD";
		public static final String ISVARIAZIONEFRAZIONAMENTORIATTIVAZIONESDD = "isVariazioneFrazionamentoRiattivazioneSDD";
		public static final String FREQUENZARIATTIVAZIONESDD = "frequenzaRiattivazioneSDD";
		public static final String ISVARIAZIONEDATAPREMIORIATTIVAZIONESDD = "isVariazioneDataPremioRiattivazioneSDD";
		public static final String GIORNOVALUTARIATTIVAZIONESDD = "giornoValutaRiattivazioneSDD";
		public static final String MESEANNOSOSPENSIONEDA = "meseAnnoSospensioneDa";
		public static final String MESEANNOSOSPENSIONEA = "meseAnnoSospensioneA";
		public static final String ISVARIAZIONERIPARTIZIONE = "isVariazioneRipartizione";
		public static final String LINEAFONDOPREMIO0 = "lineaFondoPremio0";
		public static final String LINEAFONDOPREMIO = "lineaFondoPremio";
		public static final String CODICEFONDOPREMIO0 = "codiceFondoPremio0";
		public static final String CODICEFONDOPREMIO = "codiceFondoPremio";
		public static final String SOCIETAFONDOPREMIO0 = "societaFondoPremio0";
		public static final String SOCIETAFONDOPREMIO = "societaFondoPremio";
		public static final String ISINFONDOPREMIO0 = "isinFondoPremio0";
		public static final String ISINFONDOPREMIO = "isinFondoPremio";
		public static final String DESCRIZIONEFONDOPREMIO0 = "descrizioneFondoPremio0";
		public static final String DESCRIZIONEFONDOPREMIO = "descrizioneFondoPremio";
		public static final String PERCENTUALEFONDOPREMIO0 = "percentualeFondoPremio0";
		public static final String PERCENTUALEFONDOPREMIO = "percentualeFondoPremio";
		public static final String IMPORTOFONDOPREMIO0 = "importoFondoPremio0";
		public static final String IMPORTOFONDOPREMIO = "importoFondoPremio";
		public static final String LINEAFONDOPREMIO1 = "lineaFondoPremio1";
		public static final String CODICEFONDOPREMIO1 = "codiceFondoPremio1";
		public static final String SOCIETAFONDOPREMIO1 = "societaFondoPremio1";
		public static final String ISINFONDOPREMIO1 = "isinFondoPremio1";
		public static final String DESCRIZIONEFONDOPREMIO1 = "descrizioneFondoPremio1";
		public static final String PERCENTUALEFONDOPREMIO1 = "percentualeFondoPremio1";
		public static final String IMPORTOFONDOPREMIO1 = "importoFondoPremio1";
		public static final String LINEAFONDOPREMIO2 = "lineaFondoPremio2";
		public static final String CODICEFONDOPREMIO2 = "codiceFondoPremio2";
		public static final String SOCIETAFONDOPREMIO2 = "societaFondoPremio2";
		public static final String ISINFONDOPREMIO2 = "isinFondoPremio2";
		public static final String DESCRIZIONEFONDOPREMIO2 = "descrizioneFondoPremio2";
		public static final String PERCENTUALEFONDOPREMIO2 = "percentualeFondoPremio2";
		public static final String IMPORTOFONDOPREMIO2 = "importoFondoPremio2";
		public static final String LINEAFONDOPREMIO3 = "lineaFondoPremio3";
		public static final String CODICEFONDOPREMIO3 = "codiceFondoPremio3";
		public static final String SOCIETAFONDOPREMIO3 = "societaFondoPremio3";
		public static final String ISINFONDOPREMIO3 = "isinFondoPremio3";
		public static final String DESCRIZIONEFONDOPREMIO3 = "descrizioneFondoPremio3";
		public static final String PERCENTUALEFONDOPREMIO3 = "percentualeFondoPremio3";
		public static final String IMPORTOFONDOPREMIO3 = "importoFondoPremio3";
		public static final String LINEAFONDOPREMIO4 = "lineaFondoPremio4";
		public static final String CODICEFONDOPREMIO4 = "codiceFondoPremio4";
		public static final String SOCIETAFONDOPREMIO4 = "societaFondoPremio4";
		public static final String ISINFONDOPREMIO4 = "isinFondoPremio4";
		public static final String DESCRIZIONEFONDOPREMIO4 = "descrizioneFondoPremio4";
		public static final String PERCENTUALEFONDOPREMIO4 = "percentualeFondoPremio4";
		public static final String IMPORTOFONDOPREMIO4 = "importoFondoPremio4";
		public static final String LINEAFONDOPREMIO5 = "lineaFondoPremio5";
		public static final String CODICEFONDOPREMIO5 = "codiceFondoPremio5";
		public static final String SOCIETAFONDOPREMIO5 = "societaFondoPremio5";
		public static final String ISINFONDOPREMIO5 = "isinFondoPremio5";
		public static final String DESCRIZIONEFONDOPREMIO5 = "descrizioneFondoPremio5";
		public static final String PERCENTUALEFONDOPREMIO5 = "percentualeFondoPremio5";
		public static final String IMPORTOFONDOPREMIO5 = "importoFondoPremio5";
		public static final String IMPORTOFONDOPREMIO6 = "importoFondoPremio6";
		public static final String LINEAFONDOPREMIO6 = "lineaFondoPremio6";
		public static final String CODICEFONDOPREMIO6 = "codiceFondoPremio6";
		public static final String SOCIETAFONDOPREMIO6 = "societaFondoPremio6";
		public static final String ISINFONDOPREMIO6 = "isinFondoPremio6";
		public static final String DESCRIZIONEFONDOPREMIO6 = "descrizioneFondoPremio6";
		public static final String PERCENTUALEFONDOPREMIO6 = "percentualeFondoPremio6";
		public static final String IMPORTOFONDOPREMIO7 = "importoFondoPremio7";
		public static final String LINEAFONDOPREMIO7 = "lineaFondoPremio7";
		public static final String CODICEFONDOPREMIO7 = "codiceFondoPremio7";
		public static final String SOCIETAFONDOPREMIO7 = "societaFondoPremio7";
		public static final String ISINFONDOPREMIO7 = "isinFondoPremio7";
		public static final String DESCRIZIONEFONDOPREMIO7 = "descrizioneFondoPremio7";
		public static final String PERCENTUALEFONDOPREMIO7 = "percentualeFondoPremio7";
		public static final String IMPORTOFONDOPREMIO8 = "importoFondoPremio8";
		public static final String IMPORTOFONDOPREMIO9 = "importoFondoPremio9";
		public static final String LINEAFONDOPREMIO8 = "lineaFondoPremio8";
		public static final String CODICEFONDOPREMIO8 = "codiceFondoPremio8";
		public static final String SOCIETAFONDOPREMIO8 = "societaFondoPremio8";
		public static final String ISINFONDOPREMIO8 = "isinFondoPremio8";
		public static final String DESCRIZIONEFONDOPREMIO8 = "descrizioneFondoPremio8";
		public static final String PERCENTUALEFONDOPREMIO8 = "percentualeFondoPremio8";
		public static final String IMPORTOFONDOPREMIO10 = "importoFondoPremio10";
		public static final String LINEAFONDOPREMIO9 = "lineaFondoPremio9";
		public static final String CODICEFONDOPREMIO9 = "codiceFondoPremio9";
		public static final String SOCIETAFONDOPREMIO9 = "societaFondoPremio9";
		public static final String ISINFONDOPREMIO9 = "isinFondoPremio9";
		public static final String DESCRIZIONEFONDOPREMIO9 = "descrizioneFondoPremio9";
		public static final String PERCENTUALEFONDOPREMIO9 = "percentualeFondoPremio9";
		public static final String IMPORTOFONDOPREMIO11 = "importoFondoPremio11";
		public static final String LINEAFONDOPREMIO10 = "lineaFondoPremio10";
		public static final String CODICEFONDOPREMIO10 = "codiceFondoPremio10";
		public static final String SOCIETAFONDOPREMIO10 = "societaFondoPremio10";
		public static final String ISINFONDOPREMIO10 = "isinFondoPremio10";
		public static final String DESCRIZIONEFONDOPREMIO10 = "descrizioneFondoPremio10";
		public static final String PERCENTUALEFONDOPREMIO10 = "percentualeFondoPremio10";
		public static final String IMPORTOFONDOPREMIO12 = "importoFondoPremio12";
		public static final String IMPORTOFONDOPREMIO13 = "importoFondoPremio13";
		public static final String LINEAFONDOPREMIO11 = "lineaFondoPremio11";
		public static final String CODICEFONDOPREMIO11 = "codiceFondoPremio11";
		public static final String SOCIETAFONDOPREMIO11 = "societaFondoPremio11";
		public static final String ISINFONDOPREMIO11 = "isinFondoPremio11";
		public static final String DESCRIZIONEFONDOPREMIO11 = "descrizioneFondoPremio11";
		public static final String PERCENTUALEFONDOPREMIO11 = "percentualeFondoPremio11";
		public static final String IMPORTOFONDOPREMIO14 = "importoFondoPremio14";
		public static final String LINEAFONDOPREMIO12 = "lineaFondoPremio12";
		public static final String CODICEFONDOPREMIO12 = "codiceFondoPremio12";
		public static final String SOCIETAFONDOPREMIO12 = "societaFondoPremio12";
		public static final String ISINFONDOPREMIO12 = "isinFondoPremio12";
		public static final String DESCRIZIONEFONDOPREMIO12 = "descrizioneFondoPremio12";
		public static final String PERCENTUALEFONDOPREMIO12 = "percentualeFondoPremio12";
		public static final String IMPORTOFONDOPREMIO15 = "importoFondoPremio15";
		public static final String IMPORTOFONDOPREMIO16 = "importoFondoPremio16";
		public static final String LINEAFONDOPREMIO13 = "lineaFondoPremio13";
		public static final String CODICEFONDOPREMIO13 = "codiceFondoPremio13";
		public static final String SOCIETAFONDOPREMIO13 = "societaFondoPremio13";
		public static final String ISINFONDOPREMIO13 = "isinFondoPremio13";
		public static final String DESCRIZIONEFONDOPREMIO13 = "descrizioneFondoPremio13";
		public static final String PERCENTUALEFONDOPREMIO13 = "percentualeFondoPremio13";
		public static final String LINEAFONDOPREMIO14 = "lineaFondoPremio14";
		public static final String CODICEFONDOPREMIO14 = "codiceFondoPremio14";
		public static final String SOCIETAFONDOPREMIO14 = "societaFondoPremio14";
		public static final String ISINFONDOPREMIO14 = "isinFondoPremio14";
		public static final String DESCRIZIONEFONDOPREMIO14 = "descrizioneFondoPremio14";
		public static final String PERCENTUALEFONDOPREMIO14 = "percentualeFondoPremio14";
		public static final String LINEAFONDOPREMIO15 = "lineaFondoPremio15";
		public static final String CODICEFONDOPREMIO15 = "codiceFondoPremio15";
		public static final String SOCIETAFONDOPREMIO15 = "societaFondoPremio15";
		public static final String ISINFONDOPREMIO15 = "isinFondoPremio15";
		public static final String DESCRIZIONEFONDOPREMIO15 = "descrizioneFondoPremio15";
		public static final String PERCENTUALEFONDOPREMIO15 = "percentualeFondoPremio15";
		public static final String LINEAFONDOPREMIO16 = "lineaFondoPremio16";
		public static final String CODICEFONDOPREMIO16 = "codiceFondoPremio16";
		public static final String SOCIETAFONDOPREMIO16 = "societaFondoPremio16";
		public static final String ISINFONDOPREMIO16 = "isinFondoPremio16";
		public static final String DESCRIZIONEFONDOPREMIO16 = "descrizioneFondoPremio16";
		public static final String PERCENTUALEFONDOPREMIO16 = "percentualeFondoPremio16";
		public static final String TOTALEPERCENTUALE = "totalePercentuale";
		public static final String TESTOFIRMA3CLIENTE = "testofirma3Cliente";
		public static final String FIRMA3CLIENTE1 = "firma3Cliente1";
		public static final String FIRMA3CLIENTE = "firma3Cliente";
		public static final String NOMEFIRMA3CLIENTE1 = "nomefirma3Cliente1";
		public static final String NOMEFIRMA3CLIENTE = "nomefirma3Cliente";
		public static final String TESTOFIRMA4CLIENTE = "testofirma4Cliente";
		public static final String FIRMA4CLIENTE1 = "firma4Cliente1";
		public static final String FIRMA4CLIENTE = "firma4Cliente";
		public static final String NOMEFIRMA4CLIENTE1 = "nomefirma4Cliente1";
		public static final String NOMEFIRMA4CLIENTE = "nomefirma4Cliente";
		public static final String ISVARIAZIONECONTOSDD = "isVariazioneContoSDD";
		public static final String TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE = "tipoIntestazioneContoSDDBMEDVariazione";
		public static final String TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE = "tipoContoINTESTATARIOSDDBMEDVariazione";
		public static final String IBANCONTOCORRENTECCINTESTATARIOSDDBMEDVARIAZIONE = "ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione";
		public static final String NUMEROPROPOSTACCINAPERTURAINTESTATARIOSDDBMEDVARIAZIONE = "numeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione";
		public static final String IBANCONTOCORRENTECCINTESTATARIOSDDESTERNAVARIAZIONE = "ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione";
		public static final String NDGALTROCLIENTESDDBMEDVARIAZIONE = "ndgALTROCLIENTESDDBMEDVariazione";
		public static final String COGNOMENOMEALTROCLIENTESDDBMEDVARIAZIONE = "cognomeNomeALTROCLIENTESDDBMEDVariazione";
		public static final String TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE = "tipoContoALTROCLIENTESDDBMEDVariazione";
		public static final String IBANCONTOCORRENTEALTROCLIENTESDDBMEDVARIAZIONE = "ibanContoCorrenteALTROCLIENTESDDBMEDVariazione";
		public static final String IBANCONTOCORRENTEALTROCLIENTESDDESTERNAVARIAZIONE = "ibanContoCorrenteALTROCLIENTESDDEsternaVariazione";
		public static final String TIPORELAZIONECONTRAENTETERZOPAGATORE = "tipoRelazioneContraenteTerzoPagatore";
		public static final String DESCRIZIONETIPORELAZIONECONTRAENTETERZOPAGATORE = "descrizioneTipoRelazioneContraenteTerzoPagatore";
		public static final String FIRMA1CLIENTE1 = "firma1Cliente1";
		public static final String FIRMA1CLIENTE = "firma1Cliente";
		public static final String NOMEFIRMA1CLIENTE1 = "nomefirma1Cliente1";
		public static final String NOMEFIRMA1CLIENTE = "nomefirma1Cliente";
		public static final String TESTOFIRMA1CLIENTE = "testofirma1Cliente";
		public static final String REVOCAVERSAMENTOPREMIOAGGIUNTIVO = "revocaVersamentoPremioAggiuntivo";
		public static final String FIRMA2CLIENTE1 = "firma2Cliente1";
		public static final String FIRMA2CLIENTE = "firma2Cliente";
		public static final String NOMEFIRMA2CLIENTE1 = "nomefirma2Cliente1";
		public static final String NOMEFIRMA2CLIENTE = "nomefirma2Cliente";
		public static final String TESTOFIRMA2CLIENTE = "testofirma2Cliente";
		public static final String COGNOMENOMEAGENTE = "cognomeNomeAgente";
		public static final String FIRMA1AGENTE = "firma1Agente";
		public static final String NOMEFIRMA1AGENTE = "nomefirma1Agente";
		public static final String CODICEAGENTE = "codiceAgente";
		public static final String TESTOFIRMA1AGENTE = "testofirma1Agente";

		private FieldNames() {
		}
	}

	public class FieldValues {
		public static final String ISVARIAZIONEDISPOSIZIONESDD_SI = "SI";
		public static final String ISVARIAZIONEIMPORTOSDD_SI = "SI";
		public static final String ISVARIAZIONEFREQUENZASDD_SI = "SI";
		public static final String FREQUENZAVARIAZIONESDD_12 = "12";
		public static final String FREQUENZAVARIAZIONESDD_04 = "04";
		public static final String FREQUENZAVARIAZIONESDD_02 = "02";
		public static final String FREQUENZAVARIAZIONESDD_01 = "01";
		public static final String ISVARIAZIONEDATAPREMIOSDD_SI = "SI";
		public static final String GIORNOVALUTAVARIAZIONESDD_05 = "05";
		public static final String GIORNOVALUTAVARIAZIONESDD_20 = "20";
		public static final String TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD_RIATTIVAZIONE = "RIATTIVAZIONE";
		public static final String ISVARIAZIONEIMPORTORIATTIVAZIONESDD_SI = "SI";
		public static final String ISVARIAZIONEFRAZIONAMENTORIATTIVAZIONESDD_SI = "SI";
		public static final String FREQUENZARIATTIVAZIONESDD_12 = "12";
		public static final String FREQUENZARIATTIVAZIONESDD_04 = "04";
		public static final String FREQUENZARIATTIVAZIONESDD_02 = "02";
		public static final String FREQUENZARIATTIVAZIONESDD_01 = "01";
		public static final String ISVARIAZIONEDATAPREMIORIATTIVAZIONESDD_SI = "SI";
		public static final String GIORNOVALUTARIATTIVAZIONESDD_05 = "05";
		public static final String GIORNOVALUTARIATTIVAZIONESDD_20 = "20";
		public static final String TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD_SOSPENSIONE = "SOSPENSIONE";
		public static final String TIPOSOSPENSIONEREVOCARIATTIVAZIONESDD_REVOCA = "REVOCA";
		public static final String ISVARIAZIONERIPARTIZIONE_SI = "SI";
		public static final String ISVARIAZIONECONTOSDD_SI = "SI";
		public static final String TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE_INTESTATARIO = "INTESTATARIO";
		public static final String TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE_CC = "CC";
		public static final String TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE_CCINAPERTURA = "CCINAPERTURA";
		public static final String TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE_CCESTERNA = "CCESTERNA";
		public static final String TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE_ALTROCLIENTE = "ALTROCLIENTE";
		public static final String TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE_CC = "CC";
		public static final String TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE_CCESTERNA = "CCESTERNA";
		public static final String TIPORELAZIONECONTRAENTETERZOPAGATORE_001 = "001";
		public static final String TIPORELAZIONECONTRAENTETERZOPAGATORE_002 = "002";
		public static final String TIPORELAZIONECONTRAENTETERZOPAGATORE_003 = "003";
		public static final String TIPORELAZIONECONTRAENTETERZOPAGATORE_004 = "004";

		private FieldValues() {
		}
	}
}